package com.maoyan.bigdata.datalink.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dianping.lion.shade.org.apache.curator.shaded.com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class Test {
   static  String str = "{\"111\":111,\"222\":222.0}";




    public static void main(String[] args) {
        Map<String, Object> oldMap = JSON.parseObject(str, Map.class);
        //Map<String,Double> result = Maps.newHashMap();
        for (Map.Entry<String, Object> stringBigDecimalEntry : oldMap.entrySet()) {
            System.out.println(stringBigDecimalEntry.getValue() instanceof  Number);
//            System.out.println(stringBigDecimalEntry.getValue().getClass().toString());
//            result.put(stringBigDecimalEntry.getKey(),stringBigDecimalEntry.getValue().doubleValue());
//            System.out.println(stringBigDecimalEntry.getKey()+"\t"+stringBigDecimalEntry.getValue().doubleValue());
        }
    }
}
