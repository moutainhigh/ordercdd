package com.liyang.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.loan.Loan;
import com.liyang.domain.loan.LoanRepository;
import com.liyang.domain.loan.LoanState;
import com.liyang.domain.loan.LoanStateRepository;
import com.liyang.domain.loanexception.Loanexception;
import com.liyang.domain.loanexception.LoanexceptionAct;
import com.liyang.domain.loanexception.LoanexceptionActRepository;
import com.liyang.domain.loanexception.LoanexceptionFile;
import com.liyang.domain.loanexception.LoanexceptionLog;
import com.liyang.domain.loanexception.LoanexceptionLogRepository;
import com.liyang.domain.loanexception.LoanexceptionRepository;
import com.liyang.domain.loanexception.LoanexceptionState;
import com.liyang.domain.loanexception.LoanexceptionStateRepository;
import com.liyang.domain.loanexception.LoanexceptionWorkflow;
import com.liyang.domain.loanexception.LoanexceptionWorkflowRepository;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject.Level;

@Service
public class LoanexceptionService 
		extends AbstractWorkflowService<Loanexception, LoanexceptionWorkflow,LoanexceptionAct, LoanexceptionState, LoanexceptionLog, LoanexceptionFile>{
	@Autowired
	LoanexceptionActRepository loanexceptionActRepository;
	@Autowired
	LoanexceptionStateRepository loanexceptionStateRepository;
	@Autowired
	LoanexceptionLogRepository  loanexceptionLogRepository;
	@Autowired
	LoanexceptionRepository  loanexceptionRepository;
	@Autowired
	LoanexceptionWorkflowRepository loanexceptionWorkflowRepository;
	@Autowired
	LoanRepository loanRepository;
	@Autowired
	LoanStateRepository loanStateRepository;

	@Override
	@PostConstruct
	public void sqlInit() {
		long count  = loanexceptionActRepository.count();
		if(count==0){
			LoanexceptionAct save1 = loanexceptionActRepository.save(new LoanexceptionAct("创建","create",10,ActGroup.UPDATE));
			LoanexceptionAct save2 = loanexceptionActRepository.save(new LoanexceptionAct("读取","read",20,ActGroup.READ));
			LoanexceptionAct save3 = loanexceptionActRepository.save(new LoanexceptionAct("更新","update",30,ActGroup.UPDATE));
			LoanexceptionAct save4 = loanexceptionActRepository.save(new LoanexceptionAct("删除","delete",40,ActGroup.UPDATE));
			LoanexceptionAct save5 = loanexceptionActRepository.save(new LoanexceptionAct("自己的列表","listOwn",50,ActGroup.READ));
			LoanexceptionAct save6 = loanexceptionActRepository.save(new LoanexceptionAct("部门的列表","listOwnDepartment",60,ActGroup.READ));
			LoanexceptionAct save7 = loanexceptionActRepository.save(new LoanexceptionAct("部门和下属部门的列表","listOwnDepartmentAndChildren",70,ActGroup.READ));
			LoanexceptionAct save8 = loanexceptionActRepository.save(new LoanexceptionAct("通知其他人","noticeOther",80,ActGroup.NOTICE));
			LoanexceptionAct save9 = loanexceptionActRepository.save(new LoanexceptionAct("通知操作者","noticeActUser",90,ActGroup.NOTICE));
			LoanexceptionAct save10 = loanexceptionActRepository.save(new LoanexceptionAct("通知展示人","noticeShowUser",100,ActGroup.NOTICE));
	
			LoanexceptionState newState =new LoanexceptionState("已创建",0,"CREATED");
			newState = loanexceptionStateRepository.save(newState);
			
			LoanexceptionState enableState = new LoanexceptionState("有效",100,"ENABLED");
			enableState.setActs(Arrays.asList(save1,save2,save3,save4,save5,save6,save7).stream().collect(Collectors.toSet()));
			loanexceptionStateRepository.save(enableState);
			loanexceptionStateRepository.save(new LoanexceptionState("无效",200,"DISABLED"));
			loanexceptionStateRepository.save(new LoanexceptionState("已删除",300,"DELETED"));

			loanexceptionStateRepository.save(new LoanexceptionState("未处理",30,"ACCOUNT"));
			loanexceptionStateRepository.save(new LoanexceptionState("已处理",20,"ACCOUNTED"));
			loanexceptionStateRepository.save(new LoanexceptionState("转坏账",10,"BADDEBT"));
		}
	}

	@Override
	public LogRepository<LoanexceptionLog> getLogRepository() {
		return loanexceptionLogRepository;
	}

	@Override
	public AuditorEntityRepository<Loanexception> getAuditorEntityRepository() {
		return loanexceptionRepository;
	}


	@Override
	@PostConstruct 
	public void injectLogRepository() {
		new Loanexception().setLogRepository(loanexceptionLogRepository);
	}

	@Override
	public LoanexceptionLog getLogInstance() {
		return new LoanexceptionLog();
	}

	@Override
	public ActRepository<LoanexceptionAct> getActRepository() {
		return loanexceptionActRepository;
	}

	@Override
	@PostConstruct 
	public void injectEntityService() {
		new Loanexception().setService(this);
		
	}

	@Override
	public LoanexceptionFile getFileLogInstance() {
		return new LoanexceptionFile();
	}
	
	/**创建一条异常催收记录
	 * @param loan
	 * @param debtorRepayAmount
	 * @param debtorLeftAmount
	 */
	public void create(Loan loan,BigDecimal debtorRepayAmount,BigDecimal debtorLeftAmount) {
		Loanexception before = loanexceptionRepository.findByEnable(loan.getId());
		if (before != null) {
			throw new FailReturnObject(1, "已存在一个未解决的异常记录，请先解决之前的异常", Level.ERROR);
		}
		Loanexception loanexception = new Loanexception();
		loanexception.setLoan(loan);
		loanexception.setDebtorLeftAmount(debtorLeftAmount);
		loanexception.setDebtorRepayAmount(debtorRepayAmount);
		loanexception.setExceptionRemark(loan.getExceptionRemark());
		
		LoanexceptionState state = loanexceptionStateRepository.findByStateCode("ACCOUNT");
		loanexception.setState(state);
		loanexceptionRepository.save(loanexception);
	}
	
	/**异常催收转坏账
	 * @param loan
	 */
	public void baddebt(Loan loan) {
		Loanexception loanexception = loanexceptionRepository.findByEnable(loan.getId());
		if (loanexception == null) {//如果异常催收不为空
			return;
		}
		LoanexceptionState state = loanexceptionStateRepository.findByStateCode("BADDEBT");
		loanexception.setState(state);
		loanexception.setFinishAt(new Date());
		loanexception.setHandleRemark(loan.getHandleRemark());
		loanexceptionRepository.save(loanexception);
	}
	
	/**异常催收，正常处理结束
	 * 一般一条贷款loan，对应一个处理中的异常催收
	 * @param loan
	 * @return
	 */
	public Loanexception doExceptionover(Loanexception loanexception) {
		if (loanexception == null) {
			throw new FailReturnObject(1, "loanexception不能是null", Level.ERROR);
		}
		loanexception.setFinishAt(new Date());
		loanexceptionRepository.save(loanexception);
		
		Loan loan = loanexception.getLoan();
//		LoanState state = loanStateRepository.findByStateCode("LENDERS");
//		loan.setState(state);
		loan.setIsException(false);
		loanRepository.save(loan);
		return loanexception;
	}
}
