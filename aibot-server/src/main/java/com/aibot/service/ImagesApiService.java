package com.aibot.service;

import com.alibaba.fastjson2.JSON;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.dto.ImagesCreateDTO;
import com.aibot.beans.vo.ImagesUrlCreateVO;
import com.aibot.constants.ApiBaseUrl;
import com.aibot.constants.ApiKeyConfig;
import com.aibot.constants.enums.ResultCode;
import com.aibot.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 图片处理服务
 *
 * @author: yuanshuai
 * @create: 2023-03-23 14:36
 */
@Service
@Slf4j
public class ImagesApiService {


  /**
   * 图片生成服务
   * @param dto 请求实体
   * @return 生成结果
   */
  public ResponseResult<ImagesUrlCreateVO> imagesCreate(ImagesCreateDTO dto) {

    try {
      log.info("用户: {}",  dto.getPrompt());
      OkHttpUtils client = OkHttpUtils.builder();

      client.url(ApiBaseUrl.BASE_IMAGES_CREATE_URL);
      client.addHeader("Content-Type", "application/json");
      client.addHeader("Authorization", "Bearer " + ApiKeyConfig.API_KEY1);

      client.addParam("prompt", dto.getPrompt());
      client.addParam("n", 1);
      client.addParam("size", "512x512");
      client.addParam("response_format", "url"); // b64_json
      //client.addParam("role", ChatRoleEnum.USER.getRole());

      String sync = client.post(true).sync();

      ImagesUrlCreateVO create = JSON.parseObject(sync, ImagesUrlCreateVO.class);

      log.info("生成成功: {}", create);
      return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "请求成功", create);
    }catch (Exception e) {
      log.info(e.toString());
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "服务器异常", null);
    }
  }
}
