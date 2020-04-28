package com.maoyan.bigdata.datalink.utils;

public class MaoYanMovieUtils {
    public static String getMyHotType(String type) {
        String realType;
        switch (type) {
            case "1":
            case "5":
                realType = "DRAMA";
                break;
            case "2":
                realType = "VARIETY";
                break;
            default:
                realType = type;
        }
        return realType;
    }
}
