package com.inview.rentserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.inview.rentserver.dao.LoginDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import person.inview.receiver.Result;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.StrUtil;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginDao loginDao;

    @PostMapping("/login")
    Result login(@RequestBody JSONObject jsonObject) {
        String password = jsonObject.getString("password");
        String data = jsonObject.getString("data");

        if (StrUtil.isBlank(password))
            return Result.Error(WebResultEnum.PasswordError);

        return loginDao.login(password, data);
    }

    @PostMapping("/changePwd")
    Result changePwd(@RequestBody JSONObject jsonObject) {
        String password = jsonObject.getString("password");
        String oldPwd = jsonObject.getString("oldPwd");
        String newPwd = jsonObject.getString("newPwd");
        if (StrUtil.isBlank(password))
            return Result.Error(WebResultEnum.PasswordError);
        return loginDao.changePwd(password, oldPwd, newPwd);
    }
}
