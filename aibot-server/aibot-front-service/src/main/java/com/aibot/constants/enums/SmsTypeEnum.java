package com.aibot.constants.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 短信类型枚举
 */
@Getter
public enum SmsTypeEnum {

  /**
   * 注册
   */
  REGISTER("REGISTER", "0000"),

  /**
   *登录
   */
  LOGIN("LOGIN", "0000"),




  ;



  private String type;

  private String code;

  private SmsTypeEnum() {

  }

  SmsTypeEnum(String type, String code) {
    this.type = type;
    this.code = code;
  }


  public static SmsTypeEnum search(String type) {
    Optional<SmsTypeEnum> findFirst = Arrays.stream(SmsTypeEnum.values())
            .filter(p -> p.getType().equals(type))
            .findFirst();
    return findFirst.orElse(null);
  }



}
