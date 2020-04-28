package com.maoyan.bigdata.datalink.schedule;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cip.crane.client.spring.annotation.Crane;
import com.cip.crane.client.spring.annotation.CraneConfiguration;
import com.maoyan.bigdata.datalink.dao.movie_data.DataLinkModelDao;
import com.maoyan.bigdata.datalink.core.DataLinkModelBean;
import com.maoyan.bigdata.datalink.core.DataLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@CraneConfiguration
public class DataLinkScheduleJob {

    private static final Logger logger = LoggerFactory.getLogger(DataLinkScheduleJob.class);

    @Autowired
    private org.springframework.beans.factory.BeanFactory beanFactory;

    @Autowired
    DataLinkModelDao dataLinkModelDao;

    @Autowired
    DataLinkService dataLinkService;

    @Crane("movie-hummer-data-link-job")
    public void dataLinkSchedule(String jsonPara) throws Exception {
        long startT = System.currentTimeMillis();
        JSONObject jo = JSON.parseObject(jsonPara);
        String key = jo.getString("key");
        DataLinkModelBean modelBean = dataLinkModelDao.getDataLinkModelByKey(key);
        logger.info("Get model bean:" + JSON.toJSONString(modelBean));
        dataLinkService.processETL(key,modelBean);
        logger.info("{} Job over cost {} s! ", key, (System.currentTimeMillis() - startT) / 1000);
    }

    @Crane("data-link-job-test")
    public void test(String jsonPara){
        long startT = System.currentTimeMillis();
        JSONObject jo = JSON.parseObject(jsonPara);
        String key = jo.getString("key");
        DataLinkModelBean modelBean = dataLinkModelDao.getDataLinkModelByKey(key);
        logger.info("Get model bean:" + JSON.toJSONString(modelBean));
//        dataLinkService.processETL(key,modelBean);
        logger.info("{} Job over cost {} s! ", key, (System.currentTimeMillis() - startT) / 1000);
    }
}
