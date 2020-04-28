package com.maoyan.bigdata.datalink.exception;

/**
 * Created by zhaoyangyang on 2019/10/28
 */
public class DateExprParseException extends RuntimeException {

    public DateExprParseException(String message){
        super(message);
    }

    public DateExprParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
