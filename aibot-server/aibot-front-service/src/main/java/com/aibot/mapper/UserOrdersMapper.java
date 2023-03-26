package com.aibot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aibot.beans.entity.UserOrders;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户订单mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-23 21:18
 */
@Mapper
public interface UserOrdersMapper extends BaseMapper<UserOrders> {
}
