package com.aibot.controller;

import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.dto.RegisterDTO;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.User;
import com.aibot.beans.vo.UserInfoVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 *
 * @author: yuanshuai
 * @create: 2023-03-20 11:39
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;


  @PostMapping("/login")
  public ResponseResult<String> login(@RequestBody @Valid LoginDTO dto) {
    log.info("开始请求登录, account: {}", dto.getAccount());
    return userService.login(dto);

  }

  @PostMapping("/register")
  public ResponseResult<String> register(@RequestBody @Valid RegisterDTO dto) {
    log.info("开始请求注册，account: {}", dto.getAccount());
    return userService.register(dto);

  }

  @GetMapping("/info")
  public ResponseResult<UserInfoVO> info() {
    return userService.info();
  }

}
