package com.maoyan.bigdata.datalink.core.extract;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maoyan.bigdata.datalink.dao.thrift.RTDWThriftDao;
import com.maoyan.bigdata.datalink.dao.thrift.RowPieceThriftDao;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@Scope("prototype")
public class RowPieceExtractProcessor extends ExtractProcessor {


    private static final Logger logger = LoggerFactory.getLogger(RowPieceExtractProcessor.class);

    public RowPieceExtractProcessor() {
    }

    public RowPieceExtractProcessor(String alias, JSONObject extractJsonModel) {
        super(alias,extractJsonModel);
        logger.info("RTWHExtractProcessor start! JsonModel:{}",extractJsonModel);
    }

    public RowPieceExtractProcessor(String alias, JSONObject extractJsonModel,List dynamicParamData) {
        super(alias,extractJsonModel,dynamicParamData);
        logger.info("RTWHExtractProcessor start! JsonModel:{}",extractJsonModel);
    }

    @Autowired
    RowPieceThriftDao rowPieceThriftDao;

    public Pair<JSONArray, List<String>> process() throws Exception {

        String key = this.extractJsonModel.getString("key");
        String para = this.extractJsonModel.getString("para");
        String fields = this.extractJsonModel.getString("fields");
        logger.info("{},{},{}",key,para,fields);
        JSONObject paraJo = new JSONObject();
        if (para != null && para.length() > 0) {
            String[] kvs = para.split("&");
            for (String kv : kvs) {
                String[] kvArr = kv.split("=");
                if (kvArr.length == 2) {
                    paraJo.put(kvArr[0], kvArr[1]);
                }
            }
        }
        JSONArray result = rowPieceThriftDao.getResult(key, paraJo.toJSONString());
        result = result == null ? new JSONArray() : result;
        List<String> fieldsSet;
        if (fields != null && fields.length() > 0) {
            fieldsSet = Lists.newArrayList();
            String[] fieldArr = fields.split(",");
            for (String s : fieldArr) {
                fieldsSet.add(s.trim());
            }
        } else if (result.size() > 0) {
            fieldsSet = Lists.newArrayList(result.getJSONObject(0).keySet());
        } else {
            throw new Exception("lost 'fields' key");
        }

        return Pair.with(result, fieldsSet);
    }
}
