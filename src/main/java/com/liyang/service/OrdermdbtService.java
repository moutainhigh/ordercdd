package com.liyang.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.account.Account;
import com.liyang.domain.account.AccountRepository;
import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.base.AbstractWorkflowAct.ActType;
import com.liyang.domain.ordermdbt.Ordermdbt;
import com.liyang.domain.ordermdbt.OrdermdbtAct;
import com.liyang.domain.ordermdbt.OrdermdbtActRepository;
import com.liyang.domain.ordermdbt.OrdermdbtFile;
import com.liyang.domain.ordermdbt.OrdermdbtLog;
import com.liyang.domain.ordermdbt.OrdermdbtLogRepository;
import com.liyang.domain.ordermdbt.OrdermdbtRepository;
import com.liyang.domain.ordermdbt.OrdermdbtState;
import com.liyang.domain.ordermdbt.OrdermdbtStateRepository;
import com.liyang.domain.ordermdbt.OrdermdbtWorkflow;
import com.liyang.domain.ordermdbt.OrdermdbtWorkflowRepository;
import com.liyang.domain.orderwdsjsh.Orderwdsjsh;
import com.liyang.domain.orderwdsjsh.OrderwdsjshAct;
import com.liyang.domain.person.Person;
import com.liyang.domain.person.PersonAct;
import com.liyang.domain.person.PersonActRepository;
import com.liyang.domain.person.PersonFile;
import com.liyang.domain.person.PersonLog;
import com.liyang.domain.person.PersonLogRepository;
import com.liyang.domain.person.PersonRepository;
import com.liyang.domain.person.PersonState;
import com.liyang.domain.person.PersonStateRepository;
import com.liyang.domain.person.PersonWorkflow;
import com.liyang.domain.person.PersonWorkflowRepository;
import com.liyang.domain.role.Role;
import com.liyang.domain.role.RoleRepository;
import com.liyang.domain.user.User;
import com.liyang.domain.user.UserAct;
import com.liyang.domain.user.UserActRepository;
import com.liyang.domain.user.UserLog;
import com.liyang.domain.user.UserLogRepository;
import com.liyang.domain.user.UserRepository;
import com.liyang.domain.user.UserState;
import com.liyang.domain.user.UserStateRepository;
import com.liyang.domain.user.UserWorkflow;
import com.liyang.domain.user.UserWorkflowRepository;
@Service
@Order(34)
public class OrdermdbtService extends AbstractWorkflowService<com.liyang.domain.ordermdbt.Ordermdbt, OrdermdbtWorkflow, com.liyang.domain.ordermdbt.OrdermdbtAct, OrdermdbtState, OrdermdbtLog, OrdermdbtFile>{
	@Autowired
	OrdermdbtActRepository orderwdsjshActRepository;
	
	@Autowired
	OrdermdbtStateRepository ordermdbtStateRepository;
	
	@Autowired
	OrdermdbtLogRepository  ordermdbtLogRepository;
	
	@Autowired
	OrdermdbtRepository  ordermdbtRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	
	@Autowired
	OrdermdbtWorkflowRepository ordermdbtWorkflowRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	AccountRepository accountRepository;
	@Override
	@PostConstruct 
	public void sqlInit() {
		long count  = orderwdsjshActRepository.count();
		if(count==0){

			OrdermdbtAct save1 = orderwdsjshActRepository.save(new OrdermdbtAct("创建","create",10,ActGroup.UPDATE));
			OrdermdbtAct save2 = orderwdsjshActRepository.save(new OrdermdbtAct("读取","read",20,ActGroup.READ));
			OrdermdbtAct save3 = orderwdsjshActRepository.save(new OrdermdbtAct("更新","update",30,ActGroup.UPDATE));
			OrdermdbtAct save4 = orderwdsjshActRepository.save(new OrdermdbtAct("删除","delete",40,ActGroup.UPDATE));
			OrdermdbtAct save5 = orderwdsjshActRepository.save(new OrdermdbtAct("自己的列表","listOwn",50,ActGroup.READ));
			OrdermdbtAct save6 = orderwdsjshActRepository.save(new OrdermdbtAct("部门的列表","listOwnDepartment",60,ActGroup.READ));
			OrdermdbtAct save7 = orderwdsjshActRepository.save(new OrdermdbtAct("部门和下属部门的列表","listOwnDepartmentAndChildren",70,ActGroup.READ));
			OrdermdbtAct save8 = orderwdsjshActRepository.save(new OrdermdbtAct("通知其他人","noticeOther",80,ActGroup.NOTICE));
			OrdermdbtAct save9 = orderwdsjshActRepository.save(new OrdermdbtAct("通知操作者","noticeActUser",90,ActGroup.NOTICE));
			OrdermdbtAct save10 = orderwdsjshActRepository.save(new OrdermdbtAct("通知展示人","noticeShowUser",100,ActGroup.NOTICE));
			
	
			OrdermdbtState newState =new OrdermdbtState("已创建",0,"CREATED");
			newState = ordermdbtStateRepository.save(newState);
			OrdermdbtState enableState = new OrdermdbtState("有效",100,"ENABLED");
			enableState.setActs(Arrays.asList(save1,save2,save3,save4,save5,save6,save7).stream().collect(Collectors.toSet()));
			ordermdbtStateRepository.save(enableState);
			ordermdbtStateRepository.save(new OrdermdbtState("无效",200,"DISABLED"));
			ordermdbtStateRepository.save(new OrdermdbtState("删除",300,"DELETED"));
			
		}
		
	}

	@Override
	public LogRepository<OrdermdbtLog> getLogRepository() {
		// TODO Auto-generated method stub
		return ordermdbtLogRepository;
	}

	@Override
	public AuditorEntityRepository<Ordermdbt> getAuditorEntityRepository() {
		
		return ordermdbtRepository;
	}


	@Override
	@PostConstruct 
	public void injectLogRepository() {
		new Ordermdbt().setLogRepository(ordermdbtLogRepository);
		
	}

	@Override
	public OrdermdbtLog getLogInstance() {
		// TODO Auto-generated method stub
		return new OrdermdbtLog();
	}

	@Override
	public ActRepository<OrdermdbtAct> getActRepository() {
		// TODO Auto-generated method stub
		return orderwdsjshActRepository;
	}

	@Override
	@PostConstruct 
	public void injectEntityService() {
		new Ordermdbt().setService(this);
		
	}
	@Override
	public OrdermdbtFile getFileLogInstance() {
		// TODO Auto-generated method stub
		return new OrdermdbtFile();
	}
	@Autowired
	PersonStateRepository personStateRepository;
	@Autowired
	UserRepository userRepository;
	/**
	 * mdbt申请通过创建账户
	 * @param ordermdbt
	 * @param ordermdbtAct
	 * @param linked
	 */
//	public void doAdopt(Ordermdbt ordermdbt) {
//		Account account=new Account();
//		account.setAccountIdentity(null);
//		accountRepository.save(account);
//		Person person=personRepository.findByIdentity(ordermdbt.getApplyIdentityNo());
//		if (person==null) {
//			person=new Person();
//			person=WechatPub.setPersonField(person, ordermdbt);
//			person.setState(personStateRepository.findByStateCode("CREATED"));
//		}
//		personRepository.save(person);
//		User user=ordermdbt.getCreatedBy();
//		if(user!=null) {
//			ordermdbt.setUser(user);
//			user.setPerson(person);
//			userRepository.save(user);
//		}
//		ordermdbt.setPerson(person);
//		ordermdbt.setDebtorAccount(account);
//	}
}
