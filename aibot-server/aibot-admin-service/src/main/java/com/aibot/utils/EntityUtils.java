package com.aibot.utils;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * 实体工具
 *
 * @author: aabb
 * @create: 2023-03-31 13:01
 */
public class EntityUtils {

  /**
   * 判断实体是否有某个属性
   *
   * @param entityPath 实体全路径
   * @param attrName   属性名字
   * @return boolean
   */
  public static boolean isHaveAttr(String entityPath, String attrName) {
    Optional<String> epOptional = Optional.ofNullable(entityPath);
    if (!epOptional.isPresent()) {
      return false;
    }
    try {
      Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass(epOptional.get());
      Field[] fields = aClass.getDeclaredFields();
      for (Field field : fields) {
        if (attrName.equals(field.getName())) {
          return true;
        }
      }
      return false;
    } catch (ClassNotFoundException e) {
      // log.error("SystemSqlParser->isHaveAttr类加载异常:" + e.getMessage());
      return false;
    }
  }

}
