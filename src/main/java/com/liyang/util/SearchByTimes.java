package com.liyang.util;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.Exception.TimeConditiosException;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @Description 根据时间区域查询
 * @authpr jianger
 * @Date 2017/12/18 下午5:05
 **/
public class SearchByTimes {

    public static void addTimesConditions(Map<String, Object> params, GenericQueryExpSpecification queryExpression) throws TimeConditiosException {
        //1.取出参数
        String startTime = (String) params.get("startDate");
        String endTime = (String) params.get("endDate");
        String timeRangeKey = (String) params.get("timeRangeKey");



        if (StringUtils.isNotBlank(timeRangeKey)){
            //查询区域时间内
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                //2.校验参数
                checkDate(startTime,endTime);

                queryExpression.add(QueryExpSpecificationBuilder.gte(timeRangeKey, converTime(startTime), true));
                queryExpression.add(QueryExpSpecificationBuilder.lt(timeRangeKey, addDay(converTime(endTime),1), true));
                return;
            }
            //查询开始时间
            if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
                queryExpression.add(QueryExpSpecificationBuilder.gte(timeRangeKey, converTime(startTime), true));
            }
            //查询截止日期
            if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                queryExpression.add(QueryExpSpecificationBuilder.lt(timeRangeKey,addDay(converTime(endTime),1), true));
            }
        }

    }

    //校验参数
    private static void checkDate(String startTime, String endTime) throws TimeConditiosException {
        Date startDate=converTime(startTime);
        Date endDate=converTime(endTime);
        if(startDate.getTime()>endDate.getTime()){
            //抛出异常
            throw new TimeConditiosException(TimeConditiosException.ValidateTimeError);
        }
    }


    //将时间类型装换
    public static Date converTime(String time) {

        String t = time.replace("Z"," UTC");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        try {
            return dateFormat.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //将结束时间加一天
    public static Date addDay(Date date, int days) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);


        return cal.getTime();
    }
}
