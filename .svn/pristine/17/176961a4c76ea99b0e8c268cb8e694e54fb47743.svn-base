package com.liyang.domain.baddebt;

import com.liyang.domain.base.AbstractWorkflowState;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "baddebt_state")
@Cacheable
public class BaddebtState extends AbstractWorkflowState<Baddebt, BaddebtWorkflow, BaddebtAct> {

	public BaddebtState(String label, Integer sort, String stateCode) {
		super(label, sort, stateCode);
	}

	public BaddebtState() {
		super();
	}

}
