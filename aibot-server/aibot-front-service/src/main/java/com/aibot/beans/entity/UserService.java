package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @program: aibot-server
 * @description: 用户服务实体
 * @author: yuanshuai
 * @create: 2023-04-22 22:42
 **/
@Data
public class UserService {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 剩余次数
     */
    private Integer remainTimes;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
