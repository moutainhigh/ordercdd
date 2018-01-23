package com.liyang.domain.loanexception;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.liyang.domain.loan.Loan;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;

@Entity
@Table(name = "loanexception")
@Cacheable
@Info(label="异常催收",placeholder="",tip="",help="",secret="")
public class Loanexception extends AbstractWorkflowEntity<LoanexceptionWorkflow, LoanexceptionState, LoanexceptionAct, LoanexceptionLog,LoanexceptionFile>  {
	
	@Transient
	private static AbstractWorkflowService service;
	@Transient
	private static LogRepository logRepository;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="loan_id")
	@Info(label="所属贷款",placeholder="",tip="",help="",secret="")
	@JsonBackReference
	private Loan loan;
	
	@Column(name = "debtor_repay_amount",precision=19,scale=2,nullable = false)
	@Info(label="客户已还金额",placeholder="",tip="",help="",secret="")
	private BigDecimal debtorRepayAmount;
	
	@Column(name = "debtor_left_amount",precision=19,scale=2,nullable = false)
	@Info(label="客户剩余应还金额",placeholder="",tip="",help="",secret="")
	private BigDecimal debtorLeftAmount;
	
	@Column(name="finish_at")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@Info(label="结清时间",placeholder="",tip="",help="",secret="")
	private Date finishAt;
	
	@Column(name="exception_remark")
	@Info(label="备注异常内容",placeholder="",tip="",help="",secret="")
	private String exceptionRemark;
	
	@Column(name="handle_remark")
	@Info(label="处理备注",placeholder="",tip="",help="",secret="")
	private String handleRemark;
	
	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getFinishAt() {
		return finishAt;
	}

	public void setFinishAt(Date finishAt) {
		this.finishAt = finishAt;
	}

	public BigDecimal getDebtorRepayAmount() {
		return debtorRepayAmount;
	}

	public void setDebtorRepayAmount(BigDecimal debtorRepayAmount) {
		this.debtorRepayAmount = debtorRepayAmount;
	}

	public BigDecimal getDebtorLeftAmount() {
		return debtorLeftAmount;
	}

	public void setDebtorLeftAmount(BigDecimal debtorLeftAmount) {
		this.debtorLeftAmount = debtorLeftAmount;
	}

	public String getExceptionRemark() {
		return exceptionRemark;
	}

	public void setExceptionRemark(String exceptionRemark) {
		this.exceptionRemark = exceptionRemark;
	}

	public String getHandleRemark() {
		return handleRemark;
	}

	public void setHandleRemark(String handleRemark) {
		this.handleRemark = handleRemark;
	}

	@Override
	@JsonIgnore
	@Transient
	public LoanexceptionLog getLogInstance() {
		return new LoanexceptionLog();
	}

	@Override
	@JsonIgnore
	@Transient
	public LogRepository getLogRepository() {
		return logRepository;
	}

	@Override
	public void setLogRepository(LogRepository logRepository) {
		Loanexception.logRepository = logRepository;
	}

	@Override
	@JsonIgnore
	@Transient
	public AbstractAuditorService getService() {
		return service;
	}

	@Override
	public void setService(AbstractAuditorService service) {
		Loanexception.service = (AbstractWorkflowService) service;
	}
}
