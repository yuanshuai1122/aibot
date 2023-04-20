package com.aibot.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aibot.beans.entity.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户mapper
 *
 * @author: aabb
 * @create: 2023-03-20 11:50
 */
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {

  /**
   * 登录绕过租户
   * @param account 账号
   * @param password 密码
   * @return
   */
  @Select("select * from user where account = #{account} and password = #{password} and tenant_id = #{tenantId}")
  @InterceptorIgnore(tenantLine = "true")
  User selectUserLogin(@Param("account") String account, @Param("password") String password, @Param("tenantId") Integer tenantId);


  /**
   * 登录绕过租户
   * @param account 账号
   * @return
   */
  @Select("select * from user where account = #{account} and tenant_id = #{tenantId}")
  @InterceptorIgnore(tenantLine = "true")
  User selectUserLoginSMS(@Param("account") String account, @Param("tenantId") Integer tenantId);

}
