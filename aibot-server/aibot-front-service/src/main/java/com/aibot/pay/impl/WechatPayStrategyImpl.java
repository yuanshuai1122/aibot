package com.aibot.pay.impl;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.ApiBaseUrl;
import com.aibot.pay.PayStrategy;
import com.aibot.utils.OkHttpUtils;
import com.aibot.utils.PayUtils;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:yuanshuai
 * @create: 2022-10-08 13:59
 * @Description: 微信策略实现
 */
@Component("wechatPayStrategy")
@Slf4j
public class WechatPayStrategyImpl implements PayStrategy {

    private static final String OPEN_ID = "xxxxxx";

    @Override
    public ResponseResult<Object> pay(String channel, BigDecimal amount, String merchantId, String partner, String url, String key, String easypayPubKey) throws Exception {

        JSONObject sParaTemp = PayUtils.qrcodeAndJsPayPush(channel, merchantId);
        sParaTemp.put("open_id", OPEN_ID );
        String bizContent = sParaTemp.toString();

        String service  = "easypay.js.pay.push";

        //加密类型，默认RSA
        String sign_type = "RSA";
        //编码类型
        String charset = "UTF-8";

        //根据请求参数生成的机密串
        //String sign = PayUtils.getSign(key, charset, bizContent,sign_type);
        String sign = "Nnki6qyFcWkbQJU+2802oYhiMoJ0rMbeFSF5sCGT0QuGTTgd8J6Xo0IdEnOsyqt4ZO78uQQdIbmc92Xnhy0ybcrX/YJHkuy+MNfzAACXp2Yr68zvvSMKMthW4Qi3+twqNExzhzGKy1iw2bOCnnWbv9MXKnkNFwr5NPoOXoGv3ag=";
        log.info("计算签名数据为：{}", sign);

        OkHttpUtils client = OkHttpUtils.builder();
        client.url(url);
        client.addHeader("Content-Type", "application/json");
        client.addParam("service", service);
        client.addParam("partner", partner);
        client.addParam("sign", sign);
        client.addParam("sign_type", sign_type);
        client.addParam("charset", charset);
        client.addParam("biz_content", bizContent);

        String sync = client.post(true).sync();
        log.info("请求结果为：{}", sync);

        //易生公钥验证返回签名
//        try {
//            PayUtils.rsaVerifySign(resultStrBuilder, easypayPubKey,sign_type);
//        }catch(Exception e) {
//            System.out.println(e.getMessage());
//        }


        return null;
    }
}
