package com.liyang.domain.storeadvance;

import com.liyang.domain.base.AbstractWorkflowState;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "storeadvance_state")
@Cacheable
public class StoreadvanceState extends AbstractWorkflowState<Storeadvance, StoreadvanceWorkflow, StoreadvanceAct> {

	public StoreadvanceState(String label, Integer sort, String stateCode) {
		super(label, sort, stateCode);
	}

	public StoreadvanceState() {
		super();
	}

}
