package com.aibot.service;

import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.User;
import com.aibot.constants.enums.ResultCode;
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


  /**
   * 用户登录服务
   * @param dto 用户登录实体
   * @return 登录结果
   */
  public ResponseResult<String> login(LoginDTO dto) {

    // TODO 这里记得做防刷

    // 查询数据库
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(User::getAccount, dto.getAccount());
    User user = userMapper.selectOne(wrapper);
    // 用户不存在
    if (null == user) {
      return new ResponseResult<>(ResultCode.USER_NOT_EXIST.getCode(), ResultCode.USER_NOT_EXIST.getMsg());
    }
    // 密码不正确
    if (!user.getPassword().equals(dto.getPassword())) {
      return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), ResultCode.USER_LOGIN_ERROR.getMsg());
    }

    // 返回token
    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "登录成功", JwtUtil.createToken(user));
  }

}
