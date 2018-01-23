package com.liyang.controller.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.liyang.domain.department.Department;
import com.liyang.domain.department.DepartmentRepository;
import com.liyang.domain.department.Departmenttype;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.ordercdd.OrdercddRepository;
import com.liyang.util.CommonUtil;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.domain.person.Person;
import com.liyang.domain.person.PersonRepository;

/**
 * 消费产品
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    @Resource
    private PersonRepository repository;
    @Resource
    private DepartmentRepository departmentRepository;
    @Resource
    private OrdercddRepository ordercddRepository;
    
    @RequestMapping("/search")
    public EntityPage search(@RequestParam(required=false) Map<String,Object> person) {
        GenericQueryExpSpecification<Person> queryExpression = new GenericQueryExpSpecification<Person>();
        //状态
        queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", person.get("state_id"),true));

        //客户姓名
        if (person.get("name")!=null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("name", person.get("name").toString(),true));
        }

        //联系电话
        if (person.get("telephone")!=null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("telephone", person.get("telephone").toString(),true));
        }

        //身份证
        if (person.get("identity")!=null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("identity", person.get("identity").toString(),true));
        }
        limitField(queryExpression);
        Page<Person> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(person));

        String[] fields = new String[] {"id","label","name","telephone","identity","createdAt","lastModifiedAt","state"};
        return EntityPageUtil.convert(page, fields);
    }



    /**
     * @Description 角色权限部门控制等
     * @author jianger
     * @Date 2017/12/19 下午2:08
     **/
    private  void limitField(GenericQueryExpSpecification<Person> queryExpSpecification){
        Integer curDepartmentId= CommonUtil.getCurrentUserDepartment().getId();

        Department department=departmentRepository.findByDepartmenttypeCode(Departmenttype.DepartmenttypeCode.COMPANY);
        //所有拥有这个人的订单都可以查询客户信息
        if(!curDepartmentId.equals(department.getId())){
        	List<Ordercdd> ordercdds = ordercddRepository.findByCreatedByDepartmentId(curDepartmentId);
        	Set<Integer> personIds = new HashSet<>();
        	for (Ordercdd ordercdd : ordercdds) {
				if (ordercdd.getPerson() == null) {
					continue;
				}
				personIds.add(ordercdd.getPerson().getId());
			}
        	queryExpSpecification.add(QueryExpSpecificationBuilder.in("id", personIds, true));
        }
    }
}
