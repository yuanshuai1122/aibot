package com.aibot.service;

import com.aibot.beans.ChatResult.ChatResult;
import com.aibot.beans.dto.CreationCreateDTO;
import com.aibot.beans.dto.chatProcess.ChatProcess;
import com.aibot.beans.dto.chatProcess.ChatPrompt;
import com.aibot.beans.entity.CreationConfig;
import com.aibot.beans.entity.CreationHistory;
import com.aibot.beans.entity.CreationTypeConfig;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.CreationTypeVO;
import com.aibot.beans.vo.CreationVO;
import com.aibot.enums.CreationTypeEnum;
import com.aibot.enums.ResultCode;
import com.aibot.enums.UserRoleEnum;
import com.aibot.mapper.CreationConfigMapper;
import com.aibot.mapper.CreationHistoryMapper;
import com.aibot.mapper.CreationTypeConfigMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 创作服务
 *
 * @author: aabb
 * @create: 2023-04-01 22:18
 */
@Service
@Slf4j
public class CreationService {

  @Autowired
  private CreationConfigMapper creationConfigMapper;

  @Autowired
  private CreationTypeConfigMapper creationTypeConfigMapper;

  @Autowired
  private ChatService chatService;

  @Autowired
  private CreationHistoryMapper creationHistoryMapper;

  @Autowired
  private HttpServletRequest request;


  /**
   * 根据类型id获取创作列表
   *
   * @param typeId 类型ID
   * @return {@link ResponseResult}<{@link List}<{@link CreationVO}>>
   */
  public ResponseResult<List<CreationVO>> creationList(Integer typeId) {

    MPJLambdaWrapper<CreationConfig> wrapper = new MPJLambdaWrapper<CreationConfig>()
            .select(CreationConfig::getId, CreationConfig::getTitle, CreationConfig::getDes, CreationConfig::getImgUrl, CreationConfig::getUseCount, CreationConfig::getWordCount, CreationConfig::getKeywords)
            .eq(CreationConfig::getStatus, 0)
            .eq(CreationConfig::getTypeId, typeId)
            ;

    List<CreationVO> creations = creationConfigMapper.selectJoinList(CreationVO.class, wrapper);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", creations);

  }

  /**
   * 创建类型列表
   *
   * @return {@link ResponseResult}<{@link List}<{@link CreationVO}>>
   */
  public ResponseResult<List<CreationTypeVO>> creationTypeList() {

    MPJLambdaWrapper<CreationTypeConfig> wrapper = new MPJLambdaWrapper<CreationTypeConfig>()
            .select(CreationTypeConfig::getId, CreationTypeConfig::getCreationType)
            ;
    List<CreationTypeVO> creationTypes = creationTypeConfigMapper.selectJoinList(CreationTypeVO.class, wrapper);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", creationTypes);

  }

  /**
   * 生成创作
   *
   * @param dto DTO
   * @return {@link ResponseResult}<{@link Object}>
   */
  public ResponseResult<Object> creationCreate(CreationCreateDTO dto) {
    // 根据id获取创作模板
    CreationConfig creationConfig = creationConfigMapper.selectById(dto.getCreationId());
    if (null == creationConfig) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "获取创作类型失败，请稍后再试", null);
    }
    String format = String.format(creationConfig.getTemplate(), dto.getPrompt(), dto.getWordsCount());
    // 生成创作
    ChatProcess chatProcess = new ChatProcess();
    ChatPrompt chatPrompt = new ChatPrompt();
    chatPrompt.setRole(UserRoleEnum.USER.getRole());
    chatPrompt.setContent(format);
    chatProcess.setPrompt(Collections.singletonList(chatPrompt));
    ResponseResult<ChatResult> chatPosts = chatService.chatPosts(chatProcess);
    if (!chatPosts.getCode().equals(ResultCode.SUCCESS.getCode())) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), chatPosts.getMsg(), null);
    }
    // 写入历史表
    CreationHistory history = new CreationHistory();
    history.setUserId(Integer.parseInt(request.getAttribute("id").toString()));
    history.setCreationId(creationConfig.getId());
    history.setPrompt(format);
    history.setContent(chatPosts.getData().getChoices().get(0).getMessage().getContent());
    history.setType(CreationTypeEnum.CREATION_WORDS.getType());
    history.setCreateTime(new Date());
    history.setUpdateTime(new Date());
    int insert = creationHistoryMapper.insert(history);
    if (insert <= 0) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "生成创作历史失败，请稍后再试", null);
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "生成创作成功", chatPosts.getData().getChoices().get(0).getMessage().getContent());

  }
}
