package com.chatgpt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chatgpt.beans.entity.UserRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户关系Mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-21 12:14
 */
@Mapper
public interface UserRelationMapper extends BaseMapper<UserRelation> {
}
