package com.aibot.beans.dto.entity.chatProcess;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * chat流式dto
 *
 * @author: aabb
 * @create: 2023-03-06 17:31
 */
@Data
public class ChatProcess {

  private List<ChatPrompt> prompt;

}
