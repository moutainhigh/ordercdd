package com.liyang.Exception;

/**
 * @Description 判断时间类型的参数异常类
 * @authpr jianger
 * @Date 2017/12/20 上午10:42
 **/
public class TimeConditiosException extends Exception {
    public final static int OK = 200;
    public final static int ValidateTimeError =-440500;


    private int code;

    private static String getMessage(int code) {
        switch (code) {
            case ValidateTimeError:
                return "时间范围错误，结束时间要求大于开始时间";
            default:
                return null;
        }
    }

    public int getCode() {
        return code;
    }

    public TimeConditiosException(int code) {
        super(getMessage(code));
        this.code = code;
    }



}
