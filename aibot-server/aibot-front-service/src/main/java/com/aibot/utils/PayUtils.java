package com.aibot.utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * @program: aibot-server
 * @description: 支付工具类
 * @author: yuanshuai
 * @create: 2023-04-23 13:04
 **/
public class PayUtils {

    //常量定义
    public static final String TEST_RSA_ENCODE_TYPE = "RSA";

    public static JSONObject qrcodeAndJsPayPush(String payType, String merchantId) {
        JSONObject sParaTemp = new JSONObject();
        sParaTemp.put("subject", "Echannell");
        sParaTemp.put("merchant_id", merchantId);
        //sParaTemp.put("out_trade_no", "2019040138759235");
        sParaTemp.put("out_trade_no", "demo" + DateUtils.getOutTradeNo() + "_");
        //sParaTemp.put("seller_email", "18679106330@gmail.com");
        sParaTemp.put("amount", 100);
        sParaTemp.put("pay_type", payType);
        sParaTemp.put("business_time", "2017-12-07 15:35:00");
        sParaTemp.put("notify_url", "https://www.baidu.com");
        //sParaTemp.put("order_desc", "Echannell");

        HashMap<String, Object> identityMap = new HashMap<String, Object>(30);
        identityMap.put("name", "测试");
        identityMap.put("mobile", "18010461111");
        identityMap.put("idno_type", "1");
        identityMap.put("id_no", "340827199311101111");
        identityMap.put("min_age", "18");

        //sParaTemp.put("identity", identityMap);
        return sParaTemp;
    }


    public static String getSign(String key, String charset, String bizContent,String keyType) throws Exception {
        if(TEST_RSA_ENCODE_TYPE.equals(keyType)) {
            return AlipaySignature.rsaSign(bizContent,  key, charset);
        }

        return null;
    }

    public static void rsaVerifySign(StringBuilder resultStrBuilder, String easypay_pub_key) throws Exception {
        //同步返回签名，需要对字符串进行截取后，再验证签名
        String msg =resultStrBuilder.toString();
        String returnString = StringUtils.substringBetween(msg,"response\":", ",\"sign\"");
        String returnSign = StringUtils.substringBetween(msg,",\"sign\":\"","\"}");
        boolean isTrue=AlipaySignature.rsaCheckContent(returnString, returnSign, easypay_pub_key, "UTF-8");
        System.out.println("验证返回签名是否正确：" + isTrue);
    }

}
