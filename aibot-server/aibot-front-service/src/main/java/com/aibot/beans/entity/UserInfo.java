package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 用户信息表
 *
 * @author: yuanshuai
 * @create: 2023-03-27 11:19
 */
@Data
public class UserInfo {

  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 用户id
   */
  private Integer userId;

  /**
   * 昵称
   */
  private String nickName;

  /**
   * 头像
   */
  private String avatar;

  /**
   * 真实姓名
   */
  private String trueName;

  /**
   * 身份证号
   */
  private String cerNumber;
}
