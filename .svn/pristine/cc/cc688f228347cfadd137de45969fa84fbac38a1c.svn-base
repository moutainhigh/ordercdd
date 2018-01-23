package com.liyang.domain.loan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import com.liyang.domain.bankcard.Bankcard;
import com.liyang.domain.base.AbstractWorkflowEntity;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.capitalproduct.Capitalproduct.RepaymentMethodCode;
import com.liyang.domain.creditcard.Creditcard;
import com.liyang.domain.creditrepayment.Creditrepayment;
import com.liyang.domain.creditrepayplan.Creditrepayplan;
import com.liyang.domain.enterprise.Enterprise;
//import com.liyang.domain.orderwdsjsh.Orderwdsjsh.PeriodCode;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.person.Person;
import com.liyang.domain.punishinterestrule.Punishinterestrule;
import com.liyang.domain.user.User;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;

@Entity
@Table(name = "loan")
@Cacheable
@Info(label="贷款",placeholder="",tip="",help="",secret="")
public class Loan extends AbstractWorkflowEntity<LoanWorkflow, LoanState, LoanAct, LoanLog,LoanFile> {
	@Transient
	private static AbstractWorkflowService service;
	@Transient
	private static ActRepository actRepository;
	

	@JoinColumn(name="creditcard_id")
	@ManyToOne
	@Info(label="所属授信账户",placeholder="",tip="",help="",secret="")
	private Creditcard creditcard;


	@Enumerated(EnumType.STRING)
	@Column(name="repayment_method_code")
	@Info(label="还款方式",placeholder="",tip="",help="",secret="")
	private RepaymentMethodCode repaymentMethodCode;
	
	
	@Column(name = "principal",precision=19,scale=6,nullable = false)
	@Info(label="贷款本金",placeholder="",tip="",help="",secret="")
	private BigDecimal principal;

	@Column(name="period_code")
	@Enumerated(EnumType.STRING)
	@Info(label="贷款分期时间单位",placeholder="",tip="",help="",secret="")
	private PeriodCode periodCode;
	//	private PeriodCode periodCode;

	@Column(name="period_number",nullable = false)
	@Info(label="贷款分期期数",placeholder="",tip="",help="",secret="")
	private Integer periodNumber;

	@Column(name="debtor_interest",precision=19,scale=6,nullable = false)
	@Info(label="借方利率",placeholder="",tip="",help="product来",secret="")
	private BigDecimal debtorInterest;
	
	@Column(name="store_interest",precision=19,scale=6)
	@Info(label="给门店利率",placeholder="",tip="",help="product来",secret="")
	private BigDecimal storeInterest;
	
	@Column(name="creditor_interest",precision=19,scale=6)
	@Info(label="资方供应利率",placeholder="",tip="",help="capitalproduct来",secret="")
	private BigDecimal creditorInterest;
	
	@JoinColumn(name="debtor_receive_bankcard_id")
	@ManyToOne
	@Info(label="借方收款账户",placeholder="",tip="",help="资方汇款给这个账户",secret="")
	private Bankcard debtorReceiveBankcard;
	
	
	@Column(name="creditor_repayment_day")
	@Info(label="还给资方的日期",placeholder="",tip="",help="",secret="")
	private Integer creditorRepaymentDay;
	
	@Column(name="debtor_repayment_day")
	@Info(label="借方还款的时间",placeholder="",tip="",help="",secret="")	
	private Integer debtorRepaymentDay;

	@Column(name="debtor_extented_repayment_days")
	@Info(label="借方还款宽限日期数",placeholder="",tip="",help="",secret="注意是宽限的日期数量，即在还款日当天加上这个数值是宽限数")	
	private Integer debtorExtentedRepaymentDays;	
	
	@Lob
	@Info(label="说明",placeholder="",tip="",help="",secret="")	
	private String description;

	@Lob
	@Info(label="内部备注",placeholder="",tip="",help="",secret="")	
	private String comment;

	@JoinColumn(name="punishinterestrule_id")
	@ManyToOne
	@Info(label="罚息规则",placeholder="",tip="",help="",secret="罚息规则那边，一旦创建，无法变更！")
	private Punishinterestrule punishinterestrule;

	@ManyToOne
	@JoinColumn(name="debtor_user_id")
	@Info(label="贷款人",placeholder="",tip="",help="",secret="")	
	private User debtorUser;//借方执行人

	@ManyToOne
	@JoinColumn(name="debtor_person_id")
	@Info(label="贷款自然人（个人）",placeholder="",tip="",help="",secret="")	
	private Person debtorPerson;
	
	@ManyToOne
	@JoinColumn(name="debtor_enterprise_id")
	@Info(label="贷款法人（企业）",placeholder="",tip="",help="",secret="")	
	private Enterprise debtorEnterprise;

