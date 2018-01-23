package com.liyang.domain.storebonus;

import com.liyang.domain.base.AbstractWorkflowLog;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="store_bonus_log")
@Cacheable
public class StoreBonusLog extends AbstractWorkflowLog<StoreBonus,StoreBonusWorkflow,StoreBonusState,StoreBonusAct,StoreBonusFile>  {


	
}
