package com.aibot.beans.vo;

import com.aibot.beans.entity.User;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 下级用户列表
 *
 * @author: yuanshuai
 * @create: 2023-03-28 13:40
 */
@Data
public class UserList {

  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 账号
   */
  private String account;

  /**
   * 上级用户id
   */
  private Integer userParentId;

  /**
   * 上级用户姓名
   */
  private String userParentName;

  /**
   * 推广码
   */
  private String shareCode;

  /**
   * 角色
   */
  private String role;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 昵称
   */
  private String nickName;

  /**
   * 真实姓名
   */
  private String trueName;

  /**
   * 身份证号
   */
  private String cerNumber;

  /**
   * 余额
   */
  private BigDecimal amount;

  /**
   * 创建时间
   */
  private Date createTime;


}
