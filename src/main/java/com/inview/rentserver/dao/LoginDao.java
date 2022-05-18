package com.inview.rentserver.dao;

import com.inview.rentserver.config.Init;
import com.inview.rentserver.http.TokenUtil;
import com.inview.rentserver.http.VerificationCodeUtil;
import lombok.NonNull;
import lombok.val;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import person.inview.receiver.Result;
import person.inview.receiver.VerificationCode;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.EncryptUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginDao {
    @Value("${DB.Psd}")
    private String pwd;

    /**
     * 登录，从通过randomString从验证码库中提取verificationCode，没有找到就表示验证码过期
     *         比对验证码后，从找到的verificationCode中取得加密字符串，对data进行解密，再用系统已存的pwd作为密码进行HmacSHA256摘要加密
     *         最后与Init中getEncryptPassword方法的字符串比对
     * @param randomString 通过此字符串找到verificationCode
     * @param ciphertext 加密后的密码
     * @param verificationCode 验证码，默认4位
     */
    public Result login(@NonNull String randomString, @NonNull String ciphertext,@NonNull String verificationCode) {
        //登录，从通过randomString从验证码库中提取verificationCode，没有找到就表示验证码过期
        //比对验证码后，从找到的verificationCode中取得加密字符串，对data进行解密，再用系统已存的pwd作为密码进行HmacSHA256摘要加密
        //最后与Init中getEncryptPassword方法的字符串比对
        final VerificationCode veri = VerificationCodeUtil.findByRandomString(randomString);
        if(veri==null)
            return Result.Error(WebResultEnum.VerificationCodeEndTime);
        if(!veri.getVerificationCode().equals(verificationCode))
            return Result.Error(WebResultEnum.VerificationCodeError);

        if (checkPwd(veri.getPwd(), ciphertext))
            return Result.Ok("login", "", TokenUtil.buildToken());
        else
            return Result.Error(WebResultEnum.PasswordError);
    }

    /**
     * 修改密码，从通过randomString从验证码库中提取verificationCode，没有找到就表示验证码过期
     *      比对验证码后，从找到的verificationCode中取得加密字符串，对oldPwd进行解密，再用系统已存的pwd作为密码进行HmacSHA256摘要加密
     *      然后与Init中getEncryptPassword方法的字符串比对，确认与旧密码一致后，将新密码用pwd作为密码进行HmacSHA256摘要加密
     *      最后通过Init.setEncryptPassword方法将最后的字符串写入系统配置文件
     * @param randomString 通过此字符串找到verificationCode
     * @param oldPwd 加密后的旧密码
     * @param newPwd 加密后的新密码
     * @param verificationCode 验证码，默认4位
     */
    public Result changePwd(String randomString, String oldPwd, String newPwd, String verificationCode) {
        final VerificationCode veri = VerificationCodeUtil.findByRandomString(randomString);
        if(veri==null)
            return Result.Error(WebResultEnum.VerificationCodeEndTime);
        if(!veri.getVerificationCode().equals(verificationCode))
            return Result.Error(WebResultEnum.VerificationCodeError);

        if (checkPwd(veri.getPwd(), oldPwd)) {
            //解密新密码
            BasicTextEncryptor encryptor = new BasicTextEncryptor();
            encryptor.setPassword(veri.getPwd());
            String str = encryptor.decrypt(newPwd);
            //将新密码进行HmacSHA256加密
            String hmac = EncryptUtil.HmacSHA256(pwd, str);
            Init.setEncryptPassword(hmac);
            return Result.Ok("changePwd","",null);
        }
        return Result.Error(WebResultEnum.PasswordError);
    }

    /**
     * 将password作为密码对data进行BasicTextEncryptor解密，然后用pwd作为密码对前面解密后的字符串进行hmacSHA256摘要加密
     * @param password 解密密码
     * @param data 需要解密的字符串
     */
    private boolean checkPwd(String password, @NonNull String data) {
        //解密
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(password);
        String str = encryptor.decrypt(data);
        //摘要加密后的字符串与系统已存的44位密钥比对
        String hmacSHA256 = EncryptUtil.HmacSHA256(pwd, str);
        return Init.getEncryptPassword().equals(hmacSHA256);
    }

    /**
     * 将以后要用到的加密密码、随机字符、验证图片转换的字符串
     * 登录时，客户端将输入的密码用pwd进行加密，服务端用pwd对密文进行解密
     */
    public Map<String, String> getVerificationCode() {
        Map<String, String> result=new HashMap<>();
        VerificationCode code= VerificationCodeUtil.getVerificationCode();
        result.put("randomString", code.getRandomString());
        result.put("picString",code.getPicString());
        result.put("pwd", code.getPwd());
        return result;
    }
}
