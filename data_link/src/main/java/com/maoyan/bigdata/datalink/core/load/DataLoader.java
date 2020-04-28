package com.maoyan.bigdata.datalink.core.load;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Scope("prototype")
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private org.springframework.beans.factory.BeanFactory beanFactory;

    LoadProcessor writeProcessor;


    public int dataLoad(String key, JSONObject loaderJSONModel, JSONArray sourceData) throws Exception {
        logger.info("DataLoader start! LoaderJsonModel:{}", loaderJSONModel);
        String targetType = loaderJSONModel.getString("target_type");
        int result = 0;
        switch (targetType) {
            case "mysql":
                writeProcessor = beanFactory.getBean(MySQLLoadProcessor.class, key, loaderJSONModel, sourceData);
                result = writeProcessor.process();
                break;
            case "mafka":
                writeProcessor = beanFactory.getBean(MafkaLoadProcessor.class, key, loaderJSONModel, sourceData);
                result = writeProcessor.process();
                break;
            default:
                throw new Exception(String.format("Unsupported %s target type!", targetType));
        }
        return result;
    }
}
