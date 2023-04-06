package com.aibot.constants.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
  /**
   * 通用状态码
   */
  SUCCESS(200,"OK"),
  FAILED(-1,"FAIL"),
  /*
  参数错误状态码
   */
  PARAM_IS_INVAlID(101,"参数无效"),

  PARAM_IS_BLANK(101,"参数为空"),
  /* 用户错误  201 - 299  */
  USER_NOT_LOGIN(201,"未登录"),
  USER_NOT_EXIST(202,"用户不存在"),
  USER_LOGIN_ERROR(203,"登陆失败，账号或者密码有误"),
  NOT_PERMISSION(204,"无权限访问"),
  USER_REGISTER_REPEAT(205,"注册失败，用户已存在"),
  USER_REGISTER_ERROR(206,"注册失败"),
  USER_REGISTER_SHARE_CODE_NOT_EXIT(207,"注册失败，推广码不存在"),
  USER_REAL_NAME_FAILURE(208,"实名失败，请稍后再试"),
  /* 业务错误 301 - 399*/
  /* 业务错误 301 - 399*/
  DATA_NOT_FOUND(301,"没有数据"),
  PRODUCT_NOT_FOUND(302, "商品不存在"),
  PRODUCT_NOT_PERMIT(303, "商品库存不足或已下架"),


  VISIT_OUT_TIMES(401,"访问频繁，请稍后再试"),

  /* 短信错误 */
  SMS_PARAMS_ERROR(601, "发送短信参数有误"),
  SMS_SYSTEM_ERROR(602, "发送短信出现系统错误"),
  SMS_TIMES_ERROR(603, "发端短信太频繁，请稍后再试"),
  SMS_FALIURE_ERROR(603, "发端短信失败，请稍后再试"),


  /**
   * 用户访问某个接口过于频繁--接口防刷
   */
  ACCESS_FREQUENT(50001, "访问过于频繁"),
  SERVER_ERROR(99999, "抱歉，系统繁忙，请稍后重试！");


  ;

  //返回状态码
  private Integer code;

  //返回消息
  private String msg;

  private ResultCode() {

  }

  ResultCode(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
