package com.aibot.enums;


/**
 * @author:yuanshuai
 * @create: 2022-10-08 11:29
 * @Description: 支付枚举
 */
public enum PayEnum {
    /**
     * 支付宝支付
     */
    ALI_PAY("aliPay","支付宝支付","aliPayStrategy"),
    /**
     * 微信支付
     */
    WECHAT_PAY("wxNative","微信支付","wechatPayStrategy"),
    /**
     * 银联支付
     */
    UNION_PAY("unionNative","银联支付","unionPayStrategy")


    ;


    /**
     * 渠道
     */
    private String channel;
    /**
     * 描述
     */
    private String description;

    /**策略实现类对应的 beanName*/
    private String beanName;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }


    /**
     * 通过渠道码获取枚举
     * */
    public static PayEnum findPayEnumBychannel(String channel){
        PayEnum[] enums = PayEnum.values();
        for (PayEnum payEnum : enums){
            if(payEnum.getChannel().equals(channel)){
                return payEnum;
            }
        }
        return null;
    }
    //构造器
    PayEnum(String channel, String description, String beanName) {
        this.channel = channel;
        this.description = description;
        this.beanName = beanName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
