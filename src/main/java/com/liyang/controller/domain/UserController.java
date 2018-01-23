package com.liyang.controller.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.Exception.TimeConditiosException;
import com.liyang.domain.department.Department;
import com.liyang.domain.role.Role;
import com.liyang.domain.user.User;
import com.liyang.domain.user.UserRepository;
import com.liyang.util.BeanUtil;
import com.liyang.util.CommonUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	@Resource
	private UserRepository repository;
	
	@RequestMapping("/search")
    public EntityPage search(@RequestParam(required = false) Map<String, Object> params) throws TimeConditiosException {
        GenericQueryExpSpecification<User> queryExpression = new GenericQueryExpSpecification<User>();
        //状态
        queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"), true));
        //所属部门
        queryExpression.add(QueryExpSpecificationBuilder.eq("department.id", params.get("department_id"), true));
        //所属角色
        queryExpression.add(QueryExpSpecificationBuilder.eq("role.id", params.get("role_id"), true));
        //用户分类
        if ("user".equals(params.get("is_user"))) {//前端用户
			queryExpression.add(QueryExpSpecificationBuilder.eq("role.roleCode", "USER", true));
		}else {//后端用户
			queryExpression.add(QueryExpSpecificationBuilder.ne("role.roleCode", "USER", true));
		}
        //昵称
        if (params.get("nickname") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("nickname", params.get("nickname").toString(), true));
        }
        //用户名
        if (params.get("username") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("username", params.get("username").toString(), true));
        }
        //客服推荐码
        if (params.get("referralCode") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("referralCode", params.get("referralCode").toString(), true));
        }
        Page<User> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
        String[] fields = new String[]{"id", "label", "createdAt", "lastModifiedAt", "state", "nickname", "sex", "username",
                "department", "role", "referralCode"};
        return convertCustom(EntityPageUtil.convert(page, fields));
    }
	
	private EntityPage convertCustom(EntityPage page) {
        List<Map<String, Object>> entities = page.getEntitys();
        if (entities == null) {
            return page;
        }
        for (Map<String, Object> map : entities) {
            if (map == null) {
                continue;
            }

            Object role = map.get("role");
            map.remove("role");
            if (role != null) {
            	Role obj = (Role) role;
                map.put("role_id", obj.getId());
                map.put("role_label", obj.getLabel());
                map.put("role_code", obj.getRoleCode());
            }
        }
        return page;
    }
	
	@RequestMapping("/referralCode")
	public Map<String, Object> referralCode(@RequestParam String referralCode) {
		User user = repository.findByReferralCode(referralCode);
		if (user == null) {
			return new HashMap<>();
		}
		Map<String, Object> result = BeanUtil.beanToMap(user);
        BeanUtil.commonConvert(result, user);
        result.remove("childrenDepartments");
        result.remove("userESign");
        result.remove("person");
        Object role = result.get("role");
        result.remove("role");
        if (role != null) {
			Role obj = (Role) role;
			Map<String, Object> roleMap = new HashMap<>();
			roleMap.put("id", obj.getId());
			roleMap.put("label", obj.getLabel());
			roleMap.put("role_code", obj.getRoleCode());
			result.put("role", roleMap);
		}
        Map<String, Object> curUserMap = new HashMap<>();
        User curUser = CommonUtil.getPrincipal();
        curUserMap.put("id", curUser.getId());
        curUserMap.put("username", curUser.getUsername());
        curUserMap.put("nickname", curUser.getNickname());
        result.put("curUser", curUserMap);
        Map<String, Object> curDepartmentMap = new HashMap<>();
        Department curDepartment = CommonUtil.getCurrentUserDepartment();
        curDepartmentMap.put("id", curDepartment.getId());
        curDepartmentMap.put("label", curDepartment.getLabel());
        result.put("curDepartment", curDepartmentMap);
        return result;
	}
	
	@RequestMapping("/currentuser")
	public Map<String, Object> currentUser() {
		User user = CommonUtil.getPrincipal();
		Map<String, Object> result = BeanUtil.beanToMap(user);
		result.remove("logRepository");
		result.remove("service");
		result.remove("state");
		result.remove("createdBy");
		result.remove("createdByDepartment");
		result.remove("lastModifiedBy");
		
        result.remove("childrenDepartments");
        result.remove("userESign");
        result.remove("person");
        Object role = result.get("role");
        result.remove("role");
        if (role != null) {
			Role obj = (Role) role;
			Map<String, Object> roleMap = new HashMap<>();
			roleMap.put("id", obj.getId());
			roleMap.put("label", obj.getLabel());
			roleMap.put("role_code", obj.getRoleCode());
			result.put("role", roleMap);
		}
        Object department = result.get("department");
        result.remove("department");
        if (department != null) {
			Department obj = (Department) department;
			Map<String, Object> departmentMap = new HashMap<>();
			departmentMap.put("id", obj.getId());
			departmentMap.put("label", obj.getLabel());
			result.put("department", departmentMap);
		}
        return result;
	}
}
