package com.inview.rentserver.config;

import com.inview.rentserver.base.DBBase;
import com.inview.rentserver.tool.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Order(20)
@Slf4j
public class Init implements CommandLineRunner {
    private final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");

    @Value("${DB.Psd}")
    private String pwd;


    @Override
    public void run(String... args) throws Exception {
        DBBase.setDBPwd(pwd);
        val d = SpringBeanUtil.getApplicationContext().getBeansOfType(DBBase.class).values();
        for (DBBase dbBase : d) {
            dbBase.read();
        }
        log.info("已载入初始化类[{}]", this.getClass().getSimpleName());
    }

    public DateTimeFormatter LocalDateFormatter() {
        return localDateFormatter;
    }
}
