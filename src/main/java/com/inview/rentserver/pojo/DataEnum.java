package com.inview.rentserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.yaml.snakeyaml.util.EnumUtils;
import pojo.FuelRecord;
import pojo.RentalRecord;
import pojo.RoomDetails;

import java.util.EnumSet;

@AllArgsConstructor
public enum DataEnum {
    RoomDetails(1L, pojo.RoomDetails.class),
    RentRecord(1L << 1, RentalRecord.class),
    FuelRecord(1L << 2, pojo.FuelRecord.class);


    @Getter private final long code;
    private final Class<?> clazz;

    public static long getCode(String clazzName) {
        for (DataEnum value : DataEnum.values()) {
            if(value.clazz.getSimpleName().equals(clazzName))
                return value.code;
        }
        return 0L;
    }
}
