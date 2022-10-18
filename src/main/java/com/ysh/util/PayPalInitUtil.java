package com.ysh.util;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * PayPal初始化
 *
 * @author yishenheng
 * @date 2022/3/8 15:59
 */
@Slf4j
@Component
public class PayPalInitUtil {


    /**
     * 初始化PayPalHttp
     * Sandbox：沙箱环境 https://www.sandbox.paypal.com/
     * Live；生产环境
     *
     * @return PayPalHttpClient
     */
    public static PayPalHttpClient client() {
        PayPalEnvironment environment =
                new PayPalEnvironment.Sandbox(clientId, secret);
        return new PayPalHttpClient(environment);
    }



    private static String clientId;

    private static String secret;

    @Value("${paypal.clientId}")
    public void setClientId(String clientId) {
        PayPalInitUtil.clientId = clientId;
    }

    @Value("${paypal.secret}")
    public void setSecret(String secret) {
        PayPalInitUtil.secret = secret;
    }

}
