package com.aibot.service;

import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.entity.*;
import com.aibot.beans.vo.UserList;
import com.aibot.constants.enums.ResultCode;
import com.aibot.constants.enums.UserRoleEnum;
import com.aibot.mapper.UserMapper;
import com.aibot.mapper.UserRelationMapper;
import com.aibot.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
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
    User user = userMapper.selectUserLogin(dto.getAccount(), dto.getPassword());
    // 用户不存在或者密码错误
    if (null == user) {
      return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), ResultCode.USER_LOGIN_ERROR.getMsg());
    }
    if (user.getStatus() != 0) {
      return new ResponseResult<>(ResultCode.USER_LOGIN_ERROR.getCode(), "账号状态异常，登录失败");
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "登录成功", JwtUtil.createToken(user));
  }


  public ResponseResult<HashMap<String, Object>> subUsers(String account, String nickName, String trueName, String cerNumber, Integer pageNum, Integer pageSize) {

    // 获取所有用户信息
    MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
            .select(User::getId, User::getAccount, User::getUserParentId, User::getShareCode, User::getRole, User::getCreateTime, User::getStatus)
            .leftJoin(UserInfo.class, UserInfo::getUserId, User::getUserParentId, ext->ext
                    .selectAs(UserInfo::getNickName,UserList::getUserParentName))
            .leftJoin(UserInfo.class, UserInfo::getUserId, User::getId, ext -> ext
                    .selectAs(UserInfo::getNickName, UserList::getNickName)
                    .selectAs(UserInfo::getTrueName, UserList::getTrueName)
                    .selectAs(UserInfo::getCerNumber, UserList::getCerNumber))
            .leftJoin(UserInfo.class, UserInfo::getUserId, User::getTenantId, ext -> ext
                    .selectAs(UserInfo::getNickName, UserList::getTenantName))
            .leftJoin(UserMoney.class, UserMoney::getUserId, User::getId)
            .select(UserMoney::getAmount)
            .like(StringUtils.isNotBlank(account), User::getAccount, account)
            .like(StringUtils.isNotBlank(nickName), UserInfo::getNickName, nickName)
            .like(StringUtils.isNotBlank(trueName), UserInfo::getTrueName, trueName)
            .like(StringUtils.isNotBlank(cerNumber), UserInfo::getCerNumber, cerNumber)
            ;

    // 分页
    Page<UserList> listPage = userMapper.selectJoinPage(new Page<>(pageNum, pageSize), UserList.class, wrapper);

    HashMap<String, Object> map = new HashMap<>();
    map.put("userList", listPage.getRecords());
    map.put("totalCount", listPage.getTotal());

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", map);

  }

  public ResponseResult<String> userRole() {

    String role = request.getAttribute("role").toString();

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", role);

  }
}
