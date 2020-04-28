package com.maoyan.bigdata.datalink.core.transform;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

public abstract class TransformProcessor {


    String modelKey;
    JSONObject transformJsonModel;
    Map<String, Pair<JSONArray, List<String>>> data;

    public TransformProcessor() {
    }

    public TransformProcessor(String key, JSONObject transformJsonModel,Map<String, Pair<JSONArray, List<String>>> data) {
        this.modelKey = key;
        this.transformJsonModel = transformJsonModel;
        this.data= data;
    }


    /**
     * <结果集,字段 set>
     * @return
     */
    abstract JSONArray process() throws Exception;
}
