package com.aibot.pay.impl;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.pay.PayStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author:yuanshuai
 * @create: 2022-10-08 13:59
 * @Description: 微信策略实现
 */
@Component("wechatPayStrategy")
public class WechatPayStrategyImpl implements PayStrategy {
    @Override
    public ResponseResult<Object> pay(String channel, BigDecimal amount) throws Exception {
        return null;
    }
}
