package com.liyang.controller.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.liyang.domain.ordercdd.Ordercdd;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.domain.capitalplan.Capitalplan;
import com.liyang.domain.capitalplan.CapitalplanRepository;
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.ordercddloan.Ordercddloan;

@RestController
@RequestMapping("/capitalplan")
public class CapitalplanController {
    @Resource
    private CapitalplanRepository repository;

    @RequestMapping("/search")
    public EntityPage search(@RequestParam(required = false) Map<String, Object> params) {
        GenericQueryExpSpecification<Capitalplan> queryExpression = new GenericQueryExpSpecification<Capitalplan>();
        //状态
        queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"), true));
        //计划单号
        if (params.get("planSn") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("planSn", params.get("planSn").toString(), true));
        }
        Page<Capitalplan> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
        String[] fields = new String[]{"id", "label", "createdAt", "lastModifiedAt", "state",
                "planSn", "principal", "interest", "fees", "repaymentDate", "extentedRepaymentDate", "ordercddloan", "capitalproduct", "files", "finishTime"};
        return customChange(EntityPageUtil.convert(page, fields));
    }

    private EntityPage customChange(EntityPage page) {
        List<Map<String, Object>> entities = page.getEntitys();
        if (entities == null) {
            return page;
        }
        EntityPage newPage = page.clone();

        for (Map<String, Object> map : entities) {
            if (map == null) {
                continue;
            }
            Object ordercddloan = map.get("ordercddloan");
            map.remove("ordercddloan");
            if (ordercddloan != null) {
                Ordercddloan obj = (Ordercddloan) ordercddloan;
                Ordercdd ordercdd = obj.getOrdercdd();
                if (ordercdd != null) {
//                    if (ordercdd.getPerson() != null && ordercdd.getPerson().getName() != null)
//                        map.put("apply_user_name", ordercdd.getPerson().getName());
                    if (ordercdd.getRealName()!=null)
                        map.put("apply_person_name",ordercdd.getRealName());
                    if (ordercdd.getCreatedByDepartment()!=null&&ordercdd.getCreatedByDepartment().getLabel()!=null){
                        map.put("apply_department_name",ordercdd.getCreatedByDepartment().getLabel());
                    }
                }
                map.put("ordercddloanSn", obj.getOrdercddloanSn());
            }

            Object capitalproduct = map.get("capitalproduct");
            map.remove("capitalproduct");
            if (capitalproduct != null) {
                Capitalproduct obj = (Capitalproduct) capitalproduct;
                map.put("periodNumber", obj.getPeriodNumber());
                map.put("capitalproduct_label", obj.getLabel());
                map.put("creditorDepartment", obj.getCreditorDepartment().getLabel());
                map.put("bank", obj.getCreditorRepaymentBankcard().getBank().getLabel());
                map.put("branchName", obj.getCreditorRepaymentBankcard().getBranchName());
                map.put("accountIdentity", obj.getCreditorRepaymentBankcard().getAccountIdentity());
            }
            Object files = map.get("files");
            map.remove("files");
            if (files != null) {
                Set<?> set = (Set<?>) files;
                if (set == null || (set.size() == 0)) {
                    map.put("file_length", 0);
                } else {
                    map.put("file_length", set.size());
                }
            }
            newPage.add(map);
        }
        return newPage;
    }
}
