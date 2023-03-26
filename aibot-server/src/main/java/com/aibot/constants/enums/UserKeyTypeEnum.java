package com.aibot.constants.enums;

import lombok.Getter;

/**
 * 用户key类型
 *
 * @author: yuanshuai
 * @create: 2023-03-14 12:09
 */
@Getter
public enum UserKeyTypeEnum {

  /**
   * 体验
   */
  Try("try",3000,  "体验15天"),

  /**
   * chatgpt
   */
  Month("month",6000, "包月")

          ;



  private String type;

  private Integer times;

  private String des;

  private UserKeyTypeEnum() {

  }

  UserKeyTypeEnum(String type, Integer times, String des) {
    this.type = type;
    this.times = times;
    this.des = des;
  }

}
