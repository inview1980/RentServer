package com.inview.rentserver.tool;

import com.inview.rentserver.base.DBBase;
import iface.IPrimaryID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhoushun
 * @ClassName: SpringBeanUtil
 * @Description: TODO(spring功能类 ， 用于获取bean)
 * @date 2012-11-27 下午04:22:36
 */
@Component("springBeanUtil")
@Slf4j
public class SpringBeanUtil implements ApplicationContextAware {
    private static ApplicationContext ctx = null;


    public void setApplicationContext(ApplicationContext ctx) {
        SpringBeanUtil.ctx = ctx;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    public static <T> T getBean(String prop) {
        Object obj = ctx.getBean(prop);
        if (log.isDebugEnabled()) {
            log.debug("property=[" + prop + "],object=[" + obj + "]");
        }
        return (T) obj;
    }

    /**
     * 返回继承自{@link DBBase}类，比如{@link com.inview.rentserver.dao.RoomDao}类，调用格式：getParamBean(DBBase.class,"RoomDetails")
     * @param clazz 继承自{@link DBBase}类
     * @param prop 泛形的第一个参数类的类名
     */
    public static DBBase<IPrimaryID> getParamBean(Class<DBBase> clazz, String prop){
        for (Map.Entry<String, DBBase> entry : ctx.getBeansOfType(clazz).entrySet()) {
            if(entry.getValue().getParamClass().getSimpleName().equals(prop))
                return entry.getValue();
        }
        return null;
    }
}