package com.liyang.domain.ordercddloan;

import java.math.BigDecimal;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import com.liyang.domain.base.AbstractWorkflowEntity;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;

@Entity
@Table(name = "ordercddloan")
@Cacheable
@Info(label="资方贷款审核",secret="哒哒车服")
public class Ordercddloan extends AbstractWorkflowEntity<OrdercddloanWorkflow, OrdercddloanState, OrdercddloanAct, OrdercddloanLog,OrdercddloanFile> implements Cloneable{

	@Transient
	private static AbstractWorkflowService service;
	@Transient
	private static LogRepository logRepository;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ordercdd_id")
	@Info(label="订单",placeholder="",tip="",help="",secret="")
	private Ordercdd ordercdd;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="capitalproduct_id")
	@Info(label="资金产品",placeholder="",tip="",help="",secret="")
	private Capitalproduct capitalproduct;
	
	@Column(name = "ordercddloan_sn",length = 16,unique = true)//生成的单号
	@Info(label="申请单号",placeholder="",tip="",help="",secret="")
	private String ordercddloanSn;
	
	@Column(name = "apply_amount")
	@Info(label="申请金额",placeholder="",tip="",help="",secret="")
	private BigDecimal applyAmount;

	//部分驳回所取
	@Transient
	private BigDecimal acceptAmount;

	public BigDecimal getAcceptAmount() {
		return acceptAmount;
	}

	public void setAcceptAmount(BigDecimal acceptAmount) {
		this.acceptAmount = acceptAmount;
	}

	public Ordercdd getOrdercdd() {
		return ordercdd;
	}

	public void setOrdercdd(Ordercdd ordercdd) {
		this.ordercdd = ordercdd;
	}

	public Capitalproduct getCapitalproduct() {
		return capitalproduct;
	}

	public void setCapitalproduct(Capitalproduct capitalproduct) {
		this.capitalproduct = capitalproduct;
	}

	public String getOrdercddloanSn() {
		return ordercddloanSn;
	}

	public void setOrdercddloanSn(String ordercddloanSn) {
		this.ordercddloanSn = ordercddloanSn;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	@Override
	@JsonIgnore
	public AbstractAuditorService<?, OrdercddloanState, OrdercddloanAct, OrdercddloanLog> getService() {
		return service;
	}

	@Override
	public void setService(AbstractAuditorService service) {
		Ordercddloan.service=(AbstractWorkflowService)service;
	}
	@Override
	@JsonIgnore
	public OrdercddloanLog getLogInstance() {
		return new OrdercddloanLog();
	}

	@Override
	@JsonIgnore
	public LogRepository getLogRepository() {
		return logRepository;
	}

	@Override
	public void setLogRepository(LogRepository logRepository) {
		Ordercddloan.logRepository=logRepository;
	}

	//实现深克隆
	@Override
	public Object clone() throws CloneNotSupportedException {
		//克隆Employee对象并手动的进一步克隆Employee对象中包含的Employer对象
//		Employee employee = (Employee)super.clone();
		Ordercddloan ordercddloan=(Ordercddloan)super.clone();
//		employee.setEmployer((Employer) employee.getEmployer().clone());
		ordercddloan.setState((OrdercddloanState)ordercddloan.getState().clone());

		return ordercddloan;
	}
}