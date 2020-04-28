package com.maoyan.bigdata.datalink.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.maoyan.bigdata.datalink.crane.DynamicCraneJobBean;
import com.maoyan.bigdata.datalink.schedule.DataLinkScheduleJob;
import com.sankuai.meituan.common.time.TimeUtil;
import com.sankuai.meituan.config.util.AuthUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Component
public class CraneUtils {
    private static final Logger logger = LoggerFactory.getLogger(CraneUtils.class);

    private static final String APPKEY="com.sankuai.movie.bigdata.datalink";
    private static final String KEY="545d8d165260873237e13ccdb5fc98bb";

    @Value(value = "${crane_url}")
    private String CRANE_URL;

    private static final String JOB_URI = "/v2/tasks";

    /**
     * 根据方法名和类名创建任务
     *
     * @param bean
     * @return
     * @throws IOException
     */
    public  Triplet<Boolean, String, String> createDynamicJob(DynamicCraneJobBean bean) throws IOException {
        logger.info("crane_url:{}",CRANE_URL);
        HttpPost method = new HttpPost(CRANE_URL + JOB_URI);
        String date = TimeUtil.getAuthDate(new Date());
        String authorization = AuthUtil.getAuthorization(JOB_URI,
                "POST", date, APPKEY, KEY);
        method.setHeader("Date", date);
        method.setHeader("Authorization", authorization);// 接受参数 json 列表
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("taskName", bean.getName());//
        jsonParam.put("crontab", bean.getCronExpr());//
        jsonParam.put("creator", bean.getCreator());//
        jsonParam.put("taskClass", bean.getTaskClass());//
        jsonParam.put("taskMethod", bean.getTaskMethod());//
        jsonParam.put("taskItem", bean.getTaskItem());// ,
        jsonParam.put("description", bean.getDescription());
        StringEntity entity = new StringEntity(jsonParam.toString(), "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        method.setEntity(entity);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse result = httpClient.execute(method);
        String resData = EntityUtils.toString(result.getEntity());
        //{"status":0,"message":"操作成功","data":"task_201909301159_0664"}
        JSONObject jo = JSON.parseObject(resData);
        if (jo.containsKey("status") && "0".equalsIgnoreCase(jo.getString("status"))) {
            bean.setJobId(jo.getString("data"));
            return Triplet.with(true, jo.getString("message"), jo.getString("data"));
        } else {
            return Triplet.with(false, jo.getString("message"), jo.getString("data"));
        }
    }

    /**
     * 删除任务 必需有 jobID
     *
     * @param bean
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public  Triplet<Boolean, String, String> deleteJob(DynamicCraneJobBean bean) throws IOException {
        String targetUri = JOB_URI + "/" + bean.getJobId();
        String targetUrl = CRANE_URL + targetUri;
        HttpDelete httpDelete = new HttpDelete(targetUrl);
        String date = TimeUtil.getAuthDate(new Date());
        String authorization = AuthUtil.getAuthorization(targetUri,
                "DELETE", date, APPKEY, KEY);
        httpDelete.setHeader("Date", date);
        httpDelete.setHeader("Authorization", authorization);
        HttpEntity entity;
        String result;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpDelete);
        entity = response.getEntity();
        InputStream ins = entity.getContent();
        result = IOUtils.toString(ins, "UTF-8");
        //{"status":0,"message":"操作成功","data":null}
        JSONObject jo = JSON.parseObject(result);
        if (jo.containsKey("status") && "0".equalsIgnoreCase(jo.getString("status"))) {
            return Triplet.with(true, jo.getString("message"), jo.getString("data"));
        } else {
            return Triplet.with(false, jo.getString("message"), jo.getString("data"));
        }
    }

    /**
     * 更新任务
     *
     * @param bean
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public  Triplet<Boolean, String, String> updateJob(DynamicCraneJobBean bean) throws IOException {
        String targetUri = JOB_URI + "/" + bean.getJobId();
        String targetUrl = CRANE_URL + targetUri;
        HttpPut httpPut = new HttpPut(targetUrl);
        String date = TimeUtil.getAuthDate(new Date());
        String authorization = AuthUtil.getAuthorization(targetUri,
                "PUT", date, APPKEY, KEY);
        httpPut.setHeader("Date", date);
        httpPut.setHeader("Authorization", authorization);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("taskName", bean.getName());//
        jsonParam.put("crontab", bean.getCronExpr());//
        jsonParam.put("creator", bean.getCreator());//
        jsonParam.put("taskClass", bean.getTaskClass());//
        jsonParam.put("taskMethod", bean.getTaskMethod());//
        jsonParam.put("taskItem", bean.getTaskItem());// ,
        jsonParam.put("description", bean.getDescription());
        StringEntity entity = new StringEntity(jsonParam.toString(), "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPut.setEntity(entity);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse result = httpClient.execute(httpPut);
        JSONObject jo = JSON.parseObject(EntityUtils.toString(result.getEntity()));
        if (jo.containsKey("status") && "0".equalsIgnoreCase(jo.getString("status"))) {
            return Triplet.with(true, jo.getString("message"), jo.getString("data"));
        } else {
            return Triplet.with(false, jo.getString("message"), jo.getString("data"));
        }
    }

    /**
     * 启动指定任务
     *
     * @param bean
     * @return
     * @throws IOException
     */
    public  Triplet<Boolean, String, String> startJob(DynamicCraneJobBean bean) throws IOException {
        return startOrStopJob(bean, "start");
    }

    /**
     * 暂停指定任务
     *
     * @param bean
     * @return
     * @throws IOException
     */
    public  Triplet<Boolean, String, String> stopJob(DynamicCraneJobBean bean) throws IOException {
        return startOrStopJob(bean, "stop");
    }


    private  Triplet<Boolean, String, String> startOrStopJob(DynamicCraneJobBean bean, String startOrStop) throws IOException {
        String targetUri = JOB_URI + "/" + bean.getJobId() + "/" + startOrStop;
        String targetUrl = CRANE_URL + targetUri;
        HttpPost httpPost = new HttpPost(targetUrl);
        String date = TimeUtil.getAuthDate(new Date());
        String authorization = AuthUtil.getAuthorization(targetUri,
                "POST", date, APPKEY, KEY);
        httpPost.setHeader("Date", date);
        httpPost.setHeader("Authorization", authorization);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        InputStream ins = entity.getContent();
        String result = IOUtils.toString(ins, "UTF-8");
        JSONObject jo = JSON.parseObject(result);
        if (jo.containsKey("status") && "0".equalsIgnoreCase(jo.getString("status"))) {
            return Triplet.with(true, jo.getString("message"), jo.getString("data"));
        } else {
            return Triplet.with(false, jo.getString("message"), jo.getString("data"));
        }
    }


    /**
     * 带参数单词执行任务
     * @param bean
     * @param parameterStr
     * @return
     * @throws IOException
     */
    public  Triplet<Boolean, String, String> executeJobOnce(DynamicCraneJobBean bean, String parameterStr) throws IOException {
        String targetUri = JOB_URI + "/" + bean.getJobId() + "/once";
        String targetUrl = CRANE_URL + targetUri;
        HttpPost httpPost = new HttpPost(targetUri);
        String date = TimeUtil.getAuthDate(new Date());
        String authorization = AuthUtil.getAuthorization(targetUrl,
                "POST", date, APPKEY, KEY);
        httpPost.setHeader("Date", date);
        httpPost.setHeader("Authorization", authorization);
        List<BasicNameValuePair> basicNameValuePairs = Lists.newArrayList();
        basicNameValuePairs.add(new BasicNameValuePair("taskItem",parameterStr));
        httpPost.setEntity(new UrlEncodedFormEntity(basicNameValuePairs, "UTF-8"));
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        InputStream ins = entity.getContent();
        String result = IOUtils.toString(ins, "UTF-8");
        JSONObject jo = JSON.parseObject(result);
        if (jo.containsKey("status") && "0".equalsIgnoreCase(jo.getString("status"))) {
            return Triplet.with(true, jo.getString("message"), jo.getString("data"));
        } else {
            return Triplet.with(false, jo.getString("message"), jo.getString("data"));
        }
    }

    public static void main(String[] args) throws Exception {
        CraneUtils craneUtils = new CraneUtils();
        DynamicCraneJobBean bean = new DynamicCraneJobBean();
        bean.setName("zb_test");
        bean.setCreator("zhaobin04");
        bean.setCronExpr(" 0 0 1 * * ?");
        bean.setTaskClass("com.maoyan.bigdata.datalink.schedule.DataLinkScheduleJob");
        bean.setTaskMethod("dataLinkSchedule");
        JSONObject taskItemJO = new JSONObject();
        taskItemJO.put("key", "zb_test");

        Triplet<Boolean, String, String> result = craneUtils.createDynamicJob(bean);
        System.out.println(result.getValue0() + "\t" + result.getValue1() + "\t" + result.getValue2());
        System.out.println(JSON.toJSONString(bean));
        Thread.sleep(1000 * 3);
        result = craneUtils.stopJob(bean);
        System.out.println(result.getValue0() + "\t" + result.getValue1() + "\t" + result.getValue2());
        Thread.sleep(1000 * 3);
        result = craneUtils.startJob(bean);
        System.out.println(result.getValue0() + "\t" + result.getValue1() + "\t" + result.getValue2());
        Thread.sleep(1000 * 3);
        result = craneUtils.deleteJob(bean);
        System.out.println(result.getValue0() + "\t" + result.getValue1() + "\t" + result.getValue2());
    }
}
