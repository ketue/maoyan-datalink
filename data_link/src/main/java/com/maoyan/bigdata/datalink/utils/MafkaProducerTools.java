package com.maoyan.bigdata.datalink.utils;

import com.meituan.mafka.client.MafkaClient;
import com.meituan.mafka.client.consumer.ConsumerConstants;
import com.meituan.mafka.client.producer.AsyncProducerResult;
import com.meituan.mafka.client.producer.FutureCallback;
import com.meituan.mafka.client.producer.IProducerProcessor;
import com.meituan.mafka.client.producer.ProducerResult;

import java.util.Properties;

public class MafkaProducerTools {

    private static IProducerProcessor producer;

    private String bgNamespace;
    private String appkey;
    private String topic;


    public MafkaProducerTools(String bgNamespace,String appkey,String topic){
        this.bgNamespace=bgNamespace;
        this.appkey=appkey;
        this.topic=topic;

        Properties properties = new Properties();
        // 设置业务所在BG的namespace，此参数必须配置且请按照demo正确配置
        properties.setProperty(ConsumerConstants.MafkaBGNamespace, this.bgNamespace);
        // 设置生产者appkey，此参数必须配置且请按照demo正确配置
        properties.setProperty(ConsumerConstants.MafkaClientAppkey, this.appkey);

        // 创建topic对应的producer对象（注意每次build调用会产生一个新的实例），此处配置topic名称，请按照demo正确配置
        // 请注意：若调用MafkaClient.buildProduceFactory()创建实例抛出有异常，请重点关注并排查异常原因，不可频繁调用该方法给服务端带来压力。
        try {
            producer = MafkaClient.buildProduceFactory(properties, this.topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MafkaProducerTools(String appkey,String topic) {
        this("maoyan",appkey,topic);
    }


    public ProducerResult sendMessage(Object data){
        try {
            ProducerResult result = producer.sendMessage(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ProducerResult sendAsyncMessage(Object data){
        try {
            ProducerResult result = producer.sendAsyncMessage(data, new FutureCallback() {
                @Override
                public void onSuccess(AsyncProducerResult asyncProducerResult) {

                }
                @Override
                public void onFailure(Throwable throwable) {

                }
            });
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
