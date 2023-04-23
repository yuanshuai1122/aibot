package com.aibot.pay.impl;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.pay.PayStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author:yuanshuai
 * @create: 2022-10-08 13:58
 * @Description: 阿里云策略实现
 */
@Component("aliPayStrategy")
public class AliPayStrategyImpl implements PayStrategy {
    @Override
    public ResponseResult<Object> pay(String channel, BigDecimal amount) throws Exception {
        // 支付业务逻辑
        return null;
    }
}
