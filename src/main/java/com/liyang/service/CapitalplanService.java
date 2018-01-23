package com.liyang.service;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.CharSetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.capitalplan.Capitalplan;
import com.liyang.domain.capitalplan.CapitalplanAct;
import com.liyang.domain.capitalplan.CapitalplanActRepository;
import com.liyang.domain.capitalplan.CapitalplanFile;
import com.liyang.domain.capitalplan.CapitalplanLog;
import com.liyang.domain.capitalplan.CapitalplanLogRepository;
import com.liyang.domain.capitalplan.CapitalplanRepository;
import com.liyang.domain.capitalplan.CapitalplanState;
import com.liyang.domain.capitalplan.CapitalplanStateRepository;
import com.liyang.domain.capitalplan.CapitalplanWorkflow;
import com.liyang.domain.capitalplan.CapitalplanWorkflowRepository;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject.Level;

@Service
public class CapitalplanService extends AbstractWorkflowService<Capitalplan, CapitalplanWorkflow,CapitalplanAct, CapitalplanState, CapitalplanLog, CapitalplanFile>{
	@Autowired
	CapitalplanActRepository capitalplanActRepository;
	@Autowired
	CapitalplanStateRepository capitalplanStateRepository;
	@Autowired
	CapitalplanLogRepository  capitalplanLogRepository;
	@Autowired
	CapitalplanRepository  capitalplanRepository;
	@Autowired
	CapitalplanWorkflowRepository capitalplanWorkflowRepository;

	@Override
	@PostConstruct 
	public void sqlInit() {
		long count  = capitalplanActRepository.count();
		if(count==0){
			CapitalplanAct save1 = capitalplanActRepository.save(new CapitalplanAct("创建","create",10,ActGroup.UPDATE));
			CapitalplanAct save2 = capitalplanActRepository.save(new CapitalplanAct("读取","read",20,ActGroup.READ));
			CapitalplanAct save3 = capitalplanActRepository.save(new CapitalplanAct("更新","update",30,ActGroup.UPDATE));
			CapitalplanAct save4 = capitalplanActRepository.save(new CapitalplanAct("删除","delete",40,ActGroup.UPDATE));
			CapitalplanAct save5 = capitalplanActRepository.save(new CapitalplanAct("自己的列表","listOwn",50,ActGroup.READ));
			CapitalplanAct save6 = capitalplanActRepository.save(new CapitalplanAct("部门的列表","listOwnDepartment",60,ActGroup.READ));
			CapitalplanAct save7 = capitalplanActRepository.save(new CapitalplanAct("部门和下属部门的列表","listOwnDepartmentAndChildren",70,ActGroup.READ));
			CapitalplanAct save8 = capitalplanActRepository.save(new CapitalplanAct("通知其他人","noticeOther",80,ActGroup.NOTICE));
			CapitalplanAct save9 = capitalplanActRepository.save(new CapitalplanAct("通知操作者","noticeActUser",90,ActGroup.NOTICE));
			CapitalplanAct save10 = capitalplanActRepository.save(new CapitalplanAct("通知展示人","noticeShowUser",100,ActGroup.NOTICE));
	
			CapitalplanState newState =new CapitalplanState("已创建",0,"CREATED");
			newState = capitalplanStateRepository.save(newState);
			
			CapitalplanState enableState = new CapitalplanState("有效",100,"ENABLED");
			enableState.setActs(Arrays.asList(save1,save2,save3,save4,save5,save6,save7).stream().collect(Collectors.toSet()));
			capitalplanStateRepository.save(enableState);
			capitalplanStateRepository.save(new CapitalplanState("无效",200,"DISABLED"));
			capitalplanStateRepository.save(new CapitalplanState("已删除",300,"DELETED"));

			capitalplanStateRepository.save(new CapitalplanState("待出账",30,"ACCOUNT"));
			capitalplanStateRepository.save(new CapitalplanState("已出账",20,"ACCOUNTED"));
			capitalplanStateRepository.save(new CapitalplanState("已结清",10,"CLOSED"));
		}
	}

	@Override
	public LogRepository<CapitalplanLog> getLogRepository() {
		return capitalplanLogRepository;
	}

	@Override
	public AuditorEntityRepository<Capitalplan> getAuditorEntityRepository() {
		return capitalplanRepository;
	}


	@Override
	@PostConstruct 
	public void injectLogRepository() {
		new Capitalplan().setLogRepository(capitalplanLogRepository);
	}

	@Override
	public CapitalplanLog getLogInstance() {
		return new CapitalplanLog();
	}

	@Override
	public ActRepository<CapitalplanAct> getActRepository() {
		return capitalplanActRepository;
	}

	@Override
	@PostConstruct 
	public void injectEntityService() {
		new Capitalplan().setService(this);
		
	}

	@Override
	public CapitalplanFile getFileLogInstance() {
		return new CapitalplanFile();
	}
	
	/**计划结清
	 * @param capitalplan
	 */
	public void doFinish(Capitalplan capitalplan) {
		//激活下一个还款计划
		String planSn = capitalplan.getPlanSn();
		String ordercddLoanSn = capitalplan.getOrdercddloan().getOrdercddloanSn();
		int poi = ordercddLoanSn.length();
		String sn = planSn.substring(poi);
		//得到的应该是四位数
		int tail = 0;
		try {
			Integer snInteger = Integer.valueOf(sn);
			tail = snInteger.intValue();
			tail += 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FailReturnObject(2563, "激活下一个计划失败", Level.ERROR);
		}
		String nextSn = String.valueOf(tail);
		while (nextSn.length() < sn.length()) {
			nextSn = "0" + nextSn;
		}
		Capitalplan next = capitalplanRepository.findByPlanSn(ordercddLoanSn + nextSn);
		if (next != null) {
			CapitalplanState accounted = capitalplanStateRepository.findByStateCode("ACCOUNTED");
			next.setState(accounted);
			capitalplanRepository.save(next);
		}
		//结清时间
		CapitalplanState state = capitalplanStateRepository.findByStateCode("CLOSED");
		capitalplan.setState(state);
		capitalplan.setFinishTime(new Date());
		capitalplanRepository.save(capitalplan);
	}
}
