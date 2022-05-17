package com.inview.rentserver.controller;

import com.inview.rentserver.config.Init;
import com.inview.rentserver.dao.LoginDao;
import com.inview.rentserver.http.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import person.inview.receiver.Result;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.StrUtil;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginDao loginDao;

    @GetMapping("/login")
    Result login(String password,String data){
        if(StrUtil.isBlank(password))
            return Result.Error(WebResultEnum.PasswordError);

        return loginDao.getResult(password, data);
    }


}
