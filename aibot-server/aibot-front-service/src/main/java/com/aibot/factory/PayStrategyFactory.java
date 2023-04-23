package com.aibot.factory;

import com.aibot.constants.enums.PayEnum;
import com.aibot.pay.PayStrategy;
import com.aibot.utils.SpringContextUtil;

/**
 * @author:yuanshuai
 * @create: 2022-10-08 14:05
 * @Description: 通过渠道码获取对应的策略实现类的工厂
 */
public class PayStrategyFactory {
    /**
     * 通过渠道码获取支付策略具体实现类
     * */
    public static PayStrategy getPayStrategy(String channel){
        PayEnum payEnum = PayEnum.findPayEnumBychannel(channel);
        if(payEnum == null){
            return null;
        }
        return SpringContextUtil.getBean(payEnum.getBeanName(),PayStrategy.class);
    }
}
