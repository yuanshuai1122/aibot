package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.util.Date;

/**
 * 用户实名
 *
 * @author: yuanshuai
 * @create: 2023-03-29 09:57
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRealname {

  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 用户id
   */
  private Integer userId;

  /**
   * 真实姓名
   */
  private String trueName;

  /**
   * 身份证号
   */
  private String cerNumber;

  /**
   * 是否实名
   */
  private Integer isVerify;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date updateTime;

}
