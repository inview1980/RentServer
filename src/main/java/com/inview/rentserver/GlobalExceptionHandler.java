package com.inview.rentserver;

import com.inview.rentserver.tool.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import person.inview.receiver.Result;
import person.inview.receiver.WebResultEnum;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest request, Exception e) {
        String fromClass = "";
        String methodName = "";
        int line = 0;
        if (e.getStackTrace() != null && e.getStackTrace().length > 0) {
            fromClass = e.getStackTrace()[0].getClassName();
            methodName = e.getStackTrace()[0].getMethodName();
            line = e.getStackTrace()[0].getLineNumber();
        }
        String tmpStr = String.format("，类:%s:%s():%d行", fromClass, methodName, line);
        log.error("{}:{}{}", e.toString(), e.getLocalizedMessage(), tmpStr);
        if (e instanceof MyException) {
            MyException ess = (MyException) e;
            return Result.Error(ess.getCode(), ess.getLocalizedMessage() + "," + tmpStr);
        }
        String tmp = e.toString();
        return Result.Error(WebResultEnum.Error.getCode(), tmp + tmpStr);
    }
}
