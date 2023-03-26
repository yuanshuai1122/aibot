package com.aibot.service;

import com.aibot.beans.vo.UserInfoVO;
import com.aibot.utils.ValueUtils;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.dto.RegisterDTO;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.User;
import com.aibot.beans.entity.UserRelation;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.UserMapper;
import com.aibot.mapper.UserRelationMapper;
import com.aibot.utils.JwtUtil;
import com.aibot.utils.ShareCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

  @Autowired
  private UserRelationMapper userRelationMapper;
  
  @Autowired
  private HttpServletRequest request;

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

  /**
   * 注册
   * @param dto 注册实体
   * @return 注册结果
   */
  public ResponseResult<String> register(RegisterDTO dto) {

    Integer userParentId = 0;
    Integer shareUserParentId = null;

    // 验证推广码是否存在
    if (StringUtils.isNotBlank(dto.getShareCode())) {
      Integer shareId = ShareCodeUtils.codeToId(dto.getShareCode());
      // TODO 这里查缓存
      User shareUser = userMapper.selectById(shareId);
      if (null == shareUser) {
        return new ResponseResult<>(ResultCode.USER_REGISTER_SHARE_CODE_NOT_EXIT.getCode(), ResultCode.USER_REGISTER_SHARE_CODE_NOT_EXIT.getMsg());
      }
      userParentId = shareUser.getId();

      // 赋值父id
      if (shareUser.getUserParentId() != 0) {
        shareUserParentId = shareUser.getUserParentId();
      }

    }

    // 查询数据库 验证是否已注册
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(User::getAccount, dto.getAccount());
    User user = userMapper.selectOne(wrapper);
    // 已经注册
    if (null != user) {
      return new ResponseResult<>(ResultCode.USER_REGISTER_REPEAT.getCode(), ResultCode.USER_REGISTER_REPEAT.getMsg());
    }

    // 开始注册
    User registerUser = new User(null, dto.getAccount(), dto.getPassword(), userParentId, null, new Date());
    int flag = userMapper.insert(registerUser);
    if (flag <= 0) {
      return new ResponseResult<>(ResultCode.USER_REGISTER_ERROR.getCode(), ResultCode.USER_REGISTER_ERROR.getMsg());
    }
    // 插入注册码
    registerUser.setShareCode(ShareCodeUtils.idToCode(registerUser.getId()));
    userMapper.updateById(registerUser);

    // 插入到关系表
    if (userParentId != 0) {
      UserRelation userRelation = new UserRelation(null, userParentId, registerUser.getId(), 1);
      userRelationMapper.insert(userRelation);
    }
    if (null != shareUserParentId) {
      UserRelation userRelation = new UserRelation(null, shareUserParentId, registerUser.getId(), 2);
      userRelationMapper.insert(userRelation);
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "注册成功");
  }

  /**
   * 获取登录用户信息
   * @return 登录用户信息
   */
  public ResponseResult<UserInfoVO> info() {

    UserInfoVO userInfo = new UserInfoVO();

    String account = request.getAttribute("account").toString();
    log.info("开始获取登录信息, account: {}", account);
    String maskAccount = ValueUtils.getMaskAccount(account);
    userInfo.setAccount(maskAccount);

    // TODO 这里先写死
    userInfo.setAvatar("http://img.520touxiang.com/uploads/allimg/2018121219/umqqfexihiv.jpg");

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", userInfo);
  }
}