package com.inview.rentserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.inview.rentserver.dao.LoginDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import person.inview.receiver.Result;
import person.inview.receiver.VerificationCode;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.StrUtil;

import java.util.Map;

@RestController
@RequestMapping("/login")
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginDao loginDao;

    @GetMapping("/getVerificationCode")
    Result getVerificationCode(){
        Map<String, String> map=loginDao.getVerificationCode();
        return Result.Ok("getVerificationCode", map);
    }

    @PostMapping("/login")
    Result login(@RequestBody JSONObject jsonObject) {
        String randomString = jsonObject.getString("randomString");
        String ciphertext = jsonObject.getString("ciphertext");
        String verificationCode = jsonObject.getString("verificationCode");

        if (StrUtil.hasBlank(randomString,ciphertext,verificationCode))
            return Result.Error(WebResultEnum.ParameterError);

        return loginDao.login(randomString, ciphertext,verificationCode);
    }

    @PostMapping("/changePwd")
    Result changePwd(@RequestBody JSONObject jsonObject) {
        String randomString = jsonObject.getString("randomString");
        String verificationCode = jsonObject.getString("verificationCode");
        String oldPwd = jsonObject.getString("oldPwd");
        String newPwd = jsonObject.getString("newPwd");
        if (StrUtil.hasBlank(randomString,oldPwd,newPwd,verificationCode))
            return Result.Error(WebResultEnum.ParameterError);

        return loginDao.changePwd(randomString, oldPwd, newPwd,verificationCode);
    }
}
