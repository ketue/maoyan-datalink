package com.maoyan.bigdata.datalink.core.load;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public abstract class LoadProcessor {

    public static final int DEFAULT_BLOCK_SIZE = 500;

    String modelKey ;
    JSONObject writeModel;
    JSONArray sourceData;

    public LoadProcessor() {
    }

    public LoadProcessor(String key, JSONObject writerJSONModel, JSONArray sourceData) {
        this.modelKey=key;
        this.writeModel=writerJSONModel;
        this.sourceData = sourceData;
    }

    abstract int process();

}
