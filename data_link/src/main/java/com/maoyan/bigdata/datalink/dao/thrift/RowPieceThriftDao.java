package com.maoyan.bigdata.datalink.dao.thrift;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.meituan.service.movie.data.service.thrift.CommonDataReq;
import com.meituan.service.movie.data.service.thrift.CommonDataResp;
import com.meituan.service.movie.data.service.thrift.DataThriftService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author shenx
 */
@Component
public class RowPieceThriftDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(RowPieceThriftDao.class);

    @Resource
    DataThriftService.Iface rowPieceDataThriftService;

    public JSONArray getResult(String functionName, String jsonStr) {
        LOGGER.info("getResult {},{}", functionName, jsonStr);
        String str = "[]";
        try {
            CommonDataReq req = new CommonDataReq();
            req.setFuncName(functionName);
            req.setReqJson(jsonStr);
            CommonDataResp resp = rowPieceDataThriftService.getResult(req);
            str = resp.getResultJson();
        } catch (Exception e) {
            LOGGER.error("Exception {}", e);
        }
        return JSON.parseArray(str);
    }

}
