package com.liyang.domain.storeadvance;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import com.liyang.domain.base.AbstractWorkflowEntity;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.department.Department;
import com.liyang.domain.loan.Loan;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;

@Entity
@Table(name = "storeadvance")
@Info(label="门店垫付")	
public class Storeadvance extends AbstractWorkflowEntity<StoreadvanceWorkflow, StoreadvanceState, StoreadvanceAct, StoreadvanceLog,StoreadvanceFile>{

	@Transient
	private static AbstractWorkflowService service;

	@Transient
	private static LogRepository logRepository;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="loan_id")
	@Info(label="所属贷款",placeholder="",tip="",help="",secret="")
	@JsonBackReference
	private Loan loan;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="department_id")
	@Info(label="所属门店",placeholder="",tip="",help="",secret="")	
	private Department department;
	
	@Column(name="payable_amount",precision=19,scale=6)
	@Info(label="门店应该垫付的金额",placeholder="",tip="",help="",secret="")	
	private BigDecimal payableAmount;
	
	@Column(name="real_amount",precision=19,scale=6)
	@Info(label="实际到账金额",placeholder="",tip="",help="",secret="")	
	private BigDecimal realAmount;
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
	@Info(label="垫付的类型",placeholder="",tip="",help="",secret="")
	private StoreadvanceType type;
	
	@Column(name="pay_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@Info(label="到账时间",placeholder="",tip="",help="",secret="")
	private Date payTime;
	
	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public BigDecimal getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
	}

	public BigDecimal getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	public StoreadvanceType getType() {
		return type;
	}

	public void setType(StoreadvanceType type) {
		this.type = type;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@JsonIgnore
	@Transient
	public AbstractAuditorService getService() {
		return service;
	}
	
	@Override
	public void setService(AbstractAuditorService service) {
		Storeadvance.service = (AbstractWorkflowService) service;
	}
	
	@JsonIgnore
	@Transient
	@Override
	public StoreadvanceLog getLogInstance() {
		return new StoreadvanceLog();
	}
	
	@JsonIgnore
	@Transient
	@Override
	public LogRepository getLogRepository() {
		return  logRepository;
	}
	
	@Override
	public void setLogRepository(LogRepository logRepository) {
		Storeadvance.logRepository=logRepository;
	}
	
	public enum StoreadvanceType{
		OVERDUE,//逾期垫付（利息）
		BADDEBT//坏账垫付（本金）
	}
}