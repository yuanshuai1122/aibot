package com.aibot.beans.vo;

import lombok.Data;

/**
 * 用户信息返回体
 *
 * @author: aabb
 * @create: 2023-03-26 15:48
 */
@Data
public class UserInfoVO {

  /**
   * 头像
   */
  private String avatar;

  /**
   * 账号
   */
  private String account;

  /**
   * 昵称
   */
  private String nickname;

  /**
   * 是否实名
   */
  private Integer isRealName;


  // ...

}
