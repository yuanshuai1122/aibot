package com.aibot.beans.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 修改配置dto
 *
 * @author: aabb
 * @create: 2023-03-31 21:08
 */
@Data
public class DistributionUpdateDTO {

  /**
   * 主键id
   */
  private Integer id;

  /**
   * 名称
   */
  private String name;

  /**
   * 比例
   */
  private Integer rate;
}
