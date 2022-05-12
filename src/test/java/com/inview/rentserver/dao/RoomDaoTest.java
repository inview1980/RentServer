package com.inview.rentserver.dao;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RoomDaoTest {
    RentalRecordDao rrd=new RentalRecordDao();
    PersonDao personDao=new PersonDao();
    RoomDao roomDao = new RoomDao(rrd, personDao);

    @Test
    void getRoomByCommunity() {
//               rrd.read();
//        personDao.read();
//        roomDao.read();
//        List<Map<String, String>> lst=roomDao.getRoomByCommunity("");
//        assertTrue(lst.size() > 0);
//        System.out.println(lst.get(2));
//
//        lst = roomDao.getRoomByCommunity("祥和雅居");
//        assertTrue(lst.size() > 0);
//        System.out.println(lst.get(2));
    }
}