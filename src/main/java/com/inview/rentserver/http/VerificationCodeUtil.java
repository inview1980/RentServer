package com.inview.rentserver.http;

import com.inview.rentserver.config.Init;
import com.inview.rentserver.config.StaticValues;
import person.inview.receiver.VerificationCode;
import person.inview.tools.RandomUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class VerificationCodeUtil {
    private static final Map<String, VerificationCode> stringVerificationCodeMap = new HashMap<>(50);

    public static VerificationCode getVerificationCode(){
        VerificationCode code=new VerificationCode();
        code.setCreateTime(LocalDateTime.now());
        code.setPwd(RandomUtil.randomString(StaticValues.PasswordLength));
        code.setRandomString(RandomUtil.randomString(StaticValues.TokenLength));
        code.setVerificationCode(RandomUtil.randomNumbers(StaticValues.VerificationCodeLength));
        code.setPicString(getPicString(code.getVerificationCode()));
        stringVerificationCodeMap.put(code.getRandomString(), code);
        return code;
    }

    /**
     * 通过字符串生成相应的图片并转化成字符串
     */
    private static String getPicString(String verificationCode) {
        // TODO: 2022/5/18
        return verificationCode;
    }

    public static VerificationCode findByRandomString(String randomString){
        return stringVerificationCodeMap.getOrDefault(randomString, null);
    }

    /**
     * verificationCode在规定的秒后过期
     * @param second 单位秒
     */
    public static void clearVerificationCode(int second){
        stringVerificationCodeMap.entrySet().removeIf(vc -> vc.getValue().getCreateTime().plusSeconds(second).isBefore(LocalDateTime.now()));
    }
}
