package com.aibot.service;

import com.aibot.beans.dto.RealNameDTO;
import com.aibot.beans.entity.*;
import com.aibot.beans.vo.RealNameVO;
import com.aibot.beans.vo.UserInfoVO;
import com.aibot.constants.LoginTypeConstants;
import com.aibot.constants.RegConstants;
import com.aibot.constants.enums.SmsTypeEnum;
import com.aibot.constants.enums.UserRoleEnum;
import com.aibot.mapper.*;
import com.aibot.utils.JwtUtil;
import com.aibot.utils.RedisKeyUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  @Autowired
  private TenantInfoMapper tenantInfoMapper;

  /**
   * 用户登录服务
   * @param dto 用户登录实体
   * @return 登录结果
   */
  public ResponseResult<String> login(LoginDTO dto) {

    // TODO 这里记得做防刷

    String url = request.getHeader("referer");
    if (StringUtils.isBlank(url)) {
      log.info("租户不存在，请联系管理员");
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "站点异常");
    }
    log.info("请求url为： {}", url);
    String domain = "";
    Pattern p = Pattern.compile(RegConstants.URL_REG);
    Matcher m = p.matcher(url);
    if (m.find()) {
      domain = m.group(1);
    }

    // 查询租户
    log.info("登录域名：{}", domain);
    QueryWrapper<TenantInfo> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(TenantInfo::getTenantHost, domain);
    TenantInfo tenantInfo = tenantInfoMapper.selectOne(wrapper);
    log.info("租户信息: {}", tenantInfo);
    if (null == tenantInfo) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "站点异常，请联系管理员");
    }

    User user = null;

    // 短信登录
    if (LoginTypeConstants.LOGIN_SMS.equals(dto.getType())) {
      if (StringUtils.isBlank(dto.getVerifyCode())) {
        return new ResponseResult<>(ResultCode.PARAM_IS_INVAlID.getCode(), ResultCode.SMS_PARAMS_ERROR.getMsg());
      }
      // 验证短信验证码
      String code = redisTemplate.opsForValue().get(RedisKeyUtils.getSmsVerifyKey(SmsTypeEnum.LOGIN.getType(), dto.getAccount()));
      if (StringUtils.isBlank(code) || !code.equals(dto.getVerifyCode())) {
        return new ResponseResult<>(ResultCode.FAILED.getCode(), "短信验证码错误", null);
      }
      // 删除短信验证码
      redisTemplate.delete(RedisKeyUtils.getSmsVerifyKey(SmsTypeEnum.LOGIN.getType(), dto.getAccount()));

      user = userMapper.selectUserLoginSMS(dto.getAccount(), tenantInfo.getTenantId());
      if (null == user) {
        return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), ResultCode.USER_LOGIN_ERROR.getMsg());
      }
      if (user.getStatus() == 1) {
        return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), "登录失败，账号状态异常");
      }

    }else if (LoginTypeConstants.LOGIN_PASSWORD.equals(dto.getType())) {
      // 账密登录
      if (StringUtils.isBlank(dto.getPassword())) {
        return new ResponseResult<>(ResultCode.PARAM_IS_INVAlID.getCode(), ResultCode.SMS_PARAMS_ERROR.getMsg());
      }
      // 查询数据库
      user = userMapper.selectUserLogin(dto.getAccount(), dto.getPassword(), tenantInfo.getTenantId());
      // 用户不存在
      if (null == user) {
        return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), ResultCode.USER_LOGIN_ERROR.getMsg());
      }
      if (user.getStatus() == 1) {
        return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), "登录失败，账号状态异常");
      }

    }else {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "非法登录类型", null);
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

    // 验证验证码
    String code = redisTemplate.opsForValue().get(RedisKeyUtils.getSmsVerifyKey(SmsTypeEnum.REGISTER.getType(), dto.getAccount()));
    if (StringUtils.isBlank(code) || !dto.getVerifyCode().equals(code)) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "短信验证码错误", null);
    }
    // 删除短信验证码
    redisTemplate.delete(RedisKeyUtils.getSmsVerifyKey(SmsTypeEnum.REGISTER.getType(), dto.getAccount()));

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

    QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(UserInfo::getUserId, Integer.parseInt(request.getAttribute("id").toString()));
    UserInfo userInfo = userInfoMapper.selectOne(wrapper);
    if (null == userInfo) {
      return new ResponseResult<>(ResultCode.USER_NOT_EXIST.getCode(), ResultCode.USER_NOT_EXIST.getMsg());
    }

    UserInfoVO userInfoVO = new UserInfoVO();
    userInfoVO.setAvatar(userInfo.getAvatar());
    userInfoVO.setAccount(ValueUtils.getMaskAccount(request.getAttribute("account").toString()));
    userInfoVO.setNickname(userInfo.getNickName());
    if (StringUtils.isNotBlank(userInfo.getTrueName()) && StringUtils.isNotBlank(userInfo.getCerNumber())) {
      userInfoVO.setIsRealName(1);
    }else {
      userInfoVO.setIsRealName(0);
    }


    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", userInfoVO);
  }

  /**
   * 实名接口
   * @param dto
   * @return
   */
  public ResponseResult<Object> realname(RealNameDTO dto) {

    int userId = Integer.parseInt(request.getAttribute("id").toString());
    // 验证是否已经实名
    QueryWrapper<UserInfo> infoWrapper = new QueryWrapper<>();
    infoWrapper.lambda().eq(UserInfo::getUserId, userId);
    UserInfo info = userInfoMapper.selectOne(infoWrapper);
    if (null == info) {
      return new ResponseResult<>(ResultCode.USER_NOT_EXIST.getCode(), ResultCode.USER_NOT_EXIST.getMsg());
    }
    if (StringUtils.isNotBlank(info.getTrueName()) && StringUtils.isNotBlank(info.getCerNumber())) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "您已实名，无须重复操作");
    }

    // 查询本地实名库
    QueryWrapper<UserRealname> realnameWrapper = new QueryWrapper<>();
    realnameWrapper.lambda()
            .eq(UserRealname::getTrueName, dto.getTrueName())
            .eq(UserRealname::getCerNumber, dto.getCerNumber());
    UserRealname userRealname = userRealnameMapper.selectOne(realnameWrapper);
    if (null != userRealname) {
      info.setTrueName(dto.getTrueName());
      info.setCerNumber(dto.getCerNumber());
    }else {
      // TODO 调用第三方实名接口

      //if ()
      // 写入到实名表
      UserRealname realname = new UserRealname(null, dto.getTrueName(), dto.getCerNumber(), new Date(), new Date());
      int insert = userRealnameMapper.insert(realname);
      if (insert <= 0) {
        return new ResponseResult<>(ResultCode.USER_REAL_NAME_FAILURE.getCode(), ResultCode.USER_REAL_NAME_FAILURE.getMsg());
      }
      info.setTrueName("xxx");
      info.setCerNumber("xxx");
    }

    // 写入用户信息表
    int update = userInfoMapper.updateById(info);
    if (update <= 0) {
      return new ResponseResult<>(ResultCode.USER_REAL_NAME_FAILURE.getCode(), ResultCode.USER_REAL_NAME_FAILURE.getMsg());
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "实名成功", null);
  }

  public ResponseResult<RealNameVO> realnameInfo() {

    Integer userId = Integer.parseInt(request.getAttribute("id").toString());

    QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(UserInfo::getUserId, userId);
    UserInfo userInfo = userInfoMapper.selectOne(wrapper);
    if (null == userInfo) {
      return new ResponseResult<>(ResultCode.USER_NOT_EXIST.getCode(), ResultCode.USER_NOT_EXIST.getMsg());
    }
    RealNameVO realName = new RealNameVO();
    realName.setTrueName(userInfo.getTrueName());
    realName.setCerNumber(userInfo.getCerNumber());

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "查询成功", realName);

  }
}
