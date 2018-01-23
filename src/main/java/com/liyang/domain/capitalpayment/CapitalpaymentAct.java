package com.liyang.domain.capitalpayment;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.liyang.domain.base.AbstractWorkflowAct;


@Entity
@Table(name="capitalpayment_act")
@Cacheable
public class CapitalpaymentAct extends AbstractWorkflowAct<CapitalpaymentState,CapitalpaymentWorkflow> {

	public CapitalpaymentAct(String label, String actCode, Integer sort, ActGroup actGroup) {
		super(label, actCode, sort,actGroup);
	}
	public CapitalpaymentAct(){
		super();
	}
	
}
