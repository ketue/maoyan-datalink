package com.maoyan.bigdata.datalink.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import org.javatuples.Pair;

import java.sql.*;
import java.util.Set;

public class SQLiteTools {
    private static final String DRIVE = "org.sqlite.JDBC";

    /**
     * 创建连接
     *
     * @param dbName
     * @return
     * @throws Exception
     */
    public static Connection getConnect(String dbName) throws Exception {
        Class.forName(DRIVE);// 加载驱动,连接SQLite的jdbc
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");//连接
        return connection;
    }


    public static void closeConnect(Connection connection, Statement statement) {
        closeStatement(statement);
        closeConnect(connection);
    }

    public static void closeConnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将resultSet转化为JSON数组
     *
     * @param rs
     * @return  结果集
     * @throws SQLException
     * @throws JSONException
     */
    public static  JSONArray resultSetToJsonArray(ResultSet rs) throws SQLException, JSONException {
        // json数组
        JSONArray array = new JSONArray();
        // 获取列数
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        // 遍历ResultSet中的每条数据
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();
            // 遍历每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                jsonObj.put(columnName, rs.getString(columnName));
            }
            array.add(jsonObj);
        }
        return array;
    }

    public static void main(String[] args) throws Exception{
        SQLiteTools.getConnect("abc");
    }


}
