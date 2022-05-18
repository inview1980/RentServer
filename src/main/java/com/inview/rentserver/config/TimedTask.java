package com.inview.rentserver.config;

import com.inview.rentserver.base.DBBase;
import com.inview.rentserver.http.TokenUtil;
import com.inview.rentserver.http.VerificationCodeUtil;
import com.inview.rentserver.tool.SpringBeanUtil;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import person.inview.tool.ExcelUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统定时任务，主要任务：自动上传数据库的更改、自动更新上传时需要的AccessToken、维护用户登录时的Token
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@EnableAsync
@Slf4j
public class TimedTask {
    private static final List<DBBase> dbBases = new ArrayList<>();

    //间隔1小时，检查发送给用户的Token是否过期
    @Scheduled(fixedRate = 1000 * 60 * 60)
    void tokenOverdue() {
        TokenUtil.cleanUserToken(StaticValues.TokenTimes);
    }


    //间隔5秒，检查发送给用户的VerificationCode是否过期
    @Scheduled(fixedRate = 1000 * 60 * 60)
    void verificationCodeOverdue() {
        VerificationCodeUtil.clearVerificationCode(StaticValues.VerificationCodeTimes);
    }

    /**
     * 每隔一段时间检查数据库标志位，保存数据库
     */
//    @Scheduled(fixedRate = 1000 * 60)
    @Async
    void saveDB() {
        for (DBBase dbDao : getDbBases()) {
            dbDao.save();
        }
    }

    /**
     * 每隔24小时自动保存数据库，延迟5秒执行
     */
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24, initialDelay = 1000 * 5)
//    @Async
    public static void backupDB() throws IOException, InvocationTargetException, IllegalAccessException {
        String dir = "DBBackup/";
        if (!new File(dir).exists()) {
            new File(dir).mkdir();
        }
        String path = dir + LocalDate.now().toString() + ".xls";
        List<List<?>> data = new ArrayList<>();
        for (DBBase dbBase : getDbBases()) {
            data.add(dbBase.getDB());
        }
        ExcelUtils.toExcel(path, data);
        log.info("数据库备份到[{}]文件。", path);
    }


    private static List<DBBase> getDbBases() {
        if (dbBases.size() == 0) {
            dbBases.addAll(SpringBeanUtil.getApplicationContext().getBeansOfType(DBBase.class).values());
        }
        return dbBases;
    }
}
