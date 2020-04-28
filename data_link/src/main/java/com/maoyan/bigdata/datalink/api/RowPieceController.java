package com.maoyan.bigdata.datalink.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maoyan.bigdata.datalink.utils.ThriftProxyUtils;
import com.maoyan.realtime.warehouse.thrift.RealTimeDWService;
import com.meituan.service.mobile.mtthrift.proxy.ThriftClientProxy;
import com.meituan.service.movie.data.service.thrift.CommonDataReq;
import com.meituan.service.movie.data.service.thrift.CommonDataResp;
import com.meituan.service.movie.data.service.thrift.DataThriftService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "RowPiece测试接口")
@RestController
@RequestMapping("/rowpiece")
public class RowPieceController {

    static final Logger logger = LoggerFactory.getLogger(RowPieceController.class);


    @PostMapping("/getResult")
    public Object getResult(@RequestParam Map<String, String> params) {
        ThriftClientProxy proxy = null;
        String result = "";
        try {
            logger.info("getResult,{}", JSON.toJSONString(params));
            JSONObject jsonObject = new JSONObject();
            String ip = params.getOrDefault("ip", null);
            if (params.containsKey("ip")) {
                jsonObject.put("ip", ip);
                params.remove("ip");
            }
            proxy = ThriftProxyUtils.getProxy("com.sankuai.movie.bigdata.rowpiece", DataThriftService.class, ip, 0);
            CommonDataReq commonDataReq = new CommonDataReq();
            commonDataReq.setFuncName(params.get("funcName"));
            JSONObject jo = new JSONObject();
            params.remove("funcName");
            jo.putAll(params);
            commonDataReq.setReqJson(jo.toJSONString());
            logger.info("getResult,{}", JSON.toJSONString(commonDataReq));
            CommonDataResp commonDataResp = ((DataThriftService.Iface) proxy.getObject()).getResult(commonDataReq);
            jsonObject.put("result", commonDataResp);
            result = jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            result = "error," + e.getMessage();
        } finally {
            if (proxy != null) {
                proxy.destroy();
            }
            return result;
        }

    }

}
