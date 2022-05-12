package com.inview.rentserver.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
}