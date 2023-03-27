package com.aibot.controller;

import com.aibot.beans.dto.LoginDTO;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.User;
import com.aibot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 *
 * @author: yuanshuai
 * @create: 2023-03-26 19:35
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

  /**
   * 获取下级用户
   * @param account 账号
   * @param level 等级
   * @return 用户信息
   */
  @GetMapping("/sub")
  public ResponseResult<List<User>> subUsers(@RequestParam(value = "account", required = false) String account,
                                       @RequestParam(value = "level", required = false) Integer level,
                                       @RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize) {
    return userService.subUsers(account, level, pageNum, pageSize);
  }

}
