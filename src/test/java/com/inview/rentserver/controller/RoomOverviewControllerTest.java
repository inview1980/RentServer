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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoomOverviewControllerTest {
    MockMvc mock;
    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() {
        mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getRoomList() throws Exception {
        //groupManager访问路径
        //param传入参数
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/rent/getRoomByCommunity")
                .param("community", "")).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        System.out.println(content);

        result = mock.perform(MockMvcRequestBuilders.get("/rent/getRoomByCommunity")
                .param("community", "湖口社区")).andReturn();
         response = result.getResponse();
         content = response.getContentAsString();
        System.out.println(content);
    }

    @Test
    void getRoomDetails() throws Exception {
        //groupManager访问路径
        //param传入参数
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/rent/getRoomDetails")
                .param("roomID", "2")).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        System.out.println(content);

        result = mock.perform(MockMvcRequestBuilders.get("/rent/getRoomDetails")
                .param("roomID", "0")).andReturn();
         response = result.getResponse();
         content = response.getContentAsString();
        System.out.println(content);
    }

    @Test
    void getRentRecord() {
    }

    @Test
    void getPersonDetails() {
    }
}