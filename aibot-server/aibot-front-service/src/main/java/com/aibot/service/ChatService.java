package com.aibot.service;

import com.aibot.beans.entity.UserService;
import com.aibot.config.ChatExecutorConfig;
import com.aibot.constants.enums.ServiceTypeEnum;
import com.aibot.mapper.UserServiceMapper;
import com.aibot.utils.SignUtil;
import com.alibaba.fastjson2.JSON;
import com.aibot.beans.ChatResult.ChatResult;
import com.aibot.beans.dto.ChatCommonDTO;
import com.aibot.beans.dto.chatProcess.ChatProcess;
import com.aibot.beans.entity.ChatSuccessLog;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.ApiBaseUrl;
import com.aibot.constants.ApiKeyConfig;
import com.aibot.constants.enums.ChatRoleEnum;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.ChatApiKeyMapper;
import com.aibot.mapper.ChatSuccessLogMapper;
import com.aibot.tasks.AsyncTask;
import com.aibot.utils.OkHttpUtils;
import com.aibot.utils.ValueUtils;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.github.yulichang.toolkit.JoinWrappers.lambda;

/**
 * chatGPT api版本服务
 *
 * @author: aabb
 * @create: 2023-03-08 16:55
 */
@Service
@Slf4j
public class ChatService {
  
  @Autowired
  private AsyncTask asyncTask;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private ChatSuccessLogMapper chatSuccessLogMapper;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private UserServiceMapper userServiceMapper;

  private static final String CHAT_URL = "/api/chat/sync";


  /**
   * 聊天（普通版）
   *
   * @param dto DTO
   * @return {@link ResponseResult}<{@link ChatResult}>
   */
  public ResponseResult<ChatResult> chatSync(ChatProcess dto) {

    ResponseResult<ChatResult> responseResult = chatPosts(dto);
    if (!responseResult.getCode().equals(ResultCode.SUCCESS.getCode())) {
      return responseResult;
    }

    ChatResult chatResult = responseResult.getData();

    // 插入用户提问到数据库
    log.info("user: {}", dto.getPrompt());
    ChatSuccessLog userChat = new ChatSuccessLog(null, 1, ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), chatResult.getId(), dto.getPrompt().toString(), new Date());
    chatSuccessLogMapper.insert(userChat);

    // 插入到数据库
    log.info("chat: {}",  chatResult.getChoices().get(0).getMessage().getContent());
    ChatSuccessLog chatSuccessLog = new ChatSuccessLog(null, 1, ChatRoleEnum.ASSISTANT.getRole(), chatResult.getId(),  chatResult.getId(), chatResult.getChoices().get(0).getMessage().getContent(), new Date());
    chatSuccessLogMapper.insert(chatSuccessLog);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "请求成功", chatResult);

  }

  public ResponseResult<ChatResult> chatPosts(ChatProcess dto) {
    try {
      log.info("用户: {}",  dto.getPrompt());
      OkHttpUtils client = OkHttpUtils.builder();
      client.url(ApiBaseUrl.BASE_API_URL.concat(CHAT_URL));
      client.addHeader("Content-Type", "application/json");
      // 签名
      String sign = SignUtil.sign(JSON.toJSONString(dto), Collections.emptyMap(), new String[0]);
      log.info("sign:{}", sign);
      client.addHeader("X-SIGN", sign);
      client.addParam("prompt", dto.getPrompt());
      String sync = client.post(true).sync();
      log.info("请求api返回值为：{}", sync);
      JSONObject jsonObject = JSON.parseObject(sync);
      if (Integer.parseInt(jsonObject.getString("code")) != ResultCode.SUCCESS.getCode()) {
        return new ResponseResult<>(ResultCode.FAILED.getCode(), "请求失败，请稍后再试", null);
      }

      ChatResult chatResult = new Gson().fromJson(jsonObject.getString("data"), ChatResult.class);
      if (null == chatResult) {
        return new ResponseResult<>(ResultCode.FAILED.getCode(), "请求失败，请稍后再试", null);
      }

      // 扣次数/验时间
      int userId = Integer.parseInt(request.getAttribute("id").toString());
      QueryWrapper<UserService> wrapper = new QueryWrapper<>();
      wrapper.lambda().eq(UserService::getUserId, userId);
      UserService userService = userServiceMapper.selectOne(wrapper);
      if (null == userService) {
        return new ResponseResult<>(ResultCode.FAILED.getCode(), "用户未开通该服务", null);
      }
      // 验证服务类型
      if(userService.getServiceType().equals(ServiceTypeEnum.SUBS_DATE.getType())
              && null != userService.getExpireTime()
              && userService.getExpireTime().after(new Date())) {
        log.info("时间用户请求聊天成功, account:{}", request.getAttribute("account"));
        return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "请求成功", chatResult);
      }else if (userService.getServiceType().equals(ServiceTypeEnum.SUBS_TIME.getType())
              && userService.getRemainTimes() > 0) {
        // 包月 扣减次数
        int remainTimes = userService.getRemainTimes();
        remainTimes = remainTimes - 1;
        userService.setRemainTimes(remainTimes);
        userService.setUpdateTime(new Date());
        int flag = userServiceMapper.updateById(userService);
        if (flag <= 0) {
          return new ResponseResult<>(ResultCode.FAILED.getCode(), "更新服务失败", null);
        }
        log.info("次数用户请求聊天成功, account:{}", request.getAttribute("account"));
        return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "请求成功", chatResult);
      }

      return new ResponseResult<>(ResultCode.FAILED.getCode(), "请求失败，请稍后重试", chatResult);
    }catch (Exception e) {
      log.info(e.toString());
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "请求发生异常，请稍后再试", null);
    }
  }



  /**
   * chat签名
   * @param process 请求实体
   * @return
   */
  public ResponseResult<String> chatSign(ChatProcess process) {

    // 校验敏感词
    //boolean contains = SensitiveWordHelper.contains(process.getPrompt());
    //if (contains) {
    //  ChatSensitiveLog sensitiveLog = new ChatSensitiveLog(null, chatKey.getId(), ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), ValueUtils.getUUID(), process.getPrompt(), new Date());
    //  asyncTask.setChatSensitiveLog(sensitiveLog);
    //  return new ResponseResult<>(ResultCode.NOT_PERMISSION.getCode(), "请勿输入非法词汇");
    //}

    // 加密key
    String signKey = ValueUtils.getUUID();

    // 写日志数据库
    ChatSuccessLog chatSuccessLog = new ChatSuccessLog(null, Integer.parseInt(request.getAttribute("id").toString()), ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), signKey, new Gson().toJson(process.getPrompt()), new Date());
    asyncTask.setChatLog(chatSuccessLog);
    // 放入缓存队列
    redisTemplate.opsForValue().set(signKey, new Gson().toJson(chatSuccessLog), 50, TimeUnit.MINUTES);

    // 返回key
    log.info("请求chatSign结束, 返回值key: {}", signKey);
    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", signKey);
  }


}
