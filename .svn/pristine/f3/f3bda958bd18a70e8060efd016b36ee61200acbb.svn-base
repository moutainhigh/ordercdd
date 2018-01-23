package com.liyang.domain.vehicle;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import com.liyang.domain.base.AbstractWorkflowEntity;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.LogRepository;
//import com.liyang.domain.orderwdsjsh.Orderwdsjsh.PeriodCode;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.person.Person;
import com.liyang.domain.user.User;
import com.liyang.service.AbstractAuditorService;
import com.liyang.service.AbstractWorkflowService;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author win7
 */
@Entity
@Table(name = "vehicle")
@Cacheable
@Info(label = "贷款", placeholder = "", tip = "", help = "", secret = "")
public class Vehicle extends AbstractWorkflowEntity<VehicleWorkflow, VehicleState, VehicleAct, VehicleLog, VehicleFile> {
    private static final long serialVersionUID = 3168036637739587364L;
    @Transient
    private static AbstractWorkflowService service;
    @Transient
    private static ActRepository actRepository;
    @Transient
    private static LogRepository logRepository;

    @Column(name = "register_region_name")
    @Info(label = "上牌区域", placeholder = "", tip = "", help = "", secret = "")
    private String registerRegionName;

    @Column(name = "plate_number")
    @Info(label = "车牌号", placeholder = "", tip = "", help = "", secret = "")
    private String plateNumber;

    @Column(name = "vehicle_model")
    @Info(label = "车辆型号", placeholder = "", tip = "", help = "", secret = "")
    private String vehicleModel;

    @Column(name = "GPS_code")
    @Info(label = "GPS编号", placeholder = "", tip = "", help = "", secret = "")
    private String GPSCode;


    @Column(name = "GPS_status")
    @Enumerated(EnumType.STRING)
    @Info(label = "GPS状态", placeholder = "", tip = "", help = "", secret = "")
    private GPSStatus gpsStatus=GPSStatus.PENDING;

    @Column(name = "annual_survey_maturity")
    @Info(label = "年检到期日", placeholder = "", tip = "", help = "", secret = "")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date annualSurveyMaturity;
    @Column(name = "annual_policy_maturity")
    @Info(label = "保单到期日", placeholder = "", tip = "", help = "", secret = "")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date annualPolicyMaturity;
    @Column(name = "vehicle_date")
    @Info(label = "车辆年份", placeholder = "", tip = "", help = "和车300网站评估对应，应该是车辆生产日期", secret = "")
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date vehicleDate;

    @Column(name = "vehicle_km")
    @Info(label = "公里数", placeholder = "", tip = "", help = "和车300网站评估对应，车辆行驶公里数", secret = "单位：万公里")
    private Float vehicleKm;

    @Column(name = "vehicle_province")
    @Info(label = "车辆省份", placeholder = "", tip = "", help = "和车300网站评估对应，目前只精确到市", secret = "")
    private String vehicleProvince;

    @Column(name = "vehicle_city")
    @Info(label = "车辆城市", placeholder = "", tip = "", help = "和车300网站评估对应", secret = "")
    private String vehicleCity;

    @OneToMany(mappedBy = "vehicle")
    private Set<Ordercdd> ordercdds;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @Info(label = "所属用户ID", placeholder = "", tip = "", help = "", secret = "")
    private User user;
    @JoinColumn(name = "person_id")
    @ManyToOne
    @Info(label = "所属person ID", placeholder = "", tip = "", help = "", secret = "")
    private Person person;

    public GPSStatus getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(GPSStatus gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getAnnualPolicyMaturity() {
        return annualPolicyMaturity;
    }

    public void setAnnualPolicyMaturity(Date annualPolicyMaturity) {
        this.annualPolicyMaturity = annualPolicyMaturity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Ordercdd> getOrdercdds() {
        return ordercdds;
    }

    public void setOrdercdds(Set<Ordercdd> ordercdds) {
        this.ordercdds = ordercdds;
    }

    public String getRegisterRegionName() {
        return registerRegionName;
    }

    public void setRegisterRegionName(String registerRegionName) {
        this.registerRegionName = registerRegionName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getGPSCode() {
        return GPSCode;
    }

    public void setGPSCode(String GPSCode) {
        this.GPSCode = GPSCode;
    }

    public Date getAnnualSurveyMaturity() {
        return annualSurveyMaturity;
    }

    public void setAnnualSurveyMaturity(Date annualSurveyMaturity) {
        this.annualSurveyMaturity = annualSurveyMaturity;
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

    @Override
    @JsonIgnore
    @Transient
    public VehicleLog getLogInstance() {
        // TODO Auto-generated method stub
        return new VehicleLog();
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
        Vehicle.logRepository = logRepository;
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
        Vehicle.service = (AbstractWorkflowService) service;

    }

    public enum GPSStatus {
        PENDING,//待确认
        NORMAL,//正常
        ABNORMAL//异常
    }
}