package com.aibot.controller;

import com.aibot.beans.dto.CreationCreateDTO;
import com.aibot.beans.entity.CreationTypeConfig;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.CreationTypeVO;
import com.aibot.beans.vo.CreationVO;
import com.aibot.service.CreationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 创作控制器
 *
 * @author: aabb
 * @create: 2023-04-01 22:18
 */
@RestController
@Slf4j
@RequestMapping("/creation")
public class CreationController {

  @Autowired
  private CreationService creationService;


  /**
   * 创建类型列表
   *
   * @return {@link ResponseResult}<{@link List}<{@link CreationTypeVO}>>
   */
  @GetMapping("/type/list")
  public ResponseResult<List<CreationTypeVO>> creationTypeList() {

    return creationService.creationTypeList();

  }

  /**
   * 根据类型id获取创作列表
   *
   * @param typeId 类型ID
   * @return {@link ResponseResult}<{@link List}<{@link CreationVO}>>
   */
  @GetMapping("/list")
  public ResponseResult<List<CreationVO>> creationList(@RequestParam("typeId") Integer typeId) {

    return creationService.creationList(typeId);

  }


  /**
   * 创作生成
   *
   * @param dto DTO
   * @return {@link ResponseResult}<{@link Object}>
   */
  @PostMapping("/create")
  public ResponseResult<Object> creationCreate(@RequestBody @Valid CreationCreateDTO dto) {

    return creationService.creationCreate(dto);

  }





}
