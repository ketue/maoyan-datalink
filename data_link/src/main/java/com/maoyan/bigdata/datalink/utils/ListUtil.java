package com.maoyan.bigdata.datalink.utils;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static <T> List<List<T>> splitList(List<T> sourceList, int size) {
        List<List<T>> targetList = new ArrayList<>();
        if (sourceList == null) {
            return targetList;
        }
        int div = sourceList.size() / size;
        int remainder = sourceList.size() % size;
        int start = 0;
        int end;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                end = (start + 1) * div + remainder;
            } else if (i == (size - 1)) {
                end = sourceList.size();
            } else {
                end = div + start;
            }
            targetList.add(sourceList.subList(start, end));
            start = end;
        }
        return targetList;
    }


    /**
     * 相同块大小,切割 list
     * @param sourceList
     * @param blockSize
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> splitListBySize(List<T> sourceList, int blockSize) {
        List<List<T>> resultList = Lists.newArrayList();
        int sourceSize = sourceList.size();
        if (sourceSize <= blockSize) {
            resultList.add(sourceList);
        } else {
            int times = sourceSize / blockSize + 1;
            int start, end = 0;
            for (int i = 0; i < times; i++) {
                start = end;
                end = start + blockSize;
                if (sourceSize < end) {
                    end = sourceSize;
                }
                System.out.println(start + "\t" + end);
                resultList.add(sourceList.subList(start, end));
            }
        }
        return resultList;
    }

    /**
     * 相同块大小,切割 json array
     * @param source
     * @param blockSize
     * @return
     */
    public static  List<JSONArray> splitJSONArrayBySize(JSONArray source, int blockSize) {
        List<JSONArray> resultList = Lists.newArrayList();
        int sourceSize = source.size();
        if (sourceSize <= blockSize) {
            resultList.add(source);
        } else {
            int times = sourceSize / blockSize + 1;
            int start, end = 0;
            for (int i = 0; i < times; i++) {
                start = end;
                end = start + blockSize;
                if (sourceSize < end) {
                    end = sourceSize;
                }
                JSONArray subArray = new JSONArray();
                for (int j = start; j <end ; j++) {
                    subArray.add(source.get(j));
                }
                resultList.add(subArray);
            }
        }
        return resultList;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 105; i++) {
            list.add(i, i);
        }

        List<List<Integer>> lists = splitList(list, 50);
        for (List<Integer> integers : lists) {
            System.out.println(integers);
        }
    }
}
