package com.inview.rentserver.config;

import com.inview.rentserver.base.DBBase;
import com.inview.rentserver.tool.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import person.inview.tools.PropertyUtil;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Component
@Order(20)
@Slf4j
public class Init implements CommandLineRunner {
    private final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
    private static String EncryptPassword ="";
    /**
     * token的有效期为7天
     */
    private static int TokenTimes =1000 * 60 * 60 *24 *7;

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

        loadConfig();
    }

    private void loadConfig() {
        try {
            Properties pop = PropertyUtil.readProperty("config.ini");
            Init.EncryptPassword =pop.getProperty("password", "");
            Init.TokenTimes = Integer.parseInt(pop.getProperty("TokenTimes", "604800"));
        } catch (IOException e) {
            log.error("读取配置文件失败",e);
        }
    }

    public DateTimeFormatter LocalDateFormatter() {
        return localDateFormatter;
    }

    public static String getEncryptPassword(){
        return Init.EncryptPassword;
    }
    public static int getTokenTimes(){return Init.TokenTimes;}
}
