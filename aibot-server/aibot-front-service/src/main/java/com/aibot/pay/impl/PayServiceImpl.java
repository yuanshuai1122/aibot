package com.aibot.pay.impl;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.enums.ResultCode;
import com.aibot.factory.PayStrategyFactory;
import com.aibot.pay.PayService;
import com.aibot.pay.PayStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author:yuanshuai
 * @create: 2022-10-08 11:38
 * @Description: 支付服务实现
 */
@Service("payService")
public class PayServiceImpl implements PayService {


    /**
     * 商户号
     */
    private static final String MERCHANT_ID = "900029000000354";

    /**
     * 接入机构号
     */
    private static final String PARTNER = "900029000000354";

    /**
     * URL
     */
    //private static final String URL = "https://newbox.bhecard.com/api_gateway.do";
    private static final String URL = "http://test_nucc.bhecard.com:9088/api_gateway.do";

    /**
     * 密钥
     */
    private static final String KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAIqUuxd92eEBXVneDWhfNP6XCkLcGBO1YAulexKX+OdlfZzB/4NNHkOAQQy84k3ZgIUPIk5hewLbA+XGrk9Wih5HG3ZQeFugeoTcx3vwo7AQv7KnmcKEWFNlOr/EhB3JndmcQnBRsIRRdCP+7nobfBqU0jS8dnpcQX1AtBRZRnkfAgMBAAECgYAe+u70ansZ1Q9EduKycY5MWAHAPqnXRhXppJ3l4zmOqV6ye6Aef1ADsRlZuqQw2S3lESQPN7WjRskRRiBTtjn8Atul9YeC7+QirP1K8seUP5gKB4bcjlzzl1m5dmxldkptJAmdzwYn8PRTW0+tFVyEaD/B8hKGxij4Gew0e8bwCQJBAOboG3ttBESsG2cAtmP1MfKRTjVdY7qRMXzBybcAeobBbmgCQgybVXXgjbGai+qwrQqcVRIp6p1yDWTZxVSuDWsCQQCZpBhcayOCMZR6F8dQJSuSSSIJw/GGN7IXfMYIqLxA2oGzlQ0B1DffOUe2wrid+WdpLuYCz2LYPQHDEgYM1dwdAkEAnfwhEYm9ad73wLnUEQAqdHTGtex316aP3XQZt4Q0UQ73o2IoHsgI6OYDDIlZQfIv8xqTeiIDzEXEtEPrp8yOkQJBAIWAzFZKFqHD2UO6M8vVcKX9fGFF7TH2ZX75Qc82Z9ZmyDs2sgW71QzX5hPN4cQLeqswQFeCw14orMZHfBBdKJUCQQDiWYk85okRugsWtxeJFhMEt2oUT+Kd8Yz5Aiz3J9XIS+zWtJrFlv+hXkVedPJ3xtBF32DZrCbxDn3UjXipRaCP";

    /**
     * 易生公钥
     */
    private static final String EASYPAY_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2WTfvas1JvvaRuJWIKmKlBLmkRvr2O7Fu3k/zvhJs+X1JQorPWq/yZduY6HKu0up7Qi3T6ULHWyKBS1nRqhhHpmLHnI3sIO8E/RzNXJiTd9/bpXMv+H8F8DW5ElLxCIVuwHBROkBLWS9fIpslkFPt+r13oKFnuWhXgRr+K/YkJQIDAQAB";


    @Override
    public ResponseResult<Object> pay(String channel, BigDecimal amount) throws Exception {
        // 通过支付策略工厂拿到支付类型实现
        PayStrategy payStrategy = PayStrategyFactory.getPayStrategy(channel);
        if(payStrategy == null){
            return new ResponseResult<>(ResultCode.FAILED.getCode(), "非法支付渠道", null);
        }
        return payStrategy.pay(channel, amount, MERCHANT_ID, PARTNER, URL, KEY, EASYPAY_PUB_KEY);
    }
}
