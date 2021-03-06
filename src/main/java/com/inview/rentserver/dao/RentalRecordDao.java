package com.inview.rentserver.dao;

import com.inview.rentserver.base.DBBase;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import pojo.RentalRecord;
import pojo.RoomDetails;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RentalRecordDao extends DBBase<RentalRecord> {


    public RentalRecord findByID(int recordId) {
        for (RentalRecord rr : getDB()) {
            if (rr.getPrimary_id() == recordId) {
                return rr;
            }
        }
        return null;
    }

    public List<RentalRecord> findListByRoomNumber(@NonNull String roomNumber) {
       return getDB().stream().filter(rr->roomNumber.equals(rr.getRoomNumber())).collect(Collectors.toList());
    }

}
