package com.liyang.domain.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.liyang.annotation.Info;
import com.liyang.domain.department.Department;
import com.liyang.domain.user.User;
import com.liyang.util.CommonUtil;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public abstract class BaseEntity implements Serializable {


	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Info(label="id",placeholder="",tip="",help="",secret="")						
	private Integer id;
	

	@Info(label="名称",placeholder="",tip="",help="",secret="")						
	private String label;
	
	@CreatedBy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "created_by")
	@Info(label="创建人",placeholder="",tip="",help="",secret="")						
	private User createdBy;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "created_by_department")
	@Info(label="创建部门",placeholder="",tip="",help="",secret="")						
	private Department createdByDepartment;

	
	@LastModifiedBy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "last_modified_by")
	@Info(label="最后更新人",placeholder="",tip="",help="",secret="")						
	private User lastModifiedBy;
	
	@CreatedDate
	@Column(name="created_at")
	@Info(label="创建时间",placeholder="",tip="",help="",secret="")						
	protected Date createdAt;

	@LastModifiedDate
	@Column(name="last_modified_at")
	@Info(label="最后更新时间",placeholder="",tip="",help="",secret="")						
	protected Date lastModifiedAt;

	
	public Department getCreatedByDepartment() {
		return createdByDepartment;
	}

	public void setCreatedByDepartment(Department createdByDepartment) {
		this.createdByDepartment = createdByDepartment;
	}



	@PrePersist
	public void prePersist() {

		User createdByUser = CommonUtil.getPrincipal();
//		if (createdByUser != null && createdByUser.getDepartment()!=null) {
		if (createdByDepartment==null&&createdByUser != null && createdByUser.getDepartment()!=null) {
			this.createdByDepartment = createdByUser.getDepartment();
		}
	}



	

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public Integer getId() {
	
		return id;
	}

	public void setId(Integer id) {
	
		this.id = id;
	}


	public User getCreatedBy() {
		return createdBy;
	}



	public User getLastModifiedBy() {
		return lastModifiedBy;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedBy(User createdBy) {
		
	}
	public void setLastModifiedBy(User lastModifiedBy) {
		
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}
	

	@Override
	public String toString() {
//		return "{id=" + id + ", label=" + label + "}";
		StringBuilder sb = new StringBuilder();
		sb.append("{id=") .append(id).append(",").append("label=") .append(label) .append("}");
		return sb.toString();
	}
	

}
