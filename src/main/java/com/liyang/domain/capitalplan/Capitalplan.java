package com.liyang.domain.capitalplan;

import java.math.BigDecimal;
import java.util.Date;

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
import com.liyang.domain.capitalpayment.Capitalpayment;
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.ordercddloan.Ordercddloan;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;

@Entity
@Table(name = "capitalplan")
@Info(label="资方还款计划")	
public class Capitalplan extends AbstractWorkflowEntity<CapitalplanWorkflow, CapitalplanState, CapitalplanAct, CapitalplanLog,CapitalplanFile>{

	@Transient
	private static AbstractWorkflowService service;

	@Transient
	private static LogRepository logRepository;
	
	@Column(name="principal",precision=19,scale=6)
	@Info(label="本金",placeholder="",tip="",help="",secret="")	
	private BigDecimal principal;
	
	@Column(name="interest",precision=19,scale=6)
	@Info(label="利息",placeholder="",tip="",help="",secret="")	
	private BigDecimal interest;

	@Column(name="fees",precision=19,scale=6)
	@Info(label="各种费用总和",placeholder="",tip="",help="",secret="")	
	private BigDecimal fees;

	@Column(name="repayment_date")
	@Info(label="正常还款日期",placeholder="",tip="",help="",secret="")
	private Date repaymentDate;
	
	@Column(name="extented_repayment_date")
	@Info(label="宽限还款日期",placeholder="",tip="",help="",secret="时间上大于正常还款日期，该日期以内还款免逾期")
	private Date extentedRepaymentDate;
	
	@Column(name = "plan_sn")//还款计划的单号
	@Info(label="还款计划的单号",placeholder="",tip="",help="",secret="")
	private String planSn;
	
	@JoinColumn(name = "capitalproduct_id")
	@ManyToOne(fetch=FetchType.LAZY)
	@Info(label="资金产品",placeholder="",tip="",help="",secret="")
	private Capitalproduct capitalproduct;
	
	@JoinColumn(name = "capitalpayment_id")
	@ManyToOne(fetch=FetchType.LAZY)
	@Info(label="资方放款记录",placeholder="",tip="",help="",secret="")
	private Capitalpayment capitalpayment;
	
	@JoinColumn(name = "ordercddloan_id")
	@ManyToOne(fetch=FetchType.LAZY)
	@Info(label="资方申请",placeholder="",tip="",help="",secret="")
	private Ordercddloan ordercddloan;
	
	@Column(name="finish_time")
	@Info(label="计划结清时间",placeholder="",tip="",help="",secret="")
	private Date finishTime;
	
	
	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getFees() {
		return fees;
	}

	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}

	public Date getRepaymentDate() {
		return repaymentDate;
	}

	public void setRepaymentDate(Date repaymentDate) {
		this.repaymentDate = repaymentDate;
	}

	public Date getExtentedRepaymentDate() {
		return extentedRepaymentDate;
	}

	public void setExtentedRepaymentDate(Date extentedRepaymentDate) {
		this.extentedRepaymentDate = extentedRepaymentDate;
	}

	public String getPlanSn() {
		return planSn;
	}

	public void setPlanSn(String planSn) {
		this.planSn = planSn;
	}

	public Capitalproduct getCapitalproduct() {
		return capitalproduct;
	}

	public void setCapitalproduct(Capitalproduct capitalproduct) {
		this.capitalproduct = capitalproduct;
	}

	public Capitalpayment getCapitalpayment() {
		return capitalpayment;
	}

	public void setCapitalpayment(Capitalpayment capitalpayment) {
		this.capitalpayment = capitalpayment;
	}

	public Ordercddloan getOrdercddloan() {
		return ordercddloan;
	}

	public void setOrdercddloan(Ordercddloan ordercddloan) {
		this.ordercddloan = ordercddloan;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@JsonIgnore
	@Transient
	public AbstractAuditorService getService() {
		return service;
	}

	@Override
	public void setService(AbstractAuditorService service) {
		Capitalplan.service = (AbstractWorkflowService) service;
	}
	
	@JsonIgnore
	@Transient
	@Override
	public CapitalplanLog getLogInstance() {
		return new CapitalplanLog();
	}
	
	@JsonIgnore
	@Transient
	@Override
	public LogRepository getLogRepository() {
		return  logRepository;
	}
	
	@Override
	public void setLogRepository(LogRepository logRepository) {
		Capitalplan.logRepository=logRepository;
	}
}