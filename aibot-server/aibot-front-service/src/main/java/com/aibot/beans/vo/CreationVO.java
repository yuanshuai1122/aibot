package com.aibot.beans.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 创作返回体
 *
 * @author: yuanshuai
 * @create: 2023-04-01 22:20
 */
@Data
public class CreationVO {


  private Integer id;


  /**
   * 标题
   */
  private String title;

  /**
   * 描述
   */
  private String des;

  /**
   * 背景图
   */
  private String imgUrl;

  /**
   * 使用次数
   */
  private Integer useCount;

  /**
   * 字数
   */
  private Integer wordCount;

  /**
   * 关键字
   */
  private String keywords;

}
