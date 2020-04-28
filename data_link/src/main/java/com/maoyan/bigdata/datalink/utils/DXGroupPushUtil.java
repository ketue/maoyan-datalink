package com.maoyan.bigdata.datalink.utils;

import com.alibaba.fastjson.JSONObject;
import com.sankuai.xm.pub.push.Pusher;
import com.sankuai.xm.pub.push.PusherBuilder;

/**
 * 大象群组推送消息
 */
public class DXGroupPushUtil {

    private static Pusher pusher;

    private static final String online_url = "http://dxw-in.sankuai.com/api/pub/pushToRoom";
    private static final String test_url = "http://api.xm.test.sankuai.com/api/pub/pushToRoom";


    private static final String name = "猫眼大数据监控报警";
    private static final long pubId = 137443635371L;
    private static final String key = "G145095125521041";
    private static final String token = "62d81c132be0695347ef05bb226205b2";

    private static final int try_num = 3;

    //协同报警群
    private static final long XIETONG_ALERT_GROUP_ID = 64013287686L;

    static {
        /**
         *
         * withAppkey: 开发者信息中的appKey
         * withApptoken: 开发者信息中的appSecret
         * withTargetUrl: 服务地址，如果访问线上服务，请使用线上公众号
         *                线下地址：http://api.xm.test.sankuai.com/api/pub/pushToRoom
         *                线上地址：http://dxw-in.sankuai.com/api/pub/pushToRoom
         * withFromUid: 填写PubID，即公众号ID，因为这里是long型，需要加L
         * withToAppid: 1为大象
         * **/
        pusher = PusherBuilder.defaultBuilder().withAppkey(key).withApptoken(token)
                .withTargetUrl(online_url).withFromName(name)
                .withToAppid((short) 1).withFromUid(pubId).build();
    }


    public static void pushMessage(String message, long groupId) {
        for (int i = 0; i < try_num; i++) {
            JSONObject result = pusher.pushToRoom(message, groupId);
            System.out.println(result.toJSONString());
            if (result != null && result.getIntValue("rescode") == 0) {
                break;
            }
        }


    }

    /**
     * 给
     *
     * @param message
     */
    public static void pushMessage2XieTong(String message) {
        pusher.pushToRoom(message, XIETONG_ALERT_GROUP_ID);

    }

    public static void main(String[] args) {
        DXGroupPushUtil.pushMessage2XieTong("test by zb");
    }
}
