package com.ysh.dto;

import lombok.Data;

/**
 * 退款信息
 * @author yishenheng
 * @date 2022/3/9 14:34
 */
@Data
public class OrderRefundInfo {

    /**
     * 发票id
     */
    private String invoiceId;

    /**
     * 退款理由
     */
    private String noteToPayer;

    /**
     * 货币代码
     */
    private String currencyCode;

    /**
     * 金额
     */
    private String totalAmount;
}
