package com.aibot.constants.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 创作类型枚举
 */
@Getter
public enum CreationTypeEnum {


  /**
   * 订阅类型：次数
   */
  CREATION_WORDS("CREATION_WORDS", "文字创作"),

  /**
   * 订阅类型：时间
   */
  CREATION_IMAGES("CREATION_WORDS", "图片创作"),



  ;



  private String type;

  private String desc;

  private CreationTypeEnum() {

  }

  CreationTypeEnum(String type, String desc) {
    this.type = type;
    this.desc = desc;
  }


  public static CreationTypeEnum search(String type) {
    Optional<CreationTypeEnum> findFirst = Arrays.stream(CreationTypeEnum.values())
            .filter(p -> p.getType().equals(type))
            .findFirst();
    return findFirst.orElse(null);
  }



}
