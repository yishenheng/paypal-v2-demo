package com.ysh.dto;

import lombok.Data;

/**
 * 订单地址信息
 *
 * @author yishenheng
 * @date 2022/3/8 17:43
 */
@Data
public class OrderAddress {

    /**
     * 全名
     */
    private String fullName;

    /**
     * 区
     */
    private String addressLine1;

    /**
     * 区后面的具体地址
     */
    private String addressLine2;

    /**
     * 省
     */
    private String adminArea1;

    /**
     * 市
     */
    private String adminArea2;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 国家代码
     */
    private String countryCode;
}
