package com.aibot.controller;

import com.aibot.beans.dto.AddChannelDTO;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.enums.ResultCode;
import com.aibot.service.ChannelService;
import com.aibot.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * 渠道商控制器
 *
 * @author: yuanshuai
 * @create: 2023-04-09 19:35
 */
@Slf4j
@RestController
@RequestMapping("/channel")
public class ChannelController {

  @Autowired
  private RoleService roleService;

  @Autowired
  private ChannelService channelService;


  @GetMapping("/list")
  public ResponseResult<HashMap<String, Object>> channelList(@RequestParam(value = "account", required = false) String account,
                                                             @RequestParam(value = "nickName", required = false) String nickName,
                                                             @RequestParam(value = "trueName", required = false) String trueName,
                                                             @RequestParam(value = "cerNumber", required = false) String cerNumber,
                                                             @RequestParam("pageNum") Integer pageNum,
                                                             @RequestParam("pageSize") Integer pageSize) {

    if (!roleService.isSuperAdmin()) {
      return new ResponseResult<>(ResultCode.UNAUTHORISED.getCode(), ResultCode.UNAUTHORISED.getMsg());
    }

    return channelService.channelList(account, nickName,trueName, cerNumber ,pageNum, pageSize);
  }

  @PostMapping("/add")
  public ResponseResult<Object> addChannel(@RequestBody @Valid AddChannelDTO dto) {

    if (!roleService.isSuperAdmin()) {
      return new ResponseResult<>(ResultCode.UNAUTHORISED.getCode(), ResultCode.UNAUTHORISED.getMsg());
    }

    return channelService.addChannel(dto);
  }

}
