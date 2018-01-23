package com.liyang.controller.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.liyang.domain.department.Department;
import com.liyang.domain.department.DepartmentRepository;
import com.liyang.domain.department.DepartmentState;
import com.liyang.domain.department.Departmenttype.DepartmenttypeCode;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.ordercdd.OrdercddRepository;
import com.liyang.domain.user.User;
import com.liyang.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.query.expression.QueryExpSpecificationBuilder;
import com.jpa.query.expression.generic.GenericQueryExpSpecification;
import com.liyang.domain.vehicle.Vehicle;
import com.liyang.domain.vehicle.VehicleRepository;
import com.liyang.domain.person.Person;

/**
 * 部门
 *
 * @author Jh
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Resource
    private VehicleRepository repository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Resource
    private DepartmentRepository departmentRepository;
    @Resource
    private OrdercddRepository ordercddRepository;

    @RequestMapping("/search")
    public EntityPage search(@RequestParam(required = false) Map<String, Object> params) {
        GenericQueryExpSpecification<Vehicle> queryExpression = new GenericQueryExpSpecification<Vehicle>();

        queryExpression.add(QueryExpSpecificationBuilder.eq("state.id", params.get("state_id"), true));

        //车牌号搜索
        if (params.get("plate_number") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("plateNumber", params.get("plate_number").toString(), true));
        }

        //拥有人搜索
        if (params.get("person_name") != null) {
            queryExpression.add(QueryExpSpecificationBuilder.like("person.name", params.get("person_name").toString(), true));
        }
        limitField(queryExpression);
        Page<Vehicle> page = repository.findAll(queryExpression, EntityPageUtil.createPageable(params));
        String[] fields = new String[]{"id", "label", "plateNumber", "vehicleModel", "annualSurveyMaturity", "vehicleDate", "annualPolicyMaturity", "vehicleKm", "vehicleProvince", "vehicleCity", "GPSCode", "registerRegionName", "lastModifiedAt", "createdAt", "state", "person","gpsStatus"};
        return EntityPageUtil.convert(page, fields);
    }
    /**
     * 限制权限，例如：部门、角色等
     */
    private void limitField(GenericQueryExpSpecification<Vehicle> queryExpression) {
        Integer curDepartmentId = CommonUtil.getCurrentUserDepartment().getId();
        //平台
        Department company = departmentRepository.findByDepartmenttypeCode(DepartmenttypeCode.COMPANY);
        //门店，暂时认为非平台的
        if (curDepartmentId.equals(company.getId())) {//如果登录人部门是平台，只查平台一审和平台二审的
        } else {//所有拥有这个人的订单都可以查询客户信息
        	List<Ordercdd> ordercdds = ordercddRepository.findByCreatedByDepartmentId(curDepartmentId);
        	Set<Integer> personIds = new HashSet<>();
        	for (Ordercdd ordercdd : ordercdds) {
				if (ordercdd.getPerson() == null) {
					continue;
				}
				personIds.add(ordercdd.getPerson().getId());
			}
            queryExpression.add(QueryExpSpecificationBuilder.in("person.id", personIds, true));
        }
    }


    @GetMapping("/getGpsStatusLogByID")
    public List<Map<String,Object>> getGpsStatusLogByID(@RequestParam(name ="vehicleId" ) Integer id) {
//        return jdbcTemplate.queryForList("SELECT difference FROM vehicle_log WHERE difference->>'$.gpsStatus' IS NOT NULL AND entity_id = ?", new Object[]{id});
        User user = CommonUtil.getPrincipal();
        if(user==null){
            return null;
        }
        StringBuffer dptID=new StringBuffer("0");
        user.childrenDepartments.forEach(d->{
            dptID.append(",").append(d.getId());
        });
        return jdbcTemplate.queryForList("SELECT difference->'$.gpsStatus' as gpsStatus,vlog.created_at,u.nickname FROM vehicle_log vlog left join `user` u ON vlog.created_by=u.id WHERE difference->>'$.gpsStatus' IS NOT NULL AND entity_id = ? AND vlog.created_by_department IN ("+dptID+")", new Object[]{id});
    }
}
