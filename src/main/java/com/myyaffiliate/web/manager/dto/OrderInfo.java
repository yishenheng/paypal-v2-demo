package com.myyaffiliate.web.manager.dto;

import lombok.Data;

import java.util.List;

/**
 * 订单信息
 *
 * @author yishenheng
 * @date 2022/3/8 17:15
 */
@Data
public class OrderInfo {

    /**
     * 描述
     */
    private String description;

    /**
     * 自定义id(订单号)
     */
    private String customId;

    /**
     * 发票id
     */
    private String invoiceId;

    /**
     * 订单金额与细分
     */
    private OrderAmountWithBreakdown orderAmountWithBreakdown;

    /**
     * 商品订单详情
     */
    private List<OrderCommodity> commodityInfo;

    /**
     * 地址信息
     */
    private OrderAddress orderAddress;


}
