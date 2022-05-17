package com.inview.rentserver.controller;

import com.alibaba.fastjson.JSONObject;
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

    @BeforeEach
    public void init() {
        mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    void login() throws Exception {
        Map<String, String> map=new HashMap<>();
        map.put("data","zj+zUYSKUzlck5qmX9rtHL/BT916mLA3");
        map.put("password","jjss2223kl");
        String mapStr = JSONObject.toJSONString(map);
        System.out.println(mapStr);
        MvcResult result = mock.perform(MockMvcRequestBuilders.post("/login")
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