package com.aibot.beans.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * api的key管理
 *
 * @author: yuanshuai
 * @create: 2023-04-09 17:57
 */
@Data
public class ChatApiKeyVO {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private String apiKey;

  private String account;

  private Boolean status;

}
