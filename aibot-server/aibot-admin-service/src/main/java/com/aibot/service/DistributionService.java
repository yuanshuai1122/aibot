package com.aibot.service;

import com.aibot.beans.dto.DistributionUpdateDTO;
import com.aibot.beans.entity.DistributionConfig;
import com.aibot.beans.entity.DistributionLevelConfig;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.UserInfo;
import com.aibot.beans.vo.DistributionConfigVO;
import com.aibot.beans.vo.MaxLevelVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.DistributionConfigMapper;
import com.aibot.mapper.DistributionLevelConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 分销服务
 *
 * @author: aabb
 * @create: 2023-03-31 18:20
 */
@Service
@Slf4j
public class DistributionService {

  @Autowired
  private DistributionLevelConfigMapper distributionLevelConfigMapper;

  @Autowired
  private DistributionConfigMapper distributionConfigMapper;



  public ResponseResult<List<DistributionConfigVO>> distributionList() {

    // 读配置表 验证开关
    QueryWrapper<DistributionLevelConfig> wrapper = new QueryWrapper<>();
    DistributionLevelConfig levelConfig = distributionLevelConfigMapper.selectOne(wrapper);
    if (null == levelConfig) {
      return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "未开启分销功能", null);
    }
    if (levelConfig.getStatus() == 1) {
      return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "未开启分销功能", null);
    }

    MPJLambdaWrapper<DistributionConfig> wrapperConfig = new MPJLambdaWrapper<DistributionConfig>()
            .selectAll(DistributionConfig.class)
            .innerJoin(UserInfo.class, UserInfo::getUserId, DistributionConfig::getTenantId)
            .selectAs(UserInfo::getNickName, DistributionConfigVO::getTenantName);

    List<DistributionConfigVO> configs = distributionConfigMapper.selectJoinList(DistributionConfigVO.class, wrapperConfig);

    if (configs.size() != levelConfig.getMaxLevel() + 1) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "分销配置查询异常，请联系管理员");
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", configs);

  }

  public ResponseResult<MaxLevelVO> distributionMaxLevel() {

    MaxLevelVO maxLevel = new MaxLevelVO();
    maxLevel.setDesc("注：若想减少层级，请将该层级分佣比例设置为0");

    // 读配置表 验证开关
    QueryWrapper<DistributionLevelConfig> wrapper = new QueryWrapper<>();
    DistributionLevelConfig levelConfig = distributionLevelConfigMapper.selectOne(wrapper);
    if (null == levelConfig) {
      maxLevel.setMaxLevel(0);
      return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", maxLevel);
    }
    if (levelConfig.getStatus() == 1) {
      maxLevel.setMaxLevel(0);
      return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", maxLevel);
    }

    maxLevel.setMaxLevel(levelConfig.getMaxLevel());
    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", maxLevel);

  }


  public ResponseResult<DistributionConfigVO> distribution(Integer id) {

    MPJLambdaWrapper<DistributionConfig> wrapperConfig = new MPJLambdaWrapper<DistributionConfig>()
            .selectAll(DistributionConfig.class)
            .innerJoin(UserInfo.class, UserInfo::getUserId, DistributionConfig::getTenantId)
            .selectAs(UserInfo::getNickName, DistributionConfigVO::getTenantName)
            .eq(DistributionConfig::getId, id)
            ;

    DistributionConfigVO config = distributionConfigMapper.selectJoinOne(DistributionConfigVO.class, wrapperConfig);
    if (null == config) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "分销配置不存在");
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "查询成功", config);
  }

  public ResponseResult<Boolean> distributionUpdate(DistributionUpdateDTO dto) {

    DistributionConfig config = new DistributionConfig();
    config.setId(dto.getId());
    config.setName(dto.getName());
    config.setRate(dto.getRate());
    config.setUpdateTime(new Date());

    distributionConfigMapper.updateById(config);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "修改成功", true);
  }
}
