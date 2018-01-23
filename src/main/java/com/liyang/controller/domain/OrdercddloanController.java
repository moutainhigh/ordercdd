package com.liyang.controller.domain;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.liyang.domain.department.Department;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.ordercddloan.Ordercddloan;
import com.liyang.domain.ordercddloan.OrdercddloanRepository;

@RestController
@RequestMapping("/ordercddloan")
public class OrdercddloanController {
	@Resource
	private OrdercddloanRepository repository;
	
	@RequestMapping("/search")
	public EntityPage search(@RequestParam(required=false) Map<String,Object> params) {
		GenericQueryExpSpecification<Ordercddloan> queryExpression = new GenericQueryExpSpecification<Ordercddloan>();
		//状态
		queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"),true));
		//操作人，创建人
		queryExpression.add(QueryExpSpecificationBuilder.eq("createdBy.id", params.get("user_id"),true));
		//所属门店
		if (params.get("department_label")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercdd.createdByDepartment.label",
					params.get("department_label").toString(),true));
		}
		//申请单号
		if (params.get("ordercddloanSn")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercddloanSn", params.get("ordercddloanSn").toString(),true));
		}
		//订单号
		if (params.get("cddSn")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercdd.cddSn", params.get("cddSn").toString(),true));
		}

		//客户名称
		if (params.get("realName")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercdd.realName", params.get("realName").toString(),true));
		}

		Page<Ordercddloan> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
		String[] fields = new String[] {"id","label","createdAt","lastModifiedAt","state",
				"ordercdd","capitalproduct","createdByDepartment","applyAmount","ordercddloanSn","ordercdd"};
		return convertCustom(EntityPageUtil.convert(page, fields));
	}
	
	private EntityPage convertCustom(EntityPage page) {
		List<Map<String,Object>> entities = page.getEntitys();
		if (entities==null) {
			return page;
		}
		EntityPage newPage = page.clone();
		for (Map<String, Object> map : entities) {
			if (map==null) {
				continue;
			}
			//自定义的转换，订单
			Object ordercdd = map.get("ordercdd");
			if (ordercdd instanceof Ordercdd) {
				Ordercdd obj = (Ordercdd) ordercdd;
				if (obj!=null) {
					map.put("ordercdd_cddsn", obj.getCddSn());
					map.put("ordercdd_realName", obj.getRealName());
					map.put("ordercdd_applyAmount", obj.getApplyAmount());
//					map.put("person_id",obj.getPerson().getId());
				}
				map.remove("ordercdd");

				Department store = obj.getCreatedByDepartment();
				String storeName=null;
				if(store != null){
					storeName=store.getLabel();
				}
				map.put("service_department",storeName);
			}
			
			//资金产品
			Object capitalproduct = map.get("capitalproduct");
			if (capitalproduct instanceof Capitalproduct) {
				Capitalproduct obj = (Capitalproduct) capitalproduct;
				map.put("capitalproduct_label", obj.getLabel());
				map.put("capitalproduct_id",obj.getId());
				map.remove("capitalproduct");
			}
			newPage.add(map);
		}
		return newPage;
	}
}
