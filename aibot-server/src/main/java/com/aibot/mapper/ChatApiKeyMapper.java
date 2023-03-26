package com.aibot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aibot.beans.entity.ChatApiKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * chat api key mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-24 20:52
 */
@Mapper
public interface ChatApiKeyMapper extends BaseMapper<ChatApiKey> {


  /**
   * 随机取出一条key
   * @return 随机一条key
   */
  @Select("SELECT * FROM chat_api_key ORDER BY RAND() LIMIT 1")
  ChatApiKey selectRandomKey();

}
