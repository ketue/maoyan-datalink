package com.maoyan.bigdata.datalink.api;

import com.alibaba.fastjson.JSONObject;
import com.maoyan.bigdata.datalink.core.service.DataLinkCraneService;
import com.maoyan.bigdata.datalink.schedule.DataLinkScheduleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/datalink")
public class DataLInkTestController {

    @Autowired
    DataLinkScheduleJob dataLinkScheduleJob;

    @Autowired
    DataLinkCraneService dataLinkCraneService;
    @RequestMapping("/zb_test")
    public void Test(){
        JSONObject jo = new JSONObject();
        jo.put("key","zb_test");
        try {
            dataLinkScheduleJob.dataLinkSchedule(jo.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @RequestMapping("/crane/create/{name}")
    public Object createJob(@PathVariable("name") String name) throws Exception{
       return dataLinkCraneService.createCraneJob(name);
    }

    @RequestMapping("/crane/start/{name}")
    public Object startJob(@PathVariable("name") String name) throws Exception{
        return dataLinkCraneService.startJob(name);
    }

    @RequestMapping("/crane/stop/{name}")
    public Object stop(@PathVariable("name") String name) throws Exception{
        return dataLinkCraneService.stopJob(name);
    }

    @RequestMapping("/crane/delete/{name}")
    public Object delete(@PathVariable("name") String name) throws Exception{
        return dataLinkCraneService.deleteJob(name);
    }

    @RequestMapping("/zb_test/{key}")
    public void test2(@PathVariable("key") String key){
        JSONObject jo = new JSONObject();
        jo.put("key",key);
        try {
            dataLinkScheduleJob.dataLinkSchedule(jo.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
