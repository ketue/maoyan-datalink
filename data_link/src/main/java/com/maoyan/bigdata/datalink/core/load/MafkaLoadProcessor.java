package com.maoyan.bigdata.datalink.core.load;

/**
 * @description:
 * @author: liyuejiao
 * @date: Created in 2019/11/27 14:56
 * @modified By:
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.maoyan.bigdata.datalink.utils.ListUtil;
import com.maoyan.bigdata.datalink.utils.MafkaProducerTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@Scope("prototype")
public class MafkaLoadProcessor extends LoadProcessor {

    private static final Logger logger = LoggerFactory.getLogger(LoadProcessor.class);

    //kafka appkey
    private String appkey;
    //kafka topic
    private String topic;
    //kafka 同步/异步 发送消息
    private String sendType;
    //线程数
    private Integer threadNum;

    public static final String SENDER = "data_link";

    public MafkaLoadProcessor() {
    }

    public MafkaLoadProcessor(String key, JSONObject writerJSONModel, JSONArray sourceData) {
        super(key, writerJSONModel, sourceData);
        this.appkey = String.valueOf(writeModel.getOrDefault("appkey", "com.sankuai.movie.bigdata.datalink"));
        this.topic = writeModel.getString("topic");
        this.sendType = writeModel.getString("send_type");
        this.threadNum = (Integer) writeModel.getOrDefault("thread_num", 10);
    }


    @Override
    public int process() {

        long beforeGetTime = System.currentTimeMillis();
        //获取发送的数据
        List<JSONObject> dataList = getDataList();
        logger.info("MafkaLoadProcessor get data list over,cost time : {}s",(System.currentTimeMillis() - beforeGetTime) /1000);
        logger.info("MafkaLoadProcessor get data list size is: {}", dataList.size());

        long beforeSendTime = System.currentTimeMillis();
        //发送数据
        doSend(dataList);
        logger.info("MafkaLoadProcessor send over,cost time : {}s",(System.currentTimeMillis()-beforeSendTime) /1000);

        logger.info("MafkaLoadProcessor process over!");
        return 1;
    }

    private List<JSONObject> getDataList() {
        logger.info("MafkaLoadProcessor get data list start!");
        List<JSONObject> dataList = Lists.newArrayList();
        long sendTimestamp = System.currentTimeMillis();
        int totalNum = sourceData.size();//数据发送总条数
        for (int i = 0; i < sourceData.size(); i++) {
            JSONObject rowJO = sourceData.getJSONObject(i);
            JSONObject allDataJson = new JSONObject();
            allDataJson.put("data", rowJO);

            allDataJson.put("send_timestamp", sendTimestamp);
            allDataJson.put("sender", SENDER);
            allDataJson.put("total_num", totalNum);
            allDataJson.put("key", this.modelKey);

            dataList.add(allDataJson);
        }

        return dataList;
    }


    public void doSend(List<JSONObject> dataList) {
        logger.info("MafkaLoadProcessor send start!");
        ExecutorService executorService = Executors.newFixedThreadPool(this.threadNum);
        List<List<JSONObject>> dataListList = ListUtil.splitList(dataList, this.threadNum);

        try {
            List<Future<Integer>> fList = Lists.newArrayList();

            for (int i = 0; i < dataListList.size(); i++) {
                MafkaLoader mafkaLoader = new MafkaLoader(dataListList.get(i), this.sendType, this.appkey, this.topic, i);
                fList.add(executorService.submit(mafkaLoader));
            }

            Integer sum = 0;
            for (Future<Integer> integerFuture : fList) {
                sum += integerFuture.get();
            }

            logger.info("MafkaLoadProcessor send total number : {} thread process number : {}", sourceData.size(), sum);

            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    多线程执行批量发送数据
    */
    public class MafkaLoader implements Callable<Integer> {

        private List<JSONObject> dataList;
        private String sendType;
        private String appkey;
        private String topic;
        private String threadName;


        public MafkaLoader(List<JSONObject> dataList, String sendType, String appkey, String topic, int i) {
            this.dataList = dataList;
            this.sendType = sendType;
            this.appkey = appkey;
            this.topic = topic;
            this.threadName = this.getClass().getSimpleName() + "-" + i;
        }

        @Override
        public Integer call() {
            MafkaProducerTools mafkaProducerTool = new MafkaProducerTools(this.appkey, this.topic);
            long threadStartTime = System.currentTimeMillis();
            for (JSONObject jsonData : dataList) {
                if (this.sendType.equals("sync")) {
                    mafkaProducerTool.sendMessage(jsonData.toJSONString());
                } else if (this.sendType.trim().equals("async")) {
                    mafkaProducerTool.sendAsyncMessage(jsonData.toJSONString());
                }
            }
            logger.info("{} write data : {} lines, cost: {} s", threadName, dataList.size(), (System.currentTimeMillis() - threadStartTime) / 1000);

            return 1;

        }
    }


    public static void main(String[] args) {
        String aa = "f fdsaf fdsa fd fd \n fdsa fds f\t fdsafd f fd s";
        System.out.println(aa.replaceAll("\\s+", ""));
    }

}

