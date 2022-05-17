package com.inview.rentserver.config;

import com.inview.rentserver.base.DBBase;
import com.inview.rentserver.tool.SpringBeanUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import person.inview.tools.PropertyUtil;

import java.io.IOException;
import java.text.Collator;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.Properties;

@Component
@Order(20)
@Slf4j
public class Init implements CommandLineRunner {
    @Getter
    private static final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
    @Getter
    private static String EncryptPassword = "";
    /**
     * token的有效期为7天
     */
    @Getter
    private static int TokenTimes = 60 * 60 * 24 * 7;
    private static final String ConfigFilename = "config.ini";

    @Value("${DB.Psd}")
    private String pwd;

    @Override
    public void run(String... args) {
        DBBase.setDBPwd(pwd);
        final Collection<DBBase> d = SpringBeanUtil.getApplicationContext().getBeansOfType(DBBase.class).values();
        for (DBBase dbBase : d) {
            dbBase.read();
        }
        log.info("已载入初始化类[{}]", this.getClass().getSimpleName());

        loadConfig();
    }

    private void loadConfig() {
        try {
            Properties pop = PropertyUtil.readProperty(ConfigFilename);
            Init.EncryptPassword = pop.getProperty("password", "");
            Init.TokenTimes = Integer.parseInt(pop.getProperty("TokenTimes", "604800"));
        } catch (IOException e) {
            log.error("读取配置文件失败", e);
        }
    }

    public static void setEncryptPassword(@NonNull String encryptPassword) {
        EncryptPassword = encryptPassword;
        saveConfig();
    }

    private static void saveConfig() {
        Properties pop = new Properties();
        pop.setProperty("password", Init.EncryptPassword);
        pop.setProperty("TokenTimes", String.valueOf(Init.TokenTimes));
        try {
            PropertyUtil.writeProperty(Init.ConfigFilename, pop);
        } catch (IOException e) {
            log.error("保存配置文件失败", e);
        }
    }

    public static int getChineseComparator(String s1,String s2){
        return Collator.getInstance(Locale.CHINESE).compare(s1, s2);
    }
}
