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
import com.liyang.domain.product.Product;
import com.liyang.domain.product.ProductRepository;

/**
 * 消费产品
 */
@RestController
@RequestMapping("/product")
public class ProductController {
	@Resource
	private ProductRepository repository;
	
	@RequestMapping("/search")
	public EntityPage search(@RequestParam(required=false) Map<String,Object> product) {
		GenericQueryExpSpecification<Product> queryExpression = new GenericQueryExpSpecification<Product>();
		//所属资方
//		queryExpression.add(QueryExpSpecificationBuilder.eq("capitalproduct.creditorDepartment.id", product.get("creditor_department_id"), true));
		//还款方式
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
		//状态
		queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", product.get("state_id"),true));
		//授权门店
		if (product.get("department_label")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("department.label", product.get("department_label").toString(),true));
		}
		//消费产品名称
		if (product.get("label")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("label", product.get("label").toString(),true));
		}
		Page<Product> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(product));
		String[] fields = new String[] {"id","label","createdAt","lastModifiedAt","state","department","capitalproduct","debtorInterest","storeInterest",
				"repaymentMethodCode"};
		return customChange(EntityPageUtil.convert(page, fields));
	}
	
	private EntityPage customChange(EntityPage page) {
		List<Map<String,Object>> entities = page.getEntitys();
		if (entities==null) {
			return page;
		}
		EntityPage newPage = page.clone();
		Map<RepaymentMethodCode,String> repaymentMethodCodeMap = new HashMap<>();
		repaymentMethodCodeMap.put(RepaymentMethodCode.AVERAGE_CAPITAL, "等额本金");
		repaymentMethodCodeMap.put(RepaymentMethodCode.AVERAGE_CAPITAL_PLUS_INTEREST, "等额本息");
		repaymentMethodCodeMap.put(RepaymentMethodCode.BEFORE_INTEREST_AFTER_PRINCIPAL, "先息后本");
		repaymentMethodCodeMap.put(RepaymentMethodCode.LEND_REPAY_ON_DEMAND, "随借随还");
		
		for (Map<String, Object> map : entities) {
			if (map==null) {
				continue;
			}
			//自定义的转换所属资方
			Object creditorDepartment = map.get("capitalproduct");
			if (creditorDepartment instanceof Capitalproduct) {
				Capitalproduct capitalproduct = (Capitalproduct) creditorDepartment;
				Department department = capitalproduct.getCreditorDepartment();
				if (department!=null) {
					map.put("capitalproduct_label", capitalproduct.getLabel());
				}
				map.remove("capitalproduct");
			}
			//还款方式
			Object repaymentMethodCode = map.get("repaymentMethodCode");
			map.put("repaymentMethodCode", repaymentMethodCodeMap.get(repaymentMethodCode));
			newPage.add(map);
		}
		return newPage;
	}
}
