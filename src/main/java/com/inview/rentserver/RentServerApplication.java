package com.inview.rentserver;

import com.inview.rentserver.dao.RoomDao;
import com.inview.rentserver.tool.SpringBeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RentServerApplication  {

    public static void main(String[] args) {
        SpringApplication.run(RentServerApplication.class, args);
    }

}
