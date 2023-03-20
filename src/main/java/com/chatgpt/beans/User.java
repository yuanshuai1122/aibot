package com.chatgpt.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

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

  private String account;

  private String password;
}
