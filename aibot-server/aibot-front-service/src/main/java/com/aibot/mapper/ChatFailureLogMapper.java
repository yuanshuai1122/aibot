package com.aibot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aibot.beans.entity.ChatFailureLog;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 失败日志mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-24 20:39
 */
@Mapper
public interface ChatFailureLogMapper extends MPJBaseMapper<ChatFailureLog> {
}
