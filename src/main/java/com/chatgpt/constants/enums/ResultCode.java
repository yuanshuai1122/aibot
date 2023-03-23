package com.chatgpt.constants.enums;

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
  PARAM_IS_BLANK(102,"参数为空"),

  /* 用户错误  201 - 299  */
  USER_NOT_LOGIN(201,"未登录"),
  USER_NOT_EXIST(202,"用户不存在"),
  USER_LOGIN_ERROR(203,"登陆失败，账号或者密码有误"),
  NOT_PERMISSION(204,"无权限访问"),
  USER_REGISTER_REPEAT(205,"注册失败，用户已存在"),
  USER_REGISTER_ERROR(206,"注册失败"),
  USER_REGISTER_SHARE_CODE_NOT_EXIT(207,"注册失败，推广码不存在"),

  /* 业务错误 301 - 399*/
  DATA_NOT_FOUND(301,"没有数据"),
  PRODUCT_NOT_FOUND(302, "商品不存在")




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
