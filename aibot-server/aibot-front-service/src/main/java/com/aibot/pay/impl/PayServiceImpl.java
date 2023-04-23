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

    @Override
    public ResponseResult<Object> pay(String channel, BigDecimal amount) throws Exception {
        // 通过支付策略工厂拿到支付类型实现
        PayStrategy payStrategy = PayStrategyFactory.getPayStrategy(channel);
        if(payStrategy == null){
            return new ResponseResult<>(ResultCode.FAILED.getCode(), "非法支付渠道", null);
        }
        return payStrategy.pay(channel,amount);
    }
}
