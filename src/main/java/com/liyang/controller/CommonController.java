package com.liyang.controller;

import java.util.ArrayList;
import java.util.List;

import com.liyang.domain.department.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liyang.domain.department.Department;
import com.liyang.domain.role.Role;
import com.liyang.domain.user.User;
import com.liyang.domain.user.UserRepository;
import com.liyang.service.FileUploadService.OssFile;
import com.liyang.service.UserService;
import com.liyang.service.WechatLoginService;
import com.liyang.util.CommonUtil;
import com.liyang.util.SuccessReturnObject;
import com.liyang.util.TreeNodeImpl;

@Controller
public class CommonController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	@Autowired
	DepartmentRepository departmentRepository;
	@RequestMapping(value="/childrenDepartments")
	@ResponseBody
	public List ChildrenDepartment(){
		
		List<Department> ownAndChildrenDepartments = CommonUtil.ownAndChildrenDepartments(CommonUtil.getCurrentUserDepartment());
		
		return CommonUtil.listToTree(CommonUtil.ownAndChildrenDepartments(CommonUtil.getCurrentUserDepartment()));
	}
	
	@RequestMapping(value="/linkman")
	@ResponseBody
	public List<LocalUser>linkman(){
//		System.out.println(CommonUtil.ownAndChildrenDepartments(CommonUtil.getCurrentUserDepartment()));
//		List<User> findByDepartmentIn = userRepository.findByDepartmentIn(CommonUtil.ownAndChildrenDepartments(CommonUtil.getCurrentUserDepartment()));
		List<User> findByDepartmentIn = userRepository.findByDepartmentIn(CommonUtil.ownAndChildrenDepartments(departmentRepository.findOne(CommonUtil.getCurrentUserDepartment().getId())));
		ArrayList<LocalUser> arrayList = new ArrayList<LocalUser>();
		
		for (User user : findByDepartmentIn) {
			LocalUser localUser = new LocalUser();
			localUser.setId(user.getId());
			localUser.setHeadimgurl(user.getHeadimgurl());
			localUser.setNickname(user.getNickname());
			localUser.setRole(user.getRole().getLabel());
			localUser.setSex(user.getSex());
			localUser.setUnionid(user.getUnionid());
			localUser.setOpenid(user.getOpenid());
			localUser.setUsername(user.getUsername());
			localUser.setDepartment(user.getDepartment().getLabel());
			arrayList.add(localUser);
		}
		return arrayList;
	}
	
	

	class LocalUser  {
		private String openid;
		private Integer id;
		private String nickname;
		private String username;
		private Integer sex;
		private String headimgurl;
		private String unionid;
		private String role;
		private String department;
		
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public Integer getId() {
			// TODO Auto-generated method stub
			return this.id;
		}
		
		public String getNickname() {
			// TODO Auto-generated method stub
			return this.nickname;
		}

		public String getRole() {
			// TODO Auto-generated method stub
			return this.role;
		}

		public Integer getSex() {
			// TODO Auto-generated method stub
			return this.sex;
		}

		public String getHeadimgurl() {
			// TODO Auto-generated method stub
			return this.headimgurl;
		}

		public String getUnionid() {
			// TODO Auto-generated method stub
			return this.unionid;
		}

		public String getGetNickname() {
			return nickname;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public void setSex(Integer sex) {
			this.sex = sex;
		}

		public void setHeadimgurl(String headimgurl) {
			this.headimgurl = headimgurl;
		}

		public void setUnionid(String unionid) {
			this.unionid = unionid;
		}

		public void setRole(String role) {
			this.role = role;
		}
		
	}
	

}
