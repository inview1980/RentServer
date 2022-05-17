package com.inview.rentserver.config;

import com.alibaba.fastjson.JSON;
import com.inview.rentserver.http.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import person.inview.receiver.Result;
import person.inview.receiver.WebResultEnum;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 拦截请求头中是否含有字段userToken，并验证其是否有效，然后跳转
 */
@Slf4j
@Component
public class TokenFilter implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        return true;
        String userToken = request.getHeader("userToken");// 获取token
        if (userToken != null && TokenUtil.checkToken(userToken)) {
            return true;//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        } else {
            ServletOutputStream outputStream = response.getOutputStream();
            String resultStr= JSON.toJSONString(Result.Error(WebResultEnum.NeedLogin));
            outputStream.write(resultStr.getBytes(StandardCharsets.UTF_8));
            log.error("需要登录");
            return false;
        }
    }
}
