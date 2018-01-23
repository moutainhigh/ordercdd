package com.liyang.controller.domain;

import com.alibaba.fastjson.JSON;
import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.domain.department.Department;
import com.liyang.domain.department.DepartmentRepository;
import com.liyang.domain.department.Departmenttype;
import com.liyang.domain.loan.LoanLog;
import com.liyang.domain.loan.LoanLogRepository;

import com.liyang.listener.ContextServiceLoaderListener;
import com.liyang.service.AbstractWorkflowService;
import com.liyang.config.ApplicationContextAwares;
import com.liyang.util.CommonUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;


import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description log的相关操作
 * @authpr jianger
 * @Date 2018/1/8 上午9:54
 **/
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LoanLogRepository loanLogRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    //将log的difference字段解析成map的k-v键值对
    @RequestMapping(path = "/loan")
    public EntityPage getMapFromLog(@RequestParam(required = false) Map<String, Object> params) {
        //查出特定需要的log记录条数
        GenericQueryExpSpecification<LoanLog> queryExpression = new GenericQueryExpSpecification<>();

        //根据某个实体查询
        if (params.get("entity_id") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.eq("entity.id", params.get("entity_id"), true));
        }
        //根据状态查询act_id
        if (params.get("act_id") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.eq("act.id", params.get("act_id"), true));
        }
        //根据角色部门限制
        limitField(queryExpression);

        Page page = loanLogRepository.findAll(queryExpression, EntityPageUtil.createPageable(params));
        String[] fields = new String[]{"id", "label", "createdAt", "lastModifiedAt", "notice", "act.id", "beforeState", "afterState", "noticeTo",
                "actGroup", "appCode", "client", "imei", "ip", "latitude", "longitude", "difference"};
        return convertCustom(EntityPageUtil.convert(page, fields));

    }

    private void limitField(GenericQueryExpSpecification queryExpression) {
        Integer curDepartmentId = CommonUtil.getCurrentUserDepartment().getId();
        //平台
        Department company = departmentRepository.findByDepartmenttypeCode(Departmenttype.DepartmenttypeCode.COMPANY);
        //门店，暂时认为非平台的
        if (curDepartmentId.equals(company.getId())) {//如果登录人部门是平台，只查平台一审和平台二审的
        } else {//查询其他状态的
            queryExpression.add(QueryExpSpecificationBuilder.eq("createdByDepartment.id",
                    curDepartmentId, true));
        }
    }

    //将difference字段换成json
    private EntityPage convertCustom(EntityPage page) {
        List<Map<String, Object>> entities = page.getEntitys();
        if (entities == null) {
            return page;
        }
        for (Map<String, Object> map : entities) {
            if (map == null) {
                continue;
            }
            //difference字段转成json
            String difference = (String) map.get("difference");
            Map<Object, Object> maps = (Map<Object, Object>) JSON.parse(difference);
            map.put("difference", maps);
        }
        return page;
    }

    @RequestMapping("/testlogs")
    public EntityPage testjpa(@RequestParam(required = false) Map<String, Object> params) throws ClassNotFoundException {

        //1.取出基类的logReository
        Map<String, Object> classMap = new HashMap<>();
        String name = (String) params.get("name");
        if (StringUtils.isNotBlank(name)) {
            //获取所有的service
            Map<String, Object> services = ContextServiceLoaderListener.getAllServices();
            for (Map.Entry<String, Object> entry : services.entrySet()) {
                classMap.put(entry.getKey().toLowerCase(), entry.getValue());
            }
        }
        GenericQueryExpSpecification queryExpression = new GenericQueryExpSpecification();
        AbstractWorkflowService service = (AbstractWorkflowService) classMap.get(name.toLowerCase());
        //2.添加查询的条件
        //根据某个实体查询
        if (params.get("entity_id") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.eq("entity.id", params.get("entity_id"), true));
        }
        //根据状态查询act_id
        if (params.get("act_id") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.eq("act.id", params.get("act_id"), true));
        }
        //根据角色部门限制
        limitField(queryExpression);

        Page page = service.getLogRepository().findAll(queryExpression, EntityPageUtil.createPageable(params));
        String[] fields = new String[]{"id", "label", "createdAt", "lastModifiedAt", "notice", "act.id", "beforeState", "afterState", "noticeTo",
                "actGroup", "appCode", "client", "imei", "ip", "latitude", "longitude", "difference"};
        return convertCustom(EntityPageUtil.convert(page, fields));
    }

}
