package com.ysh.dto;

import lombok.Data;

/**
 * 商品订单详情
 *
 * @author yishenheng
 * @date 2022/3/8 17:24
 */
@Data
public class OrderCommodity {

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 货币代码
     */
    private String currencyCode;

    /**
     * 金额
     */
    private String amount;

    /**
     * 数量
     */
    private String quantity;
}
