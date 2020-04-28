package com.maoyan.bigdata.datalink.core.extract;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.maoyan.bigdata.datalink.utils.ParamParseUtil;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

@Component
public class DataExtract {

    private static final Logger logger = LoggerFactory.getLogger(DataExtract.class);

    ExtractProcessor extractProcessor;

    public static final String ALIAS_DYNAMIC_PARAM = "dynamic_param";


    @Autowired
    private org.springframework.beans.factory.BeanFactory beanFactory;

    public Map<String, Pair<JSONArray, List<String>>> dataExtract(String name, JSONObject extractJson) throws Exception {
        logger.info("Data extract start! ");
        JSONObject dynamicJson = extractJson.getJSONObject(ALIAS_DYNAMIC_PARAM);
        int threadNum = 1;
        if (dynamicJson != null) {
            threadNum = Integer.valueOf(dynamicJson.getOrDefault("thread_num", 1) + "");
        }
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        logger.info("init threadPool size:{} end.", threadNum);
        //获取动态参数数据
        Map<String, JSONArray> dynamicParamData = ExtractDynamicParamData(extractJson);

        Map<String, Pair<JSONArray, List<String>>> dataMap = Maps.newHashMap();//存放最终结果数据
        for (String alias : extractJson.keySet()) {
            if (ALIAS_DYNAMIC_PARAM.equals(alias)) continue;
            JSONObject dataModelJo = extractJson.getJSONObject(alias);
            String type = String.valueOf(dataModelJo.getOrDefault("type", "rtwh"));
            String para = dataModelJo.getString("para");
            String[] fields = ParamParseUtil.getFields(para);

            List<Future<Pair<JSONArray, List<String>>>> result = new ArrayList<>();
            switch (fields.length) {
                case 0:
                    process(executorService, alias, dataModelJo, type, result);
                    break;
                case 1:
                    String fieldKey = fields[0];
                    JSONObject paramJson = extractJson.getJSONObject(ALIAS_DYNAMIC_PARAM).getJSONObject("params");
                    Map<String, Set> dynData = transformDynamicData(paramJson, dynamicParamData);
                    Set data = dynData.get(fieldKey);
                    for (Object datum : data) {
                        String newPram = ParamParseUtil.replaceDynamicData(para, fieldKey, String.valueOf(datum));
                        dataModelJo.put("para", newPram);
                        process(executorService, alias, dataModelJo, type, result);
                    }
                    break;
                default:
                    throw new RuntimeException("do not support dynamic param more than one.");
            }
            for (Future<Pair<JSONArray, List<String>>> future : result) {
                Pair<JSONArray, List<String>> pair = future.get();
                if (pair != null) {
                    Pair<JSONArray, List<String>> mapj = dataMap.get(alias);
                    if (mapj != null) {
                        mapj.getValue0().addAll(pair.getValue0());
                    } else {
                        dataMap.put(alias, pair);
                    }
                    logger.info("Data extract tableName:{},table data size:{}", alias, pair.getValue0().size());
                } else {
                    throw new Exception(String.format("DataModel {} get data is null", dataModelJo));
                }
            }
        }
        executorService.shutdown();
        logger.info("Data extract over!");
        return dataMap;
    }

    private void process(ExecutorService executorService, String alias, JSONObject dataModelJo, String type, List<Future<Pair<JSONArray, List<String>>>> result) throws Exception {
        switch (type.toLowerCase()) {
            case "rtwh":
                extractProcessor = beanFactory.getBean(RTWHExtractProcessor.class, alias, dataModelJo);
                Future<Pair<JSONArray, List<String>>> futureRtwh = executorService.submit(extractProcessor);
                result.add(futureRtwh);
                break;
            case "rowpiece":
                extractProcessor = beanFactory.getBean(RowPieceExtractProcessor.class, alias, dataModelJo);
                Future<Pair<JSONArray, List<String>>> futureRow = executorService.submit(extractProcessor);
                result.add(futureRow);
                break;
            default:
                throw new Exception(String.format("Unsupported Data extract type:{}", type));
        }
    }

    /**
     * 转换数据格式
     *
     * @param paramJson
     * @param paramData
     * @return
     */
    private Map<String, Set> transformDynamicData(JSONObject paramJson, Map<String, JSONArray> paramData) {
        Map<String, Set> groupData = new HashMap<>();
        for (String s : paramJson.keySet()) {
            JSONArray data = paramData.get(s);
            for (int i = 0; i < data.size(); i++) {
                JSONObject dat = data.getJSONObject(i);
                for (Map.Entry<String, Object> entry : dat.entrySet()) {
                    String fieldKey = s + "." + entry.getKey();
                    Set values = groupData.get(fieldKey);
                    if (values == null) {
                        values = new HashSet();
                        groupData.put(fieldKey, values);
                    }
                    values.add(entry.getValue());
                }
            }
        }
        return groupData;
    }


    /**
     * 获取动态参数数据
     *
     * @param extractJson
     * @return key:动态接口虚拟key value jsonarray
     * @throws Exception
     */
    public Map<String, JSONArray> ExtractDynamicParamData(JSONObject extractJson) throws Exception {
        logger.info("Data extract dynamic param start!");
        Map<String, JSONArray> dataMap = Maps.newHashMap();
        JSONObject dataModelJo = extractJson.getJSONObject(ALIAS_DYNAMIC_PARAM);
        if (dataModelJo == null || dataModelJo.getJSONObject("params").isEmpty()) {
            return dataMap;
        }
        JSONObject paramJson = dataModelJo.getJSONObject("params");
        String type = String.valueOf(dataModelJo.getOrDefault("type", "rtwh"));
        Pair<JSONArray, List<String>> pair;
        for (String paramKey : paramJson.keySet()) {
            JSONObject keyJson = paramJson.getJSONObject(paramKey);
            switch (type.toLowerCase()) {
                case "rtwh":
                    extractProcessor = beanFactory.getBean(RTWHExtractProcessor.class, paramKey, keyJson);
                    pair = extractProcessor.process();
                    break;
                case "rowpiece":
                    extractProcessor = beanFactory.getBean(RowPieceExtractProcessor.class, paramKey, keyJson);
                    pair = extractProcessor.process();
                    break;
                default:
                    throw new Exception(String.format("Unsupported Data extract type:{}", type));
            }
            if (pair != null) {
                dataMap.put(paramKey, pair.getValue0());
                logger.info("Data extract tableName:{},table data size:{}", paramKey, pair.getValue0().size());
            } else {
                throw new Exception(String.format("Dynamic model {} get data is null", keyJson));
            }
        }
        logger.info("Data extract dynamic param over!");
        return dataMap;
    }

}
