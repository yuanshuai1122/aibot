package com.aibot.pay.impl;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.enums.ResultCode;
import com.aibot.pay.PayStrategy;
import com.aibot.utils.OkHttpUtils;
import com.aibot.utils.PayUtils;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @program: aibot-server
 * @description: 易生h5收银台
 * @author: yuanshuai
 * @create: 2023-04-26 21:44
 **/
@Component("easyPayh5Strategy")
@Slf4j
public class EasyPayH5StrategyImpl implements PayStrategy {
    @Override
    public ResponseResult<Object> pay(String channel, BigDecimal amount, String merchantId, String partner, String url, String key, String easypayPubKey) throws Exception {

        JSONObject sParaTemp = PayUtils.easyPayH5Push(merchantId);
        String bizContent = sParaTemp.toString();

        String service  = "easypay.merchant.easyPayh5";

        //加密类型，默认RSA
        String sign_type = "RSA";
        //编码类型
        String charset = "UTF-8";

        //根据请求参数生成的机密串
        String sign = PayUtils.getSign(key, charset, bizContent,sign_type);
        log.info("计算签名数据为：{}", sign);

        OkHttpUtils client = OkHttpUtils.builder();
        client.url(url);
        client.addParam("service", service);
        client.addParam("partner", partner);
        client.addParam("sign", sign);
        client.addParam("sign_type", sign_type);
        client.addParam("charset", charset);
        client.addParam("biz_content", bizContent);

        String sync = client.post(false).sync();
        log.info("请求结果为：{}", sync);

        return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "请求成功", sync);
    }
}
