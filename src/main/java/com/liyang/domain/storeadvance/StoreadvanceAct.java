package com.liyang.domain.storeadvance;

import com.liyang.domain.base.AbstractWorkflowAct;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="storeadvance_act")
@Cacheable
public class StoreadvanceAct extends AbstractWorkflowAct<StoreadvanceState,StoreadvanceWorkflow> {

	public StoreadvanceAct(String label, String actCode, Integer sort, ActGroup actGroup) {
		super(label, actCode, sort,actGroup);
		// TODO Auto-generated constructor stub
	}
	public StoreadvanceAct(){
		super();
	}
}
