package com.aibot.enums;

import lombok.Getter;

/**
 * chat角色枚举
 *
 * @author: aabb
 * @create: 2023-03-14 12:09
 */
@Getter
public enum ChatRoleEnum {

  /**
   * 用户
   */
  USER("user", "用户"),

  /**
   * chatgpt
   */
  ASSISTANT("assistant", "chatGpt")

          ;



  private String role;

  private String des;

  private ChatRoleEnum() {

  }

  ChatRoleEnum(String role, String des) {
    this.role = role;
    this.des = des;
  }

}
