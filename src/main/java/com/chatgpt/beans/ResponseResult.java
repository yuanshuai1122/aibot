package com.chatgpt.beans;

import com.chatgpt.constants.enums.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @param <T> T 返回的数据类型泛型
 * @JsonInclude(JsonInclude.Include.NON_NULL) 对象返回的字段中为null(空)的字段不进行序列化，也就是不会显示到响应体中
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {

  /**
   * 状态码
   */
  private Integer code;

  /**
   * 提示信息，如果有错误时，前端可以获取该字段进行提示
   */
  private String msg;

  /**
   * 返回的结果数据
   */
  private T data;

  private ResponseResult() {

  }

  public ResponseResult(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public ResponseResult(Integer code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
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

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public static <T> ResponseResult<T> setCommonStatusAndData(ResultCode resultCode, T data){
    return new ResponseResult<>(resultCode.getCode(), resultCode.getMsg(),data);
  }

  public static ResponseResult setCommonStatusNoData(ResultCode resultCode){
    return new ResponseResult<>(resultCode.getCode(), resultCode.getMsg());
  }

}