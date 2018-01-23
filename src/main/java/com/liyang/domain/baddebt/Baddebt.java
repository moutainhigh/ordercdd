package com.liyang.domain.baddebt;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "baddebt")
@Info(label="坏账")	
public class Baddebt extends AbstractWorkflowEntity<BaddebtWorkflow, BaddebtState, BaddebtAct, BaddebtLog,BaddebtFile>{

	@Transient
	private static AbstractWorkflowService service;

	@Transient
	private static LogRepository logRepository;
	
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="loan_id")
	@Info(label="所属贷款",placeholder="",tip="",help="",secret="")
	@JsonBackReference
	private Loan loan;
	
	@Column(name = "debtor_name")
	@Info(label="客户名称",placeholder="",tip="",help="",secret="")
	private String debtorName;
	
	@Column(name = "debtor_mobile")
	@Info(label="客户联系方式",placeholder="",tip="",help="",secret="")
	private String debtorMobile;
	
	@Column(name="debtor_repay_amount",precision=19,scale=2)
	@Info(label="客户已还金额",placeholder="",tip="",help="",secret="")	
	private BigDecimal debtorRepayAmount;
	
	@Column(name="debtor_left_amount",precision=19,scale=2)
	@Info(label="客户剩余应还金额",placeholder="",tip="",help="",secret="")	
	private BigDecimal debtorLeftAmount;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="department_id")
	@Info(label="所属门店",placeholder="",tip="",help="",secret="")	
	private Department department;
	
	@Column(name="store_advance_interest",precision=19,scale=2)
	@Info(label="门店垫付的利息",placeholder="",tip="",help="",secret="")	
	private BigDecimal storeAdvanceInterest;
	
	@Column(name="store_advance_principal",precision=19,scale=2)
	@Info(label="门店垫付的本金",placeholder="",tip="",help="",secret="")	
	private BigDecimal storeAdvancePrincipal;
	
	@Column(name="vehicle_price",precision=19,scale=2)
	@Info(label="卖车的钱",placeholder="",tip="",help="",secret="")	
	private BigDecimal vehiclePrice;
	
	@Column(name="vehicle_expense",precision=19,scale=2)
	@Info(label="卖车的开支",placeholder="",tip="",help="",secret="")	
	private BigDecimal vehicleExpense;
	
	@Column(name="vehicle_actual_amount",precision=19,scale=2)
	@Info(label="实际收到的卖车的钱",placeholder="",tip="",help="",secret="")	
	private BigDecimal vehicleActualAmount;
	
	@Column(name="store_amount",precision=19,scale=2)
	@Info(label="门店分到的钱",placeholder="",tip="",help="",secret="")	
	private BigDecimal storeAmount;
	
	@Column(name="company_amount",precision=19,scale=2)
	@Info(label="平台分到的钱",placeholder="",tip="",help="",secret="")	
	private BigDecimal companyAmount;
	
	@Column(name="store_loss_amount",precision=19,scale=2)
	@Info(label="门店亏损的钱",placeholder="",tip="",help="",secret="")	
	private BigDecimal storeLossAmount;
	
	@Column(name="company_loss_amount",precision=19,scale=2)
	@Info(label="平台亏损的钱",placeholder="",tip="",help="",secret="")	
	private BigDecimal companyLossAmount;
	
	@Column(name="capital_real_amount",precision=19,scale=2)
	@Info(label="实际向资方还款",placeholder="",tip="",help="",secret="")	
	private BigDecimal capitalRealAmount;
	
	@Column(name="comment")
	@Info(label="备注",placeholder="",tip="",help="",secret="")
	private String comment;
	
	@Column(name="finish_at")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@Info(label="结束时间",placeholder="",tip="",help="",secret="")
	private Date finishAt;
	

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	public String getDebtorName() {
		return debtorName;
	}

	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}

	public String getDebtorMobile() {
		return debtorMobile;
	}

	public void setDebtorMobile(String debtorMobile) {
		this.debtorMobile = debtorMobile;
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public BigDecimal getStoreAdvanceInterest() {
		return storeAdvanceInterest;
	}

	public void setStoreAdvanceInterest(BigDecimal storeAdvanceInterest) {
		this.storeAdvanceInterest = storeAdvanceInterest;
	}

	public BigDecimal getStoreAdvancePrincipal() {
		return storeAdvancePrincipal;
	}

	public void setStoreAdvancePrincipal(BigDecimal storeAdvancePrincipal) {
		this.storeAdvancePrincipal = storeAdvancePrincipal;
	}

	public BigDecimal getVehiclePrice() {
		return vehiclePrice;
	}

	public void setVehiclePrice(BigDecimal vehiclePrice) {
		this.vehiclePrice = vehiclePrice;
	}

	public BigDecimal getVehicleExpense() {
		return vehicleExpense;
	}

	public void setVehicleExpense(BigDecimal vehicleExpense) {
		this.vehicleExpense = vehicleExpense;
	}

	public BigDecimal getVehicleActualAmount() {
		return vehicleActualAmount;
	}

	public void setVehicleActualAmount(BigDecimal vehicleActualAmount) {
		this.vehicleActualAmount = vehicleActualAmount;
	}

	public BigDecimal getStoreAmount() {
		return storeAmount;
	}

	public void setStoreAmount(BigDecimal storeAmount) {
		this.storeAmount = storeAmount;
	}

	public BigDecimal getCompanyAmount() {
		return companyAmount;
	}

	public void setCompanyAmount(BigDecimal companyAmount) {
		this.companyAmount = companyAmount;
	}

	public BigDecimal getStoreLossAmount() {
		return storeLossAmount;
	}

	public void setStoreLossAmount(BigDecimal storeLossAmount) {
		this.storeLossAmount = storeLossAmount;
	}

	public BigDecimal getCompanyLossAmount() {
		return companyLossAmount;
	}

	public void setCompanyLossAmount(BigDecimal companyLossAmount) {
		this.companyLossAmount = companyLossAmount;
	}

	public BigDecimal getCapitalRealAmount() {
		return capitalRealAmount;
	}

	public void setCapitalRealAmount(BigDecimal capitalRealAmount) {
		this.capitalRealAmount = capitalRealAmount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getFinishAt() {
		return finishAt;
	}

	public void setFinishAt(Date finishAt) {
		this.finishAt = finishAt;
	}

	@JsonIgnore
	@Transient
	public AbstractAuditorService getService() {
		return service;
	}

	@Override
	public void setService(AbstractAuditorService service) {
		Baddebt.service = (AbstractWorkflowService) service;
	}
	
	@JsonIgnore
	@Transient
	@Override
	public BaddebtLog getLogInstance() {
		return new BaddebtLog();
	}
	
	@JsonIgnore
	@Transient
	@Override
	public LogRepository getLogRepository() {
		return  logRepository;
	}
	
	@Override
	public void setLogRepository(LogRepository logRepository) {
		Baddebt.logRepository=logRepository;
	}
}