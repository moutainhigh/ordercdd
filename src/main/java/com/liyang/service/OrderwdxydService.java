package com.liyang.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.liyang.domain.department.Departmenttype;
import com.liyang.domain.department.DepartmenttypeRepository;
import com.liyang.util.CommonUtil;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject;
import com.liyang.util.WechatImageAsyncFetchEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.orderwdxyd.Orderwdxyd;
import com.liyang.domain.orderwdxyd.OrderwdxydAct;
import com.liyang.domain.orderwdxyd.OrderwdxydActRepository;
import com.liyang.domain.orderwdxyd.OrderwdxydFile;
import com.liyang.domain.orderwdxyd.OrderwdxydLog;
import com.liyang.domain.orderwdxyd.OrderwdxydLogRepository;
import com.liyang.domain.orderwdxyd.OrderwdxydRepository;
import com.liyang.domain.orderwdxyd.OrderwdxydState;
import com.liyang.domain.orderwdxyd.OrderwdxydStateRepository;
import com.liyang.domain.orderwdxyd.OrderwdxydWorkflow;
import com.liyang.domain.orderwdxyd.OrderwdxydWorkflowRepository;
import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.base.AbstractWorkflowAct.ActType;
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
@Order(37)
public class OrderwdxydService extends AbstractWorkflowService<Orderwdxyd, OrderwdxydWorkflow, OrderwdxydAct, OrderwdxydState, OrderwdxydLog, OrderwdxydFile>{
	@Autowired
	OrderwdxydActRepository orderwdxydActRepository;
	
	@Autowired
	OrderwdxydStateRepository orderwdxydStateRepository;
	
	@Autowired
	OrderwdxydLogRepository  personLogRepository;
	
	@Autowired
	OrderwdxydRepository  orderwdxydRepository;

	@Autowired
	RoleRepository roleRepository;

	@Value("${spring.wlqz.wechat.OPEN_ACC_APPLY}")
	private String OPEN_ACC_APPLY;
	@Autowired
	OrderwdxydWorkflowRepository orderwdxydWorkflowRepository;

	@Autowired
	DepartmenttypeRepository departmenttypeRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	WlqzWechatPubService wechatPubService;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	PersonStateRepository personStateRepository;
	@Override
	@PostConstruct 
	public void sqlInit() {
		long count  = orderwdxydActRepository.count();
		if(count==0){
			
			
			OrderwdxydAct save1 = orderwdxydActRepository.save(new OrderwdxydAct("创建","create",10,ActGroup.UPDATE));
			OrderwdxydAct save2 = orderwdxydActRepository.save(new OrderwdxydAct("读取","read",20,ActGroup.READ));
			OrderwdxydAct save3 = orderwdxydActRepository.save(new OrderwdxydAct("更新","update",30,ActGroup.UPDATE));
			OrderwdxydAct save4 = orderwdxydActRepository.save(new OrderwdxydAct("删除","delete",40,ActGroup.UPDATE));
			OrderwdxydAct save5 = orderwdxydActRepository.save(new OrderwdxydAct("自己的列表","listOwn",50,ActGroup.READ));
			OrderwdxydAct save6 = orderwdxydActRepository.save(new OrderwdxydAct("部门的列表","listOwnDepartment",60,ActGroup.READ));
			OrderwdxydAct save7 = orderwdxydActRepository.save(new OrderwdxydAct("部门和下属部门的列表","listOwnDepartmentAndChildren",70,ActGroup.READ));
			OrderwdxydAct save8 = orderwdxydActRepository.save(new OrderwdxydAct("通知其他人","noticeOther",80,ActGroup.NOTICE));
			OrderwdxydAct save9 = orderwdxydActRepository.save(new OrderwdxydAct("通知操作者","noticeActUser",90,ActGroup.NOTICE));
			OrderwdxydAct save10 = orderwdxydActRepository.save(new OrderwdxydAct("通知展示人","noticeShowUser",100,ActGroup.NOTICE));
			
	
			OrderwdxydState newState =new OrderwdxydState("已创建",0,"CREATED");
			newState = orderwdxydStateRepository.save(newState);
			
			OrderwdxydState enableState = new OrderwdxydState("有效",100,"ENABLED");
			enableState.setActs(Arrays.asList(save1,save2,save3,save4,save5,save6,save7).stream().collect(Collectors.toSet()));
			orderwdxydStateRepository.save(enableState);
			orderwdxydStateRepository.save(new OrderwdxydState("无效",200,"DISABLED"));
			orderwdxydStateRepository.save(new OrderwdxydState("删除",300,"DELETED"));
			
		}
		
	}

	@Override
	public LogRepository<OrderwdxydLog> getLogRepository() {
		// TODO Auto-generated method stub
		return personLogRepository;
	}

	@Override
	public AuditorEntityRepository<Orderwdxyd> getAuditorEntityRepository() {
		
		return orderwdxydRepository;
	}


	@Override
	@PostConstruct 
	public void injectLogRepository() {
		new Orderwdxyd().setLogRepository(personLogRepository);
		
	}

	@Override
	public OrderwdxydLog getLogInstance() {
		// TODO Auto-generated method stub
		return new OrderwdxydLog();
	}

	@Override
	public ActRepository<OrderwdxydAct> getActRepository() {
		// TODO Auto-generated method stub
		return orderwdxydActRepository;
	}

	@Override
	@PostConstruct 
	public void injectEntityService() {
		new Orderwdxyd().setService(this);
		
	}

