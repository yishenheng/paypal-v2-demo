package com.myyaffiliate.web.manager.dto;

import lombok.Data;

/**
 * 订单金额与细分
 *
 * @author yishenheng
 * @date 2022/3/8 17:18
 */
@Data
public class OrderAmountWithBreakdown {

    /**
     * 货币代码
     */
    private String currencyCode;

    /**
     * 总金额是由项目总计 + 航运 + handling + 税收总额 + 运费折扣相加而等到的总额
     */
    private String totalAmount;

    /**
     * 项目总计
     */
    private String itemTotal;

    /**
     * 航运
     */
    private String shipping;

    /**
     * 处理时间？
     */
    private String handling;

    /**
     * 税收总额
     */
    private String taxTotal;

    /**
     * 运费折扣
     */
    private String shippingDiscount;
}
