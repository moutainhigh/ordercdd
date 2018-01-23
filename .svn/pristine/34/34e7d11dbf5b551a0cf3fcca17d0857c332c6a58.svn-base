package com.liyang.domain.capitalplan;

import com.liyang.domain.base.AbstractWorkflowState;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "capitalplan_state")
@Cacheable
public class CapitalplanState extends AbstractWorkflowState<Capitalplan, CapitalplanWorkflow, CapitalplanAct> {

	public CapitalplanState(String label, Integer sort, String stateCode) {
		super(label, sort, stateCode);
	}

	public CapitalplanState() {
		super();
	}

}
