package com.maoyan.bigdata.datalink.core.load;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.maoyan.bigdata.datalink.dao.DBCommonQueryDao;
import com.maoyan.bigdata.datalink.utils.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@Scope("prototype")
public class MySQLLoadProcessor extends LoadProcessor {
    private static final Logger logger = LoggerFactory.getLogger(LoadProcessor.class);
    //目标db
    private String targetDB;
    //目标表明成
    private String targetTable;
    //要写入的字段
    private String[] fields;
    //写入方式
    private String writeType;
    //写入前操作
    private String preWrite;
    //插入方式
    private String insertModel;//INSERT INTO / INSERT IGNORE INTO / INSERT INTO VALUES ON DUPLICATE KEY UPDATE / REPLACE INTO
    //线程数
    private Integer threadNum;

    @Autowired
    DBCommonQueryDao dbCommonQueryDao;

    public MySQLLoadProcessor() {
    }

    public MySQLLoadProcessor(String key, JSONObject writerJSONModel, JSONArray sourceData) {
        super(key, writerJSONModel, sourceData);
        this.targetDB = writeModel.getString("target_database");
        this.targetTable = writeModel.getString("target_table");
        this.fields = writeModel.getString("fields").replaceAll("\\s+", "").split(",");
        this.writeType = writeModel.getString("write_type");
        this.preWrite = writeModel.getString("pre_write");
        this.insertModel = writeModel.getString("insert_model");
        this.threadNum = (Integer) writeModel.getOrDefault("thread_num", 10);
    }

    @Override
    public int process() {
        //获取 target table 的字段类型.
        Map<String, String> schemaMap = dbCommonQueryDao.getTableMetaData(this.targetTable, this.targetDB);

        long beforeBuildTime = System.currentTimeMillis();
        //获得sql集合
        List<String> insertSQLList = buildSqlList(schemaMap);
        logger.info("MySQLLoadProcessor build sql over, cost time : {} s", (System.currentTimeMillis() - beforeBuildTime) /1000);
        logger.info("MySQLLoadProcessor build sql list size is :{} ", insertSQLList.size());

        long beforePreWriteTime = System.currentTimeMillis();
        //执行preWrite操作
        doPreWrite();
        logger.info("MySQLLoadProcessor preWrite over, cost time : {} s", (System.currentTimeMillis() - beforePreWriteTime) /1000);

        long beforeWriteTime = System.currentTimeMillis();
        //执行write操作
        doWrite(insertSQLList);
        logger.info("MySQLLoadProcessor write over, cost time : {} s", (System.currentTimeMillis() - beforeWriteTime) /1000);

        logger.info("MySQLLoadProcessor process over!");

        return 1;

    }

    private List<String> buildSqlList(Map<String, String> schemaMap) {
        logger.info("MySQLLoadProcessor build Sql List start");
        List<String> insertSQLList = Lists.newArrayList();
        for (int i = 0; i < sourceData.size(); i++) {
            List<String> fieldsList = Lists.newArrayList();
            List<String> valueList = Lists.newArrayList();
            String sentence = " ";
            JSONObject rowJO = sourceData.getJSONObject(i);
            for (String field : this.fields) {
                if (rowJO.containsKey(field) && rowJO.get(field) != null) {
                    fieldsList.add(field);
                    sentence += field + " = VALUES(" + field + "),";
                    if ("varchar".equalsIgnoreCase(schemaMap.get(field))) {
                        String targetValue = null;
                        if (rowJO.containsKey(field) && rowJO.get(field) != null) {
                            targetValue = "'" + rowJO.getString(field).replace("'", "''") + "'";
                        }
                        valueList.add(targetValue);
                    } else {
                        valueList.add(String.valueOf(rowJO.get(field)));
                    }
                }
            }

            String insertStr = getInsertStr(sentence, fieldsList, valueList);

            insertSQLList.add(insertStr);
        }

        return insertSQLList;

    }

