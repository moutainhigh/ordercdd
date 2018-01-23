package com.liyang.controller.domain;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.liyang.Exception.TimeConditiosException;
import com.liyang.util.SearchByTimes;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.domain.creditrepayplan.Creditrepayplan;
import com.liyang.domain.department.Department;
import com.liyang.domain.department.DepartmentRepository;
import com.liyang.domain.department.Departmenttype.DepartmenttypeCode;
import com.liyang.domain.loan.Loan;
import com.liyang.domain.loan.LoanRepository;
import com.liyang.domain.loan.Loanoverdue;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.punishinterestrule.Punishinterestrule;
import com.liyang.util.CommonUtil;

@RestController
@RequestMapping("/loanother")
public class LoanOtherController {
	@Resource
	private LoanRepository repository;
	@Resource
    private DepartmentRepository departmentRepository;
	
	@RequestMapping("/search")
	public EntityPage search(@RequestParam(required=false) Map<String,Object> params) throws TimeConditiosException {
		GenericQueryExpSpecification<Loan> queryExpression = new GenericQueryExpSpecification<Loan>();

		//状态
		queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"),true));
		//操作人
		queryExpression.add(QueryExpSpecificationBuilder.eq("serviceUser.id", params.get("user_id"),true));
		//授权门店，创建人部门
		if (params.get("department_label")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercdd.createdByDepartment.label", params.get("department_label").toString(),true));
		}
		//手机号
		if (params.get("applyMobile")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercdd.applyMobile", params.get("applyMobile").toString(),true));
		}
		//查逾期
		if (params.get("overdue")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.eq("overdue", Boolean.valueOf(params.get("overdue").toString()),true));
		}
		//单号
		if (params.get("loanSn")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("loanSn", params.get("loanSn").toString(),true));
		}
		//客户姓名
		if (params.get("realName")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercdd.realName", params.get("realName").toString(),true));
		}
		//根据时间查询
		if(params.get("timeRangeKey")!=null){
			SearchByTimes.addTimesConditions(params,queryExpression);
		}

		limitField(queryExpression);
		Page<Loan> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
		String[] fields = new String[] {"id","label","createdAt","lastModifiedAt","state",
				"loanSn","principal","ordercdd","punishinterestrule","loanoverdues","creditrepayplans","overdue","loanTime"};
		return convertCustom(EntityPageUtil.convert(page, fields));
	}
	
	private EntityPage convertCustom(EntityPage page) {
		List<Map<String,Object>> entities = page.getEntitys();
		if (entities==null) {
			return page;
		}
//		EntityPage newPage = page.clone();
		for (Map<String, Object> map : entities) {
			if (map==null) {
				continue;
			}
			//自定义的转换，订单
			Object ordercdd = map.get("ordercdd");
			if (ordercdd instanceof Ordercdd) {
				Ordercdd obj = (Ordercdd) ordercdd;
				map.put("ordercdd_realName", obj.getRealName());
				map.put("ordercdd_applyMobile", obj.getApplyMobile());
				map.remove("ordercdd");


				Department store = obj.getCreatedByDepartment();
				String storeName=null;
				if(store != null){
					storeName=store.getLabel();
				}
				map.put("service_department",storeName);
			}
			//逾期罚息率
			Object punishinterestrule = map.get("punishinterestrule");
			if (punishinterestrule instanceof Punishinterestrule) {
				Punishinterestrule obj = (Punishinterestrule) punishinterestrule;
				map.put("punishinterestrule_rate", obj.getRate());
				map.remove("punishinterestrule");
			}
			supplement(map);
//			newPage.add(map);
		}
		return page;
	}
	
	private void supplement(Map<String, Object> map) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Object creditrepayplans = map.get("creditrepayplans");
		map.remove("creditrepayplans");
		BigDecimal planLeftAmount = new BigDecimal("0");
		Date repaymentDate = null;
		if (creditrepayplans != null) {
			Set<Creditrepayplan> set = (Set<Creditrepayplan>) creditrepayplans;
			for (Creditrepayplan creditrepayplan : set) {
				BigDecimal leftAmount = creditrepayplan.getLeftAmount();//剩余应还金额
				String stateCode = creditrepayplan.getState().getStateCode();//状态
				Boolean isOverdue = creditrepayplan.getIsOverdue();//是否逾期
				
				if ("ACCOUNT".equals(stateCode)) {//未出账状态返回
					continue;
				}
				//应还金额
				if ((leftAmount.compareTo(new BigDecimal("0"))>0)//有剩余金额
						&&(isOverdue==null||(!isOverdue))) {//未过期
					planLeftAmount = leftAmount;
				}
				//应还日期
				Date debtorRepaymentDate = creditrepayplan.getDebtorRepaymentDate();//应还日期
				if (repaymentDate == null) {
					repaymentDate = debtorRepaymentDate;
					continue;
				}
				//剩余金额不为0，且已过期
				if ((leftAmount.compareTo(new BigDecimal("0"))>0)//有剩余金额
						&& isOverdue) {//已过期
					repaymentDate = debtorRepaymentDate;
					break;//就是它了
				}
				//正常计划，已出账的最后一个
				if (debtorRepaymentDate.after(repaymentDate)) {
					repaymentDate = debtorRepaymentDate;
				}
			}
		}
		Object loanoverdues = map.get("loanoverdues");
		BigDecimal overdueAmount = new BigDecimal("0");
		//保证数据结构统一性
		map.put("overdueDays", 0);
		map.put("penalSum", 0);
		
		if (loanoverdues != null) {
			Set<Loanoverdue> set = (Set<Loanoverdue>) loanoverdues;
			for (Loanoverdue loanoverdue : set) {
				Integer status = loanoverdue.getStatus();
				if (0==status) {//逾期中
					map.put("overdueDays", loanoverdue.getOverdueDays());
					map.put("penalSum", loanoverdue.getPenalSum());
					overdueAmount = loanoverdue.getOverdueAmount();
				}
			}
			map.remove("loanoverdues");
		}
		map.put("leftAmount", planLeftAmount.add(overdueAmount));
		if (repaymentDate == null) {
			map.put("repaymentDate", "");
		}else {
			map.put("repaymentDate", dateFormat.format(repaymentDate));
		}
	}
	
	/**
     * 限制权限，例如：部门、角色等
     */
    private void limitField(GenericQueryExpSpecification<Loan> queryExpression) {
    	Integer curDepartmentId = CommonUtil.getCurrentUserDepartment().getId();
    	//平台
    	Department company = departmentRepository.findByDepartmenttypeCode(DepartmenttypeCode.COMPANY);
    	//门店，暂时认为非平台的
    	if(curDepartmentId.equals(company.getId())) {//如果登录人部门是平台，只查平台一审和平台二审的
    	}else {//查询其他状态的
			queryExpression.add(QueryExpSpecificationBuilder.eq("ordercdd.createdByDepartment.id", 
					curDepartmentId, true));
		}
	}
}
