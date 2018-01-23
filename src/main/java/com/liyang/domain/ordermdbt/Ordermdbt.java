package com.liyang.domain.ordermdbt;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import com.liyang.annotation.PersonField;
import com.liyang.domain.account.Account;
import com.liyang.domain.advertise.Advertise;
import com.liyang.domain.application.Application;
import com.liyang.domain.base.AbstractWorkflowEntity;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.enterprise.Enterprise;
//import com.liyang.domain.orderwdsjsh.Orderwdsjsh.PeriodCode;
import com.liyang.domain.loan.Loan.PeriodCode;
import com.liyang.domain.person.Person;
import com.liyang.domain.product.Product;
import com.liyang.domain.user.User;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;

import java.math.BigDecimal;

/**
 * 
 * @author win7
 *
 */
@Entity
@Table(name = "ordermdbt")
@Cacheable
@Info(label="面单白条",placeholder="",tip="",help="",secret="")	
public class Ordermdbt extends AbstractWorkflowEntity<OrdermdbtWorkflow, OrdermdbtState, OrdermdbtAct, OrdermdbtLog,OrdermdbtFile> {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;
	@Transient
	private static AbstractWorkflowService service;
	
	@OneToOne(targetEntity = Application.class)
	@JoinColumn(name = "application_id")
	@Info(label="来源应用ID",placeholder="",tip="",help="",secret="")	
	private Application application;
	
	@JoinColumn(name="product_id")
	@OneToOne
	//这里改成Product_id
	@Info(label="产品ID",placeholder="",tip="",help="",secret="")	
	private Product product;

	@JoinColumn(name = "apply_user_id")
	@OneToOne
	@Info(label="用户",placeholder="",tip="",help="",secret="")	
	private User user;
	
	@OneToOne
	@JoinColumn(name = "apply_person_id")
	@Info(label="自然人",placeholder="",tip="",help="",secret="")	
	private Person person;
	
	@Info(label="申请金额",placeholder="",tip="",help="",secret="")
	@Column(name = "apply_amount")
	private Integer applyAmount;
	
	@Column(name = "comfirm_amount",precision=19,scale=6)
	@Info(label="确认的金额",placeholder="",tip="",help="申请之后应该有一个审核放款额度，做为授信额度",secret="")
	private BigDecimal comfirmAmount;

	@Info(label="分期数量",placeholder="",tip="",help="",secret="")
	@Column(name = "apply_period_number")
	private Integer applyPeriodNumber;

	@PersonField(name="name")
	@Column(name = "apply_real_name")
	@Info(label="申请人姓名",placeholder="",tip="",help="",secret="")
	private String realName;

	@PersonField(name="mobile")
	@Column(name = "apply_mobile")
	@Info(label="申请人手机号",placeholder="",tip="",help="",secret="")
	private String applyMobile;

	@PersonField(name="identity")
	@Column(name = "apply_identity_no")
	@Info(label="身份证号",placeholder="",tip="",help="",secret="")
	private String applyIdentityNo;
	
	@JoinColumn(name = "apply_enterprise_id")
	@OneToOne
	@Info(label="网点营业执照",placeholder="",tip="",help="",secret="")
	private Enterprise enterprise;

	@Column(name = "apply_enterprise_name")
	@Info(label="网点名称",placeholder="",tip="",help="",secret="")
	private String applyEnterpriseName;
	
	@Column(name = "apply_enterprise_reigionname")
	@Info(label="经营区域",placeholder="",tip="",help="",secret="")
	private String applyEnterpriseReigionName;
	
	@Column(name = "apply_enterprise_address")
	@Info(label="经营地址",placeholder="",tip="",help="",secret="")
	private String applyEnterpriseAddress;//经营地点
	
	@Column(name = "apply_referrer_realname")
	@Info(label="推荐人姓名",placeholder="",tip="",help="",secret="")
	private String applyReferrerRealName;//推荐人姓名
	
	@Column(name = "apply_daypickexpress")
	@Info(label="日收件量",placeholder="",tip="",help="",secret="")
	private Integer applyDayPickExpress;//日收件量
	
	@Column(name = "apply_daypatchexpress")
	@Info(label="日派件量",placeholder="",tip="",help="",secret="")
	private Integer applyDayPatchExpress;//日派件量
	
	@Column(name = "apply_waybillfee",precision=19,scale=6)
	@Info(label="面单费",placeholder="",tip="",help="",secret="")
	private BigDecimal applyWayBillFee;//面单费
	
	
	@Lob
	@Info(label="/内部备注",placeholder="",tip="",help="",secret="")
	private String comment;//内部备注
	
	@Column(name="apply_enterprise_province")
	@Info(label="省份",placeholder="",tip="",help="",secret="")
	private String applyEnterpriseProvince;
	
	@Column(name="apply_enterprise_city")
	@Info(label="城市",placeholder="",tip="",help="",secret="")
	private String applyEnterpriseCity;
	
	@Column(name="apply_enterprise_district")
	@Info(label="区县",placeholder="",tip="",help="",secret="")
	private String applyEnterpriseDistrict;
	
	@Column(name="apply_enterprise_town")
	@Info(label="乡镇",placeholder="",tip="",help="",secret="")
	private String applyEnterpriseTown;
	
	@Column(name="apply_period_code")
	@Enumerated(EnumType.STRING)
	@Info(label="分期时间单位",placeholder="",tip="",help="",secret="")
	private PeriodCode applyPeriodCode;
	

	@ManyToOne
	@JoinColumn(name="debtor_accound_id")
	@Info(label="授信账户",placeholder="",tip="",help="",secret="")
	private Account debtorAccount;
	
	@ManyToOne
	@JoinColumn(name="debtor_receive_accound_id")
	@Info(label="收款账户",placeholder="",tip="",help="",secret="")
	private Account debtorReceiveAccount;//贷款的收款账户
	
	
		
