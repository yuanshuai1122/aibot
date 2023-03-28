package com.aibot.constants.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author: yuanshuai
 * @create: 2023-03-26 20:36
 */
@Getter
public enum UserRoleEnum {

  /**
   * 超级管理员
   */
  SUPER_ADMIN("superAdmin", "超级管理员"),

  /**
   * 超级管理员
   */
  CHANNEL("channel", "渠道商"),

  /**
   * 用户
   */
  USER("user", "用户")



  ;



  private String role;

  private String des;

  private UserRoleEnum() {

  }

  UserRoleEnum(String role, String des) {
    this.role = role;
    this.des = des;
  }

}
