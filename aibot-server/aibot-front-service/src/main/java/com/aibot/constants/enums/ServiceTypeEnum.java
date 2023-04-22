package com.aibot.constants.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 服务类型枚举
 */
@Getter
public enum ServiceTypeEnum {


  /**
   * 订阅类型：次数
   */
  SUBS_TIME("SUBS_TIME", "次数"),

  /**
   * 订阅类型：时间
   */
  SUBS_DATE("SUBS_DATE", "时间"),



  ;



  private String type;

  private String desc;

  private ServiceTypeEnum() {

  }

  ServiceTypeEnum(String type, String desc) {
    this.type = type;
    this.desc = desc;
  }


  public static ServiceTypeEnum search(String type) {
    Optional<ServiceTypeEnum> findFirst = Arrays.stream(ServiceTypeEnum.values())
            .filter(p -> p.getType().equals(type))
            .findFirst();
    return findFirst.orElse(null);
  }



}
