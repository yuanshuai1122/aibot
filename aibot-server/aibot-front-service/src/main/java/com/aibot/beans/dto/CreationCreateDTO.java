package com.aibot.beans.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: aibot-server
 * @description: 创作dto
 * @author: yuanshuai
 * @create: 2023-04-23 09:35
 **/
@Data
public class CreationCreateDTO {

    /**
     * 创作ID
     */
    private Integer creationId;


    /**
     * 字数
     */
    private Integer wordsCount;

    /**
     * 提示
     */
    @NotBlank
    private String prompt;
}
