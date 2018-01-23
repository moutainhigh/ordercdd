package com.liyang.domain.account;

import com.liyang.domain.base.AbstractWorkflowState;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "account_state")
@Cacheable
public class AccountState extends AbstractWorkflowState<Account, AccountWorkflow, AccountAct> {

	public AccountState(String label, Integer sort, String stateCode) {
		super(label, sort, stateCode);
		// TODO Auto-generated constructor stub
	}

	public AccountState() {
		super();
	}

}
