package com.aibot.service;

import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.entity.LoginUser;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.User;
import com.aibot.beans.entity.UserAdmin;
import com.aibot.constants.enums.ResultCode;
import com.aibot.constants.enums.UserRoleEnum;
import com.aibot.mapper.UserAdminMapper;
import com.aibot.mapper.UserMapper;
import com.aibot.mapper.UserRelationMapper;
import com.aibot.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author: yuanshuai
 * @create: 2023-03-20 11:51
 */
@Service
@Slf4j
public class UserService {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserAdminMapper userAdminMapper;

  @Autowired
  private HttpServletRequest request;


  /**
   * 用户登录服务
   * @param dto 用户登录实体
   * @return 登录结果
   */
  public ResponseResult<String> login(LoginDTO dto) {

    // TODO 这里记得做防刷

    LoginUser loginUser = new LoginUser();

    // 查询管理员表
    QueryWrapper<UserAdmin> adminWrapper = new QueryWrapper<>();
    adminWrapper.lambda()
            .eq(UserAdmin::getAccount, dto.getAccount())
            .eq(UserAdmin::getPassword, dto.getPassword());
    UserAdmin userAdmin = userAdminMapper.selectOne(adminWrapper);
    if (null != userAdmin) {
      loginUser.setId(userAdmin.getId());
      loginUser.setAccount(userAdmin.getAccount());
      loginUser.setRole(userAdmin.getRole());
      return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "登录成功", JwtUtil.createToken(loginUser));
    }


    // 查询数据库
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.lambda()
            .eq(User::getAccount, dto.getAccount())
            .eq(User::getPassword, dto.getPassword());
    User user = userMapper.selectOne(wrapper);
    // 用户不存在或者密码错误
    if (null == user) {
      return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), ResultCode.USER_LOGIN_ERROR.getMsg());
    }

    // 返回token
    loginUser.setId(user.getId());
    loginUser.setAccount(user.getAccount());
    loginUser.setRole(UserRoleEnum.COMMON_USER.getRole());
    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "登录成功", JwtUtil.createToken(loginUser));
  }

  /**
   * 获取下级用户
   * @param account 账号
   * @param level 等级
   * @return 用户信息
   */
  public ResponseResult<User> subUsers(String account, Integer level) {

    // 获取用户的角色
    String role = request.getAttribute("role").toString();

    // 超管可以查询全部用户

    return null;
  }
}
