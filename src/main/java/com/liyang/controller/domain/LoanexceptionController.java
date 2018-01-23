package com.liyang.controller.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.domain.department.Department;
import com.liyang.domain.department.DepartmentRepository;
import com.liyang.domain.department.Departmenttype.DepartmenttypeCode;
import com.liyang.domain.feebackup.Feebackup;
import com.liyang.domain.loan.Loan;
import com.liyang.domain.loan.LoanStateRepository;
import com.liyang.domain.loan.LoanUtil;
import com.liyang.domain.loanexception.Loanexception;
import com.liyang.domain.loanexception.LoanexceptionRepository;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.product.Product;
import com.liyang.util.BeanUtil;
import com.liyang.util.CommonUtil;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject.Level;

@RestController
@RequestMapping("/loanexception")
public class LoanexceptionController {
	@Resource
	private LoanexceptionRepository repository;
	@Resource
	private LoanStateRepository loanStateRepository;
	@Resource
	private DepartmentRepository departmentRepository;
	
	@RequestMapping("/search")
	public EntityPage search(@RequestParam(required=false) Map<String,Object> params) {
		GenericQueryExpSpecification<Loanexception> queryExpression = new GenericQueryExpSpecification<Loanexception>();

		//状态
		queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"),true));
		//借款人姓名
		if (params.get("debtorName")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("loan.debtorName", params.get("debtorName").toString(),true));
		}
		//借款人联系方式
		if (params.get("debtorMobile")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("loan.ordercdd.applyMobile", params.get("debtorMobile").toString(),true));
		}
		//借款单号
		if (params.get("loanSn")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("loan.loanSn", params.get("loanSn").toString(),true));
		}
		limitField(queryExpression);
		Page<Loanexception> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
		String[] fields = new String[] {"id","label","createdAt","lastModifiedAt","state","finishAt","exceptionRemark","loan",
				"debtorRepayAmount","debtorLeftAmount","handleRemark"};

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
			//自定义的转换，订单
			Object loan = map.get("loan");
			map.remove("loan");
			if (loan != null) {
				Loan obj = (Loan) loan;
				map.put("loanId", obj.getId());
				map.put("loanSn", obj.getLoanSn());
				map.put("debtorName", obj.getDebtorName());
				
				Ordercdd ordercdd = obj.getOrdercdd();
				map.put("debtorMobile", ordercdd.getApplyMobile());
				Department department = ordercdd.getCreatedByDepartment();
				if (department != null) {
					map.put("department_label", department.getLabel());
				}
			}
		}
		return page;
	}
	
	/**
     * 限制权限，例如：部门、角色等
     */
    private void limitField(GenericQueryExpSpecification<Loanexception> queryExpression) {
    	Integer curDepartmentId = CommonUtil.getCurrentUserDepartment().getId();
    	//平台
    	Department company = departmentRepository.findByDepartmenttypeCode(DepartmenttypeCode.COMPANY);
    	//门店，暂时认为非平台的
    	if(curDepartmentId.equals(company.getId())) {//如果登录人部门是平台，只查平台一审和平台二审的
    	}else {//查询其他状态的
			queryExpression.add(QueryExpSpecificationBuilder.eq("loan.ordercdd.createdByDepartment.id", 
					curDepartmentId, true));
		}
	}
    
    
    @RequestMapping("/detail")
    public Map<String, Object> detail(@RequestParam Integer id) {
        if (id == null) {
            throw new FailReturnObject(1, "id不能为空", Level.ERROR);
        }
        Loanexception exception = repository.findOne(id);
        if (exception == null) {
            throw new FailReturnObject(2, "ordercdd未找到", Level.ERROR);
        }
        Map<String, Object> result = BeanUtil.beanToMap(exception);
        BeanUtil.commonConvert(result, exception);
        Object loan = result.get("loan");
        result.remove("loan");
        if (loan != null) {
        	Loan obj = (Loan) loan;
        	result.put("loanId", obj.getId());
        	result.put("loanSn", obj.getLoanSn());
        	result.put("debtorName", obj.getDebtorName());
			
			Ordercdd ordercdd = obj.getOrdercdd();
			result.put("debtorMobile", ordercdd.getApplyMobile());
			Department department = ordercdd.getCreatedByDepartment();
			if (department != null) {
				result.put("department_label", department.getLabel());
			}
        }
        Object finishAt = result.get("finishAt");
        if (finishAt != null) {
			Date obj = (Date) finishAt;
			result.put("finishAt", DateFormatUtils.format(obj, "yyyy-MM-dd HH:mm:ss"));
		}
        return result;
    }
}
