package com.inview.rentserver.dao;

import com.inview.rentserver.base.DBBase;
import com.inview.rentserver.config.Init;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import person.inview.receiver.ToRentRecord;
import person.inview.receiver.ToRentalTotal;
import person.inview.receiver.ToRoomByCommunity;
import person.inview.tools.StrUtil;
import pojo.PersonDetails;
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
     */
    public List<Map<String, String>> getRoomOverview() {
        List<Map<String, String>> result = new ArrayList<>();
        getUndeleteRooms().stream().map(RoomDetails::getCommunityName).distinct().forEachOrdered(comm -> {
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
            for (RoomDetails room : getUndeleteRooms()) {
                putRoomByCommunityData(result, room);
            }
        } else {
            getDB().stream().filter(rm -> rm.getCommunityName().equals(community))
                    .forEach(rd -> putRoomByCommunityData(result, rd));
        }
        return result;
    }

    private void putRoomByCommunityData(List<ToRoomByCommunity> result, @NonNull RoomDetails room) {
        ToRoomByCommunity rb = new ToRoomByCommunity(room.getPrimary_id(), room.getRoomNumber(), room.getCommunityName(), room.getRoomArea());
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
     *
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
            if (room.getPrimary_id() == id) {
                return room;
            }
        }
        return null;
    }

    /**
     * 获取指定房间所有出租记录，包括记录ID，租户信息、开始及结束日期
     *
     * @param roomID 房间ID
     * @return
     */
    public ToRentRecord getRentRecord(int roomID) {
        RoomDetails roomDetails = findByID(roomID);
        ToRentRecord toRentRecord = null;
        if (roomDetails != null) {
            toRentRecord = new ToRentRecord(roomDetails.getPrimary_id(), roomDetails.getRoomNumber(), roomDetails.getRoomArea());
            for (RentalRecord record : recordDao.findListByRoomNumber(roomDetails.getRoomNumber())) {
                ToRentRecord.RentDate date = new ToRentRecord.RentDate();
                Optional.ofNullable(personDao.findByID(record.getManID())).ifPresent(person -> {
                    date.setPersonName(person.getName());
                    date.setManID(person.getPrimary_id());
                });
                date.setRecordID(record.getPrimary_id());
                date.setStartDate(record.getStartDate());
                date.setEndDate(record.getStartDate().plusMonths(record.getPayMonth()).plusDays(-1));
                toRentRecord.getRecord().add(date);
            }
        }
        return toRentRecord;
    }

    public PersonDetails getPersonByRoom(int roomID) {
        RoomDetails rd = findByID(roomID);
        if (rd == null || rd.getRecordId() == 0)
            return null;
        RentalRecord rr = recordDao.findByID(rd.getRecordId());
        if (rr == null || rr.getManID() == 0) {
            return null;
        }
        return personDao.findByID(rr.getManID());
    }

    /**
     * 获取指定租房记录ID的所有信息，包括房间信息、当前的出租信息、当前的租客信息
     *
     * @param recordID 租房记录ID
     */
    public ToRoomAllInfo getRoomDetailsByRecord(int recordID) {
        ToRoomAllInfo all = null;
        RentalRecord rr = recordDao.findByID(recordID);
        if (rr != null && StrUtil.isNotBlank(rr.getRoomNumber())) {
            all = new ToRoomAllInfo();
            all.setRentalRecord(rr);
            all.setRoomDetails(findByRoomNumber(rr.getRoomNumber()));
            all.setPersonDetails(personDao.findByID(rr.getManID()));
        }
        return all;
    }

    public RoomDetails findByRoomNumber(@NonNull String roomNumber) {
        for (RoomDetails room : getDB()) {
            if (roomNumber.equals(room.getRoomNumber())) {
                return room;
            }
        }
        return null;
    }

    /**
     * 获取标记为未删除的所有房源
     */
    public List<RoomDetails> getUndeleteRooms() {
        return getDB().stream().filter(rd -> !rd.isDelete()).collect(Collectors.toList());
    }

    /**
     * 获取标记为删除的所有房源
     */
    public List<RoomDetails> getDeleteRooms() {
        return getDB().stream().filter(RoomDetails::isDelete).collect(Collectors.toList());
    }

    /**
     * 获取未删除的房间的id,房间号，小区、面积、月租金、月物业费
     */
    public List<ToRentalTotal> getRentalTotal() {
        List<ToRentalTotal> result = new ArrayList<>();
        for (RoomDetails room : getUndeleteRooms()) {
            ToRentalTotal rt = new ToRentalTotal(room.getPrimary_id(), room.getRoomNumber(), room.getCommunityName(),
                    room.getRoomArea(),room.getPropertyPrice());
            RentalRecord rr = recordDao.findByID(room.getRecordId());
            if (rr != null) {
                rt.setMonthlyRent(rr.getMonthlyRent());
            }
            result.add(rt);
        }
        result.sort((o1,o2)-> Init.getChineseComparator(o1.getCommunity(),o2.getCommunity()));
        //统计
        ToRentalTotal rt=new ToRentalTotal();
        rt.setCommunity("全部");
        rt.setArea(result.stream().map(ToRentalTotal::getArea).reduce(BigDecimal::add).orElse(new BigDecimal(0)));
        rt.setMonthlyRent(result.stream().map(ToRentalTotal::getMonthlyRent).reduce(BigDecimal::add).orElse(new BigDecimal(0)));
        result.add(rt);
        return result;
    }
}
