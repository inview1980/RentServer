package com.inview.rentserver.http;

import com.inview.rentserver.config.Init;
import com.inview.rentserver.config.StaticValues;
import lombok.NonNull;
import person.inview.receiver.Token;
import person.inview.tools.RandomUtil;

import java.time.LocalDateTime;
import java.util.*;

public class TokenUtil {
    private static final Map<String, Token> TOKEN_MAP=new HashMap<>(20);

    public static boolean checkToken(String userToken) {
        return TOKEN_MAP.containsKey(userToken);
    }

    /**
     * token在规定的秒后过期
     * @param second 单位秒
     */
    public static void cleanUserToken(int second) {
        TOKEN_MAP.entrySet().removeIf(entry->entry.getValue().getCreatTime().plusSeconds(second).isBefore(LocalDateTime.now()));
    }

    public static Token buildToken(){
        String tokenStr = RandomUtil.randomString(StaticValues.TokenLength);
        Token token=new Token();
        token.setToken(tokenStr);
        token.setCreatTime(LocalDateTime.now());
        token.setPwd(RandomUtil.randomString(StaticValues.PasswordLength));
        TOKEN_MAP.put(tokenStr, token);
        return token;
    }

    public static Token getToken(@NonNull String tokenStr){
        return TOKEN_MAP.getOrDefault(tokenStr, null);
    }
}
