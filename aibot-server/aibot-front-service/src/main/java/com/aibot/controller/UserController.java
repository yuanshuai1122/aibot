package com.aibot.controller;

import com.aibot.annotation.AccessLimit;
import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.dto.RealNameDTO;
import com.aibot.beans.dto.RegisterDTO;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.RealNameVO;
import com.aibot.beans.vo.UserInfoVO;
import com.aibot.constants.RegConstants;
import com.aibot.constants.enums.ResultCode;
import com.aibot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 *
 * @author: aabb
 * @create: 2023-03-20 11:39
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;


  @PostMapping("/login")
  //@AccessLimit(second = 30, maxTime = 2, forbiddenTime = 40L)
  public ResponseResult<String> login(@RequestBody @Valid LoginDTO dto) {
    log.info("开始请求登录, account: {}, type: {}", dto.getAccount(), dto.getType());
    if (!dto.getAccount().matches(RegConstants.PHONE_REG)) {
      return new ResponseResult<>(ResultCode.PARAM_IS_INVAlID.getCode(), "请输入正确格式手机号");
    }
    return userService.login(dto);
  }

  @PostMapping("/register")
  public ResponseResult<String> register(@RequestBody @Valid RegisterDTO dto) {
    log.info("开始请求注册，account: {}", dto.getAccount());
    if (!dto.getAccount().matches(RegConstants.PHONE_REG)) {
      return new ResponseResult<>(ResultCode.PARAM_IS_INVAlID.getCode(), "请输入正确格式手机号");
    }
    return userService.register(dto);

  }

  @GetMapping("/info")
  public ResponseResult<UserInfoVO> info() {
    return userService.info();
  }


  @PostMapping("/realname")
  public ResponseResult<Object> realname(@RequestBody @Valid RealNameDTO dto) {
    return userService.realname(dto);
  }

  @GetMapping("/realname/info")
  public ResponseResult<RealNameVO> realnameInfo() {

    return userService.realnameInfo();
  }

}
