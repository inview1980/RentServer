package com.inview.rentserver.tool;

import com.inview.rentserver.pojo.DataEnum;
import org.junit.jupiter.api.Test;
import person.inview.tool.ExcelUtils;
import pojo.*;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DBFileTest {
    private String pwd = "YW420102zxcvbnm,.asdfghjkl;";

    //    @Test
    void save() {
        PodamFactory factory = new PodamFactoryImpl();

        List<RoomDetails> rd = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            rd.add(factory.manufacturePojo(RoomDetails.class));
        }
        assertTrue(DBFile.save(rd, pwd));
    }

    @Test
    void read() {
        System.out.println(DBFile.read(RoomDetails.class, pwd).get(2));
//        System.out.println(DBFile.read(ShoppingRecord.class,pwd).get(2));
//        System.out.println(DBFile.read(DebtRecord.class,pwd).get(2));
//        System.out.println(DBFile.read(PersonDetails.class,pwd).get(2));
//        System.out.println(DBFile.read(LivingExpenses.class,pwd).get(2));
//        System.out.println(DBFile.read(PayProperty.class,pwd).get(2));
//        System.out.println(DBFile.read(CarMaintenanceRecord.class,pwd).get(2));
//        System.out.println(DBFile.read(RentalRecord.class,pwd).get(2));
//        System.out.println(DBFile.read(UserItem.class,pwd).get(2));
//        System.out.println(DBFile.read(FuelRecord.class,pwd).get(2));
        for (DataEnum value : DataEnum.values()) {
            System.out.println(DBFile.read(value.getClazz(), pwd).get(2));
        }
    }

    @Test
    void readXls() throws IOException, ParseException, IllegalAccessException, InstantiationException {
        String filename = "F:\\server\\AndroidDB\\2021-8-23(16_01).xlsx";
        InputStream is = new FileInputStream(filename);
        List<RoomDetails> rds = ExcelUtils.readExcel(is, RoomDetails.class);
        for (int i = 0; i < rds.size(); i++) {
            rds.get(i).setPrimary_id(i + 1);
        }
        assertTrue(DBFile.save(rds, pwd));

        Class[] clazz = new Class[]{
                ShoppingRecord.class,
                DebtRecord.class,
                PersonDetails.class,
                LivingExpenses.class,
                PayProperty.class,
                CarMaintenanceRecord.class,
                RentalRecord.class,
                UserItem.class,
                FuelRecord.class,
        };
        for (Class aClass : clazz) {
            is = new FileInputStream(filename);
            assertTrue(DBFile.save(ExcelUtils.readExcel(is, aClass), pwd));
        }
    }

    @Test
    void test() {
    }
}