package com.inview.rentserver.controller;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.LineSeparatorDetector;
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
import org.w3c.dom.stylesheets.LinkStyle;
import person.inview.receiver.Result;
import person.inview.receiver.ToRentalTotal;
import person.inview.tools.RandomUtil;

import java.util.List;

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

    @Test
    void getRentalTotal() throws Exception {
        MvcResult result = mock.perform(MockMvcRequestBuilders.get("/rent/getRentalTotal" )).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        Result rt = JSONObject.parseObject(content, Result.class);
        assertNotNull(rt);
        System.out.println(rt);

        List<ToRentalTotal> rtt = rt.getList(ToRentalTotal.class);
        assertNotNull(rtt);
        rtt.forEach(System.out::println);
    }

    @Test
    void getContractByRoomID() {
        query("getContractByRoomID","roomID", new String[]{"2"});
    }
}