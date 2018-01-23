package com.liyang.domain.storebonus;

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
import com.liyang.domain.department.Department;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.user.User;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;

@Entity
@Table(name = "store_bonus")
@Cacheable
@Info(label="门店分红表",secret="门店分红表")
public class StoreBonus extends AbstractWorkflowEntity<StoreBonusWorkflow, StoreBonusState, StoreBonusAct, StoreBonusLog,StoreBonusFile> {

	@Transient
	private static AbstractWorkflowService service;
	@Transient
	private static LogRepository logRepository;

	@JoinColumn(name = "store_manager_id")
	@ManyToOne
	@Info(label="门店负责人",placeholder="",tip="",help="",secret="")
	private User user;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "department_id")
	@Info(label="所属门店",placeholder="",tip="所属门店",help="",secret="")
	private Department department;
	
	@Column(name = "reality_amount")
	@Info(label = "实际分红金额")
	private BigDecimal realityAmount;
	
	@Column(name = "storebonus_sn")
	@Info(label = "单号")
	private String storebonusSn;
	
	@OneToOne
	@JoinColumn(name = "ordercdd_id")
	@Info(label = "所属订单")
	private Ordercdd ordercdd;
	
	@Column(name = "bonus_time")
	@Info(label = "分红确认时间")
	private Date bonusTime;
	
	@Column(name = "loan_amount")
	@Info(label = "平台向门店放款金额")
	private BigDecimal loanAmount;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public BigDecimal getRealityAmount() {
		return realityAmount;
	}

	public void setRealityAmount(BigDecimal realityAmount) {
		this.realityAmount = realityAmount;
	}

	public Ordercdd getOrdercdd() {
		return ordercdd;
	}

	public void setOrdercdd(Ordercdd ordercdd) {
		this.ordercdd = ordercdd;
	}

	public String getStorebonusSn() {
		return storebonusSn;
	}

	public void setStorebonusSn(String storebonusSn) {
		this.storebonusSn = storebonusSn;
	}

	public Date getBonusTime() {
		return bonusTime;
	}

	public void setBonusTime(Date bonusTime) {
		this.bonusTime = bonusTime;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	@Override
	@JsonIgnore
	public AbstractAuditorService<?, StoreBonusState, StoreBonusAct, StoreBonusLog> getService() {
		return service;
	}

	@Override
	public void setService(AbstractAuditorService service) {
		StoreBonus.service=(AbstractWorkflowService)service;
	}
	@Override
	@JsonIgnore
	public StoreBonusLog getLogInstance() {
		return new StoreBonusLog();
	}

	@Override
	@JsonIgnore
	public LogRepository getLogRepository() {
		return logRepository;
	}

	@Override
	public void setLogRepository(LogRepository logRepository) {
		StoreBonus.logRepository=logRepository;
	}
}