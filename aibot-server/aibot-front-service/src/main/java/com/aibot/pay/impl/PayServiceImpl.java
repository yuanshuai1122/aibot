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
    private static final String KEY = "Mewy51A6JSC1a7LNffbxlJoUqsMRFG9DZtAzMZc1Mw==";

    /**
     * 易生公钥
     */
    private static final String EASYPAY_PUB_KEY = "BDhSCCYLZ94t5SGKUg2Dm5DjNK6k8mOaMgRUkq87u03CUZOS236lPHkXHo85+f3ChF+GO26EkWKxWSiGHRm594M=";


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
