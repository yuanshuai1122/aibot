package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @program: aibot-server
 * @description: 创作历史实体
 * @author: yuanshuai
 * @create: 2023-04-23 09:47
 **/
@Data
public class CreationHistory {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 创作ID
     */
    private Integer creationId;

    /**
     * 提问
     */
    private String prompt;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型
     */
    private String type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
