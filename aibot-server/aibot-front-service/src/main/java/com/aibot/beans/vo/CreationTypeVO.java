package com.aibot.beans.vo;

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
public class CreationTypeVO {


    private Integer id;

    /**
     * 创作类型
     */
    private String creationType;


}
