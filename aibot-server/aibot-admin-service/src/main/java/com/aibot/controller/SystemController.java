package com.aibot.controller;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.CheckBillingVO;
import com.aibot.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/billing/check")
  public ResponseResult<CheckBillingVO> checkBilling(@RequestParam("apiKey") String apiKey) {

    return systemService.checkBilling(apiKey);
  }


}
