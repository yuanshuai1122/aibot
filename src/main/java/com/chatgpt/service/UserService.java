package com.chatgpt.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chatgpt.beans.ResponseResult;
import com.chatgpt.beans.User;
import com.chatgpt.beans.dto.LoginDTO;
import com.chatgpt.beans.dto.RegisterDTO;
import com.chatgpt.constants.enums.ResultCode;
import com.chatgpt.mapper.UserMapper;
import com.chatgpt.utils.JwtUtil;
import com.chatgpt.utils.RedisKeyUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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
  private RedisTemplate<String, String> redisTemplate;

  /**
   * 用户登录服务
   * @param dto 用户登录实体
   * @return 登录结果
   */
  public ResponseResult<String> login(LoginDTO dto) {

    // 查询缓存
    String value = redisTemplate.opsForValue().get(RedisKeyUtils.getLoginKey(dto.getAccount()));
    if (null != value) {
      User user = JSON.parseObject(value, User.class);
      if (!user.getPassword().equals(dto.getPassword())) {
        return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), ResultCode.USER_LOGIN_ERROR.getMsg());
      }
      String token = JwtUtil.createToken(user);
      return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "登录成功", token);
    }

    // 查询数据库
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(User::getAccount, dto.getAccount());
    User user = userMapper.selectOne(wrapper);
    if (null == user) {
      return new ResponseResult<>(ResultCode.USER_NOT_EXIST.getCode(), ResultCode.USER_NOT_EXIST.getMsg());
    }
    if (!user.getPassword().equals(dto.getPassword())) {
      return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), ResultCode.USER_LOGIN_ERROR.getMsg());
    }

    // 写入缓存
    redisTemplate.opsForValue().set(RedisKeyUtils.getLoginKey(user.getAccount()), JSON.toJSONString(user), 24, TimeUnit.HOURS);

    // 返回token

    String token = JwtUtil.createToken(user);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "登录成功", token);
  }

  /**
   * 注册
   * @param dto 注册实体
   * @return 注册结果
   */
  public ResponseResult<String> register(RegisterDTO dto) {

    // 查询目前登录的用户 来验证是否已经被注册
    String value = redisTemplate.opsForValue().get(RedisKeyUtils.getLoginKey(dto.getAccount()));
    if (null != value) {
      return new ResponseResult<>(ResultCode.USER_REGISTER_REPEAT.getCode(), ResultCode.USER_REGISTER_REPEAT.getMsg());
    }

    // 查询数据库
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(User::getAccount, dto.getAccount());
    User user = userMapper.selectOne(wrapper);
    if (null != user) {
      return new ResponseResult<>(ResultCode.USER_REGISTER_REPEAT.getCode(), ResultCode.USER_REGISTER_REPEAT.getMsg());
    }

    // 注册
    User registerUser = new User(null, dto.getAccount(), dto.getPassword());
    int flag = userMapper.insert(registerUser);
    if (flag <= 0) {
      return new ResponseResult<>(ResultCode.USER_REGISTER_ERROR.getCode(), ResultCode.USER_REGISTER_ERROR.getMsg());
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "注册成功");
  }
}
