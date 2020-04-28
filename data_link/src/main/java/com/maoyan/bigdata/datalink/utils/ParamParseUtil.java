package com.maoyan.bigdata.datalink.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaoyangyang on 2019/12/10
 */
public class ParamParseUtil {

    public static final String REG_DYNAMIC = "\\$\\{(.*?)\\}";
    private static final Pattern PARAM_PATTERN=Pattern.compile(REG_DYNAMIC);

    public static String[] getFields(String param){
        List<String> fields=new ArrayList<>();
        Matcher matcher = PARAM_PATTERN.matcher(param);
        while (matcher.find()){
            fields.add(matcher.group(1));
        }
        return fields.toArray(new String[]{});
    }

    public static String replaceDynamicData(String dynamicParam,String oldField,String replaceValue){
        return dynamicParam.replace("${"+oldField+"}",replaceValue);
    }

    public static boolean isHaveDynamicParam(String param){
        return false;
    }

    public static void main(String[] args) {
        String json="\"allscore\": {\n" +
                "      \"key\": \"getCelebrityScore\",\n" +
                "      \"para\": \"rankType=all&dt=$now.delta(1).datekey&mId=${key1.maoyanId}&mId=${key1.yanId}\",\n" +
                "      \"fields\": \"celebrityId,score\"\n" +
                "    },";

        Arrays.stream(ParamParseUtil.getFields(json)).forEach(System.out::println);

        System.out.println(StringUtils.reverse("6A8EC20FEA00-E608-C844-A56E-0DB68FE6"));
        System.out.println(ParamParseUtil.replaceDynamicData(json, "key1.maoyanId","123"));
        System.out.println(json.replace("${key1.maoyanId}", "1111"));
    }
}
