package com.liyang.domain.department;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.core.annotation.Order;

import com.liyang.domain.base.BaseEntity;
import com.liyang.domain.role.Role;


@Entity
@Table(name = "departmenttype")
@Order(11111111)
public class Departmenttype extends BaseEntity{
	@Transient
	private static final long serialVersionUID = 1L;

	
	@Enumerated(EnumType.STRING)
	@Column(name="departmenttype_code")
	private DepartmenttypeCode departmenttypeCode;
	
	@OneToMany(mappedBy = "departmenttype")
	private Set<Department> departments;
	
	
	@OneToMany(mappedBy = "departmenttype")
	private Set<Role> roles;


	public DepartmenttypeCode getDepartmenttypeCode() {
		return departmenttypeCode;
	}

	public void setDepartmenttypeCode(DepartmenttypeCode departmenttypeCode) {
		this.departmenttypeCode = departmenttypeCode;
	}

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public static enum DepartmenttypeCode{
		STORE, 
		GROUP, 
		COMPANY,
		CREDITOR, 
		DEBTOR, 
		LAW, 
		DISTRIBUTOR
	}
}