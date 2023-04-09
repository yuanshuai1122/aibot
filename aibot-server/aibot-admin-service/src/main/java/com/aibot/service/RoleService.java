package com.aibot.service;

import com.aibot.constants.enums.UserRoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 角色服务
 *
 * @author: yuanshuai
 * @create: 2023-04-09 18:05
 */
@Service
@Slf4j
public class RoleService {

  @Autowired
  private HttpServletRequest request;


  public boolean isSuperAdmin() {

    return request.getAttribute("role").equals(UserRoleEnum.SUPER_ADMIN.getRole());

  }


}
