package com.aibot.mapper;

import com.aibot.beans.entity.UserAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员Mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-26 20:29
 */
@Mapper
public interface UserAdminMapper extends BaseMapper<UserAdmin> {
}
