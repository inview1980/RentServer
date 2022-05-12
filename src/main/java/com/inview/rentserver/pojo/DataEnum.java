package com.inview.rentserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataEnum {
    RoomDetails(1L),
    RentRecord(1L << 1),
    FuelRecord(1L << 2);


    private final long code;
}
