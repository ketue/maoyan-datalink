package com.maoyan.bigdata.datalink.utils;

import com.maoyan.realtime.warehouse.thrift.BaseResponse;

import java.util.Map;

/**
 */
public class ResponseUtil {
    //成功提示
    private static final boolean OK = true;
    //失败提示
    private static final boolean NO = false;


    public static final String QUERY_TIME = "queryTime";
    public static final String DATA = "data";
    public static final String IS_CACHE = "isCache";

    /**
     * 成功调用接口
     *
     * @param message -- 成功后提醒信息
     * @return -- 返回结果
     */
    public static BaseResponse success(String message) {
        BaseResponse resp = new BaseResponse();
        resp.setSuccess(ResponseUtil.OK);
        resp.setMsg(message);
        return resp;
    }

    /**
     * 成功调用接口
     *
     * @param message -- 成功后提醒信息
     * @param meta    -- 其他信息
     * @return -- 返回结果
     */
    public static BaseResponse success(String message, Map meta) {
        BaseResponse resp = new BaseResponse();
        resp.setMeta(meta);
        resp.setSuccess(ResponseUtil.OK);
        resp.setMsg(message);
        return resp;
    }

    /**
     * 成功调用接口
     *
     * @param message -- 成功后提醒信息
     * @return -- 返回结果
     */
    public static BaseResponse success(String message, String data) {
        BaseResponse resp = new BaseResponse();
        resp.setSuccess(ResponseUtil.OK);
        resp.setMsg(message);
        resp.setData(data);
        return resp;
    }

    /**
     * 成功调用接口
     *
     * @param message -- 成功后提醒信息
     * @param meta    --其他信息
     * @return -- 返回结果
     */
    public static BaseResponse success(String message, String data, Map meta) {
        BaseResponse resp = new BaseResponse();
        resp.setSuccess(ResponseUtil.OK);
        resp.setMsg(message);
        resp.setData(data);
        resp.setMeta(meta);
        return resp;
    }

    /**
     * 成功调用接口
     *
     * @param meta    --其他信息
     * @return -- 返回结果
     */
    public static BaseResponse successMsg(String data, Map meta) {
        BaseResponse resp = new BaseResponse();
        resp.setSuccess(ResponseUtil.OK);
        resp.setMsg("success");
        resp.setData(data);
        resp.setMeta(meta);
        return resp;
    }

    /**
     * 失败调用的接口
     *
     * @param message -- 返回的失败提醒信息
     * @return -- 返回结果
     */
    public static BaseResponse failed(String message) {
        BaseResponse resp = new BaseResponse();
        resp.setSuccess(ResponseUtil.NO);
        resp.setMsg(message);
        return resp;
    }

    /**
     * 失败调用的接口
     *
     * @param message -- 返回的失败提醒信息
     * @param meta    --其他信息
     * @return -- 返回结果
     */
    public static BaseResponse failed(String message, Map meta) {
        BaseResponse resp = new BaseResponse();
        resp.setSuccess(ResponseUtil.NO);
        resp.setMsg(message);
        resp.setMeta(meta);
        return resp;
    }

}
