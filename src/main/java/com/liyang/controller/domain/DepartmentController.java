package com.liyang.controller.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.liyang.domain.department.DepartmentState;
import com.liyang.util.CommonUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.domain.department.Department;
import com.liyang.domain.department.DepartmentRepository;
import com.liyang.domain.department.Departmenttype;
import com.liyang.domain.enterprise.Enterprise;

/**
 * 部门
 * @author Jh
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Resource
    private DepartmentRepository repository;

    @RequestMapping("/search")
    public EntityPage search(@RequestParam(required=false) Map<String,Object> params) {
        GenericQueryExpSpecification<Department> queryExpression = new GenericQueryExpSpecification<Department>();

        queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"),true));
        limitField(queryExpression);
        Page<Department> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
        String[] fields = new String[] {"id","label","province","city","district","linkman","telephone","lastModifiedAt","createdAt","state","departmenttype","enterprise","parent"};
        return convertCustom(EntityPageUtil.convert(page, fields));
    }
    private EntityPage convertCustom(EntityPage page) {
        List<Map<String,Object>> entities = page.getEntitys();
        if (entities==null) {
            return page;
        }
        for (Map<String, Object> map : entities) {
            if (map==null) {
                continue;
            }
            //自定义的转换
            Object state = map.get("state");
            if (state instanceof DepartmentState) {
                DepartmentState stateObj = (DepartmentState) state;
                map.put("state_label", stateObj.getLabel());
                map.remove("state");
            }

            Object departmenttype = map.get("departmenttype");
            if (departmenttype instanceof Departmenttype){
                Departmenttype departmenttypeObj = (Departmenttype) departmenttype;
                map.put("departmenttype_label",departmenttypeObj.getLabel());
                map.remove("departmenttype");
            }

            Object enterprise = map.get("enterprise");
            if (enterprise instanceof Enterprise){
                Enterprise enterpriseObj = (Enterprise) enterprise;
                map.put("enterprise_label",enterpriseObj.getLabel());
                map.remove("enterprise");
            }

            Object parent = map.get("parent");
            if (parent instanceof Department){
                Department parentObj = (Department) parent;
                map.put("parent_label",parentObj.getLabel());
                map.remove("parent");
            }
        }
        return page;
    }
    /**
     * 限制权限，例如：部门、角色等
     */
    private void limitField(GenericQueryExpSpecification<Department> queryExpression) {
        Integer curDepartmentId = CommonUtil.getCurrentUserDepartment().getId();
        //平台
//        Department company = repository.findByDepartmenttypeCode(Departmenttype.DepartmenttypeCode.COMPANY);
//        //门店，暂时认为非平台的
//        if(curDepartmentId.equals(company.getId())) {//如果登录人部门是平台，只查平台一审和平台二审的
//        }else {//查询其他状态的
            queryExpression.add(QueryExpSpecificationBuilder.in("createdByDepartment",
                    CommonUtil.getPrincipal().childrenDepartments, true));
//        }
    }
}
