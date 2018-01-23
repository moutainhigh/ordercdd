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
import com.liyang.domain.role.Role;
import com.liyang.domain.role.RoleRepository;
import com.liyang.domain.department.Departmenttype;

/**
 * 部门
 * @author Jh
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleRepository repository;

    @RequestMapping("/search")
    public EntityPage search(@RequestParam(required=false) Map<String,Object> params) {
        GenericQueryExpSpecification<Role> queryExpression = new GenericQueryExpSpecification<Role>();

        queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"),true));

        if (params.get("role_label")!=null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("label", params.get("role_label").toString(),true));
        }

        Page<Role> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
        String[] fields = new String[] {"id","label","lastModifiedAt","createdAt","state","departmenttype"};
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

        }
        return page;
    }
}
