package com.aibot.controller;

import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.dto.RegisterDTO;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.User;
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

    return userService.login(dto);

  }

  @PostMapping("/register")
  public ResponseResult<String> register(@RequestBody @Valid RegisterDTO dto) {

    return userService.register(dto);

  }

  @GetMapping("/info")
  public ResponseResult<String> info() {
    log.info("info....");
    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "成功", "121212");
  }

}
