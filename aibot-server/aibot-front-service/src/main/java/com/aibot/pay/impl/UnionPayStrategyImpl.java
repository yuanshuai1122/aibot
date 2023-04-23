package com.aibot.pay.impl;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.pay.PayStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author:yuanshuai
 * @create: 2022-10-08 13:59
 * @Description: 银联策略实现
 */
@Component("unionPayStrategy")
public class UnionPayStrategyImpl implements PayStrategy {
    @Override
    public ResponseResult<Object> pay(String channel, BigDecimal amount) throws Exception {
        return null;
    }
}
