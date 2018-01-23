package com.liyang.domain.baddebt;

import com.liyang.domain.base.AbstractWorkflowLog;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="baddebt_log")
@Cacheable
public class BaddebtLog extends AbstractWorkflowLog<Baddebt,BaddebtWorkflow,BaddebtState,BaddebtAct,BaddebtFile>  {
	
}
