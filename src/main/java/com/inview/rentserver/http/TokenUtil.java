package com.inview.rentserver.http;

import person.inview.tools.RandomUtil;

import java.time.LocalDateTime;
import java.util.*;

public class TokenUtil {
    private static final Map<String, LocalDateTime> userTokenMap = new HashMap<>();
    private static final int TokenLength=16;

    public static boolean checkToken(String userToken) {
        return userTokenMap.containsKey(userToken);
    }

    /**
     * token在规定的秒后过期
     * @param second 单位秒
     */
    public static void cleanUserToken(int second) {
        userTokenMap.entrySet().removeIf(entry -> entry.getValue().plusSeconds(second).isBefore(LocalDateTime.now()));
    }

    public static String buildToken(){
        String token = RandomUtil.randomString(TokenLength);
        userTokenMap.put(token, LocalDateTime.now());
        return token;
    }
}
