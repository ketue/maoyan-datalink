package com.maoyan.bigdata.datalink.datasource.dynamic;

/**
 * Created by zhaoyangyang on 2018/12/5
 */
public class DataSourceHolder {
    private final static ThreadLocal<String> dbHolder =new ThreadLocal<>();

    public static void setDbKey(String dbKey){
        dbHolder.set(dbKey);
    }

    public static String getDbKey(){
        return dbHolder.get();
    }

    public static void clear(){
        dbHolder.remove();
    }
}