	@Override
	public OrderwdxydFile getFileLogInstance() {
		// TODO Auto-generated method stub
		return new OrderwdxydFile();
	}
	/**
	 * 提交申请动作  一次只能允许一次提交，待提交成功后才能进行下一次申请
	 *
	 * @param orderwdxyd
	 */
	public void doCreate(Orderwdxyd orderwdxyd) {
		orderwdxyd.setWorkflow(orderwdxydWorkflowRepository.findOne(1));

		Orderwdxyd findByApplyMobile =orderwdxydRepository .findByApplyMobile(orderwdxyd.getApplyMobile());
		if (null != findByApplyMobile) {
			throw new FailReturnObject(6657, "网点信用贷订单相同手机号" + orderwdxyd.getApplyMobile() + "已存在，请使用不同手机号", ReturnObject.Level.WARNING);
		}
		Orderwdxyd findByApplyIdentityNo = orderwdxydRepository.findByApplyIdentityNo(orderwdxyd.getApplyIdentityNo());
		if (null != findByApplyIdentityNo) {
			throw new FailReturnObject(6657, "网点信用贷订单相同身份证号" + orderwdxyd.getApplyIdentityNo() + "已存在，请使用不同身份证", ReturnObject.Level.WARNING);
		}
		Page<Orderwdxyd> orderwdxydss = orderwdxydRepository.listOwn(null);
		List<Orderwdxyd> list = orderwdxydss.getContent();

		if (orderwdxydss != null && list.size() != 0) {
			for (Orderwdxyd exitOrderwdsjsh : list) {

				if (!(exitOrderwdsjsh.isStateAdopt() || exitOrderwdsjsh.isStateEnable())) {
					System.out.println(exitOrderwdsjsh.getApplyIdentityNo() + ":" + exitOrderwdsjsh.getState().getStateCode() + ":" + exitOrderwdsjsh.getId());
					throw new FailReturnObject(199, "前一个申请还在审核中！", ReturnObject.Level.WARNING);
				}
			}
		}
	}
	@Override
	public OrderwdxydLog fillmultiWechatImageToLog(Orderwdxyd entity, OrderwdxydLog log) {
		// TODO Auto-generated method stub
		String[] wechatFiles = entity.getWechatFiles();
		for (String fileName : wechatFiles) {
			OrderwdxydFile fileLogInstance = getFileLogInstance();
			fileLogInstance.setEntity(entity);
			fileLogInstance.setAct(log.getAct());
			if (entity.getSubcategory() != null) {
				fileLogInstance.setSubcategory(entity.getSubcategory());
			}
			if (entity.getTopcategory() != null) {
				fileLogInstance.setTopcategory(entity.getTopcategory());
			}
			if (CommonUtil.getCurrentUserDepartment() != null) {
				fileLogInstance.setUploaderDepartmenttype(CommonUtil.getCurrentUserDepartment().getDepartmenttype());
			}
			fileLogInstance.setOriginalFileName(String.format(IMAGE_URL_TEMPLATE,
					wechatGetService.getCacheTokenTotal(), fileName));
			fileLogInstance.setUploadType("file");
			fileLogInstance.setLog(log);
			Departmenttype departmentType = departmenttypeRepository.findByDepartmenttypeCode(Departmenttype.DepartmenttypeCode.DEBTOR);
			fileLogInstance.setUploaderDepartmenttype(departmentType);
			OrderwdxydFile file = fileRepository.save(fileLogInstance);
			applicationContext.publishEvent(new WechatImageAsyncFetchEvent(entity, file));
		}
		return log;
	}
	public void doDistribution(Orderwdxyd orderwdxyd) {
		User user = userRepository.findOne(orderwdxyd.getServiceId());
		if(user==null){
			throw new FailReturnObject(1643,"分配的人员不存在！", ReturnObject.Level.INFO);
		}
		orderwdxyd.setServiceName(user.getNickname());
		orderwdxyd.setServiceUser(user);
		orderwdxyd.setDistribution(true);
		System.out.println("--------------" + orderwdxyd.getServiceId());
	}
	public void doFailed(Orderwdxyd orderwdxyd) {
		wechatPubService.pushOpenAccMessage(orderwdxyd.getCreatedBy(), "初步筛选未通过", orderwdxyd.getRealName(), orderwdxyd.getApplyMobile(), OPEN_ACC_APPLY, orderwdxyd.getComment());
	}
	public void doAdopt(Orderwdxyd orderwdsjsh) {
		User user = orderwdsjsh.getCreatedBy();
		if (user == null) {
			throw new FailReturnObject(499, "没有创建人", ReturnObject.Level.WARNING);//lhg
		}

		System.out.println("doAdopt");

		String identity = orderwdsjsh.getApplyIdentityNo();
		Person person = personRepository.findByIdentity(identity);
		if (person == null) {
			person = new Person();
			person = wechatPubService.setPersonField(person, orderwdsjsh);

			person.setState(personStateRepository.findByStateCode("ENABLED"));
			wechatPubService.pushOpenAccMessage(user, "通过初步筛选", person.getName(), person.getTelephone(), OPEN_ACC_APPLY, orderwdsjsh.getComment());
		}
		personRepository.save(person);
		orderwdsjsh.setUser(user);
		user.setPerson(person);
		userRepository.save(user);
		orderwdsjsh.setPerson(person);
	}

}
