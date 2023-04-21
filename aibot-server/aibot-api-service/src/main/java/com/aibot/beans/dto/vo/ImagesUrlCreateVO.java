package com.aibot.beans.dto.vo;

import lombok.Data;

import java.util.List;

/**
 * 生成图片返回体
 *
 * @author: aabb
 * @create: 2023-03-23 15:01
 */
@Data
public class ImagesUrlCreateVO {

  private Integer created;

  private List<ImageUrl> data;
}
