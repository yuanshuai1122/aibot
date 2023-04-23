package com.aibot.exception;


import com.aibot.enums.ResultCode;

/**
 * @author: Zero
 * @time: 2023-2-13
 * @description:
 */

public class CommonException extends Exception{
    public CommonException(String context) {
        super(context);
    }

    public CommonException(ResultCode context) {
        super(context.getMsg());
    }
}
