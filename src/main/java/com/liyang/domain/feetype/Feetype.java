package com.liyang.domain.feetype;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.liyang.annotation.Info;

/**
 * Created by win7 on 2017-07-26.
 */
@Entity
@Table(name = "feetype")
@Info(label="费用库",placeholder="",tip="",help="",secret="")
public class Feetype {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Info(label="id",placeholder="",tip="",help="",secret="")						
	private Integer id;
	
	@CreatedDate
	@Column(name="created_at")
	@Info(label="创建时间",placeholder="",tip="",help="",secret="")						
	private Date createdAt;
	
	@Info(label="名称",placeholder="",tip="",help="",secret="")						
	private String label;
	
    private String alias;

    @Column(name = "feetype_code")
    @Info(label="费用的标识code",placeholder="如ICBC、ALIPAY",tip="",help="大写的拼音",secret="")
    private String feetypeCode;

    @Lob
    @Info(label="费用说明",placeholder="",tip="",help="",secret="")
    private String description;

    @Lob
    @Info(label="费用备注",placeholder="",tip="",help="",secret="")
    private String comment;


    @Info(label="前台排序",placeholder="",tip="",help="",secret="")	   
    private Integer sort;
    
    @Info(label="类型分类",placeholder="",tip="",help="",secret="0：门店向客户收费，1：资方向门店收费")
    private Integer type;
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFeetypeCode() {
        return feetypeCode;
    }

    public void setFeetypeCode(String feetypeCode) {
        this.feetypeCode = feetypeCode;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
