package com.maoyan.bigdata.datalink.contants;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.HashSet;
import java.util.Set;

public class MaoYanHotConstants {

    public static final int K = 3;
    public static final int M = 1000;
    public static final int N = 10000;


    //影视剧包含的内容
    public static final Set DRAMA_SET = new HashSet<String>() {{
        add("TV_DRAMA");
        add("NETWORK_DRAMA");
    }};
    //综艺包含的内容
    public static final Set VARIETY_SET = new HashSet<String>() {{
        add("NETWORK_VARIETY");
    }};

    public static final String categoryEachDimHot = "each_dim_hot";
    public static final String categoryEachDimHotNew = "each_dim_hot_new";
    public static final String categoryEachDimHotHourlySnapshot = "each_dim_hot_hourly_snapshot";
    public static final String categoryEachDimHotHourlySnapshotNew = "each_dim_hot_hourly_snapshot_n";
    public static final String CATEGORY_HOT_HOURLY_SNAPSHOT = "maoyan_hot_hourly_snapshot";
    public static final FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyyMMddHH");


    public static final String PLATFORM_BAIDU = "BAIDU";
    public static final String PLATFORM_WEIXIN = "WEIXIN";
    public static final String PLATFORM_WEIBO = "WEIBO";

    public static final String VIDEOTYPE_DRAMA = "DRAMA";
    public static final String VIDEOTYPE_VARIETY = "VARIETY";

    public static final String DATATYPE_BAIDUIDX = "baiduIdx";
    public static final String DATATYPE_WEIBOIDX = "weiboIdx";
    public static final String DATATYPE_WEIXINIDX = "weixinIdx";

    //String
    public static final String STR_HOT = "hot";


    //hash
    public static final String CATEGORY__MAOYAN_HOT_WAVE_NOW = "maoyan_host_wave_now";
    //hash
    public static final String CATEGORY__MAOYAN_HOT_WAVE_PRE = "maoyan_host_wave_pre";
    //string
    public static final String CATEGORY__MAOYAN_HOT_WAVE_SS = "maoyan_hot_wave_snapshot";
    //hash
    public static final String CATEGORY__MAOYAN_HOT = "maoyan_hot";
    public static final String CATEGORY__MAOYAN_HOT_HOURLY_SNAPSHOT = "maoyan_hot_hourly_snapshot";

    public static final String CATEGORY__MAOYAN_HOT_SYSTEM_CONFIG = "maoyan_hot_system_config";//猫眼热度配置
    public static final String CATEGORY__MAOYAN_HOT_MANUAL_CONFIG = "maoyan_hot_manual_config";//猫眼热度配置

    //maoyan hot time_line
    //当前时间线  hash
    public static final String CATEGORY__MAOYAN_HOT_TIMELINE_NOW = "maoyan_hot_timeline_now";
    //下一个时间线 hash
    public static final String CATEGORY__MAOYAN_HOT_TIMELINE_PRE = "maoyan_hot_timeline_pre";
    //热度快照,用于计算时间线 string
    public static final String CATEGORY__MAOYAN_HOT_TIMELINE_SS = "maoyan_hot_timeline_snapshot";
    //时间线的小时热度 string
    public static final String CATEGORY__MAOYAN_HOT_TIMELINE_HOURLY = "maoyan_hot_timeline_hourly";



    public static final String MAOYANHOT_SYSTEM_WAVE_SMALL_DELTA_TIME = "wave_small_delta_time";//秒级粒度
    public static final String MAOYANHOT_SYSTEM_WAVE_GAMMA = "wave_gamma";//波动γ值


    public static Set<String> TYPE_SET = new HashSet<String>() {{
        add(MaoYanHotConstants.VIDEOTYPE_DRAMA);
        add(MaoYanHotConstants.VIDEOTYPE_VARIETY);
    }};




    public static final String CATEGORY_MAOYAN_HOT_TIMELINE_NOW = "maoyan_hot_timeline_now";
    public static final String CATEGORY_MAOYAN_HOT_TIMELINE_HOURLY = "maoyan_hot_timeline_hourly";
    public static final String CATEGORY_MAOYAN_HOT_MANUAL_CONFIG = "maoyan_hot_manual_config";//猫眼热度配置
}
