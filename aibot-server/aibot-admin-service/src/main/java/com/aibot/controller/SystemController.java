package com.aibot.controller;

import com.aibot.beans.dto.UpdateKeyStatusDTO;
import com.aibot.beans.entity.ChatApiKey;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.ChatApiKeyVO;
import com.aibot.beans.vo.CheckBillingVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.service.RoleService;
import com.aibot.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 系统控制器
 *
 * @author: yuanshuai
 * @create: 2023-04-02 14:01
 */
@RestController
@Slf4j
@RequestMapping("/system")
public class SystemController {

  @Autowired
  private SystemService systemService;

  @Autowired
  private RoleService roleService;


  @GetMapping("/billing/check")
  public ResponseResult<CheckBillingVO> checkBilling(@RequestParam("apiKey") String apiKey) {

    if (!roleService.isSuperAdmin()) {
      return new ResponseResult<>(ResultCode.UNAUTHORISED.getCode(), ResultCode.UNAUTHORISED.getMsg());
    }

    return systemService.checkBilling(apiKey);
  }

  @GetMapping("/key/list")
  public ResponseResult<List<ChatApiKeyVO>> keyList() {

    if (!roleService.isSuperAdmin()) {
      return new ResponseResult<>(ResultCode.UNAUTHORISED.getCode(), ResultCode.UNAUTHORISED.getMsg());
    }

    return systemService.keyList();
  }

  @PostMapping("/key/status")
  public ResponseResult<Object> updateKeyStatus(@RequestBody @Valid UpdateKeyStatusDTO dto) {
    if (!roleService.isSuperAdmin()) {
      return new ResponseResult<>(ResultCode.UNAUTHORISED.getCode(), ResultCode.UNAUTHORISED.getMsg());
    }

    return systemService.updateKeyStatus(dto);
  }


}
