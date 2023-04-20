package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @program: aibot
 * @description: 创作类型配置
 * @author: yuanshuai
 * @create: 2023-04-21 00:38
 **/
@Data
public class CreationTypeConfig {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 创作类型
     */
    private String creationType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
