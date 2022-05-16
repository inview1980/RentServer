package com.inview.rentserver.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import person.inview.tools.RandomUtil;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoomOverviewControllerTest {
    MockMvc mock;
    @Autowired
    WebApplicationContext webApplicationContext;
    String randomNum = String.valueOf(RandomUtil.randomInt(5)+1);

    @BeforeEach
    public void init() {
        mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getRoomList() throws Exception {
//        //groupManager访问路径
//        //param传入参数
//        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/rent/getRoomByCommunity")
//                .param("community", "")).andReturn();
//        MockHttpServletResponse response = result.getResponse();
//        String content = response.getContentAsString();
//        System.out.println(content);
//
//        result = mock.perform(MockMvcRequestBuilders.get("/rent/getRoomByCommunity")
//                .param("community", "湖口社区")).andReturn();
//        response = result.getResponse();
//        content = response.getContentAsString();
//        System.out.println(content);
        query("getRoomByCommunity", "community", new String[]{"", "community", "湖口社区"});
    }

    @Test
    void getRoomDetails() throws Exception {
        query("getRoomDetails", "roomID", new String[]{randomNum, "0", "110"});
    }

    @Test
    void getRentRecord() {
        query("getRentRecord", "roomID", new String[]{randomNum, "0", "110"});
    }

    @Test
    void getPersonDetails() {
        query("getPersonByRoom", "roomID", new String[]{randomNum, "0", "110"});
    }

    @Test
    void getRoomDetailsByRecord() {
        query("getRoomDetailsByRecord", "recordID", new String[]{randomNum, "0", "1110"});
    }

    @Test
    void getRoomPropertyPaymentStatus() {
        query("getRoomPropertyPaymentStatus", "roomNumber", new String[]{"7-1-801", null, "", "1110"});
    }

    void query(String url, String param, String[] values) {
        try {
            //groupManager访问路径
            //param传入参数
            for (int i = 0; i < values.length; i++) {
                MvcResult result = mock.perform(MockMvcRequestBuilders.get("/rent/" + url)
                        .param(param, values[i])).andReturn();
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