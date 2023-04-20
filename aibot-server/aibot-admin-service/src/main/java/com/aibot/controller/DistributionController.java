package com.aibot.controller;

import com.aibot.beans.dto.DistributionUpdateDTO;
import com.aibot.beans.entity.DistributionConfig;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.DistributionConfigVO;
import com.aibot.beans.vo.MaxLevelVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.service.DistributionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 分销控制器
 *
 * @author: aabb
 * @create: 2023-03-31 18:19
 */
@RestController
@Slf4j
@RequestMapping("/distribution")
public class DistributionController {

  @Autowired
  private DistributionService distributionService;



  @GetMapping("/list")
  public ResponseResult<List<DistributionConfigVO>> distributionList() {

    return distributionService.distributionList();
  }

  @GetMapping("/level/max")
  public ResponseResult<MaxLevelVO> distributionMaxLevel() {

    return distributionService.distributionMaxLevel();
  }

  @GetMapping("/config")
  public ResponseResult<DistributionConfigVO> distribution(@RequestParam("id") Integer id) {

    return distributionService.distribution(id);
  }

  @PostMapping("/config/update")
  public ResponseResult<Boolean> distributionUpdate(@RequestBody DistributionUpdateDTO dto) {
    if (null == dto.getId()) {
      return new ResponseResult<>(ResultCode.PARAM_IS_INVAlID.getCode(), ResultCode.PARAM_IS_INVAlID.getMsg());
    }

    return distributionService.distributionUpdate(dto);
  }







}
