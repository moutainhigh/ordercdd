package com.liyang.controller.domain;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.liyang.Exception.TimeConditiosException;
import com.liyang.util.CommonUtil;
import com.liyang.util.SearchByTimes;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.domain.notice.Notice;
import com.liyang.domain.notice.NoticeRepository;

/**
 * 部门
 * @author Jh
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    private NoticeRepository repository;

    @RequestMapping("/search")
    public Page<Notice> search(@RequestParam(required=false) Map<String,Object> params) throws TimeConditiosException {
        GenericQueryExpSpecification<Notice> queryExpression = new GenericQueryExpSpecification<Notice>();

//        if(params.get("userId") != null){
            queryExpression.add(QueryExpSpecificationBuilder.eq("noticeUser.id", CommonUtil.getPrincipal().getId(),true));
//        }

        if(params.get("isRead") != null){
            queryExpression.add(QueryExpSpecificationBuilder.eq("isRead", Boolean.valueOf((String )params.get("isRead")),true));
        }

        //根据时间查询
        if(params.get("startDate") != null){
            queryExpression.add(QueryExpSpecificationBuilder.gt("noticeInfo.createdAt",SearchByTimes.converTime((String )params.get("startDate")),true));
        }
        if(params.get("endDate") != null){
            queryExpression.add(QueryExpSpecificationBuilder.lt("noticeInfo.createdAt", SearchByTimes.converTime((String )params.get("endDate")),true));
        }

        Page<Notice> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
        return  page;
//        String[] fields = new String[] {"id","isRead","status"};
//        return EntityPageUtil.convert(page, fields);
    }
}
