package com.inview.rentserver.dao;

import com.inview.rentserver.config.Init;
import com.inview.rentserver.http.TokenUtil;
import lombok.NonNull;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import person.inview.receiver.Result;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.EncryptUtil;

@Service
public class LoginDao {
    @Value("${DB.Psd}")
    private String pwd;

    public Result login(String password, @NonNull String data) {
        if (checkPwd(password, data))
            return Result.Ok("login", "", TokenUtil.buildToken());
        else
            return Result.Error(WebResultEnum.PasswordError);
    }

    public Result changePwd(String password, String oldPwd, String newPwd) {
        if (checkPwd(password, oldPwd)) {
            String hmac = EncryptUtil.HmacSHA256(pwd, newPwd);
            Init.setEncryptPassword(hmac);
            return Result.Ok("changePwd","",null);
        }
        return Result.Error(WebResultEnum.PasswordError);
    }

    private boolean checkPwd(String password, @NonNull String data) {
        //解密
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(password);
        String str = encryptor.decrypt(data);
        //摘要加密后的字符串与系统已存的44位密钥比对
        String hmacSHA256 = EncryptUtil.HmacSHA256(pwd, str);
        return Init.getEncryptPassword().equals(hmacSHA256);
    }
}
