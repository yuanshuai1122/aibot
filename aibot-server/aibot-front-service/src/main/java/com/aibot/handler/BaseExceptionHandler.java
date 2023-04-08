package com.aibot.handler;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.enums.ResultCode;
import com.aibot.exception.CommonException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: Zero
 * @time: 2023/2/14
 * @description: 统一异常处理
 */
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 通用自定义异常捕获(登录状态/权限验证、接口防刷等)
     *
     * @return
     */
    @ExceptionHandler(value = CommonException.class)
    public ResponseResult commonException(CommonException exception) {
//        if (exception.getMessage() != null && exception.getMessage().equals(ResultCode.REQUEST_PARARMETER_MISS.message())) {
//            // 请求参数缺失
//            return new Result(ResultCode.REQUEST_PARARMETER_MISS);
//        }
//        if (exception.getMessage() != null && exception.getMessage().equals(ResultCode.UNAUTHENTICATED.getMsg())) {
//            // 未登录/token非法
//            return new ResponseResult(ResultCode.UNAUTHENTICATED.getCode(), ResultCode.UNAUTHENTICATED.getMsg());
//        }
//        if (exception.getMessage() != null && exception.getMessage().equals(ResultCode.TOKEN_LOSE_EFFICACY.getMsg())) {
//            // 登录凭证token已经失效
//            return new ResponseResult(ResultCode.TOKEN_LOSE_EFFICACY.getCode(), ResultCode.TOKEN_LOSE_EFFICACY.getMsg());
//        }
//        if (exception.getMessage() != null && exception.getMessage().equals(ResultCode.UNAUTHORISED.getMsg())) {
//            // 访问权限不足
//            return new ResponseResult(ResultCode.UNAUTHORISED.getCode(), ResultCode.UNAUTHORISED.getMsg());
//        }
//        if (exception.getMessage() != null && exception.getMessage().equals(ResultCode.REQUEST_METHOD_NOT_SUPPORT.message())) {
//            // 不支持的请求方法类型
//            return new Result(ResultCode.REQUEST_METHOD_NOT_SUPPORT);
//        }
        if(exception.getMessage() != null && exception.getMessage().equals(ResultCode.ACCESS_FREQUENT.getMsg())){
            // 访问过于频繁
            return new ResponseResult(ResultCode.ACCESS_FREQUENT.getCode(), ResultCode.ACCESS_FREQUENT.getMsg());
        }
        if (exception.getMessage() != null) {
            // 给定异常信息
            return new ResponseResult(10001, exception.getMessage(), false);
        }
        // 请求失败
        return new ResponseResult(ResultCode.FAILED.getCode(), "请求失败");
    }


    /**
     * 服务器异常统一返回
     *
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult error() {
        return new ResponseResult(ResultCode.SERVER_ERROR.getCode(), ResultCode.SERVER_ERROR.getMsg());
    }
}
