package com.maoyan.bigdata.datalink.utils;

import com.dianping.squirrel.client.impl.redis.RedisClientBuilder;
import com.dianping.squirrel.client.impl.redis.RedisStoreClient;


public class SquirrelClientFactory {

    /**
     * squirrel cluster
     */
    public static final String SQUIRREL_CLUSTER__MAOYAN_HOT = "redis-movie-bigdata-stormeye_product";
    public static final String SQUIRREL_CLUSTER__MAOYAN_HOT_STAGE = "redis-movie-bigdata-stormeye_stage";
    public static final String SQUIRREL_CLUSTER__MAOYAN_HOT_DEV = "redis-movie-bigdata-stormeye_dev";

    public static RedisStoreClient getMaoYanHotClientProduct(){
        return new RedisClientBuilder(SQUIRREL_CLUSTER__MAOYAN_HOT)
                .readTimeout(10000)
                .routerType("master-only")
                .build();
    }


    public static RedisStoreClient getMaoYanHotClientStage(){
        return new RedisClientBuilder(SQUIRREL_CLUSTER__MAOYAN_HOT_STAGE)
                .readTimeout(10000)
                .routerType("master-only")
                .build();
    }

    public static RedisStoreClient getMaoYanHotClienDev(){
        return new RedisClientBuilder(SQUIRREL_CLUSTER__MAOYAN_HOT_DEV)
                .readTimeout(10000)
                .routerType("master-only")
                .build();
    }



}