	@OneToMany(mappedBy = "loan")
	@JsonIgnore
	private Set<Creditrepayplan> creditrepayplans;
	
	@OneToMany(mappedBy = "loan")
	@JsonIgnore
	private Set<Creditrepayment> creditrepayments;
	
	@Lob
	@Info(label="其它相关信息",placeholder="",tip="",help="例如第三方还款返回的信息，JSON序列化存储",secret="")
	private String information;
//	private String beidou;

	@Column(name = "creditor_loan_sn")
	@Info(label="资方贷款单号",placeholder="",tip="",help="",secret="")
	private String creditorLoanSn;


	@Column(name = "service_user_name")
	@Info(label="业务员姓名",placeholder="",tip="",help="",secret="")
	private String serviceUserName;

	@ManyToOne
	@JoinColumn(name="service_user_id")
	@Info(label = "业务员id",placeholder = "",tip = "",secret = "")
	private User serviceUser;
	
	@Column(name = "loan_sn",length = 16,unique = true)//生成的单号
	@Info(label="借款单号",placeholder="",tip="",help="",secret="")
	private String loanSn;


	@Column(name = "overdue")
	@Info(label="逾期",placeholder="",tip="",help="",secret="")
	private Boolean overdue;
	
	@OneToOne
	@JoinColumn(name = "ordercdd_id")
	private Ordercdd ordercdd;
	
	@Info(label = "借款人姓名")
	@Column(name = "debtor_name")
	private String debtorName;
	
	@Column(name = "debtor_reality_amount")
	@Info(label = "向客户放款的金额")
	private BigDecimal debtorRealityAmount;
	@Column(name = "one_time_fee")
	@Info(label = "一次性收费")
	private BigDecimal oneTimeFee;
	@Column(name = "retreat_fee")
	@Info(label = "可退收费")
	private BigDecimal retreatFee;
	
	@Column(name = "loan_time")
	@Info(label = "放贷时间")
	private Date loanTime;
	
	@Column(name = "is_exception")
	@Info(label = "是否异常")
	private Boolean isException;
	
	@JsonIgnore
	@OneToMany(mappedBy="loan")
	@Info(label = "逾期信息记录")
	private Set<Loanoverdue> loanoverdues;
	
/***************************临时字段****************************/
	@Transient
	@Enumerated(EnumType.STRING)
	private LoanType loanType;
	
	@Transient
	private String exceptionRemark;//备注异常内容
	
	@Transient
	private String handleRemark;//处理备注
/***************************临时字段****************************/
	public BigDecimal getRetreatFee() {
		return retreatFee;
	}

	public void setRetreatFee(BigDecimal retreatFee) {
		this.retreatFee = retreatFee;
	}

	public Boolean getOverdue() {
		return overdue;
	}

	public void setOverdue(Boolean overdue) {
		this.overdue = overdue;
	}

	public BigDecimal getOneTimeFee() {
		return oneTimeFee;
	}

	public void setOneTimeFee(BigDecimal oneTimeFee) {
		this.oneTimeFee = oneTimeFee;
	}

	public BigDecimal getDebtorRealityAmount() {
		return debtorRealityAmount;
	}

	public void setDebtorRealityAmount(BigDecimal debtorRealityAmount) {
		this.debtorRealityAmount = debtorRealityAmount;
	}

