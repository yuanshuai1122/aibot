package com.chatgpt.controller;

import com.chatgpt.beans.entity.ResponseResult;
import com.chatgpt.beans.dto.LoginDTO;
import com.chatgpt.beans.dto.RegisterDTO;
import com.chatgpt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
