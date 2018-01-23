package com.liyang.domain.capitalplan;

import com.liyang.domain.base.AbstractWorkflowAct;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="capitalplan_act")
@Cacheable
public class CapitalplanAct extends AbstractWorkflowAct<CapitalplanState,CapitalplanWorkflow> {

	public CapitalplanAct(String label, String actCode, Integer sort, ActGroup actGroup) {
		super(label, actCode, sort,actGroup);
		// TODO Auto-generated constructor stub
	}
	public CapitalplanAct(){
		super();
	}
}
