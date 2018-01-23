package com.liyang.domain.vehicle;

import com.liyang.domain.base.AbstractWorkflowFile;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="vehicle_file")
public class VehicleFile extends AbstractWorkflowFile<Vehicle,VehicleAct,VehicleLog>  {


	
}
