package com.inview.rentserver.controller;

import com.alibaba.fastjson.JSON;
import com.inview.rentserver.dao.RentalRecordDao;
import com.inview.rentserver.dao.RoomDao;
import com.inview.rentserver.pojo.DataEnum;
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
import pojo.RentalRecord;
import pojo.RoomDetails;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class UpdateControllerTest {
    MockMvc mock;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    RoomDao roomDao;
    @Autowired
    RentalRecordDao rentDao;

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
        get("getRecordByID", "id", "1");
        PodamFactory factory = new PodamFactoryImpl();
        Receiver re = factory.manufacturePojo(Receiver.class);
        re.setDataCode(DataEnum.RentRecord.getCode());
        re.setOpcode("changeRoomDeposit");
        Map map = new HashMap();
        map.put("recordID", 1);
        map.put("value", 33.3);
        re.setData(JSON.toJSONString(map));
        query("/updateOrAdd", JSON.toJSONString(re));
        get("getRecordByID", "id", "1");
    }

    @Test
    void roomUpdate() {
        PodamFactory factory = new PodamFactoryImpl();
        Receiver re = factory.manufacturePojo(Receiver.class);
        re.setDataCode(DataEnum.RoomDetails.getCode() | DataEnum.RentRecord.getCode());//????????????RoomDetails\RentRecord
        re.setOpcode("changeDB");
        //??????room
        RoomDetails room = factory.manufacturePojo(RoomDetails.class);
        room.setPrimary_id(2);
        Map map = new HashMap();
        map.put(RoomDetails.class.getSimpleName(), room);
        //??????rent
        RentalRecord record = factory.manufacturePojo(RentalRecord.class);
        record.setPrimary_id(2);
        map.put(RentalRecord.class.getSimpleName(), record);

        re.setData(JSON.toJSONString(map));
        query("/updateOrAdd", JSON.toJSONString(re));
        System.out.println(roomDao.findByID(2));
        System.out.println(rentDao.findByID(2));
    }

    @Test
    void changeRoomDeposit() {
        PodamFactory factory = new PodamFactoryImpl();
        Receiver re = factory.manufacturePojo(Receiver.class);
        re.setDataCode(DataEnum.RentRecord.getCode());//????????????RoomDetails\RentRecord
        re.setOpcode("changeRoomDeposit");
        Map<String, String> map = new HashMap<>();
        map.put("recordID", "2");
        map.put("value", "11");
        re.setData(JSON.toJSONString(map));
        query("/updateOrAdd", JSON.toJSONString(re));
        System.out.println(rentDao.findByID(2));
    }

    @Test
    void changeMonthlyRent() {
        PodamFactory factory = new PodamFactoryImpl();
        Receiver re = factory.manufacturePojo(Receiver.class);
        re.setDataCode(DataEnum.RentRecord.getCode());//??????RentRecord
        re.setOpcode("changeMonthlyRent");
        Map<String, String> map = new HashMap<>();
        map.put("recordID", "2");
        map.put("value", "111");
        re.setData(JSON.toJSONString(map));
        query("/updateOrAdd", JSON.toJSONString(re));
        System.out.println(rentDao.findByID(2));
    }

    @Test
    void DBBackup() {
        try {
            //groupManager????????????
            //param????????????
            System.out.println("--------------------------------------------------------");
            MvcResult result = mock.perform(MockMvcRequestBuilders.get("/DBBackup")).andReturn();
            MockHttpServletResponse response = result.getResponse();
            String content = response.getContentAsString();
            System.out.println("/DBBackup");
            System.out.println(content);
            System.out.println("--------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void query(String url, String... values) {
        try {
            //groupManager????????????
            //param????????????
            for (int i = 0; i < values.length; i++) {
                System.out.println("--------------------------------------------------------");
                MvcResult result = mock.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(values[i])
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn();
                MockHttpServletResponse response = result.getResponse();
                String content = response.getContentAsString();
                System.out.println("??????[" + url + "]??????[" + values[i] + "]");
                System.out.println(content);
                System.out.println("--------------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void get(String url, String param, String... values) {
        try {
            //groupManager????????????
            //param????????????
            for (int i = 0; i < values.length; i++) {
                System.out.println("--------------------------------------------------------");
                MvcResult result = mock.perform(MockMvcRequestBuilders.get("/rent/" + url)
                        .param(param, values[i])).andReturn();
                MockHttpServletResponse response = result.getResponse();
                String content = response.getContentAsString();
                System.out.println("??????[" + param + "]??????[" + values[i] + "]");
                System.out.println(content);
                System.out.println("--------------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}