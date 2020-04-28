package com.maoyan.bigdata.datalink.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maoyan.bigdata.datalink.utils.ThriftProxyUtils;
import com.maoyan.realtime.warehouse.thrift.BaseRequest;
import com.maoyan.realtime.warehouse.thrift.BaseResponse;
import com.maoyan.realtime.warehouse.thrift.RealTimeDWService;
import com.meituan.service.mobile.mtthrift.proxy.ThriftClientProxy;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "RTDW测试接口")
@RestController
@RequestMapping("/rtdw")
public class RTDWController {

    static final Logger logger = LoggerFactory.getLogger(RTDWController.class);

    @PostMapping("/commonQuery")
    public Object commonQuery(@RequestParam Map<String, String> params) {
        ThriftClientProxy proxy = null;
        String result = "";
        try {
            logger.info("commonQuery,{}", JSON.toJSONString(params));
            JSONObject jsonObject = new JSONObject();
            String ip = params.getOrDefault("ip", null);
            if (ip != null) {
                ip = ip.split(":")[0];
                ip += ":9897";
                jsonObject.put("ip", ip);
                params.remove("ip");
            }
            proxy = ThriftProxyUtils.getProxy("com.sankuai.movie.bigdata.rtwh", RealTimeDWService.class, ip,9897);
            BaseRequest baseRequest = new BaseRequest();
            baseRequest.setKey(params.get("key"));
            params.remove("key");
            baseRequest.setParams(params);
            logger.info("commonQuery,{}", JSON.toJSONString(baseRequest));
            BaseResponse baseResponse = ((RealTimeDWService.Iface) proxy.getObject()).commonQuery(baseRequest);
            jsonObject.put("result", baseResponse);
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



    @PostMapping("/dim")
    public Object getDim(@RequestParam Map<String, String> params) {
        ThriftClientProxy proxy = null;
        String result = "";
        try {
            logger.info("dim,{}", JSON.toJSONString(params));
            JSONObject jsonObject = new JSONObject();
            String ip = params.getOrDefault("ip", null);
            if (ip != null) {
                ip = ip.split(":")[0];
                ip += ":9897";
                jsonObject.put("ip", ip);
                params.remove("ip");
            }
            proxy = ThriftProxyUtils.getProxy("com.sankuai.movie.bigdata.rtwh", RealTimeDWService.class, ip,9897);
            BaseRequest baseRequest = new BaseRequest();
            baseRequest.setKey(params.get("key"));
            params.remove("key");
            baseRequest.setParams(params);
            logger.info("dim,{}", JSON.toJSONString(baseRequest));
            BaseResponse baseResponse = ((RealTimeDWService.Iface) proxy.getObject()).getDimension(baseRequest);
            jsonObject.put("result", baseResponse);
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
