package com.aibot.beans.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 下级用户列表
 *
 * @author: aabb
 * @create: 2023-03-28 13:40
 */
@Data
public class TenentUserInfo {

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
   * 租户id
   */
  private String tenantName;

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
   * 租户域名
   */
  private String tenantHost;

  /**
   * 创建时间
   */
  private Date createTime;


}