	public String getDebtorName() {
		return debtorName;
	}

	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}

	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}

	public String getLoanSn() {
		return loanSn;
	}

	public void setLoanSn(String loanSn) {
		this.loanSn = loanSn;
	}

	public User getServiceUser() {
		return serviceUser;
	}

	public void setServiceUser(User serviceUser) {
		this.serviceUser = serviceUser;
	}

	@Transient
	private static LogRepository logRepository;

	public Ordercdd getOrdercdd() {
		return ordercdd;
	}

	public void setOrdercdd(Ordercdd ordercdd) {
		this.ordercdd = ordercdd;
	}

	public Punishinterestrule getPunishinterestrule() {
		return punishinterestrule;
	}

	public void setPunishinterestrule(Punishinterestrule punishinterestrule) {
		this.punishinterestrule = punishinterestrule;
	}


	public RepaymentMethodCode getRepaymentMethodCode() {
		return repaymentMethodCode;
	}


	public void setRepaymentMethodCode(RepaymentMethodCode repaymentMethodCode) {
		this.repaymentMethodCode = repaymentMethodCode;
	}


	public Set<Creditrepayplan> getCreditrepayplans() {
		return creditrepayplans;
	}

	public PeriodCode getPeriodCode() {
		return periodCode;
	}

	public void setPeriodCode(PeriodCode periodCode) {
		this.periodCode = periodCode;
	}




	public Integer getPeriodNumber() {
		return periodNumber;
	}

	public void setPeriodNumber(Integer periodNumber) {
		this.periodNumber = periodNumber;
	}

	public BigDecimal getDebtorInterest() {
		return debtorInterest;
	}

	public void setDebtorInterest(BigDecimal debtorInterest) {
		this.debtorInterest = debtorInterest;
	}

	public BigDecimal getStoreInterest() {
		return storeInterest;
	}

	public void setStoreInterest(BigDecimal storeInterest) {
		this.storeInterest = storeInterest;
	}


	public BigDecimal getCreditorInterest() {
		return creditorInterest;
	}

	public void setCreditorInterest(BigDecimal creditorInterest) {
		this.creditorInterest = creditorInterest;
	}






	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getDebtorUser() {
		return debtorUser;
	}

	public void setDebtorUser(User debtorUser) {
		this.debtorUser = debtorUser;
	}

	public Person getDebtorPerson() {
		return debtorPerson;
	}

	public void setDebtorPerson(Person debtorPerson) {
		this.debtorPerson = debtorPerson;
	}

	public Enterprise getDebtorEnterprise() {
		return debtorEnterprise;
	}

	public void setDebtorEnterprise(Enterprise debtorEnterprise) {
		this.debtorEnterprise = debtorEnterprise;
	}


	public Creditcard getCreditcard() {
		return creditcard;
	}

	public void setCreditcard(Creditcard creditcard) {
		this.creditcard = creditcard;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public Bankcard getDebtorReceiveBankcard() {
		return debtorReceiveBankcard;
	}

	public void setDebtorReceiveBankcard(Bankcard debtorReceiveBankcard) {
		this.debtorReceiveBankcard = debtorReceiveBankcard;
	}

	public Integer getCreditorRepaymentDay() {
		return creditorRepaymentDay;
	}

	public void setCreditorRepaymentDay(Integer creditorRepaymentDay) {
		this.creditorRepaymentDay = creditorRepaymentDay;
	}

	public Integer getDebtorRepaymentDay() {
		return debtorRepaymentDay;
	}

	public void setDebtorRepaymentDay(Integer debtorRepaymentDay) {
		this.debtorRepaymentDay = debtorRepaymentDay;
	}



	public Integer getDebtorExtentedRepaymentDays() {
		return debtorExtentedRepaymentDays;
	}

	public void setDebtorExtentedRepaymentDays(Integer debtorExtentedRepaymentDays) {
		this.debtorExtentedRepaymentDays = debtorExtentedRepaymentDays;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}




	public String getCreditorLoanSn() {
		return creditorLoanSn;
	}

	public void setCreditorLoanSn(String creditorLoanSn) {
		this.creditorLoanSn = creditorLoanSn;
	}


	public String getServiceUserName() {
		return serviceUserName;
	}

	public void setServiceUserName(String serviceUserName) {
		this.serviceUserName = serviceUserName;
	}

	public Set<Loanoverdue> getLoanoverdues() {
		return loanoverdues;
	}

	public void setLoanoverdues(Set<Loanoverdue> loanoverdues) {
		this.loanoverdues = loanoverdues;
	}

	public Set<Creditrepayment> getCreditrepayments() {
		return creditrepayments;
	}

	public void setCreditrepayments(Set<Creditrepayment> creditrepayments) {
		this.creditrepayments = creditrepayments;
	}

	public void setCreditrepayplans(Set<Creditrepayplan> creditrepayplans) {
		this.creditrepayplans = creditrepayplans;
	}

	public Date getLoanTime() {
		return loanTime;
	}

	public void setLoanTime(Date loanTime) {
		this.loanTime = loanTime;
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

	public Boolean getIsException() {
		return isException;
	}

	public void setIsException(Boolean isException) {
		this.isException = isException;
	}

	@Override
	@JsonIgnore
	@Transient
	public LoanLog getLogInstance() {
		// TODO Auto-generated method stub
		return new LoanLog();
	}

	@Override
	@JsonIgnore
	@Transient
	public LogRepository getLogRepository() {
		// TODO Auto-generated method stub
		return logRepository;
	}

	@Override
	public void setLogRepository(LogRepository logRepository) {
		Loan.logRepository = logRepository;
	}

	@Override
	@JsonIgnore
	@Transient
	public AbstractAuditorService getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@Override
	public void setService(AbstractAuditorService service) {
		Loan.service = (AbstractWorkflowService) service;
		
	}

	public static enum PeriodCode{
//		@Info(label="一次性",placeholder="",tip="",help="",secret="ONE")
//		ONE,
		@Info(label="天",placeholder="",tip="",help="",secret="DAY")
		DAY,
		@Info(label="周",placeholder="",tip="",help="",secret="WEEK")
		WEEK,
		@Info(label="月",placeholder="",tip="",help="",secret="MONTH")
		MONTH
	}

	//放款类型
	public static enum  LoanType{
		ORDERCDD
	}
}