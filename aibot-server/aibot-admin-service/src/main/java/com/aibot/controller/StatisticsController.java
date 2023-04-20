package com.aibot.controller;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.StatisticsVO;
import com.aibot.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计控制器
 *
 * @author: aabb
 * @create: 2023-03-31 21:59
 */
@RestController
@Slf4j
@RequestMapping("/statistics")
public class StatisticsController {

  @Autowired
  private StatisticsService statisticsService;


  @GetMapping("/list")
  public ResponseResult<StatisticsVO> totalList() {

    return statisticsService.totalList();
  }

}
