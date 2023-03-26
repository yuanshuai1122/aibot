package com.aibot.constants.enums;

import lombok.Getter;

/**
 * chat角色枚举
 *
 * @author: yuanshuai
 * @create: 2023-03-14 12:09
 */
@Getter
public enum ChatKeyStatusEnum {

  /**
   * 已激活
   */
  ACTIVED("actived", "已激活"),

  /**
   * 未激活
   */
  NOACTIVE("noactive", "未激活")

          ;



  private String status;

  private String des;

  private ChatKeyStatusEnum() {

  }

  ChatKeyStatusEnum(String status, String des) {
    this.status = status;
    this.des = des;
  }

}
