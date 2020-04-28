package com.maoyan.bigdata.datalink.handler;

import com.maoyan.bigdata.datalink.exception.DateExprParseException;
import com.maoyan.bigdata.datalink.utils.StringUtils;
import com.maoyan.bigdata.datalink.utils.TimeUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间表达式处理
 * Created by zhaoyangyang on 2019/10/28
 */
public class DateExprHandler {

    private static final String DATE_PRE = "\\$now";
    private static final String DATE_DELTA_REG = "(?i)((?<delta>\\.delta\\((((?<units>days|seconds|minutes|hours|weeks|months|years)=)?(?<cnt>[0-9]+))\\)))?";
    private static final String DATE_DTYPE_REG = "(\\.(?<dType>month_begin_date|month_end_date))?";
    private static final String DATE_SUF = "(\\.(?<format>datekey|date)){1}";
    private static final String DATE_REG = DATE_PRE + DATE_DELTA_REG + DATE_DTYPE_REG + DATE_SUF;

    private static final Pattern DATE_EXPR_PATTERN = Pattern.compile(DATE_REG);

    private static Map<String, String> formatKeyMap = new HashMap();
    private static Map<String, Integer> unitMap = new HashMap();
    private static Map<String, IDateHandler> unitMap2 = new HashMap();

    static {
        formatKeyMap.put("date", "yyyy-MM-dd");
        formatKeyMap.put("datekey", "yyyyMMdd");

        unitMap.put("seconds", Calendar.SECOND);
        unitMap.put("minutes", Calendar.MINUTE);
        unitMap.put("hours", Calendar.HOUR);
        unitMap.put("weeks", Calendar.WEEK_OF_MONTH);
        unitMap.put("days", Calendar.DAY_OF_MONTH);
        unitMap.put("months", Calendar.MONTH);
        unitMap.put("years", Calendar.YEAR);

        unitMap2.put("month_begin_date", TimeUtil::getFirstDayOfMonth);
        unitMap2.put("month_end_date", TimeUtil::getLastDayOfMonth);
    }

    public static String parseExpr(String expr) {
        Matcher matcher = DATE_EXPR_PATTERN.matcher(expr);
        boolean isDelta = false;//是否当前时间往前回溯
        String dType;//回溯类型
        String delta;//回溯类型
        String delta_unit;//回溯单位
        String delta_cnt;//回溯数量
        String format;//输出类型

        if (matcher.find()) {
            delta = matcher.group("delta");
            dType = matcher.group("dType");
//            isDelta = StringUtils.isNotBlank(delta) || StringUtils.isNotBlank(dType);
            delta_unit = matcher.group("units") == null ? "days" : matcher.group("units");
            delta_cnt = matcher.group("cnt");
            format = matcher.group("format");
        } else {
            throw new DateExprParseException("can not parse date expr,please check it.");
        }

        Date now = new Date();
        if (StringUtils.isNotBlank(delta)) {
            now = TimeUtil.subtract(now, Integer.valueOf(delta_cnt), unitMap.get(delta_unit));
        }
        if (StringUtils.isNotBlank(dType)) {
            IDateHandler dateHandler = unitMap2.get(dType);
            now = dateHandler.apply(now);
        }
        return TimeUtil.format(now, formatKeyMap.get(format));
    }

    /**
     * 替换字符串中的表达式
     *
     * @param expr 需要替换的字符串
     * @return
     */
    public static String replaceDateExpr(String expr) {
        Matcher matcher = DATE_EXPR_PATTERN.matcher(expr);
        while (matcher.find()) {
            String value = parseExpr(matcher.group());
            expr = expr.replaceFirst(DATE_REG, value);
        }
        return expr;
    }

    public static void main(String[] args) {
        System.out.println(DATE_REG);
        System.out.println("$now.delta(days=1).date:\t" + DateExprHandler.parseExpr("$now.delta(days=1).date"));
        System.out.println("$now.delta(1).date:\t" + DateExprHandler.parseExpr("$now.delta(1).date"));
        System.out.println("$now.date:\t" + DateExprHandler.parseExpr("$now.date"));
        System.out.println("$now.datekey:\t" + DateExprHandler.parseExpr("$now.datekey"));
        System.out.println("$now.month_begin_date.date:\t" + DateExprHandler.parseExpr("$now.month_begin_date.date"));
        System.out.println("$now.delta(30).month_begin_date.date:\t" + DateExprHandler.parseExpr("$now.delta(30).month_begin_date.date"));
        System.out.println("$now.delta(30).month_end_date.date:\t" + DateExprHandler.parseExpr("$now.delta(30).month_end_date.date"));
        String sql = "SELECT $now.delta(1).datekey dt,allscore.celebrityId celebrityId, cast(allscore.score as double) a_score, cast(dramascore.score as double) d_score, cast(varietyscore.score as double) v_score,cast(moviescore.score as double) m_score FROM allscore LEFT JOIN dramascore LEFT JOIN varietyscore LEFT JOIN moviescore ON allscore.celebrityId=dramascore.celebrityId AND allscore.celebrityId=varietyscore.celebrityId AND allscore.celebrityId=moviescore.celebrityId";

        System.out.println(replaceDateExpr(sql));

        System.out.println(StringUtils.reverse("E11A9A71A3EB7239F36F8AFCE885FFC8A7B79167F55EDCCD71367827D1C21A20"));
    }

}
