package com.maoyan.bigdata.datalink.core.transform;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maoyan.bigdata.datalink.utils.SQLiteTools;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class SQLiteTransformProcessor extends TransformProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SQLiteTransformProcessor.class);


    public SQLiteTransformProcessor() {
    }

    public SQLiteTransformProcessor(String key, JSONObject transformJsonModel, Map<String, Pair<JSONArray, List<String>>> data) {
        super(key, transformJsonModel, data);
    }
    /**
     * 使用 sqlite 进行 transform 操作
     *
     * @return 列名, 结果集
     * @throws Exception
     */
    public JSONArray process() throws Exception {
        Connection connection = SQLiteTools.getConnect(modelKey);
        Statement statement = connection.createStatement();
        String transformSql = this.transformJsonModel.getString("transform_sql");

        JSONArray resultJa;
        try {
            logger.info("Data transform create table and insert data start");
            for (String tableName : this.data.keySet()) {
                logger.info("Data transform:process {}",tableName);
                Pair<JSONArray, List<String>> sourceDataPair = this.data.get(tableName);
                JSONArray tableData = sourceDataPair.getValue0();
                List<String> filedList = sourceDataPair.getValue1();

                StringBuilder createTableSB = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
                for (String filed : filedList) {
                    createTableSB.append(filed).append(" TEXT,");
                }
                String createTableSQL = createTableSB.substring(0, createTableSB.length() - 1) + ");";
                statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);//判断是否有表tables的存在。有则删除
                statement.executeUpdate(createTableSQL);
                logger.info("Data transform create table sql:{}",createTableSQL);

                StringBuilder insertSb = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
                insertSb.append(String.join(",", filedList)).append(") VALUES ");
                for (int i = 0; i < tableData.size(); i++) {
                    JSONObject rowJo = tableData.getJSONObject(i);
                    StringBuilder valueSb = new StringBuilder("(");
                    for (String key : filedList) {
                        String targetValue =null;
                        if(rowJo.containsKey(key) && rowJo.get(key)!=null){
                            targetValue = "'"+rowJo.getString(key).replace("'", "''")+"'";
                        }
                        valueSb.append(targetValue).append(",");
                    }
                    insertSb.append(valueSb.substring(0, valueSb.length() - 1) + "),");
                }

                String insertSql = insertSb.substring(0, insertSb.length() - 1) + ";";
                try {
                    statement.executeUpdate(insertSql);
                } catch (Exception e) {
                    logger.error(insertSql);
                    e.printStackTrace();
                }
            }
            logger.info("Data transform create table and insert data end");
            ResultSet rs = statement.executeQuery(transformSql);
            resultJa = SQLiteTools.resultSetToJsonArray(rs);
            logger.info("Data transform over!");
        } finally {
            SQLiteTools.closeConnect(connection, statement);
        }
        return resultJa;
    }

}
