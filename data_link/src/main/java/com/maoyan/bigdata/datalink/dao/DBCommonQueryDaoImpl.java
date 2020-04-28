package com.maoyan.bigdata.datalink.dao;

import com.maoyan.bigdata.datalink.datasource.dynamic.DataSourceHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyangyang on 2018/12/5
 */
@Repository
public class DBCommonQueryDaoImpl implements DBCommonQueryDao {

    @Resource(name = "dynamicJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, String>> query(String sql, String storeType) {
        DataSourceHolder.setDbKey(storeType);
        return jdbcTemplate.query(sql, (resultSet, row) -> {
            Map<String, String> data = new HashMap<>();
            ResultSetMetaData r = resultSet.getMetaData();
            for (int i = 1; i <= r.getColumnCount(); i++) {
                data.put(r.getColumnLabel(i), resultSet.getString(i));
            }
            return data;
        });
    }

    @Override
    public int delete(String sql, String storeType){
        DataSourceHolder.setDbKey(storeType);
        return jdbcTemplate.update(sql);
    }

    /**
     * 批量插入sql
     *
     * @param sqlList
     * @param storeType
     * @return
     */
    @Override
    public int[] insertData(List<String> sqlList, String storeType) {
        if (sqlList.size() > 0) {
            DataSourceHolder.setDbKey(storeType);
            String[] sqlArr = new String[sqlList.size()];
            return jdbcTemplate.batchUpdate(sqlList.toArray(sqlArr));
        } else {
            return new int[]{0};
        }
    }


    /**
     * 获取目标表的列和类型
     *
     * @param tableName
     * @param storeType
     * @return
     */
    @Override
    public Map<String, String> getTableMetaData(String tableName, String storeType) {
        String sql = "SELECT * FROM " + tableName + " LIMIT 0";
        DataSourceHolder.setDbKey(storeType);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
        Map<String, String> data = new HashMap<>();
        int cCount = metaData.getColumnCount();
        for (int i = 1; i <= cCount; i++) {
            data.put(metaData.getColumnName(i), metaData.getColumnTypeName(i));
        }
        return data;
    }

}
