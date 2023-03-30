package com.aibot.mapper;

import com.aibot.beans.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息Mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-27 11:22
 */
@Mapper
public interface UserInfoMapper extends MPJBaseMapper<UserInfo> {
}
