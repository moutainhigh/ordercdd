package com.liyang.domain.loanexception;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.liyang.domain.base.AbstractWorkflow;

@Entity
@Table(name="loanexception_workflow")
@Cacheable
public class LoanexceptionWorkflow extends AbstractWorkflow<Loanexception, LoanexceptionState> {
}
