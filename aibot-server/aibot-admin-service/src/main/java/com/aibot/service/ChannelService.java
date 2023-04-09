package com.aibot.service;

import com.aibot.beans.dto.AddChannelDTO;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.TenantInfo;
import com.aibot.beans.entity.User;
import com.aibot.beans.entity.UserInfo;
import com.aibot.beans.entity.UserMoney;
import com.aibot.beans.vo.TenentUserInfo;
import com.aibot.beans.vo.UserList;
import com.aibot.constants.enums.ResultCode;
import com.aibot.constants.enums.UserRoleEnum;
import com.aibot.mapper.TenantInfoMapper;
import com.aibot.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.HashMap;

/**
 * 渠道商服务
 *
 * @author: yuanshuai
 * @create: 2023-04-09 19:37
 */
@Service
@Slf4j
public class ChannelService {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private TenantInfoMapper tenantInfoMapper;

  /**
   * 获取渠道商列表
   * @return
   */
  public ResponseResult<HashMap<String, Object>> channelList(String account, String nickName, String trueName, String cerNumber, Integer pageNum, Integer pageSize) {

    // 获取所有租户信息
    MPJLambdaWrapper<User> wrapper = new MPJLambdaWrapper<User>()
            .select(User::getId, User::getAccount, User::getUserParentId, User::getShareCode, User::getRole, User::getCreateTime, User::getStatus)
            .leftJoin(UserInfo.class, UserInfo::getUserId, User::getUserParentId, ext->ext
                    .selectAs(UserInfo::getNickName, UserList::getUserParentName))
            .leftJoin(UserInfo.class, UserInfo::getUserId, User::getId, ext -> ext
                    .selectAs(UserInfo::getNickName, UserList::getNickName)
                    .selectAs(UserInfo::getTrueName, UserList::getTrueName)
                    .selectAs(UserInfo::getCerNumber, UserList::getCerNumber))
            .leftJoin(UserInfo.class, UserInfo::getUserId, User::getTenantId, ext -> ext
                    .selectAs(UserInfo::getNickName, UserList::getTenantName))
            .leftJoin(UserMoney.class, UserMoney::getUserId, User::getId)
            .select(UserMoney::getAmount)
            .leftJoin(TenantInfo.class, TenantInfo::getTenantId, User::getId)
            .select(TenantInfo::getTenantHost)
            .like(StringUtils.isNotBlank(account), User::getAccount, account)
            .like(StringUtils.isNotBlank(nickName), UserInfo::getNickName, nickName)
            .like(StringUtils.isNotBlank(trueName), UserInfo::getTrueName, trueName)
            .like(StringUtils.isNotBlank(cerNumber), UserInfo::getCerNumber, cerNumber)
            .eq(User::getRole, UserRoleEnum.CHANNEL.getRole())
            ;

    // 分页
    Page<TenentUserInfo> listPage = userMapper.selectJoinPage(new Page<>(pageNum, pageSize), TenentUserInfo.class, wrapper);

    HashMap<String, Object> map = new HashMap<>();
    map.put("userList", listPage.getRecords());
    map.put("totalCount", listPage.getTotal());

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", map);

  }


  /**
   * 添加渠道商
   * @param dto
   * @return
   */
  public ResponseResult<Object> addChannel(AddChannelDTO dto) {

    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(User::getShareCode, dto.getShareCode());
    User user = userMapper.selectOne(wrapper);
    if (null == user) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "用户不存在，请先注册");
    }
    if (!user.getRole().equals(UserRoleEnum.USER.getRole())) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "只允许对普通用户进行此操作");
    }

    // 添加到渠道商表
    TenantInfo tenantInfo = new TenantInfo();
    tenantInfo.setTenantId(user.getId());
    tenantInfo.setTenantHost(dto.getTenantHost());
    tenantInfo.setCreateTime(new Date());
    tenantInfo.setUpdateTime(new Date());
    tenantInfoMapper.insert(tenantInfo);

    // 更新渠道商用户信息
    user.setTenantId(user.getId());
    user.setUserParentId(user.getId());
    user.setRole(UserRoleEnum.CHANNEL.getRole());
    userMapper.updateById(user);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "操作成功", null);

  }
}
