package com.liyang.domain.feebackup;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.liyang.annotation.Info;
import com.liyang.domain.feetype.Feetype;
import com.liyang.domain.ordercdd.Ordercdd;

/**
 * Created by win7 on 2017-07-26.
 */
@Entity
@Table(name = "feebackup")
@Info(label = "产品消费项目备份")
public class Feebackup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Info(label="id",placeholder="",tip="",help="",secret="")						
	private Integer id;
	
	@CreatedDate
	@Column(name="created_at")
	@Info(label="创建时间",placeholder="",tip="",help="",secret="")						
	private Date createdAt;
	
    @ManyToOne
    @JoinColumn(name = "feetype_id")
    @Info(label = "费用库ID", placeholder = "", tip = "", help = "", secret = "")
    private Feetype feetype;

    @Column(name = "is_period")
    @Info(label = "是否分期")
    private Boolean isPeriod;

    @Column(name = "fee_rate", precision = 19, scale = 6)
    @Info(label = "按比例收费，优先")
    private BigDecimal feeRate;

    @Column(name = "fee_amount", precision = 19, scale = 6)
    @Info(label = "固定收费")
    private BigDecimal feeAmount;

    @Lob
    @Info(label = "公司或门店收费说明", placeholder = "", tip = "", help = "", secret = "")
    private String description;

    @Lob
    @Info(label = "公司或门店收费备注", placeholder = "", tip = "", help = "", secret = "")
    private String comment;
    @Lob
    @Info(label = "feetype_code", placeholder = "", tip = "", help = "", secret = "")
    private String feetypeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordercdd_id", nullable = false)
    private Ordercdd ordercdd;
    
    @Enumerated(EnumType.STRING)
    @Info(label = "属于什么备份，company或store")
    private BACKUPSOURCE backupsource;
    
    @Column(name = "is_refund")
    @Info(label="费用是否可退",placeholder="",tip="",help="",secret="")
    private Boolean isRefund;
    
    @Column(name="period_number")
    @Info(label="费用分期数",placeholder="",tip="",help="",secret="")
    private Integer periodNumber;

    public Feetype getFeetype() {
        return feetype;
    }

    public void setFeetype(Feetype feetype) {
        this.feetype = feetype;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
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

    public String getFeetypeCode() {
        return feetypeCode;
    }

    public void setFeetypeCode(String feetypeCode) {
        this.feetypeCode = feetypeCode;
    }

    public Ordercdd getOrdercdd() {
        return ordercdd;
    }

    public void setOrdercdd(Ordercdd ordercdd) {
        this.ordercdd = ordercdd;
    }

    public BACKUPSOURCE getBackupsource() {
        return backupsource;
    }

    public void setBackupsource(BACKUPSOURCE backupsource) {
        this.backupsource = backupsource;
    }

    public Boolean getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(Boolean isRefund) {
		this.isRefund = isRefund;
	}

    public Boolean getIsPeriod() {
		return isPeriod;
	}

	public void setIsPeriod(Boolean isPeriod) {
		this.isPeriod = isPeriod;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getPeriodNumber() {
		return periodNumber;
	}

	public void setPeriodNumber(Integer periodNumber) {
		this.periodNumber = periodNumber;
	}

	public enum BACKUPSOURCE {
		COMPANYFEE,
        STOREFEE
    }
}
