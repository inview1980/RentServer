package com.inview.rentserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.inview.rentserver.http.VerificationCodeUtil;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import person.inview.receiver.Result;
import person.inview.tools.RandomUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginControllerTest {
    MockMvc mock;
    @Autowired
    WebApplicationContext webApplicationContext;

    private final String PWD_String = "111111";

    @BeforeEach
    public void init() {
        mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void login() throws Exception {
        Map veri=getVerificationCode2();
        //加密
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(veri.get("pwd").toString());
        String str = encryptor.encrypt(PWD_String);

        Map<String, String> map = new HashMap<>();
        map.put("ciphertext", str);
        map.put("randomString", veri.get("randomString").toString());
        map.put("verificationCode", veri.get("picString").toString());
        String mapStr = JSONObject.toJSONString(map);
        System.out.println(mapStr);
        MvcResult result = mock.perform(MockMvcRequestBuilders.post("/login/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapStr)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        System.out.println("--------------------------------------------------------");
        System.out.println(content);
        System.out.println("--------------------------------------------------------");
    }

    @Test
    void getVerificationCode() throws Exception {
        getVerificationCode2();
    }

    Map getVerificationCode2() throws Exception {
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/login/getVerificationCode")).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        System.out.println("--------------------------------------------------------");
        System.out.println(content);

        Map map =JSONObject.parseObject(content, Result.class).getObject(Map.class);
        String randomStr = map.get("randomString").toString();
        System.out.println(VerificationCodeUtil.findByRandomString(randomStr));
        System.out.println("--------------------------------------------------------");
        return map;
    }

    @Test
    void changePwd() throws Exception {
        Map veri=getVerificationCode2();
        //加密
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(veri.get("pwd").toString());
        String oldPwd = encryptor.encrypt(PWD_String);
        String newPwd = encryptor.encrypt(PWD_String);

        Map<String, String> map = new HashMap<>();
        map.put("oldPwd", oldPwd);
        map.put("newPwd", newPwd);
        map.put("randomString", veri.get("randomString").toString());
        map.put("verificationCode", veri.get("picString").toString());
        String mapStr = JSONObject.toJSONString(map);
        System.out.println(mapStr);
        MvcResult result = mock.perform(MockMvcRequestBuilders.post("/login/changePwd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapStr)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        System.out.println("--------------------------------------------------------");
        System.out.println(content);
        System.out.println("--------------------------------------------------------");
    }
}