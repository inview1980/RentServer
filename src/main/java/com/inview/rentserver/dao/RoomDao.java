package com.inview.rentserver.dao;

import com.inview.rentserver.iface.DBBase;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import person.inview.receiver.ToRoomByCommunity;
import person.inview.tools.StrUtil;
import pojo.RentalRecord;
import pojo.RoomDetails;
import pojo.show.ToRoomAllInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoomDao extends DBBase<RoomDetails> {
    private final RentalRecordDao recordDao;
    private final PersonDao personDao;

    /**
     * 获取小区名、小区内房间数量、小区内房间总面积
     *
     * @return
     */
    public List<Map<String, String>> getRoomOverview() {
        List<Map<String, String>> result = new ArrayList<>();
        getDB().stream().map(RoomDetails::getCommunityName).distinct().forEachOrdered(comm -> {
            Map<String, String> map = new HashMap<>();
            map.put("community", comm);
            List<RoomDetails> tmp = getDB().stream().filter(room -> comm.equals(room.getCommunityName())).collect(Collectors.toList());
            long commCount = tmp.size();
            map.put("count", String.valueOf(commCount));
            BigDecimal area = tmp.stream().map(RoomDetails::getRoomArea).reduce(new BigDecimal(0), BigDecimal::add);
            map.put("area", area.toString());
            result.add(map);
        });
        return result;
    }

    /**
     * 获取指定小区名的房间信息，房间的门牌号、面积、id、出租记录号、房租结束日期、租户id、租户名称
     *
     * @param community 小区名
     * @return
     */
    public List<ToRoomByCommunity> getRoomByCommunity(String community) {
        List<ToRoomByCommunity> result = new ArrayList<>();
        if (StrUtil.hasBlank(community) || "全部".equals(community)) {
            for (RoomDetails room : getDB()) {
                putRoomByCommunityData(result, room);
            }
        } else {
            getDB().stream().filter(rm -> rm.getCommunityName().equals(community))
                    .forEach(rd -> putRoomByCommunityData(result, rd));
        }
        return result;
    }

    private void putRoomByCommunityData(List<ToRoomByCommunity> result, @NonNull RoomDetails room) {
        ToRoomByCommunity rb = new ToRoomByCommunity(room.getId(), room.getRoomNumber(), room.getCommunityName(), room.getRoomArea());
        if (room.getRecordId() != 0) {
            rb.setRecordID(room.getRecordId());
            Optional.ofNullable(recordDao.findByID(room.getRecordId())).ifPresent(rr -> {
                LocalDate endRental = rr.getStartDate().plusMonths(rr.getPayMonth()).plusDays(-1);
                rb.setEndRentalDate(endRental);
                Optional.ofNullable(personDao.findByID(rr.getManID())).ifPresent(pd -> {
                    rb.setPersonName(pd.getName());
                    rb.setPersonID(pd.getPrimary_id());
                });
            });
        }
        result.add(rb);
    }

    /**
     * 获取指定房间ID的所有信息，包括房间信息、当前的出租信息、当前的租客信息
     * @param roomID 房间的ID
     * @return {@link ToRoomAllInfo}
     */
    public ToRoomAllInfo getRoomDetails(int roomID) {
        RoomDetails roomDetails = findByID(roomID);
        if (roomDetails != null) {
            ToRoomAllInfo result = new ToRoomAllInfo(roomDetails);
            Optional.ofNullable(recordDao.findByID(roomDetails.getRecordId())).ifPresent(record -> {
                result.setRentalRecord(record);
                Optional.ofNullable(personDao.findByID(record.getManID())).ifPresent(result::setPersonDetails);
            });
            return result;
        }
        return null;
    }

    public RoomDetails findByID(int id) {
        if (id == 0) return null;
        for (RoomDetails room : getDB()) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }
}
