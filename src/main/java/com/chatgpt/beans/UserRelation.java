package com.chatgpt.beans;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

/**
 * 用户关系表
 *
 * @author: yuanshuai
 * @create: 2023-03-21 12:04
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRelation {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private Integer userParentId;

  private Integer userChildrenId;

  private Integer userLevel;

}