    private void doPreWrite() {
        logger.info("MySQLLoadProcessor preWrite start!");

        if (this.preWrite != null && this.preWrite.length() > 0) {
            logger.info("MySQLLoadProcessor preWrite sqls: {}", this.preWrite);
            String[] preWriteSQLs = this.preWrite.split(";");
            for (String preWriteSQL : preWriteSQLs) {
                if (preWriteSQL.trim().length() > 0) {
                    logger.info("MySQLLoadProcessor preWrite sql: {}", preWriteSQL.trim());
                    dbCommonQueryDao.delete(preWriteSQL.trim(), this.targetDB);
                }
            }
        }

    }

    /*
    多线程执行批量插入数据
    */
    public void doWrite(List<String> insertSQLList) {
        ExecutorService executorService;
        List<List<String>> insertSQLLists;
        if (!"1".equals(this.insertModel)) {
            this.threadNum = 1;
        }
        executorService = Executors.newFixedThreadPool(this.threadNum);
        insertSQLLists = ListUtil.splitList(insertSQLList, this.threadNum);
        List<Future<Integer>> fList = Lists.newArrayList();

        try {
            long writeStartTime = System.currentTimeMillis();
            logger.info("MySQLLoadProcessor write start!");
            Integer sum = 0;
            for (int i = 0; i < insertSQLLists.size(); i++) {
                MysqlLoader mysqlLoader = new MysqlLoader(insertSQLLists.get(i), this.targetDB, i);
                Future<Integer> future = executorService.submit(mysqlLoader);
                fList.add(future);
            }
            logger.info("MySQLLoadProcessor write over, cost time : {} s", (System.currentTimeMillis() - writeStartTime) / 1000);

            for (Future<Integer> integerFuture : fList) {
                sum += integerFuture.get();
            }

            logger.info("MySQLLoadProcessor write total number :{} thread process number : {}", sourceData.size(), sum);

            executorService.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
    load数据到Mysql
     */
    public class MysqlLoader implements Callable<Integer> {

        private List<String> insertSqlList;
        private String targetDB;
        private String threadName;


        public MysqlLoader(List<String> insertSqlList, String targetDB, int i) {
            this.insertSqlList = insertSqlList;
            this.targetDB = targetDB;
            this.threadName = this.getClass().getSimpleName() + "-" + i;
        }

        @Override
        public Integer call() {
            List<List<String>> sqlListList = ListUtil.splitListBySize(this.insertSqlList, DEFAULT_BLOCK_SIZE);
            long threadStartTime = System.currentTimeMillis();
            for (List<String> sqlList : sqlListList) {
                //插入新数据
                dbCommonQueryDao.insertData(sqlList, this.targetDB);
            }

            logger.info("{} write data : {} lines, cost: {} s", threadName, sqlListList.size(), (System.currentTimeMillis() - threadStartTime) / 1000);

            return 1;
        }
    }


    /*
    获得插入数据的sql语句
    */
    public String getInsertStr(String sentence, List<String> fieldsList, List<String> valueList) {
        String insertSqlStr;
        String middleStr = this.targetTable +
                " (" +
                String.join(",", fieldsList) +
                ") VALUES ( " +
                String.join(",", valueList) +
                ")";
        switch (this.insertModel) {
            case "2":
                insertSqlStr = "INSERT IGNORE INTO " + middleStr;
                break;
            case "3":
                insertSqlStr = "REPLACE INTO " + middleStr;
                break;

            case "4":
                int i = sentence.lastIndexOf(",");
                String sen = sentence.substring(0, i);
                insertSqlStr = "INSERT INTO " + middleStr + " ON DUPLICATE KEY UPDATE " + sen;
                break;

            default:
                insertSqlStr = "INSERT INTO " + middleStr;
        }
        return insertSqlStr;
    }


    public static void main(String[] args) {
        List<String> abc = Lists.newArrayList();
        abc.add("a");
        abc.add("b");
        abc.add(null);
        abc.add("c");
        System.out.println(String.join(",", abc));
    }


}
