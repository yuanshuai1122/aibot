package com.aibot.mapper;

import com.aibot.beans.dto.entity.ChatApiKey;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * chat api key mapper
 *
 * @author: aabb
 * @create: 2023-03-24 20:52
 */
@Mapper
public interface ChatApiKeyMapper extends MPJBaseMapper<ChatApiKey> {


  /**
   * 随机取出一条key
   * @return 随机一条key
   */
  @Select("SELECT * FROM chat_api_key ORDER BY RAND() LIMIT 1")
  ChatApiKey selectRandomKey();

}
