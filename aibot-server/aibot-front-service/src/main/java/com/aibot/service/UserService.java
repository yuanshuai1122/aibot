package com.aibot.service;

import com.aibot.beans.dto.RealNameDTO;
import com.aibot.beans.entity.*;
import com.aibot.beans.vo.UserInfoVO;
import com.aibot.constants.enums.UserRoleEnum;
import com.aibot.mapper.*;
import com.aibot.utils.JwtUtil;
import com.aibot.utils.ValueUtils;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.dto.RegisterDTO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.utils.ShareCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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

  @Autowired
  private UserInfoMapper userInfoMapper;

  @Autowired
  private UserRealnameMapper userRealnameMapper;

  @Autowired
  private UserMoneyMapper userMoneyMapper;

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
    if (user.getStatus() == 1) {
      return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), "登录失败，账号状态异常");
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

    // 获取站点租户id
    int tenantId = Integer.parseInt(request.getAttribute("tenantId").toString());
    // 这里谁的站点 就初始化为谁的id
    Integer userParentId = tenantId;

    // 验证推广码是否存在
    if (StringUtils.isNotBlank(dto.getShareCode())) {
      Integer shareId = ShareCodeUtils.codeToId(dto.getShareCode());
      User shareUser = userMapper.selectById(shareId);
      if (null == shareUser) {
        return new ResponseResult<>(ResultCode.USER_REGISTER_SHARE_CODE_NOT_EXIT.getCode(), ResultCode.USER_REGISTER_SHARE_CODE_NOT_EXIT.getMsg());
      }
      userParentId = shareUser.getId();

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
    User registerUser = new User(null, tenantId, dto.getAccount(), dto.getPassword(), userParentId, null, UserRoleEnum.USER.getRole(), 0, new Date());
    int flag = userMapper.insert(registerUser);
    if (flag <= 0) {
      return new ResponseResult<>(ResultCode.USER_REGISTER_ERROR.getCode(), ResultCode.USER_REGISTER_ERROR.getMsg());
    }
    // 插入注册码
    registerUser.setShareCode(ShareCodeUtils.idToCode(registerUser.getId()));
    userMapper.updateById(registerUser);

    // 插入到用户信息表
    UserInfo userInfo = new UserInfo();
    userInfo.setUserId(registerUser.getId());
    userInfoMapper.insert(userInfo);

    // 插入到关系表
    UserRelation userRelation = new UserRelation(null, registerUser.getId(), registerUser.getId(), 0);
    // 先把自己和自己的关系插入
    userRelationMapper.insert(userRelation);
    // 如果有推荐人 则将推荐关系都写入
    if (userParentId != tenantId) {
      userRelationMapper.insertRegRelation(registerUser.getId(), userParentId);
    }

    // 插入到钱包
    UserMoney userMoney = new UserMoney(null, registerUser.getId(), new BigDecimal(0), new Date(), new Date());
    userMoneyMapper.insert(userMoney);

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

  /**
   * 实名接口
   * @param dto
   * @return
   */
  public ResponseResult<Object> realname(RealNameDTO dto) {

    // TODO 查询本地实名库
    QueryWrapper<UserRealname> realnameWrapper = new QueryWrapper<>();
    realnameWrapper.lambda()
            .eq(UserRealname::getTrueName, dto.getTrueName())
            .eq(UserRealname::getCerNumber, dto.getCerNumber());
    UserRealname userRealname = userRealnameMapper.selectOne(realnameWrapper);
    if (null == userRealname) {
      // TODO 调用第三方实名接口
    }

    // 写入用户信息表
    int userId = Integer.parseInt(request.getAttribute("userId").toString());
    QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(UserInfo::getUserId, userId);
    UserInfo userInfo = new UserInfo();
    userInfo.setTrueName(dto.getTrueName());
    userInfo.setCerNumber(dto.getCerNumber());
    int update = userInfoMapper.update(userInfo, wrapper);
    if (update <= 0) {
      return new ResponseResult<>(ResultCode.USER_REAL_NAME_FAILURE.getCode(), ResultCode.USER_REAL_NAME_FAILURE.getMsg());
    }

    // 写入到实名表
    UserRealname realname = new UserRealname(null, userId, dto.getTrueName(), dto.getCerNumber(), 1, new Date(), new Date());
    int insert = userRealnameMapper.insert(realname);
    if (insert <= 0) {
      return new ResponseResult<>(ResultCode.USER_REAL_NAME_FAILURE.getCode(), ResultCode.USER_REAL_NAME_FAILURE.getMsg());
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "实名成功", null);
  }
}
