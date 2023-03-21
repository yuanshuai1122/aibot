package com.chatgpt.beans;


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

  private Integer id;

  private Integer userParentId;

  private Integer userChildrenId;

  private Integer userLevel;

}
