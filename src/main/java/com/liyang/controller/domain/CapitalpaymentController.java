package com.liyang.controller.domain;

import java.util.HashMap;
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
import com.liyang.domain.capitalpayment.Capitalpayment;
import com.liyang.domain.capitalpayment.CapitalpaymentRepository;
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.ordercddloan.Ordercddloan;

@RestController
@RequestMapping("/capitalpayment")
public class CapitalpaymentController {
	@Resource
	private CapitalpaymentRepository repository;
	
	@RequestMapping("/search")
	public EntityPage search(@RequestParam(required=false) Map<String,Object> params) throws TimeConditiosException {
		GenericQueryExpSpecification<Capitalpayment> queryExpression = new GenericQueryExpSpecification<Capitalpayment>();
		//状态
		queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"),true));
		//申请单号
		if (params.get("ordercddloanSn")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercddloan.ordercddloanSn", params.get("ordercddloanSn").toString(),true));
		}
		//订单号
		if (params.get("cddSn")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercdd.cddSn", params.get("cddSn").toString(),true));
		}
		//客户姓名
		if (params.get("realName")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("ordercdd.realName", params.get("realName").toString(),true));
		}
		//资方
		if (params.get("departmentLabel")!=null) {
			queryExpression.add(QueryExpSpecificationBuilder.like("creditorDepartment.label", params.get("departmentLabel").toString(),true));
		}
		//根据时间查询
		if(params.get("timeRangeKey")!=null){
			SearchByTimes.addTimesConditions(params,queryExpression);
		}

		Page<Capitalpayment> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));

		String[] fields = new String[] {"id","label","createdAt","lastModifiedAt","state",
				"ordercdd","capitalproduct","ordercddloan","creditorDepartment","applyAmount","files","paymentTime"};
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
					map.put("person_id",obj.getPerson().getId());

				}
				map.remove("ordercdd");
			}
			
			//资金产品
			Object capitalproduct = map.get("capitalproduct");
			if (capitalproduct instanceof Capitalproduct) {
				Capitalproduct obj = (Capitalproduct) capitalproduct;
				map.put("capitalproduct_label", obj.getLabel());
				map.remove("capitalproduct");
			}
			
			//
			Object ordercddloan = map.get("ordercddloan");
			if (ordercddloan instanceof Ordercddloan) {
				Ordercddloan obj = (Ordercddloan) ordercddloan;
				map.put("ordercddloanSn", obj.getOrdercddloanSn());
				map.remove("ordercddloan");
			}
			
			//自定义的转换，订单
			Object files = map.get("files");
			if (files instanceof Set<?>) {
				Set<?> set = (Set<?>) files;
				if (set==null||(set.size()==0)) {
					map.put("file_length", 0);
				}else {
					map.put("file_length", set.size());
				}
				map.remove("files");
			}
			newPage.add(map);
		}
		return newPage;
	}
}
