package com.liyang.domain.ordercdd;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import com.liyang.annotation.PersonField;
import com.liyang.domain.base.AbstractWorkflowEntity;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.creditcard.Creditcard;
import com.liyang.domain.feebackup.Feebackup;
import com.liyang.domain.loan.Loan;
import com.liyang.domain.ordercddloan.Ordercddloan;
import com.liyang.domain.person.Person;
import com.liyang.domain.person.Person.MARITAL;
import com.liyang.domain.product.Product;
import com.liyang.domain.user.User;
import com.liyang.domain.vehicle.Vehicle;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "ordercdd")
@Cacheable
@Info(label="车抵贷",secret="车抵贷")
public class Ordercdd extends AbstractWorkflowEntity<OrdercddWorkflow, OrdercddState, OrdercddAct, OrdercddLog,OrdercddFile> {

	@Transient
	private static AbstractWorkflowService service;
	@Transient
	private static LogRepository logRepository;

	@JoinColumn(name="product_id")
	@ManyToOne(fetch=FetchType.EAGER)
	//这里改成Product_id
	@Info(label="产品ID",placeholder="",tip="",help="",secret="主要用于以后统计")
	private Product product;

	@JoinColumn(name = "apply_user_id")
	@OneToOne(fetch=FetchType.LAZY)
	@Info(label="申请的用户",placeholder="",tip="",help="",secret="")
	private User user;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "apply_person_id")
	@Info(label="申请的自然人",placeholder="",tip="",help="",secret="")
	private Person person;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="debtor_creditcard_id")
	@Info(label="授信账户",placeholder="",tip="",help="",secret="")
	private Creditcard debtorCreditcard;

	@Column(name = "apply_real_name")
	@Info(label="真实姓名",placeholder="",tip="",help="",secret="")
	@PersonField(name="name")
	private String realName;

	////begin------------------银行卡信息---------------------------------
	@Column(name = "apply_bank_card")
	@Info(label="储蓄卡卡号",placeholder="",tip="",help="",secret="")
	@PersonField(name="applyBankCard")
	private String applyBankCard;

	@Column(name = "apply_bank_card_branch")
	@Info(label="储蓄卡支行",placeholder="",tip="",help="",secret="")
	@PersonField(name="applyBankCardBranch")
	private String applyBankCardBranch;
	/////end-----------------------------------------------------------------
	@Column(name = "apply_mobile",unique = false)
	@PersonField(name="mobile")
	@Info(label="手机号",placeholder="",tip="",help="",secret="")
	private String applyMobile;

	@Column(name = "apply_identity_no",unique =false)
	@PersonField(name="identity")
	@Info(label="身份证号",placeholder="",tip="",help="",secret="")
	private String applyIdentityNo;

	@Info(label="生日",placeholder="",tip="",help="",secret="")
	private Date birthday;

	@Info(label="性别",placeholder="",tip="",help="",secret="")
	private Integer sex;

	@Column(name = "marital_status")
	@Enumerated(EnumType.STRING)
	@Info(label="婚姻状态",placeholder="",tip="",help="",secret="")
	private MARITAL maritalStatus;

	@Info(label="电子邮件",placeholder="",tip="",help="",secret="")
	private String email;

	@Column(name="apply_enterprise_province")
	@Info(label="服务城市",placeholder="",tip="",help="",secret="")
	private String applyEnterpriseProvince;

	@Column(name = "personal_address")
	@Info(label="个人通讯地址",placeholder="",tip="",help="",secret="")
	private String personalAddress;

	@Column(name = "company_name")
	@Info(label="就职公司",placeholder="",tip="",help="",secret="")
	private String companyName;

	@Column(name = "company_telephone")
	@Info(label="公司固话",placeholder="",tip="",help="",secret="")
	private String companyTelephone;

	@Column(name = "company_provice")
	@Info(label="户籍",placeholder="",tip="",help="",secret="")
	private String companyProvince;

	@Column(name = "company_address")
	@Info(label="公司地址",placeholder="",tip="",help="",secret="")
	private String companyAddress;

	/*******************************贷款信息Start*********************************/
	@Column(name = "apply_amount")
	@Info(label="借款总额",placeholder="",tip="",help="",secret="")
	private BigDecimal applyAmount;

	@Column(name = "left_match_amount")
	@Info(label="剩余匹配金额",placeholder="",tip="",help="相当于自有资金出资金额",secret="")
	private BigDecimal leftMatchAmount;

	@Column(name = "repayment_method_code")
	@Info(label="还款规则",placeholder="",tip="",help="",secret="")
	private String  repaymentMethodCode;

	/*******************************车辆信息Start*********************************/
	@Column(name = "apply_register_reigionname")
	@Info(label="上牌区域",placeholder="",tip="",help="",secret="")
	private String applyRegisterReigionName;

	@Column(name = "apply_plate_number")
	@Info(label="车牌号",placeholder="",tip="",help="",secret="")
	private String  applyPlateNumber;

	@Column(name = "apply_vehicle_model")
	@Info(label="车辆型号",placeholder="",tip="",help="",secret="")
	private String  applyVehicleModel;

	@Column(name = "GPS_code")
	@Info(label="GPS编号",placeholder="",tip="",help="",secret="")
	private String  GPSCode;

	@Column(name = "apply_annual_survey_maturity")
	@Info(label="年检到期日",placeholder="",tip="",help="",secret="")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date applyAnnualSurveyMaturity;

	@Column(name = "apply_annual_policy_maturity")
	@Info(label="保单到期日",placeholder="",tip="",help="",secret="")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date applyAnnualPolicyMaturity;

	@Column(name = "vehicle_date")
	@Info(label="车辆年份",placeholder="",tip="",help="和车300网站评估对应，应该是车辆生产日期",secret="")
	@DateTimeFormat(pattern="yyyy-MM")
	@JsonFormat(pattern = "yyy-MM",timezone = "GMT+8")
	private Date vehicleDate;

	@Column(name = "vehicle_km")
	@Info(label="公里数",placeholder="",tip="",help="和车300网站评估对应，车辆行驶公里数",secret="单位：万公里")
	private Float vehicleKm;

	@Column(name = "vehicle_province")
	@Info(label="车辆省份",placeholder="",tip="",help="和车300网站评估对应，目前只精确到市",secret="")
	private String  vehicleProvince;

	@Column(name = "vehicle_city")
	@Info(label="车辆城市",placeholder="",tip="",help="和车300网站评估对应",secret="")
	private String  vehicleCity;

	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;
	/*******************************车辆信息End*********************************/

	@Lob
	@ManyToOne
	@JoinColumn(name="service_user_id")
	@Info(label = "业务员",placeholder = "",tip = "",secret = "")
	private User serviceUser;

	@Column(name="service_name")//业务员名称
	private String serviceName;

	@Column(name = "cdd_sn",length = 16,unique = true)//生成的单号
	@Info(label="订单号",placeholder="",tip="",help="",secret="")
	private String cddSn;

	//表示是否已经分配
	@Column(name="is_distribution")
	private Boolean isDistribution;

	@Lob
	@Info(label="/内部备注",placeholder="",tip="",help="",secret="")
	private String comment;//内部备注
	
	@Column(name="referral_code")
	@Info(label="推荐码",placeholder="",tip="",help="",secret="")
	private String referralCode;
	
	@Column(name="is_interest_advance")
	@Info(label="最后一期利息提前还清",placeholder="",tip="",help="",secret="")
	private Boolean isInterestAdvance;
	
	@Column(name="loan_to_store")
	@Info(label="是否放款给门店",placeholder="",tip="",help="true:放款给门店; false:给款给客户",secret="")
	private Boolean loanToStore;

	@Transient//服务人员
	private Integer serviceId;

	@Transient//短信验证码
	private String smsCode;

	@Transient//微信appWeb传的code
	private String wechatCode;


	@OneToOne(mappedBy = "ordercdd")
	private Loan loan;

	@JsonIgnore
	@OneToMany(mappedBy="ordercdd")
	@Info(label="收费项目快照",placeholder="",tip="",help="审核通过之后不能再修改",secret="")
	private Set<Feebackup> feebackups;

	@JsonIgnore
	@OneToMany(mappedBy="ordercdd")
	@Info(label="资方产品审核",placeholder="",tip="",help="",secret="")
	private Set<Ordercddloan> ordercddloans;

	@Transient
	private Set<Object> fees;


	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Loan getLoan() {
		return loan;
	}

	public Date getApplyAnnualSurveyMaturity() {
		return applyAnnualSurveyMaturity;
	}

	public void setApplyAnnualSurveyMaturity(Date applyAnnualSurveyMaturity) {
		this.applyAnnualSurveyMaturity = applyAnnualSurveyMaturity;
	}

	public String getRepaymentMethodCode() {
		return repaymentMethodCode;
	}

	public void setRepaymentMethodCode(String repaymentMethodCode) {
		this.repaymentMethodCode = repaymentMethodCode;
	}

	public String getCddSn() {
		return cddSn;
	}

	public void setCddSn(String cddSn) {
		this.cddSn = cddSn;
	}

	public String getApplyBankCard() {
		return applyBankCard;
	}

	public void setApplyBankCard(String applyBankCard) {
		this.applyBankCard = applyBankCard;
	}

	public String getApplyBankCardBranch() {
		return applyBankCardBranch;
	}

	public String getGPSCode() {
		return GPSCode;
	}

	public void setGPSCode(String GPSCode) {
		this.GPSCode = GPSCode;
	}

	public void setApplyBankCardBranch(String applyBankCardBranch) {
		this.applyBankCardBranch = applyBankCardBranch;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getApplyEnterpriseProvince() {
		return applyEnterpriseProvince;
	}

	public void setApplyEnterpriseProvince(String applyEnterpriseProvince) {
		this.applyEnterpriseProvince = applyEnterpriseProvince;
	}

	public String getPersonalAddress() {
		return personalAddress;
	}

	public void setPersonalAddress(String personalAddress) {
		this.personalAddress = personalAddress;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyTelephone() {
		return companyTelephone;
	}

	public void setCompanyTelephone(String companyTelephone) {
		this.companyTelephone = companyTelephone;
	}

	public String getCompanyProvince() {
		return companyProvince;
	}

	public void setCompanyProvince(String companyProvince) {
		this.companyProvince = companyProvince;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getApplyPlateNumber() {
		return applyPlateNumber;
	}

	public void setApplyPlateNumber(String  applyPlateNumber) {
		this.applyPlateNumber = applyPlateNumber;
	}

	public String getApplyVehicleModel() {
		return applyVehicleModel;
	}

	public void setApplyVehicleModel(String applyVehicleModel) {
		this.applyVehicleModel = applyVehicleModel;
	}

	public MARITAL getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MARITAL maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getApplyMobile() {
		return applyMobile;
	}

	public void setApplyMobile(String applyMobile) {
		this.applyMobile = applyMobile;
	}

	public String getApplyRegisterReigionName() {
		return applyRegisterReigionName;
	}

	public void setApplyRegisterReigionName(String applyRegisterReigionName) {
		this.applyRegisterReigionName = applyRegisterReigionName;
	}

	public Date getApplyAnnualPolicyMaturity() {
		return applyAnnualPolicyMaturity;
	}

	public void setApplyAnnualPolicyMaturity(Date applyAnnualPolicyMaturity) {
		this.applyAnnualPolicyMaturity = applyAnnualPolicyMaturity;
	}

	public String getApplyIdentityNo() {
		return applyIdentityNo;
	}

	public void setApplyIdentityNo(String applyIdentityNo) {
		this.applyIdentityNo = applyIdentityNo;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Creditcard getDebtorCreditcard() {
		return debtorCreditcard;
	}

	public void setDebtorCreditcard(Creditcard debtorCreditcard) {
		this.debtorCreditcard = debtorCreditcard;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Boolean getDistribution() {
		return isDistribution;
	}

	public void setDistribution(Boolean distribution) {
		isDistribution = distribution;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getWechatCode() {
		return wechatCode;
	}

	public void setWechatCode(String wechatCode) {
		this.wechatCode = wechatCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

//	public String getCddSn() {
//		return cddSn;
//	}
//
//	public void setCddSn(String cddSN) {
//		this.cddSn = cddSN;
//	}

	public Set<Feebackup> getFeebackups() {
		return feebackups;
	}

	public void setFeebackups(Set<Feebackup> feebackups) {
		this.feebackups = feebackups;
	}

	public BigDecimal getLeftMatchAmount() {
		return leftMatchAmount;
	}

	public void setLeftMatchAmount(BigDecimal leftMatchAmount) {
		this.leftMatchAmount = leftMatchAmount;
	}

	public Set<Ordercddloan> getOrdercddloans() {
		return ordercddloans;
	}

	public void setOrdercddloans(Set<Ordercddloan> ordercddloans) {
		this.ordercddloans = ordercddloans;
	}

	public Date getVehicleDate() {
		return vehicleDate;
	}

	public void setVehicleDate(Date vehicleDate) {
		this.vehicleDate = vehicleDate;
	}

	public Float getVehicleKm() {
		return vehicleKm;
	}

	public void setVehicleKm(Float vehicleKm) {
		this.vehicleKm = vehicleKm;
	}

	public String getVehicleProvince() {
		return vehicleProvince;
	}

	public void setVehicleProvince(String vehicleProvince) {
		this.vehicleProvince = vehicleProvince;
	}

	public String getVehicleCity() {
		return vehicleCity;
	}

	public void setVehicleCity(String vehicleCity) {
		this.vehicleCity = vehicleCity;
	}

	public Set<Object> getFees() {
		return fees;
	}

	public void setFees(Set<Object> fees) {
		this.fees = fees;
	}

	public Boolean getIsInterestAdvance() {
		return isInterestAdvance;
	}

	public void setIsInterestAdvance(Boolean isInterestAdvance) {
		this.isInterestAdvance = isInterestAdvance;
	}

	public Boolean getLoanToStore() {
		return loanToStore;
	}

	public void setLoanToStore(Boolean loanToStore) {
		this.loanToStore = loanToStore;
	}

	@Override
	@JsonIgnore
	public AbstractAuditorService<?, OrdercddState, OrdercddAct, OrdercddLog> getService() {
		return service;
	}

	@Override
	public void setService(AbstractAuditorService service) {
		Ordercdd.service=(AbstractWorkflowService)service;
	}
	@Override
	@JsonIgnore
	public OrdercddLog getLogInstance() {
		return new OrdercddLog();
	}

	@Override
	@JsonIgnore
	public LogRepository getLogRepository() {
		return logRepository;
	}

	@Override
	public void setLogRepository(LogRepository logRepository) {
		Ordercdd.logRepository=logRepository;
	}

	public User getServiceUser() {
		return serviceUser;
	}

	public void setServiceUser(User serviceUser) {
		this.serviceUser = serviceUser;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
}