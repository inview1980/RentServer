package com.inview.rentserver.controller;

import com.alibaba.fastjson.JSON;
import com.inview.rentserver.dao.RoomDao;
import com.inview.rentserver.server.RoomModifyServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import person.inview.receiver.Result;
import person.inview.receiver.ToRoomByCommunity;
import person.inview.receiver.WebResultEnum;
import pojo.RoomDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rent")
@Slf4j
@RequiredArgsConstructor
public class RoomOverviewController {
    private final RoomDao roomDao;


    @GetMapping("/getRoomOverview")
    Result getRoomOverview() {
        List<Map<String, String>> rooms = roomDao.getRoomOverview();
        return Result.Ok("getRoomOverview", JSON.toJSONString(rooms));
    }


    @GetMapping("/getRoomByCommunity")
    Result getRoomList(String community) {
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
        //住户、租房时间段
        return Result.Ok();
// TODO: 2022/5/12  
    }

    @GetMapping("/getPerson")
    Result getPersonDetails(int roomID) {
        //当前房间当前租户的信息
        return Result.Ok();
// TODO: 2022/5/12  
    }
}
