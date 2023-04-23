package com.aibot.pay;

import com.aibot.beans.entity.ResponseResult;

import java.math.BigDecimal;

/**
 * @author:yuanshuai
 * @create: 2022-10-08 13:57
 * @Description: 策略接口类
 */
public interface PayStrategy {


    /**
     * 支付
     *
     * @param channel       通道
     * @param amount        金额
     * @param merchantId    商户号
     * @param partner       接入机构号
     * @param url           URL
     * @param key           私钥
     * @param easypayPubKey 易生公钥
     * @return {@link ResponseResult}<{@link Object}>
     * @throws Exception 例外情况
     */
    ResponseResult<Object> pay(String channel, BigDecimal amount, String merchantId, String partner, String url, String key, String easypayPubKey) throws Exception;
}
