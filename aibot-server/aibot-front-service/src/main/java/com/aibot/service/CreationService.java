package com.aibot.service;

import com.aibot.beans.entity.CreationConfig;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.CreationVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.CreationConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创作服务
 *
 * @author: aabb
 * @create: 2023-04-01 22:18
 */
@Service
@Slf4j
public class CreationService {

  @Autowired
  private CreationConfigMapper creationConfigMapper;


  public ResponseResult<List<CreationVO>> creationList() {

    MPJLambdaWrapper<CreationConfig> wrapper = new MPJLambdaWrapper<CreationConfig>()
            .select(CreationConfig::getId, CreationConfig::getTitle, CreationConfig::getDes, CreationConfig::getImgUrl, CreationConfig::getUseCount, CreationConfig::getWordCount, CreationConfig::getKeywords)
            .eq(CreationConfig::getStatus, 0);

    List<CreationVO> creations = creationConfigMapper.selectJoinList(CreationVO.class, wrapper);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", creations);

  }
}
