package com.liyang.domain.department;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import com.liyang.domain.base.AbstractAuditorEntity;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.enterprise.Enterprise;
import com.liyang.domain.product.Product;
import com.liyang.domain.user.User;
import com.liyang.service.AbstractAuditorService;


@Entity
@Table(name = "department")
@Cacheable
@Info(label="部门/门店",placeholder="",tip="",help="",secret="")	   
public class Department extends AbstractAuditorEntity<DepartmentState,DepartmentAct,DepartmentLog>{


	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	
	@Transient
	private static LogRepository logRepository;
	
	@Transient
	private static AbstractAuditorService service;

	@ManyToOne
	@JoinColumn(name = "departmenttype_id")
	@Info(label="部门类型",placeholder="",tip="",help="",secret="")	   
	private Departmenttype departmenttype;

	@ManyToOne
	@JoinColumn(name = "enterprise_id")
	@Info(label="所属企业",placeholder="",tip="",help="",secret="")	   
	private Enterprise enterprise;	
		
	@Info(label="排序",placeholder="",tip="",help="",secret="")	   
	private Integer sort;
	
	@JsonIgnore
	@OneToMany(mappedBy = "department")
	private Set<User> employees;
	
	@JsonIgnore
	@OneToMany(mappedBy = "department")
	private Set<Product> products;
	
	@JsonIgnore
	@OneToMany(mappedBy = "creditorDepartment")
	private Set<Capitalproduct> capitalproducts;

	
//	@JsonBackReference(value="departmentTree")
	@ManyToOne(fetch=FetchType.LAZY)
	@Info(label="父级",placeholder="",tip="",help="",secret="")	   
	private Department parent;
	
//	@JsonManagedReference(value="departmentTree")
	@OneToMany(mappedBy = "parent")  
	private Set<Department> children = new HashSet<Department>();
	
	
	@Transient
	@JsonIgnore
	@Info(label="父级id",placeholder="",tip="",help="",secret="")	   
	private Integer parentId;

	@Column(name="province")
	@Info(label="省",placeholder="",tip="",help="",secret="")
	private String province;

	@Column(name="city")
	@Info(label="市",placeholder="",tip="",help="",secret="")
	private String city;

	@Column(name="district")
	@Info(label="区",placeholder="",tip="",help="",secret="")
	private String district;

	@Column(name="linkman")
	@Info(label="联系人",placeholder="",tip="",help="",secret="")
	private String linkman;

	@Column(name="telephone")
	@Info(label="联系电话",placeholder="",tip="",help="",secret="")
	private String telephone;

	public Departmenttype getDepartmenttype() {
		return departmenttype;
	}



	public void setDepartmenttype(Departmenttype departmenttype) {
		this.departmenttype = departmenttype;
	}



	public Set<User> getEmployees() {
		return employees;
	}



	public void setEmployees(Set<User> employees) {
		this.employees = employees;
	}



	public Department getParent() {
		return parent;
	}



	public void setParent(Department parent) {
		this.parent = parent;
	}



	public Set<Department> getChildren() {
		return children;
	}



	public void setChildren(Set<Department> children) {
		this.children = children;
	}



	public Integer getSort() {
		// TODO Auto-generated method stub
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province){
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district){
		this.district = district;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman){
		this.linkman = linkman;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone){
		this.telephone = telephone;
	}

	public Integer getParentId() {
		// TODO Auto-generated method stub
		return parent == null?0:parent.getId();
	}

	public void setParentId(Integer id) {
		this.parentId = parentId;
		
	}




	public Set<Product> getProducts() {
		return products;
	}



	public void setProducts(Set<Product> products) {
		this.products = products;
	}



	public Set<Capitalproduct> getCapitalproducts() {
		return capitalproducts;
	}



	public void setCapitalproducts(Set<Capitalproduct> capitalproducts) {
		this.capitalproducts = capitalproducts;
	}



	@Override
	@JsonIgnore
	@Transient
	public DepartmentLog getLogInstance() {
		// TODO Auto-generated method stub
		return new DepartmentLog();
	}



	@Override
	@JsonIgnore
	@Transient
	public LogRepository getLogRepository() {
		return logRepository;
	}
	@Override
	public void setLogRepository(LogRepository logRepository) {
		Department.logRepository = logRepository;
		
	}


	@JsonIgnore
	@Override
	@Transient
	public AbstractAuditorService getService() {
		// TODO Auto-generated method stub
		return service;
	}



	@Override
	public void setService(AbstractAuditorService service) {
		// TODO Auto-generated method stub
		Department.service = service;
	}



	public Enterprise getEnterprise() {
		return enterprise;
	}



	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}














}