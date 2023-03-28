package com.aibot.mapper;

import com.aibot.beans.entity.User;
import com.aibot.beans.vo.SubUserList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-20 11:50
 */
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {


}
