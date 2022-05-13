package com.inview.rentserver.controller;

import com.alibaba.fastjson.JSON;
import com.inview.rentserver.dao.PayPropertyDao;
import com.inview.rentserver.dao.RoomDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import person.inview.receiver.Result;
import person.inview.receiver.ToRoomByCommunity;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.StrUtil;
import pojo.PayProperty;
import pojo.PersonDetails;
import pojo.RoomDetails;
import pojo.show.ToRoomAllInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rent")
@Slf4j
@RequiredArgsConstructor
public class RoomOverviewController {
    private final RoomDao roomDao;
    private final PayPropertyDao payPropertyDao;

    @GetMapping("/getRoomOverview")
    Result getRoomOverview() {
        //获取小区名、小区内房间数量、小区内房间总面积
        List<Map<String, String>> rooms = roomDao.getRoomOverview();
        return Result.Ok("getRoomOverview", JSON.toJSONString(rooms));
    }


    @GetMapping("/getRoomByCommunity")
    Result getRoomList(String community) {
        //获取指定小区名的房间信息，房间的门牌号、面积、id、出租记录号、房租结束日期、租户id、租户名称
        List<ToRoomByCommunity> rooms = roomDao.getRoomByCommunity(community);
        return Result.Ok("getRoomByCommunity", community, JSON.toJSONString(rooms));
    }

    @GetMapping("/getRoomDetails")
    Result getRoomDetails(int roomID) {
        if (roomID == 0)
            return Result.Error(WebResultEnum.ParameterError.getCode(), "房间ID不能为0");
        //当前房间当前时段全部的租房信息
        return Result.Ok("getRoomDetails", roomDao.getRoomDetails(roomID));
    }


    @GetMapping("/getRentRecord")
    Result getRentRecord(int roomID) {
        if (roomID == 0)
            return Result.Error(WebResultEnum.ParameterError.getCode(), "房间ID不能为0");

        //获取指定房间所有出租记录，包括记录ID，租户信息、开始及结束日期
        return Result.Ok("getRentRecord", roomDao.getRentRecord(roomID));
    }

    @GetMapping("/getPersonByRoom")
    Result getPersonDetails(int roomID) {
        if (roomID == 0)
            return Result.Error(WebResultEnum.ParameterError.getCode(), "房间ID不能为0");
        PersonDetails pd = roomDao.getPersonByRoom(roomID);
        String roomNumber = Optional.ofNullable(roomDao.findByID(roomID)).map(RoomDetails::getRoomNumber).orElse(null);
        //当前房间当前租户的信息
        return Result.Ok("getPersonByRoom", roomNumber, pd);
    }

    @GetMapping("/getRoomDetailsByRecord")
    Result getRoomDetailsByRecord(int recordID) {
        if (recordID == 0)
            return Result.Error(WebResultEnum.ParameterError.getCode(), "租房记录ID不能为0");

        //当前记录的房间全部的租房信息
        return Result.Ok("getRoomDetailsByRecord", roomDao.getRoomDetailsByRecord(recordID));
    }

    @GetMapping("/getRoomPropertyPaymentStatus")
    Result getRoomPropertyPaymentStatus(String roomNumber) {
        //获取所有指定房间号的物业缴费记录
        if(StrUtil.hasBlank(roomNumber))
            return Result.Error(WebResultEnum.ParameterError.getCode(), "房间号不能为空");
        List<PayProperty> proLst = payPropertyDao.findByRoomNumber(roomNumber);
        return Result.Ok("getRoomPropertyPaymentStatus", roomNumber, proLst);
    }
}
