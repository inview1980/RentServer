package com.inview.rentserver.dao;

import org.junit.jupiter.api.Test;
import pojo.RentalRecord;
import pojo.RoomDetails;
import pojo.show.ToRoomAllInfo;

import java.time.LocalDate;
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

        ToRoomAllInfo room=new ToRoomAllInfo();
        System.out.println(room.getRentalEndDate());
        RentalRecord rd=new RentalRecord();
        rd.setStartDate(LocalDate.now());
        rd.setPayMonth(3);
        room.setRentalRecord(rd);
        LocalDate ld=room.getRentalEndDate();
        System.out.println(ld);
    }
}