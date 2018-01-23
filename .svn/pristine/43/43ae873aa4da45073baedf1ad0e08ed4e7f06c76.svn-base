package com.liyang.domain.capitalpayment;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.liyang.domain.base.AbstractWorkflowState;

@Entity
@Table(name = "capitalpayment_state")
@Cacheable
public class CapitalpaymentState extends AbstractWorkflowState<Capitalpayment, CapitalpaymentWorkflow, CapitalpaymentAct> {

	public CapitalpaymentState(String label, Integer sort, String stateCode) {
		super(label, sort, stateCode);
	}

	public CapitalpaymentState() {
		super();
	}

}
