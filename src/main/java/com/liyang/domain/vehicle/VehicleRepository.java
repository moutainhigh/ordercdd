package com.liyang.domain.vehicle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.base.WorkflowEntityRepository;

//@RepositoryRestResource(excerptProjection = UserProjection.class)
public interface VehicleRepository extends WorkflowEntityRepository<Vehicle>,SpecificationQueryRepository<Vehicle>{
    Vehicle findVehicleByPlateNumber(String plateNumber);

}
