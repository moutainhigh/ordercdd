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
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.capitalproduct.Capitalproduct.RepaymentMethodCode;
import com.liyang.domain.department.Department;
import com.liyang.domain.capitalproduct.CapitalproductRepository;

/**
 * 资金产品
 */
@RestController
@RequestMapping("/capitalproduct")
public class CapitalproductController {
	@Resource
	private CapitalproductRepository repository;

	@RequestMapping("/search")
	public EntityPage search(@RequestParam(required=false) Map<String,Object> product) {
		GenericQueryExpSpecification<Capitalproduct> queryExpression = new GenericQueryExpSpecification<Capitalproduct>();
		queryExpression.add(QueryExpSpecificationBuilder.eq("creditorDepartment.id", product.get("creditor_department_id"), true));
		String methodCode = String.valueOf(product.get("repayment_method_code"));
		if (RepaymentMethodCode.AVERAGE_CAPITAL.toString().equals(methodCode)) {
			queryExpression.add(QueryExpSpecificationBuilder.eq("repaymentMethodCode", RepaymentMethodCode.AVERAGE_CAPITAL,true));
		}
		if (RepaymentMethodCode.AVERAGE_CAPITAL_PLUS_INTEREST.toString().equals(methodCode)) {
			queryExpression.add(QueryExpSpecificationBuilder.eq("repaymentMethodCode", RepaymentMethodCode.AVERAGE_CAPITAL_PLUS_INTEREST,true));
		}
		if (RepaymentMethodCode.BEFORE_INTEREST_AFTER_PRINCIPAL.toString().equals(methodCode)) {
			queryExpression.add(QueryExpSpecificationBuilder.eq("repaymentMethodCode", RepaymentMethodCode.BEFORE_INTEREST_AFTER_PRINCIPAL,true));
		}
		if (RepaymentMethodCode.LEND_REPAY_ON_DEMAND.toString().equals(methodCode)) {
			queryExpression.add(QueryExpSpecificationBuilder.eq("repaymentMethodCode", RepaymentMethodCode.LEND_REPAY_ON_DEMAND,true));
		}
		queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", product.get("state_id"),true));
		if (product.get("label")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("label", product.get("label").toString(),true));
		}
		Page<Capitalproduct> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(product));
		String[] fields = new String[] {"id","label","createdAt","lastModifiedAt","creditorDepartment","state","creditorInterest","repaymentMethodCode"};
		return EntityPageUtil.convert(page, fields);
	}
}
