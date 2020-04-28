package com.maoyan.bigdata.datalink.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by zhaomin on 2016/12/1.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{

    public static final char UNDERLINE = '_';

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean isValidJson(String json) {
        try {
            JSON.parse(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 字符串过滤空格,空值返回null
     *
     * @param str -- 要处理的字符串
     * @return -- 处理后的结果
     */
    public static String trim(String str) {
        return trim(str, null);
    }

    /**
     * 字符串过滤空格,空值返回默认default值
     *
     * @param str -- 要处理的字符串
     * @param def -- 当字符串为空时默认值
     * @return -- 处理后的结果
     */
    public static String trim(String str, String def) {
        if (str == null || "".equals(str.trim())) {
            return def;
        } else {
            return str.trim();
        }
    }

    /**
     * 将String类型转为int类型
     *
     * @param str -- 待转换字符串
     * @return -- 转换后的数字
     */
    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    /**
     * 将字符串转换为int类型，如果字符串为空，返回默认值def
     *
     * @param str -- 待转换的字符串
     * @param def -- 默认值
     * @return -- 转换后的数字
     */
    public static int parseInt(String str, int def) {
        str = trim(str);
        return str == null ? def : Integer.parseInt(str);
    }

    /**
     * 将字符串数组拼接成一个字符串，通过逗号“,”分隔
     *
     * @param array -- 带拼接的字符串
     * @return -- 拼接后的字符串
     */
    public static String implode(String[] array) {
        return implode(array, ",");
    }

    public static String implode(Integer[] array) {
        return implode(array, ",");
    }

    /**
     * 将字符串数组拼接成一个字符串，通过传入separator分隔
     *
     * @param array     -- 带拼接的字符串
     * @param separator -- 字符串间的分隔符
     * @return -- 拼接后的字符串
     */
    public static String implode(String[] array, String separator) {
        if (array == null) {
            return null;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(array[0]);
            for (int i = 1; i < array.length; i++) {
                sb.append(separator).append(array[i]);
            }
            return sb.toString();
        }
    }

    public static String implode(Integer[] array, String separator) {
        if (array == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append(separator).append(array[i]);
        }
        return sb.toString();
    }

    /**
     * 对传入的字符串进行urlencode操作
     *
     * @param str -- 待编码字符串
     * @return -- 编码后的字符串
     */
    public static String URLEncode(String str) {
        try {
            if (str == null) {
                return null;
            } else {
                return URLEncoder.encode(str, "UTF-8");
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 对传入的字符串进行URLdecode操作
     *
     * @param str -- 带解码字符串
     * @return -- 解码后的字符串
     */
    public static String URLDecode(String str) {
        try {
            if (str == null) {
                return null;
            } else {
                return URLDecoder.decode(str, "UTF-8");
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 对传入的字符串进行base64编码
     *
     * @param str -- 需要进行base64处理的字符串
     * @return -- 编码后的字符串
     */
    public static String base64Encode(String str) {
        if (str == null) {
            return null;
        }
        return Base64.encodeBase64String(str.getBytes());
    }

    /**
     * 针对base64编码的字符串进行解码
     *
     * @param str -- 需要解码的字符串
     * @return -- 解码后的字符串
     */
    public static String base64Decode(String str) {
        if (str == null) {
            return null;
        }
        return new String(Base64.decodeBase64(str.getBytes()));
    }

    /**
     * 检验输入是不是为空，或者null
     *
     * @param text
     * @return
     */
    public static boolean isText(String text) {
        if (text != null && text.length() > 0) {
            return true;
        }
        return false;
    }

    public static double getRandomNum(double small, double bigger) {
        Random random = new Random(1);
        return random.nextDouble();
    }

    public static void main(String[] args) {
        Random random = new Random(1);
        for (int i = 0; i < 100; i++) {
            double a = random.nextDouble() - 0.5;
            if (a > 0) {
                a = a - 0.1;
            } else if (a < 0) {
                a = a + 0.1;
            }
            System.out.println(a);

            if (a > 0.4 || a < -0.4) {
                System.out.println(a + "---" + a);
            }
        }

    }

}
