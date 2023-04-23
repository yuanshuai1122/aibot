package com.aibot.pay;

import com.aibot.beans.entity.ResponseResult;

import java.math.BigDecimal;

/**
 * @author:yuanshuai
 * @create: 2022-10-08 13:57
 * @Description: 策略接口类
 */
public interface PayStrategy {

    ResponseResult<Object> pay(String channel, BigDecimal amount) throws Exception;
}
