package com.maoyan.bigdata.datalink.core.service;

import com.alibaba.fastjson.JSONObject;
import com.maoyan.bigdata.datalink.crane.DynamicCraneJobBean;
import com.maoyan.bigdata.datalink.dao.movie_data.DataLinkModelDao;
import com.maoyan.bigdata.datalink.core.DataLinkModelBean;
import com.maoyan.bigdata.datalink.utils.CraneUtils;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DataLinkCraneService {

    private static final String PRE_JOB_NAME = "DataLinkJob-";
    private static final String TASK_CLASS = "com.maoyan.bigdata.datalink.schedule.data_link.DataLinkScheduleJob";
    private static final String TASK_METHOD = "test";

    @Autowired
    DataLinkModelDao dataLinkModelDao;

    @Autowired
    CraneUtils craneUtils;

    public Triplet<Boolean, String, String> createCraneJob(String name) throws IOException {
        DataLinkModelBean bean = dataLinkModelDao.getDataLinkModelByKey(name);
        DynamicCraneJobBean craneJobBean =transModelBean2CraneJobBean(bean);
        Triplet<Boolean, String, String> resultTriplet = craneUtils.createDynamicJob(craneJobBean);
        if (resultTriplet.getValue0()) {
            bean.setCraneJobId(craneJobBean.getJobId());
            dataLinkModelDao.updateDataLinkModel(bean);
        }

        return resultTriplet;
    }

    public Triplet<Boolean, String, String> stopJob(String name) throws IOException {
        DataLinkModelBean bean = dataLinkModelDao.getDataLinkModelByKey(name);
        DynamicCraneJobBean craneJobBean =transModelBean2CraneJobBean(bean);
        Triplet<Boolean, String, String> resultTriplet = craneUtils.stopJob(craneJobBean);
        return resultTriplet;
    }

    public Triplet<Boolean, String, String> startJob(String name) throws IOException {
        DataLinkModelBean bean = dataLinkModelDao.getDataLinkModelByKey(name);
        DynamicCraneJobBean craneJobBean =transModelBean2CraneJobBean(bean);
        Triplet<Boolean, String, String> resultTriplet = craneUtils.startJob(craneJobBean);
        return resultTriplet;
    }

    private DynamicCraneJobBean transModelBean2CraneJobBean(DataLinkModelBean bean){
        DynamicCraneJobBean craneJobBean = new DynamicCraneJobBean();
        craneJobBean.setName(PRE_JOB_NAME + bean.getKey());
        craneJobBean.setTaskClass(TASK_CLASS);
        craneJobBean.setTaskMethod(TASK_METHOD);
        craneJobBean.setCronExpr(bean.getCraneCron());
        JSONObject paraJo = new JSONObject();
        paraJo.put("key", bean.getKey());
        craneJobBean.setTaskItem(paraJo.toJSONString());
        craneJobBean.setJobId(bean.getCraneJobId());
        return craneJobBean;
    }

    public Triplet<Boolean, String, String> deleteJob(String name) throws IOException {
        DataLinkModelBean bean = dataLinkModelDao.getDataLinkModelByKey(name);
        DynamicCraneJobBean craneJobBean =transModelBean2CraneJobBean(bean);

        Triplet<Boolean, String, String> resultTriplet = craneUtils.deleteJob(craneJobBean);
        if (resultTriplet.getValue0()) {
            bean.setCraneJobId(null);
            dataLinkModelDao.updateDataLinkModel(bean);
        }
        return resultTriplet;
    }
}
