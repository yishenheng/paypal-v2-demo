package com.myyaffiliate.web.manager.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.myyaffiliate.web.manager.dto.OrderInfo;
import com.myyaffiliate.web.manager.dto.OrderRefundInfo;
import com.myyaffiliate.web.manager.constant.PayPalStatusEnum;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.payments.CapturesGetRequest;
import com.paypal.payments.CapturesRefundRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * @author yishenheng
 * @date 2022/3/8 18:02
 */
@Slf4j
public class PayPalApiUtil {

    private static final String APPROVE = "approve";

    /**
     * 创建订单
     *
     * @param order 订单信息
     * @return 创建订单后，需要买家进行支付，此操作不会立马到账，只是买家确认操作
     */
    public static String createOrder(OrderInfo order) throws IOException {
        OrdersCreateRequest request = new OrdersCreateRequest();
        // 设置header
        request.prefer("return=representation");
        // 设置body
        request.requestBody(OrderSubjectUtil.buildRequestBody(order));

        HttpResponse<Order> response = PayPalInitUtil.client().execute(request);
        if (response.statusCode() != HTTP_CREATED) {
            log.info("创建订单失败，状态码：{}", response.statusCode());
            return null;
        }

        log.info("创建订单成功。statusCode:{},status:{},orderId:{}"
                , response.statusCode()
                , response.result().status()
                , response.result().id());

        return response.result().links()
                .stream()
                .filter(linkItem -> APPROVE.equals(linkItem.rel()))
                .map(LinkDescription::href)
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单号
     * @throws IOException io
     */
    public static PurchaseUnit getOrderInfo(String orderId) throws IOException {
        log.info("orderId：{}", orderId);

        OrdersGetRequest request = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = PayPalInitUtil.client().execute(request);
        log.info("orderDeduction操作返回------> statusCode:{},status:{},orderId:{}"
                , response.statusCode()
                , response.result().status()
                , response.result().id());

        List<PurchaseUnit> purchaseUnits = response.result().purchaseUnits();
        if (CollUtil.isEmpty(purchaseUnits)) {
            log.info("无法查询到订单信息，orderId：{}", orderId);
            return null;
        }
        // 订单信息
        return purchaseUnits.get(0);
    }

    /**
     * 订单扣款
     * 订单创建成功后，会返回一个URL地址，用户需要点开页面付款。
     * 此处不是真正地付款只是只是授权，PayPal并未收到钱款，需要调用此接口来将钱存入到PayPal账户中
     *
     * @param orderId 订单号
     */
    public static void orderDeduction(String orderId) throws IOException {
        log.info("orderId:{}", orderId);
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new OrderRequest());
        // 执行扣款操作
        HttpResponse<Order> response = PayPalInitUtil
                .client()
                .execute(request);
        log.info("orderDeduction操作返回------> statusCode:{},status:{},orderId:{}"
                , response.statusCode()
                , response.result().status()
                , response.result().id());

        List<PurchaseUnit> purchaseUnits = response.result().purchaseUnits();
        if (CollUtil.isEmpty(purchaseUnits)) {
            log.info("支付失败，purchaseUnits未返回任何信息");
            return;
        }

        // 支付成功
        paySuccess(purchaseUnits);
    }

    /**
     * 订单退款
     *
     * @param orderId 订单id
     * @param info    退款信息
     */
    public static void refundOrder(String orderId, OrderRefundInfo info) throws IOException {
        log.info("orderId:{}，退款信息:{}", orderId, JSONUtil.toJsonStr(info));
        PurchaseUnit orderInfo = getOrderInfo(orderId);
        if (orderInfo == null) {
            log.info("无法通过订单号查询到订单详情信息。orderId:{}", orderId);
            return;
        }

        // 如果付款信息为空则代表该笔订单暂未支付，无需退款
        if (orderInfo.payments() == null || CollUtil.isEmpty(orderInfo.payments().captures())) {
            log.info("该订单无法查询到付款信息，请晚点在尝试退款。订单号:{}", orderId);
            return;
        }

        // 扣款id
        String captureId = orderInfo.payments().captures().get(0).id();
        // 构建信息
        CapturesRefundRequest request = new CapturesRefundRequest(captureId);
        request.prefer("return=representation");
        request.requestBody(RefundSubjectUtil.getRefundRequest(info));

        // 调用退款接口
        HttpResponse<com.paypal.payments.Refund> response = PayPalInitUtil.client().execute(request);

        log.info("refundOrder操作返回------> statusCode:{},status:{},退款id:{}"
                , response.statusCode()
                , response.result().status()
                , response.result().id());

        // 退款成功
        if (PayPalStatusEnum.COMPLETED.name().equals(response.result().status())) {
            log.info("退款操作成功。");
            return;
        }
        log.error("退款操作失败。");
    }

    /**
     * 获取扣款状态
     *
     * @param captureId 扣款id
     * @return 状态码200 代表成功
     */
    public static Integer getDeductionStatus(String captureId) throws IOException {
        log.info("扣款id:{}", captureId);
        CapturesGetRequest request = new CapturesGetRequest(captureId);
        // 查询扣款详情
        HttpResponse<com.paypal.payments.Capture> result = PayPalInitUtil
                .client()
                .execute(request);

        return result.statusCode();
    }

    /**
     * 支付成功后的处理 如果要实际业务的话应该要在支付成功后发送消息给物流那边进行发货等处理,这里只是判断下是否成功
     *
     * @param purchaseUnits 采购信息
     * @throws IOException io
     */
    private static void paySuccess(List<PurchaseUnit> purchaseUnits) throws IOException {
        Capture capture = purchaseUnits.get(0).payments().captures().get(0);
        String captureId = capture.id();
        // 支付成功
        if (PayPalStatusEnum.COMPLETED.name().equals(capture.status()) && getDeductionStatus(captureId) == HTTP_OK) {
            log.info("支付成功 状态码:{}", PayPalStatusEnum.COMPLETED.name());
        }
        // 支付成功的另外一种状态
        if (PayPalStatusEnum.PENDING.name().equals(capture.status()) && getDeductionStatus(captureId) == HTTP_OK) {
            log.info("状态详情: {}", capture.captureStatusDetails().reason());
            String reason = PayPalStatusEnum.PENDING.name();
            if (capture.captureStatusDetails() != null && capture.captureStatusDetails().reason() != null) {
                reason = capture.captureStatusDetails().reason();
            }
            log.info("支付成功 状态码:{}", reason);
        }
    }
}
