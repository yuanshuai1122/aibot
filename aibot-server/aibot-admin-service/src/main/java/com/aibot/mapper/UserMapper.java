package com.aibot.mapper;

import com.aibot.beans.entity.User;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户mapper
 *
 * @author: yuanshuai
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
  @Select("select * from user where account = #{account} and password = #{password}")
  @InterceptorIgnore(tenantLine = "true")
  User selectUserLogin(@Param("account") String account, @Param("password") String password);


}
