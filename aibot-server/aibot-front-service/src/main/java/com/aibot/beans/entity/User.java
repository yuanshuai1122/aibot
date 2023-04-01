package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.util.Date;

/**
 * 用户实体
 *
 * @author: yuanshuai
 * @create: 2023-03-20 11:32
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private Integer tenantId;

  private String account;

  private String password;

  private Integer userParentId;

  private String shareCode;

  private String role;

  private Integer status;

  private Date createTime;


}
