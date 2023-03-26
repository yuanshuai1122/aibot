package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 管理员实体
 *
 * @author: yuanshuai
 * @create: 2023-03-26 20:27
 */
@Data
public class UserAdmin {

  /**
   * id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 账号
   */
  private String account;

  /**
   * 密码
   */
  private String password;

  /**
   * 角色
   */
  private String role;

  /**
   * 创建时间
   */
  private Date createTime;

}
