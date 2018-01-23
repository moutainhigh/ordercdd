package com.liyang.domain.capitalpayment;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import com.liyang.domain.base.AbstractWorkflowEntity;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.department.Department;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.ordercddloan.Ordercddloan;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;

@Entity
@Table(name = "capitalpayment")
@Cacheable
@Info(label="资方放款",secret="")
public class Capitalpayment extends AbstractWorkflowEntity<CapitalpaymentWorkflow, CapitalpaymentState, CapitalpaymentAct, CapitalpaymentLog,CapitalpaymentFile> {

	@Transient
	private static AbstractWorkflowService service;
	@Transient
	private static LogRepository logRepository;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ordercdd_id")
	@Info(label="订单",placeholder="",tip="",help="",secret="")
	private Ordercdd ordercdd;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ordercddloan_id")
	@Info(label="申请资方实体",placeholder="",tip="",help="",secret="")
	private Ordercddloan ordercddloan;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="capitalproduct_id")
	@Info(label="资金产品",placeholder="",tip="",help="",secret="")
	private Capitalproduct capitalproduct;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="creditor_department_id")
	@Info(label="所属资方",placeholder="",tip="",help="",secret="")
	private Department creditorDepartment;
	
	@Column(name = "apply_amount")
	@Info(label="放款金额",placeholder="",tip="",help="",secret="")
	private BigDecimal applyAmount;
	
	@Column(name = "payment_time")
	@Info(label = "回款确认时间")
	private Date paymentTime;

	public Capitalproduct getCapitalproduct() {
		return capitalproduct;
	}

	public void setCapitalproduct(Capitalproduct capitalproduct) {
		this.capitalproduct = capitalproduct;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	public Ordercddloan getOrdercddloan() {
		return ordercddloan;
	}

	public void setOrdercddloan(Ordercddloan ordercddloan) {
		this.ordercddloan = ordercddloan;
	}

	public Department getCreditorDepartment() {
		return creditorDepartment;
	}

	public void setCreditorDepartment(Department creditorDepartment) {
		this.creditorDepartment = creditorDepartment;
	}

	public Ordercdd getOrdercdd() {
		return ordercdd;
	}

	public void setOrdercdd(Ordercdd ordercdd) {
		this.ordercdd = ordercdd;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	@Override
	@JsonIgnore
	public AbstractAuditorService<?, CapitalpaymentState, CapitalpaymentAct, CapitalpaymentLog> getService() {
		return service;
	}

	@Override
	public void setService(AbstractAuditorService service) {
		Capitalpayment.service=(AbstractWorkflowService)service;
	}
	@Override
	@JsonIgnore
	public CapitalpaymentLog getLogInstance() {
		return new CapitalpaymentLog();
	}

	@Override
	@JsonIgnore
	public LogRepository getLogRepository() {
		return logRepository;
	}

	@Override
	public void setLogRepository(LogRepository logRepository) {
		Capitalpayment.logRepository=logRepository;
	}
}