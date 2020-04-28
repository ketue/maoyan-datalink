package com.maoyan.bigdata.datalink.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyangyang on 2018/12/5
 */
public interface DBCommonQueryDao {
    List<Map<String, String>> query(String sql, String storeType);

    int delete(String sql,String storeType);

    int[] insertData(List<String> sql, String storeType);

    Map<String, String> getTableMetaData(String tableName, String storeType);
}
