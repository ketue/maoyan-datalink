package com.maoyan.bigdata.datalink.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maoyan.bigdata.datalink.core.extract.DataExtract;
import com.maoyan.bigdata.datalink.core.load.DataLoader;
import com.maoyan.bigdata.datalink.core.transform.DataTransform;
import com.maoyan.bigdata.datalink.handler.DateExprHandler;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataLinkService {

    private static final Logger logger = LoggerFactory.getLogger(DataLinkService.class);

    @Autowired
    DataExtract dataExtract;

    @Autowired
    DataTransform dataTransform;

    @Autowired
    DataLoader dataLoader;


    public int processETL(String name, DataLinkModelBean dataLinkModelBean) throws Exception {
        //参数替换
        String modelStr = DateExprHandler.replaceDateExpr(dataLinkModelBean.getModel());
        logger.info("Real model:{}",modelStr);
        JSONObject jsonModel = JSON.parseObject(modelStr);
        JSONObject dataExtractModel = jsonModel.getJSONObject("data_extract");
        JSONObject dataTransformModel = jsonModel.getJSONObject("data_transform");
        JSONObject dataLoaderModel = jsonModel.getJSONObject("data_loader");

        logger.info("DataLinkService Data extract start!");
        Map<String, Pair<JSONArray, List<String>>> sourceDataMap = dataExtract.dataExtract(name,dataExtractModel);
        logger.info("DataLinkService Data extract end !table size:{}", sourceDataMap.size());

        logger.info("DataLinkService Data transform start!");
        JSONArray transformResult = dataTransform.dataTransform(name, dataTransformModel, sourceDataMap);
        logger.info("DataLinkService Data transform end !result data size:{}", transformResult.size());


        logger.info("DataLinkService Data loader start!");
        int loadResult = dataLoader.dataLoad(name, dataLoaderModel, transformResult);
        logger.info("DataLinkService Data loader end !result :{}", transformResult.size());

        return loadResult;
    }
}
