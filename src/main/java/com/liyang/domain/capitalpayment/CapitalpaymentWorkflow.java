package com.liyang.domain.capitalpayment;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.liyang.domain.base.AbstractWorkflow;

@Entity
@Table(name="capitalpayment_workflow")
@Cacheable
public class CapitalpaymentWorkflow extends AbstractWorkflow<Capitalpayment, CapitalpaymentState> {

}
