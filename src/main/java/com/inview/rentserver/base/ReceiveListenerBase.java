package com.inview.rentserver.base;

import com.alibaba.fastjson.JSONObject;
import com.inview.rentserver.pojo.DataEnum;
import com.inview.rentserver.tool.MyException;
import com.inview.rentserver.tool.SpringBeanUtil;
import iface.IPrimaryID;
import lombok.extern.slf4j.Slf4j;
import person.inview.receiver.Receiver;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.StrUtil;

import java.lang.reflect.ParameterizedType;

@Slf4j
/**
 * 广播接收器基类
 *
 * @param <T> 取值范围为基础数据类，如 {@link pojo.RoomDetails}
 *            且与枚举 {@link DataEnum}中的值对应，如枚举中无此值，相应的接收器将不会收到消息
 */
public abstract class ReceiveListenerBase<T extends IPrimaryID> {
    private Class<T> clazz;

    public ReceiveListenerBase() {
        clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 注册广播，并根据传入的{@link Receiver}中DataCode字段分派给相应的子类的notice子程序处理
     */
    public boolean register(Receiver receiver) {
        long dataCode = receiver.getDataCode();
        long code = DataEnum.getCode(clazz.getSimpleName());
        long l = code & dataCode;
        if (l != 0) {
            log.info("数据库[{}]已修改", clazz.getSimpleName());
            return notice(receiver);
        }
        return true;
    }

    /**
     * 接收到指定编码的消息后，执行此方法
     */
    public abstract boolean notice(Receiver receiver);

    /**
     * 修改或新建数据库的pojo
     *
     * @param data 数据库的pojo类Json序列化后的字符串
     */
    public boolean updateOrAddPojo(String data) {
        if (StrUtil.hasBlank(data)) throw new MyException(WebResultEnum.ParameterError);

        T rd = JSONObject.parseObject(data, clazz);
        DBBase<IPrimaryID> base = SpringBeanUtil.getParamBean(DBBase.class, clazz.getSimpleName());
        if (base == null) {
            log.error("没有找到相应的数据库类[{}]", clazz.getSimpleName());
            return false;
        }
        if (rd.getPrimary_id() == 0) {
            rd.setPrimary_id(base.getMaxID() + 1);
            base.getDB().add((rd));
        } else {
            IPrimaryID old = base.findByID(rd.getPrimary_id());
            if (old != null) {
                base.getDB().removeIf((t) -> t.getPrimary_id() == rd.getPrimary_id());
                base.getDB().add(rd);
            }
        }
        base.DBChanged();
        return true;
    }

    public boolean deleteByID(int id) {
        if (id == 0) return true;
        DBBase<IPrimaryID> base = SpringBeanUtil.getParamBean(DBBase.class, clazz.getSimpleName());
        if (base != null) {
            if (base.getDB() != null) {
                base.getDB().removeIf((t) -> t.getPrimary_id() == id);
                return true;
            } else {
                log.error("数据库[{}]空值", clazz.getSimpleName());
            }
        } else {
            log.error("没有找到相应的数据库类[{}]", clazz.getSimpleName());
        }
        return false;
    }
}
