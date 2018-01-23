package com.liyang.domain.department;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.department.Departmenttype.DepartmenttypeCode;

//@RepositoryRestResource(excerptProjection = DepartmentProjection.class)
public interface DepartmentRepository extends AuditorEntityRepository<Department>,SpecificationQueryRepository<Department> {
	
    @Query("select d from Department d join d.enterprise e where e.id=:eID")
    Page<Department> findAllByEnterpriseId(@Param("eID") Integer enId, Pageable pageable);
    
    @Query("select d from Department d join d.departmenttype dt where dt.departmenttypeCode ='STORE' and d.province =?1 and d.city=?2 and d.district=?3")
    List<Department> findByProvinceAndCityAndDistrict(@Param(value = "province") String province,@Param(value = "city") String city,@Param(value = "district") String district);
    
    @Query("select d from Department d join d.departmenttype dt where dt.departmenttypeCode =?1")
    Department findByDepartmenttypeCode(DepartmenttypeCode typeCode);
}