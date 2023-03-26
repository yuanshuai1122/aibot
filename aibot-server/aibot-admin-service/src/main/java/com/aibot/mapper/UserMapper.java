package com.aibot.mapper;

import com.aibot.beans.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-20 11:50
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}