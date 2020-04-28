package com.maoyan.bigdata.datalink.core.transform;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataTransform {

    private static final Logger logger = LoggerFactory.getLogger(DataTransform.class);


    @Autowired
    private org.springframework.beans.factory.BeanFactory beanFactory;


    private TransformProcessor transformProcessor;


    public JSONArray dataTransform(String modelKey, JSONObject dataMergeModel, Map<String, Pair<JSONArray, List<String>>> extractDataMap) throws Exception {
        logger.info("Data merge start!");
        String mergeType = String.valueOf(dataMergeModel.getOrDefault("type", "sqlite"));
        JSONArray result = new JSONArray();
        switch (mergeType.toLowerCase()) {
            case "sqlite":
                transformProcessor = beanFactory.getBean(SQLiteTransformProcessor.class, modelKey, dataMergeModel, extractDataMap);
                result = transformProcessor.process();
                break;
            case "ignore":
                if(extractDataMap.size()==1){//有且只能有一个数据源
                for (String s : extractDataMap.keySet()) {
                    result = extractDataMap.get(s).getValue0();
                }
                }else{
                    throw new Exception(String.format("When Data Transform Type equals 'ignore', Only support 1 Data Extract Source,now:{}",extractDataMap.size()));
                }
                break;
            default:
                throw new Exception(String.format("Unsupported %s type merge", mergeType));
        }
        logger.info("Data transform over!");
        return result;
    }


}
