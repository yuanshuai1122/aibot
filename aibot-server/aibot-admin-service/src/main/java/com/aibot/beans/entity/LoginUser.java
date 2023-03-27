package com.aibot.beans.entity;

import lombok.Data;

/**
 * 登录实体
 *
 * @author: yuanshuai
 * @create: 2023-03-26 20:33
 */
@Data
public class LoginUser {

  /**
   * ID
   */
  private Integer id;

  /**
   * 账号
   */
  private String account;

  /**
   * 角色
   */
  private String role;

}
