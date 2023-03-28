package com.aibot.service;

import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.entity.*;
import com.aibot.constants.enums.ResultCode;
import com.aibot.constants.enums.UserRoleEnum;
import com.aibot.mapper.UserAdminMapper;
import com.aibot.mapper.UserMapper;
import com.aibot.mapper.UserRelationMapper;
import com.aibot.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

  @Autowired
  private UserRelationMapper userRelationMapper;


  /**
   * 用户登录服务
   * @param dto 用户登录实体
   * @return 登录结果
   */
  public ResponseResult<String> login(LoginDTO dto) {

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

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "登录成功", JwtUtil.createToken(user));
  }


  public ResponseResult<List<User>> subUsers(String account, String nickName,String trueName, String cerNumber, Integer pageNum, Integer pageSize) {

    // 获取用户的角色
    String role = request.getAttribute("role").toString();



    return null;
  }
}
