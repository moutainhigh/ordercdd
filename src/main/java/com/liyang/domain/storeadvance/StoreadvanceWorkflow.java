package com.liyang.domain.storeadvance;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.liyang.domain.base.AbstractWorkflow;

@Entity
@Table(name="storeadvance_workflow")
@Cacheable
public class StoreadvanceWorkflow extends AbstractWorkflow<Storeadvance, StoreadvanceState> {

}
