package com.liyang.domain.baddebt;

import com.liyang.domain.base.AbstractWorkflowAct;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="baddebt_act")
@Cacheable
public class BaddebtAct extends AbstractWorkflowAct<BaddebtState,BaddebtWorkflow> {

	public BaddebtAct(String label, String actCode, Integer sort, ActGroup actGroup) {
		super(label, actCode, sort,actGroup);
		// TODO Auto-generated constructor stub
	}
	public BaddebtAct(){
		super();
	}
}
