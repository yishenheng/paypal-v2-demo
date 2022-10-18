package com.ysh.util;

import com.ysh.dto.OrderRefundInfo;
import com.paypal.payments.Money;
import com.paypal.payments.RefundRequest;

/**
 * 退款信息构建
 *
 * @author yishenheng
 * @date 2022/3/9 14:40
 */
public class RefundSubjectUtil {

    /**
     * 构建退款信息内容
     *
     * @param orderRefundInfo 退款信息
     * @return RefundRequest
     */
    public static RefundRequest getRefundRequest(OrderRefundInfo orderRefundInfo) {
        RefundRequest result = new RefundRequest();
        // 退款金额信息
        result.amount(new Money()
                .value(orderRefundInfo.getTotalAmount())
                .currencyCode(orderRefundInfo.getCurrencyCode()));
        // 发票id
        result.invoiceId(orderRefundInfo.getInvoiceId());
        // 退款理由
        result.noteToPayer(orderRefundInfo.getNoteToPayer());

        return result;
    }
}
