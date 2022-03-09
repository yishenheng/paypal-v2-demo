package com.myyaffiliate.web.manager.util;

import cn.hutool.json.JSONUtil;
import com.myyaffiliate.web.manager.constant.Constant;
import com.myyaffiliate.web.manager.dto.OrderAddress;
import com.myyaffiliate.web.manager.dto.OrderAmountWithBreakdown;
import com.myyaffiliate.web.manager.dto.OrderCommodity;
import com.myyaffiliate.web.manager.dto.OrderInfo;
import com.paypal.orders.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单主体工具类
 *
 * @author yishenheng
 * @date 2022/3/8 17:57
 */
@Slf4j
public class OrderSubjectUtil {

    /**
     * 构建订单主体
     *
     * @param orderInfo 订单信息
     * @return OrderRequest
     */
    public static OrderRequest buildRequestBody(OrderInfo orderInfo) {
        log.info("orderInfo：{}", JSONUtil.toJsonStr(orderInfo));

        OrderRequest orderRequest = new OrderRequest();

        //设置订单付款的意图
        orderRequest.checkoutPaymentIntent(Constant.INTENT_TYPE);

        // 构建基本信息
        ApplicationContext applicationContext = getApplicationContext();
        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();

        // 构建订单的详情信息
        PurchaseUnitRequest purchaseUnitRequest = getPurchaseUnitRequest(orderInfo);
        purchaseUnitRequests.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequests);

        return orderRequest;

    }

    /**
     * 构建PurchaseUnitRequest基本信息
     *
     * @param orderInfo 订单信息
     * @return PurchaseUnitRequest
     */
    private static  PurchaseUnitRequest getPurchaseUnitRequest(OrderInfo orderInfo) {
        return new PurchaseUnitRequest()
                // 订单描述
                .description(orderInfo.getDescription())
                // 自定义id(订单号)
                .customId(orderInfo.getCustomId())
                // 发票id
                .invoiceId(orderInfo.getInvoiceId())
                // 订单金额与细分
                .amountWithBreakdown(getAmountWithBreakdown(orderInfo.getOrderAmountWithBreakdown()))
                // 商品信息
                .items(getItems(orderInfo.getCommodityInfo()))
                // 地址信息
                .shippingDetail(getShippingDetail(orderInfo.getOrderAddress()));
    }

    /**
     * 构建地址信息
     *
     * @param shippingDetail 地址信息
     * @return ShippingDetail
     */
    private static ShippingDetail getShippingDetail(OrderAddress shippingDetail) {
        return new ShippingDetail()
                .name(new Name().fullName(shippingDetail.getFullName()))
                .addressPortable(new AddressPortable()
                        .adminArea1(shippingDetail.getAdminArea1())
                        .adminArea2(shippingDetail.getAdminArea2())
                        .addressLine1(shippingDetail.getAddressLine1())
                        .addressLine2(shippingDetail.getAddressLine2())
                        .postalCode(shippingDetail.getPostalCode())
                        .countryCode(shippingDetail.getCountryCode()));
    }

    /**
     * 构建订单金额与细分
     *
     * @param orderAmountWithBreakdown 订单金额与细分信息
     * @return AmountWithBreakdown
     */
    private static AmountWithBreakdown getAmountWithBreakdown(OrderAmountWithBreakdown orderAmountWithBreakdown) {
        return new AmountWithBreakdown()
                .currencyCode(orderAmountWithBreakdown.getCurrencyCode())
                .value(orderAmountWithBreakdown.getTotalAmount())
                .amountBreakdown(new AmountBreakdown()
                        .itemTotal(new Money().currencyCode(orderAmountWithBreakdown.getCurrencyCode()).value(orderAmountWithBreakdown.getItemTotal()))
                        .shipping(new Money().currencyCode(orderAmountWithBreakdown.getCurrencyCode()).value(orderAmountWithBreakdown.getShipping()))
                        .handling(new Money().currencyCode(orderAmountWithBreakdown.getCurrencyCode()).value(orderAmountWithBreakdown.getHandling()))
                        .taxTotal(new Money().currencyCode(orderAmountWithBreakdown.getCurrencyCode()).value(orderAmountWithBreakdown.getTaxTotal()))
                        .discount(new Money().currencyCode(orderAmountWithBreakdown.getCurrencyCode()).value("0"))
                        .insurance(new Money().currencyCode(orderAmountWithBreakdown.getCurrencyCode()).value("0"))
                        .shippingDiscount(new Money().currencyCode(orderAmountWithBreakdown.getCurrencyCode()).value(orderAmountWithBreakdown.getShippingDiscount())));
    }

    /**
     * 构建商品信息
     *
     * @return
     */
    private static List<Item> getItems(List<OrderCommodity> orderCommodityInfos) {
        List<Item> resultItems = new ArrayList<>();
        orderCommodityInfos.forEach(item -> resultItems.add(new Item()
                .name(item.getName())
                .description(item.getDescription())
                .unitAmount(new Money()
                        .currencyCode(item.getCurrencyCode())
                        .value(item.getAmount()))
                .quantity(item.getQuantity())));
        return resultItems;
    }

    /**
     * 构建基本信息
     *
     * @return ApplicationContext
     */
    private static ApplicationContext getApplicationContext() {
        return new ApplicationContext()
                .brandName(Constant.BRAND_NAME)
                .landingPage(Constant.LANDING_PAGE)
                .cancelUrl(Constant.CANCEL_URL)
                .returnUrl(Constant.RETURN_URL)
                .userAction(Constant.USER_ACTION)
                .shippingPreference(Constant.SHIPPING_PREFERENCE);
    }
}
