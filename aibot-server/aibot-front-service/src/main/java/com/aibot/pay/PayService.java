package com.aibot.pay;

import com.aibot.beans.entity.ResponseResult;

import java.math.BigDecimal;

/**
 * @author:yuanshuai
 * @create: 2022-10-08 11:37
 * @Description: 支付服务
 */
public interface PayService {

    /**
     * 支付服务
     * @param channel 渠道
     * @param amount 消费金额
     * @return String
     * @throws Exception
     */
    ResponseResult<Object> pay(String channel, BigDecimal amount) throws Exception;
}
