package com.liyang.controller.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.Exception.TimeConditiosException;
import com.liyang.domain.baddebt.Baddebt;
import com.liyang.domain.baddebt.BaddebtLog;
import com.liyang.domain.baddebt.BaddebtLogRepository;
import com.liyang.domain.baddebt.BaddebtRepository;
import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.department.Department;
import com.liyang.domain.department.DepartmentRepository;
import com.liyang.domain.department.Departmenttype.DepartmenttypeCode;
import com.liyang.domain.loan.Loan;
import com.liyang.domain.user.User;
import com.liyang.message.EnumOperationMessageType;
import com.liyang.util.CommonUtil;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject.Level;

@RestController
@RequestMapping("/baddebt")
public class BaddebtController {
	@Resource
	private BaddebtRepository repository;
	@Resource
	private BaddebtLogRepository baddebtLogRepository;
	@Resource
    private DepartmentRepository departmentRepository;
	
	@RequestMapping("/search")
    public EntityPage search(@RequestParam(required = false) Map<String, Object> params) throws TimeConditiosException {
        GenericQueryExpSpecification<Baddebt> queryExpression = new GenericQueryExpSpecification<Baddebt>();
        //状态
        queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"), true));
        //客户名称
        if (params.get("debtorName") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("debtorName", params.get("debtorName").toString(), true));
        }
        //客户联系方式
        if (params.get("debtorMobile") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("debtorMobile", params.get("debtorMobile").toString(), true));
        }
        //所属门店
        queryExpression.add(QueryExpSpecificationBuilder.eq("department.id", params.get("department_id"), true));
        
        limitField(queryExpression);
        Page<Baddebt> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
        String[] fields = new String[]{"id", "label", "createdAt", "lastModifiedAt", "state", "debtorName", "debtorMobile", "debtorRepayAmount",
                "debtorLeftAmount", "department", "finishAt","loan"};
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

            Object loan = map.get("loan");
            map.remove("loan");
            if (loan != null) {
            	Loan obj = (Loan) loan;
            	map.put("loanId", obj.getId());
                map.put("loanSn", obj.getLoanSn());
            }
        }
        return page;
    }
	/**
     * 限制权限，例如：部门、角色等
     */
    private void limitField(GenericQueryExpSpecification<Baddebt> queryExpression) {
        Integer curDepartmentId = CommonUtil.getCurrentUserDepartment().getId();
        //平台
        Department company = departmentRepository.findByDepartmenttypeCode(DepartmenttypeCode.COMPANY);
        //门店，暂时认为非平台的
        if (curDepartmentId.equals(company.getId())) {//如果登录人部门是平台，只查平台一审和平台二审的
        } else {//查询其他状态的
            queryExpression.add(QueryExpSpecificationBuilder.eq("department.id",
                    curDepartmentId, true));
        }
    }
	
	@RequestMapping("/log")
	public Integer log(HttpServletRequest request,@RequestParam Integer id,@RequestParam String content) {
		if (id == null) {
			throw new FailReturnObject(1, "Id不能为null", Level.ERROR);
		}
		Baddebt baddebt = repository.findOne(id);
		if (baddebt == null) {
			throw new FailReturnObject(1, "未找到对应的实体", Level.ERROR);
		}
		BaddebtLog log = new BaddebtLog();
		log.setActGroup(ActGroup.LIST_OPERATE);
        log.setEntity(baddebt);
        log.setLabel(content);
        Map entityToDiffMap = CommonUtil.entityToDiffMap(baddebt, null);
        log.setDifference(CommonUtil.prettyPrint(entityToDiffMap));
        
        log.setMessageType(EnumOperationMessageType.TIMCustomElem);//需要标记唯一，表示是页面添加的日志

        log.setAppCode(request.getHeader("app_code"));
        log.setClient(request.getHeader("client"));
        log.setImei(request.getHeader("imei"));
        log.setIp(request.getRemoteHost());
        if (request.getHeader("longitude") != null) {
            log.setLongitude(Double.valueOf(request.getHeader("longitude")));
            log.setLatitude(Double.valueOf(request.getHeader("latitude")));
        }
        baddebtLogRepository.save(log);
        return id;
	}
	
	@RequestMapping("/logList")
	public List<Map<String, Object>> logList(@RequestParam Integer id) {
		if (id == null) {
			throw new FailReturnObject(1, "Id不能为null", Level.ERROR);
		}
		List<Map<String, Object>> result = new ArrayList<>();
		List<BaddebtLog> list = baddebtLogRepository.findByEntityIdAndMessageType(id, EnumOperationMessageType.TIMCustomElem);
		if (list == null) {
			return result;
		}
		for (BaddebtLog baddebtLog : list) {
			if (baddebtLog == null) {
				continue;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("id", baddebtLog.getId());
			map.put("createdAt", baddebtLog.getCreatedAt());
			if (baddebtLog.getCreatedAt() != null) {
				map.put("createdAt", DateFormatUtils.format(baddebtLog.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			}
			map.put("label", baddebtLog.getLabel());
			map.put("actGroup", baddebtLog.getActGroup());
			map.put("appCode", baddebtLog.getAppCode());
			map.put("client", baddebtLog.getClient());
			map.put("imei", baddebtLog.getImei());
			map.put("ip", baddebtLog.getIp());
			map.put("latitude", baddebtLog.getLatitude());
			map.put("longitude", baddebtLog.getLongitude());
			map.put("messageType", baddebtLog.getMessageType());
			
			User user = baddebtLog.getCreatedBy();
			if (user != null) {
				map.put("createdBy", user.getNickname());
			}
			Department department = baddebtLog.getCreatedByDepartment();
			if (department != null) {
				map.put("createdByDepartment", department.getLabel());
			}
			result.add(map);
		}
		return result;
	}
}
