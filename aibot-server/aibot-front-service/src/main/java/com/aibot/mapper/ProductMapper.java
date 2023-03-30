package com.aibot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aibot.beans.entity.Product;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-23 18:07
 */
@Mapper
public interface ProductMapper extends MPJBaseMapper<Product> {
}
