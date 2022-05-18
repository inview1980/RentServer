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
    private static final String ConfigFilename = "config.ini";

    /**
     * token的有效期为7天
     */
    @Value("${Config.TokenTimes}")
    private int tokenTimes;

    /**
     * VerificationCodeTimes的有效期为60秒
     */
    @Value("${Config.VerificationCodeTimes}")
    private int verificationCodeTimes;

    @Value("${Config.PasswordLength}")
    private int passwordLength;

    @Value("${Config.TokenLength}")
    private int tokenLength;

    @Value("${Config.VerificationCodeLength}")
    private int verificationCodeLength;

    @Value("${DB.Psd}")
    private String pwd;

    @Override
    public void run(String... args) {
        setStaticValues();

        final Collection<DBBase> d = SpringBeanUtil.getApplicationContext().getBeansOfType(DBBase.class).values();
        for (DBBase dbBase : d) {
            dbBase.read();
        }
        log.info("已载入初始化类[{}]", this.getClass().getSimpleName());

        loadConfig();
    }

    private void setStaticValues() {
        StaticValues.VerificationCodeLength = this.verificationCodeLength;
        StaticValues.TokenTimes = this.tokenTimes;
        StaticValues.VerificationCodeTimes = this.verificationCodeTimes;
        StaticValues.PasswordLength = this.passwordLength;
        StaticValues.TokenLength = this.tokenLength;
        DBBase.setDBPwd(pwd);
    }

    private void loadConfig() {
        try {
            Properties pop = PropertyUtil.readProperty(ConfigFilename);
            Init.EncryptPassword = pop.getProperty("password", "");
        } catch (IOException e) {
            log.error("读取配置文件失败", e);
        }
    }

    /**
     * 设置登录的密码，此密码是经过HmacSHA256加密后的字符串，加密此密码的密码在配置文件中
     */
    public static void setEncryptPassword(@NonNull String encryptPassword) {
        Init.EncryptPassword = encryptPassword;
        saveConfig();
    }

    private static void saveConfig() {
        Properties pop = new Properties();
        pop.setProperty("password", Init.EncryptPassword);
        try {
            PropertyUtil.writeProperty(Init.ConfigFilename, pop);
        } catch (IOException e) {
            log.error("保存配置文件失败", e);
        }
    }

    public static int getChineseComparator(String s1, String s2) {
        return Collator.getInstance(Locale.CHINESE).compare(s1, s2);
    }
}
