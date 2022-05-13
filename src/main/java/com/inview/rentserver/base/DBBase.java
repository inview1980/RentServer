package com.inview.rentserver.base;

import com.inview.rentserver.tool.DBFile;
import person.inview.tools.StrUtil;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DBBase<T> {
    /**
     * 数据存在格式为：类名，列表，如：
     * Map <"RoomDetails", List<{@link pojo.RoomDetails}>>
     */
    private final static Map<String, List<Object>> dbMap = new HashMap<>();
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
            lst = DBFile.read((Class<Object>) clazz, pwd);
        }
        if (lst != null && lst.size() != 0) {
            dbMap.put(clazz.getSimpleName(), lst);
        }
    }

    public static void setDBPwd(String pwd) {
        if (StrUtil.isNotBlank(pwd)) {
            DBBase.pwd=pwd;
        }
    }
}
