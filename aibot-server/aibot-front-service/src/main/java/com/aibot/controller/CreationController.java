package com.aibot.controller;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.CreationVO;
import com.aibot.service.CreationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 创作控制器
 *
 * @author: yuanshuai
 * @create: 2023-04-01 22:18
 */
@RestController
@Slf4j
@RequestMapping("/creation")
public class CreationController {

  @Autowired
  private CreationService creationService;


  @GetMapping("/list")
  public ResponseResult<List<CreationVO>> creationList() {

    return creationService.creationList();

  }





}