	public Account getDebtorReceiveAccount() {
		return debtorReceiveAccount;
	}

	public void setDebtorReceiveAccount(Account debtorReceiveAccount) {
		this.debtorReceiveAccount = debtorReceiveAccount;
	}

	public PeriodCode getApplyPeriodCode() {
		return applyPeriodCode;
	}

	public void setApplyPeriodCode(PeriodCode applyPeriodCode) {
		this.applyPeriodCode = applyPeriodCode;
	}

	public String getApplyEnterpriseProvince() {
		return applyEnterpriseProvince;
	}

	public void setApplyEnterpriseProvince(String applyEnterpriseProvince) {
		this.applyEnterpriseProvince = applyEnterpriseProvince;
	}

	public String getApplyEnterpriseCity() {
		return applyEnterpriseCity;
	}

	public void setApplyEnterpriseCity(String applyEnterpriseCity) {
		this.applyEnterpriseCity = applyEnterpriseCity;
	}

	public String getApplyEnterpriseDistrict() {
		return applyEnterpriseDistrict;
	}

	public void setApplyEnterpriseDistrict(String applyEnterpriseDistrict) {
		this.applyEnterpriseDistrict = applyEnterpriseDistrict;
	}

	public String getApplyEnterpriseTown() {
		return applyEnterpriseTown;
	}

	public void setApplyEnterpriseTown(String applyEnterpriseTown) {
		this.applyEnterpriseTown = applyEnterpriseTown;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
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

	public Integer getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(Integer applyAmount) {
		this.applyAmount = applyAmount;
	}

	public Integer getApplyPeriodNumber() {
		return applyPeriodNumber;
	}

	public void setApplyPeriodNumber(Integer applyPeriodNumber) {
		this.applyPeriodNumber = applyPeriodNumber;
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

	public String getApplyIdentityNo() {
		return applyIdentityNo;
	}

	public void setApplyIdentityNo(String applyIdentityNo) {
		this.applyIdentityNo = applyIdentityNo;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public String getApplyEnterpriseName() {
		return applyEnterpriseName;
	}

	public void setApplyEnterpriseName(String applyEnterpriseName) {
		this.applyEnterpriseName = applyEnterpriseName;
	}

	public String getApplyEnterpriseReigionName() {
		return applyEnterpriseReigionName;
	}

	public void setApplyEnterpriseReigionName(String applyEnterpriseReigionName) {
		this.applyEnterpriseReigionName = applyEnterpriseReigionName;
	}

	public String getApplyEnterpriseAddress() {
		return applyEnterpriseAddress;
	}

	public void setApplyEnterpriseAddress(String applyEnterpriseAddress) {
		this.applyEnterpriseAddress = applyEnterpriseAddress;
	}

	public String getApplyReferrerRealName() {
		return applyReferrerRealName;
	}

	public void setApplyReferrerRealName(String applyReferrerRealName) {
		this.applyReferrerRealName = applyReferrerRealName;
	}

	public Integer getApplyDayPickExpress() {
		return applyDayPickExpress;
	}

	public void setApplyDayPickExpress(Integer applyDayPickExpress) {
		this.applyDayPickExpress = applyDayPickExpress;
	}

	public Integer getApplyDayPatchExpress() {
		return applyDayPatchExpress;
	}

	public void setApplyDayPatchExpress(Integer applyDayPatchExpress) {
		this.applyDayPatchExpress = applyDayPatchExpress;
	}

	public BigDecimal getApplyWayBillFee() {
		return applyWayBillFee;
	}

	public void setApplyWayBillFee(BigDecimal applyWayBillFee) {
		this.applyWayBillFee = applyWayBillFee;
	}

	public BigDecimal getComfirmAmount() {
		return comfirmAmount;
	}

	public void setComfirmAmount(BigDecimal comfirmAmount) {
		this.comfirmAmount = comfirmAmount;
	}

	@Transient
	private static ActRepository actRepository;

	@Transient
	private static LogRepository logRepository;
	@JsonIgnore
	@Override
	public AbstractAuditorService getService() {
		return service;
	}

	@Override
	public void setService(AbstractAuditorService service) {
		Ordermdbt.service= (AbstractWorkflowService) service;
	}

	@Override
	@JsonIgnore
	@Transient
	public OrdermdbtLog getLogInstance() {
		// TODO Auto-generated method stub
		return new OrdermdbtLog();
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
		Ordermdbt.logRepository = logRepository;
	}

	

	public Account getDebtorAccount() {
		return debtorAccount;
	}

	public void setDebtorAccount(Account debtorAccount) {
		this.debtorAccount = debtorAccount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "OrderWdd{" +
				", application=" + application +
				", product=" + product +
				", user=" + user +
				", brand="  +
				", person=" + person +
				", applyAmount=" + applyAmount +
				", applyPeriodNumber=" + applyPeriodNumber +
				", realName='" + realName + '\'' +
				", applyMobile='" + applyMobile + '\'' +
				", applyIdentityNo=" + applyIdentityNo +
				", enterprise=" + enterprise +
				", applyEnterpriseName='" + applyEnterpriseName + '\'' +
				", applyEnterpriseReigionName='" + applyEnterpriseReigionName + '\'' +
				", applyEnterpriseAddress='" + applyEnterpriseAddress + '\'' +
				", applyReferrerRealName='" + applyReferrerRealName + '\'' +
				", applyDayPickExpress=" + applyDayPickExpress +
				", applyDayPatchExpress=" + applyDayPatchExpress +
				", applyWayBillFee=" + applyWayBillFee +
				", comfirmAmount=" + comfirmAmount +
				", comment='" + comment + '\'' +
				'}';
	}
}