package com.maoyan.bigdata.datalink.core.extract;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

@Component
public abstract class ExtractProcessor implements Callable<Pair<JSONArray, List<String>>>{

    String alias;
    JSONObject extractJsonModel;
    List dynamicParamData;

    public ExtractProcessor() {
    }

    public ExtractProcessor(String key, JSONObject extractJsonModel) {
        this.alias = key;
        this.extractJsonModel = extractJsonModel;
    }

    public ExtractProcessor(String key, JSONObject extractJsonModel, List dynamicParamData) {
        this.alias = key;
        this.extractJsonModel = extractJsonModel;
        this.dynamicParamData = dynamicParamData;
    }

    /**
     * <结果集,字段 set>
     * @return
     */
    abstract Pair<JSONArray, List<String>> process() throws Exception;

    @Override
    public Pair<JSONArray, List<String>> call() throws Exception {
        return process();
    }
}
