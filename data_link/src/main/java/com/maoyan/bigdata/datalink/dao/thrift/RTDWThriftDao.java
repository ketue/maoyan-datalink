package com.maoyan.bigdata.datalink.dao.thrift;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.maoyan.realtime.warehouse.thrift.BaseRequest;
import com.maoyan.realtime.warehouse.thrift.BaseResponse;
import com.maoyan.realtime.warehouse.thrift.RealTimeDWService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author shenx
 */
@Component
public class RTDWThriftDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(RTDWThriftDao.class);

    @Resource
    private RealTimeDWService.Iface realTimeDWService;

    private static Cache<String, BaseResponse> getDimensionCahce=CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(2, TimeUnit.HOURS)
            .expireAfterAccess(2, TimeUnit.HOURS)
            .build();

    public List<String> getCommonQueryData(HashMap<String, String> paramsMap, String key) {
        BaseRequest request = new BaseRequest();
        request.setKey(key);
        request.setParams(paramsMap);
        request.setConfig(new HashMap<>(0));
        BaseResponse resultDataResopnse = new BaseResponse();
        try {
            switch (key){
                case "getDimension":resultDataResopnse=getDimeCache(request);break;
                default: resultDataResopnse = realTimeDWService.commonQuery(request);break;
            }
        } catch (TException e) {
            LOGGER.info(String.format("resultDataResponse:%s,getKey:%s", JSON.toJSONString(resultDataResopnse),key));
            e.printStackTrace();
        }

        List<String> resultDataStr = new ArrayList<>();
        if (resultDataResopnse.getData() != null) {
            resultDataStr = JSON.parseArray(resultDataResopnse.getData(), String.class);
        }
        return resultDataStr;
    }


    public JSONArray getCommonQueryData(String key ,HashMap<String, String> paramsMap) {
        List<String> list = getCommonQueryData(paramsMap,key);
        JSONArray resultJa = new JSONArray();
        for (String s : list) {
            resultJa.add(JSON.parseObject(s));
        }
        return resultJa;
    }

    /**
     * getDimension 维度缓存处理
     * @param request 请求参数
     * @return BaseResponse
     */
    private BaseResponse getDimeCache(BaseRequest request) throws TException {
        BaseResponse resultDataResponse=new BaseResponse();
        Map<String,String> paramsMap=request.getParams();
        if (paramsMap!=null&&paramsMap.size()>0) {
            for (Map.Entry<String, String> paramsEntry : paramsMap.entrySet()) {
                String parmKey = paramsEntry.getKey() + ":" + paramsEntry.getValue();
                try {
                    resultDataResponse=getDimensionCahce.get(parmKey, () -> {
                       BaseResponse resultDataResopnse1 = realTimeDWService.getDimension(request);
                       LOGGER.info(String.format("key:%s not Hit cache",parmKey));
                       return resultDataResopnse1;
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultDataResponse;
    }


    /**
     * 获取影片名称
     *
     * @param movieId
     * @return
     */
    public String getMovieName(String movieId) {
        String result = "";
        Map paramsMap = new HashMap<>();
        paramsMap.put("movieId", movieId);
        BaseRequest request = new BaseRequest();
        request.setKey("getDimension");
        request.setParams(paramsMap);
        request.setConfig(new HashMap<>(0));
        BaseResponse response = new BaseResponse();
        try {
            response = realTimeDWService.getDimension(request);
        } catch (TException e) {
            LOGGER.info(String.format("resultDataResponse:%s", JSON.toJSONString(response)));
            e.printStackTrace();
        }
        if (response != null && response.getData() != null) {
            List<Map> resultList = JSON.parseArray(response.getData(), Map.class);
            if (!resultList.isEmpty()) {
                result = String.valueOf(resultList.get(0).get("name"));
            }
        }
        return result;
    }
}
