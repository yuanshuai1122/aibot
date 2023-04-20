package com.aibot.mapper;

import com.aibot.beans.entity.UserRealname;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户实名Mapper
 *
 * @author: aabb
 * @create: 2023-03-29 10:01
 */
@Mapper
public interface UserRealnameMapper extends MPJBaseMapper<UserRealname> {
}
