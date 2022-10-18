package com.ysh.controller;

import com.ysh.dto.OrderInfo;
import com.ysh.dto.OrderRefundInfo;
import com.ysh.util.PayPalApiUtil;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author yishenheng
 * @date 2022/3/8 18:00
 */
@RestController
public class OrderController {


    /**
     * 创建订单
     *
     * @param order 基本信息
     * @return 创建成功后 买家支付的地址
     * @throws IOException io
     */
    @PostMapping("/create/order")
    public String createOrder(@RequestBody OrderInfo order) throws IOException {
        return PayPalApiUtil.createOrder(order);
    }

    /**
     * 支付
     *
     * @param orderId 订单号
     * @throws IOException io
     */
    @GetMapping("/deduction/{order_id}")
    public void orderDeduction(@PathVariable("order_id") String orderId) throws IOException {
        PayPalApiUtil.orderDeduction(orderId);
    }

    /**
     * 退款
     *
     * @param orderId         订单号
     * @param orderRefundInfo 退款信息
     * @throws IOException io
     */
    @PostMapping("refund/{order_id}")
    public void refundOrder(@PathVariable("order_id") String orderId, @RequestBody OrderRefundInfo orderRefundInfo) throws IOException {
        PayPalApiUtil.refundOrder(orderId, orderRefundInfo);
    }

}
