package com.inview.rentserver.controller;

import com.alibaba.fastjson.JSON;
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
import person.inview.receiver.Receiver;
import person.inview.tools.RandomUtil;
import person.inview.tools.StringCompress;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
class UpdateControllerTest {
    MockMvc mock;
    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() {
        mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void updateOrAdd() {
    }

    @Test
    void compress() {

        String ss = RandomUtil.randomNumbers(1000);
        System.out.println(ss);
        System.out.println(ss.length());
        String s1 = StringCompress.zipBase64(ss);
        System.out.println(s1);
        System.out.println(s1.length());
        System.out.println(StringCompress.unZipBase64(s1));
    }

    @Test
    void testUpdateOrAdd() {
        query("/updateOrAdd", "dataCode", new String[]{"0", "1", "11", "111"});
    }

    void query(String url, String param, String[] values) {
        try {
            //groupManager访问路径
            //param传入参数
            for (int i = 0; i < values.length; i++) {
                MvcResult result = mock.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"opcode\":\"1\",\n" +
                                " \"data\":\"1\",\n" +
                                " \"other\":\"1\",\n" +
                                " \"dataCode\":\"" + values[i] + "\"\n" +
                                "}"
                        )
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();
//                        .param("opcode","1")
//                        .param("data","1")
//                        .param("other","1")
//                        .param(param, values[i])).andReturn();
                MockHttpServletResponse response = result.getResponse();
                String content = response.getContentAsString();
                System.out.println("--------------------------------------------------------");
                System.out.println("参数[" + param + "]，值[" + values[i] + "]");
                System.out.println(content);
                System.out.println("--------------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}