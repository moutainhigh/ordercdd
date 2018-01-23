package com.liyang.domain.capitalplan;

import com.liyang.domain.base.AbstractWorkflowLog;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="capitalplan_log")
@Cacheable
public class CapitalplanLog extends AbstractWorkflowLog<Capitalplan,CapitalplanWorkflow,CapitalplanState,CapitalplanAct,CapitalplanFile>  {
	
}
