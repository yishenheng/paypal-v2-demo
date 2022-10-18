package com.ysh.constant;

/**
 * @author yishenheng
 * @date 2022/3/8 16:13
 */
public class Constant {

    /**
     * 在订单创建后立即获取付款或授权为订单付款的意图。
     * CAPTURE：商家打算在客户付款后立即扣款。
     * AUTHORIZE：家打算在客户付款后授权付款并暂停资金。授权付款最好在授权后三天内获取，但最长可获取29天。三天的兑付期过后，原授权付款到期，您必须重新授权付款。您必须提出单独的请求才能按需获取付款。如果您的订单中有多个“purchase_unit”，则不支持此意图。
     */
    public static final String INTENT_TYPE = "CAPTURE";

    /**
     * 显示的公司名称
     */
    public static final String BRAND_NAME = "baidu";

    /**
     * LOGIN：当客户单击PayPal Checkout时，客户将被重定向到页面以登录PayPal并批准付款。
     * <p>
     * BILLING：当客户单击PayPal Checkout时，客户将被重定向到一个页面，以输入信用卡或借记卡以及完成购买所需的其他相关账单信息
     * <p>
     * NO_PREFERENCE：当客户单击“ PayPal Checkout”时，将根据其先前的交互方式将其重定向到页面以登录PayPal并批准付款，或重定向至页面以输入信用卡或借记卡以及完成购买所需的其他相关账单信息使用PayPal。
     * 默认值：NO_PREFERENCE
     */
    public static final String LANDING_PAGE = "NO_PREFERENCE";

    /**
     * 用户在支付页面时，取消支付后跳转到的页面
     */
    public static final String CANCEL_URL = "https://www.baidu.com";

    /**
     * 用户支付后跳转的页面
     */
    public static final String RETURN_URL = "https://www.taobao.com";

    /**
     * CONTINUE：将客户重定向到PayPal付款页面后，将出现“继续”按钮。当结帐流程启动时最终金额未知时，请使用此选项，并且您想将客户重定向到商家页面而不处理付款。
     * PAY_NOW：将客户重定向到PayPal付款页面后，出现“立即付款”按钮。当启动结帐时知道最终金额并且您要在客户单击“ 立即付款”时立即处理付款时，请使用此选项。
     */
    public static final String USER_ACTION = "CONTINUE";

    /**
     * GET_FROM_FILE：使用贝宝网站上客户提供的送货地址。
     * NO_SHIPPING：从PayPal网站编辑送货地址。推荐用于数字商品
     * SET_PROVIDED_ADDRESS：使用商家提供的地址。客户无法在PayPal网站上更改此地址
     */
    public static final String SHIPPING_PREFERENCE = "SET_PROVIDED_ADDRESS";
}
