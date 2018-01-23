package com.liyang.domain.loanexception;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.liyang.domain.base.AbstractWorkflowAct;


@Entity
@Table(name="loanexception_act")
@Cacheable
public class LoanexceptionAct extends AbstractWorkflowAct<LoanexceptionState,LoanexceptionWorkflow> {

	public LoanexceptionAct(String label, String actCode, Integer sort, ActGroup actGroup) {
		super(label, actCode, sort,actGroup);
	}
	public LoanexceptionAct(){
		super();
	}
}
