package com.liyang.domain.productcompanyfee;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.liyang.domain.product.Product;

/**
 * Created by win7 on 2017-07-26.
 */
@Entity
@Table(name = "product_companyfee")
@Info(label="费用库",placeholder="",tip="",help="",secret="")
public class Productcompanyfee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Info(label="id",placeholder="",tip="",help="",secret="")						
	private Integer id;
	
	@CreatedDate
	@Column(name="created_at")
	@Info(label="创建时间",placeholder="",tip="",help="",secret="")						
	private Date createdAt;
	
    private String alias;
    @ManyToOne
    @JoinColumn(name="feetype_id")
    @Info(label="费用库ID",placeholder="",tip="",help="",secret="")
    private Feetype feetype;

    @Column(name = "is_period")
    @Info(label="是否分期",placeholder="",tip="",help="",secret="")
    private Boolean isPeriod;

    @Column(name = "fee_rate",precision = 19,scale = 6)
    @Info(label="按比例收费，优先",placeholder="",tip="",help="",secret="")
    private BigDecimal feeRate;

    @Column(name = "fee_amount",precision = 19,scale = 6)
    @Info(label="固定收费")
    private BigDecimal feeAmount;

    @Lob
    @Info(label="公司收费说明",placeholder="",tip="",help="",secret="")
    private String description;

    @Lob
    @Info(label="公司收费备注",placeholder="",tip="",help="",secret="")
    private String comment;

    @Lob
    @Info(label="feetype_code",placeholder="",tip="",help="",secret="")
    private String feetypeCode;
    
    @ManyToOne
    @JoinColumn(name="product_id")
    @Info(label="销售产品ID",placeholder="",tip="",help="",secret="")
    private Product product;

	@Info(label="前台排序",placeholder="",tip="",help="",secret="")	   
    private Integer sort;
	
	@Column(name = "is_refund")
    @Info(label="费用是否可退",placeholder="",tip="",help="",secret="0：不可退；1：可退")
    private Boolean isRefund;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Feetype getFeetype() {
        return feetype;
    }
    
    public String getFeetypeCode() {
		return feetypeCode;
	}

	public void setFeetypeCode(String feetypeCode) {
		this.feetypeCode = feetypeCode;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

	public Boolean getIsPeriod() {
		return isPeriod;
	}

	public void setIsPeriod(Boolean isPeriod) {
		this.isPeriod = isPeriod;
	}

	public Boolean getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(Boolean isRefund) {
		this.isRefund = isRefund;
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
}
