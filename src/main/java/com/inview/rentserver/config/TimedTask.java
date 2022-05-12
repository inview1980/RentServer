package com.inview.rentserver.config;

import com.inview.rentserver.iface.DBBase;
import com.inview.rentserver.tool.SpringBeanUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 系统定时任务，主要任务：自动上传数据库的更改、自动更新上传时需要的AccessToken、维护用户登录时的Token
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@EnableAsync
public class TimedTask {
    private static List<DBBase> dbBases = new ArrayList<>();

    //间隔24小时，检查发送给用户的Token是否过期
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    void checkTokenOverdue() {
//        tokenServer.checkTokenOverdue();

    }

    /**
     * 每隔一段时间检查数据库标志位，保存数据库
     */
//    @Scheduled(fixedRate = 1000 * 60)
    @Async
    void saveDB(){
        if (dbBases.size() == 0) {
            dbBases.addAll(SpringBeanUtil.getApplicationContext().getBeansOfType(DBBase.class).values());
        }
        for (DBBase dbDao : dbBases) {
            dbDao.save();
        }
    }
}
