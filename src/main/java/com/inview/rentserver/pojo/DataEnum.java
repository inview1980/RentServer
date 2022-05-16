package com.inview.rentserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pojo.RentalRecord;
import pojo.RoomDetails;
import pojo.*;

@AllArgsConstructor
public enum DataEnum {
    RoomDetails(1L, RoomDetails.class),
    RentRecord(1L << 1, RentalRecord.class),
    FuelRecord(1L << 2, FuelRecord.class),
    ShoppingRecord(1L << 3, ShoppingRecord.class),
    DebtRecord(1L << 4, DebtRecord.class),
    PersonDetails(1L << 5, PersonDetails.class),
    LivingExpenses(1L << 6, LivingExpenses.class),
    PayProperty(1L << 7, PayProperty.class),
    CarMaintenanceRecord(1L << 8, CarMaintenanceRecord.class),
    UserItem(1L << 9, UserItem.class);
//    FuelRecord(1L << 10, FuelRecord.class);


    @Getter
    private final long code;
    @Getter
    private final Class<?> clazz;

    public static long getCode(String clazzName) {
        for (DataEnum value : DataEnum.values()) {
            if (value.clazz.getSimpleName().equals(clazzName))
                return value.code;
        }
        return 0L;
    }
}
