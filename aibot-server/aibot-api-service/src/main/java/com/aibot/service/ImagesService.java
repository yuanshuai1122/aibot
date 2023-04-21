package com.aibot.service;

import com.aibot.beans.dto.ImagesCreateDTO;
import com.aibot.beans.dto.JourneyCreateDTO;
import com.aibot.beans.dto.entity.ResponseResult;
import com.aibot.beans.dto.vo.ImagesUrlCreateVO;
import com.aibot.beans.dto.vo.journey.JourneyCreateVO;
import com.aibot.beans.dto.vo.journey.JourneyResultVO;
import com.aibot.config.ApiKeyConfig;
import com.aibot.constants.ApiBaseUrl;
import com.aibot.enums.ResultCode;
import com.aibot.utils.OkHttpUtils;
import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 图片处理服务
 *
 * @author: aabb
 * @create: 2023-03-23 14:36
 */
@Service
@Slf4j
public class ImagesService {

  private static final String REPLICATE_API_TOKEN = "ab7617fc72bfbb3db29477268f3395eee5623f39";

  private static final String OPEN_JOURNEY_URL = "https://api.replicate.com/v1/predictions";



  public ResponseResult<JourneyCreateVO> journeyCreate(JourneyCreateDTO dto) {


    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("prompt", dto.getPrompt());
    hashMap.put("width", dto.getWidth());
    hashMap.put("height", dto.getHeight());
    hashMap.put("num_outputs", dto.getNumOutputs());
    hashMap.put("num_inference_steps", dto.getNumInferenceSteps());
    hashMap.put("guidance_scale", dto.getGuidanceScale());
    hashMap.put("seed", dto.getSeed());


    OkHttpUtils client = OkHttpUtils.builder();
    client.url(OPEN_JOURNEY_URL);
    client.addHeader("Content-Type", "application/json");
    client.addHeader("Authorization", "Token " + REPLICATE_API_TOKEN);
    client.addParam("version", "9936c2001faa2194a261c01381f90e65261879985476014a0a37a334593a05eb");
    client.addParam("input", hashMap);
    String sync = client.post(true).sync();
    System.out.println(sync);

    JourneyCreateVO journey = new Gson().fromJson(sync, JourneyCreateVO.class);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "生成成功", journey);
  }

  public ResponseResult<JourneyResultVO> getJourney(String id) {
    OkHttpUtils client = OkHttpUtils.builder();
    client.url(OPEN_JOURNEY_URL + "/" + id);
    client.addHeader("Content-Type", "application/json");
    client.addHeader("Authorization", "Token " + REPLICATE_API_TOKEN);
    String sync = client.get().sync();
    System.out.println(sync);
    JourneyResultVO journeyResult = JSON.toJavaObject(sync, JourneyResultVO.class);
    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "生成成功", journeyResult);
  }


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
