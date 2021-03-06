package com.inview.rentserver.base;

import com.inview.rentserver.tool.DBFile;
import iface.IPrimaryID;
import person.inview.tools.StrUtil;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据文件基类
 *
 * @param <T> 取值范围为基础数据类，如 {@link pojo.RoomDetails}
 *            且与枚举 {@link com.inview.rentserver.pojo.DataEnum}中的值对应，如枚举中无此值，相应的接收器将不会收到消息
 */
public abstract class DBBase<T extends IPrimaryID> {
    /**
     * 数据存在格式为：类名，列表，如：
     * Map <"RoomDetails", List<{@link pojo.RoomDetails}>>
     */
    private final static Map<String, List> dbMap = new HashMap<>();
    /**
     * 数据库是否已修改的标志，格式为：类名，布尔值
     */
    private final static Map<String, Boolean> modifyMap = new HashMap<>();
    private final static Object lockMap = new Object();
    private static String pwd = "YW420102zxcvbnm,.asdfghjkl;";
    private Class<T> clazz;

    public DBBase() {
        clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getParamClass(){
        return clazz;
    }
    public List<T> getDB() {
        return (List<T>) DBBase.dbMap.get(clazz.getSimpleName());
    }

    public void DBChanged() {
        modifyMap.put(clazz.getSimpleName(), true);
    }

    public boolean isModify() {
        if (!modifyMap.containsKey(clazz.getSimpleName())) modifyMap.put(clazz.getSimpleName(), false);
        return modifyMap.get(clazz.getSimpleName());
    }

    public void save() {
        if (isModify() && dbMap.containsKey(clazz.getSimpleName())
                && DBFile.save(dbMap.get(clazz.getSimpleName()), pwd)) {
            modifyMap.put(clazz.getSimpleName(), false);
        }
    }

    public void read() {
        List<Object> lst = null;
        synchronized (lockMap) {
            lst = DBFile.read((Class) clazz, pwd);
        }
        if (lst != null && lst.size() != 0) {
            dbMap.put(clazz.getSimpleName(), lst);
        }
    }

    public static void setDBPwd(String pwd) {
        if (StrUtil.isNotBlank(pwd)) {
            DBBase.pwd = pwd;
        }
    }

    public  int getMaxID(){
        return getDB().stream().mapToInt(IPrimaryID::getPrimary_id).max().orElse(0);
    }

    public T findByID(int id){
        for (T t : getDB()) {
            if(t.getPrimary_id()==id)
                return t;
        }
        return null;
    }
}
