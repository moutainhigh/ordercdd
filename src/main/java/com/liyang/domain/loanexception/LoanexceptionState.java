package com.liyang.domain.loanexception;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.liyang.domain.base.AbstractWorkflowState;

@Entity
@Table(name = "loanexception_state")
@Cacheable
public class LoanexceptionState extends AbstractWorkflowState<Loanexception, LoanexceptionWorkflow, LoanexceptionAct> {

	public LoanexceptionState(String label, Integer sort, String stateCode) {
		super(label, sort, stateCode);
	}

	public LoanexceptionState() {
		super();
	}
}
