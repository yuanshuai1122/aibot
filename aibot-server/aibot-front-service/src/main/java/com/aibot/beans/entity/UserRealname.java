package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.util.Date;

/**
 * 用户实名
 *
 * @author: aabb
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
   * 真实姓名
   */
  private String trueName;

  /**
   * 身份证号
   */
  private String cerNumber;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date updateTime;

}
