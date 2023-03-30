package com.aibot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aibot.beans.entity.UserRelation;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户关系Mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-21 12:14
 */
@Mapper
public interface UserRelationMapper extends MPJBaseMapper<UserRelation> {


  /**
   * 注册插入关系表
   * @param userChildrenId 子用户
   * @param userParentId 父用户
   * @return 影响行数
   */
  @Insert("INSERT INTO user_relation(user_parent_id,user_children_id,user_level) (SELECT user_parent_id, #{userChildrenId}, user_level+1 FROM user_relation WHERE user_children_id= #{userParentId})")
  Integer insertRegRelation(@Param("userChildrenId") Integer userChildrenId, @Param("userParentId") Integer userParentId);

}
