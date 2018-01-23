package com.liyang.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.ordercdd.OrdercddRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyang.domain.bank.BankRepository;
import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.creditcard.Creditcard;
import com.liyang.domain.creditcard.CreditcardAct;
import com.liyang.domain.creditcard.CreditcardActRepository;
import com.liyang.domain.creditcard.CreditcardFile;
import com.liyang.domain.creditcard.CreditcardLog;
import com.liyang.domain.creditcard.CreditcardLogRepository;
import com.liyang.domain.creditcard.CreditcardRepository;
import com.liyang.domain.creditcard.CreditcardState;
import com.liyang.domain.creditcard.CreditcardStateRepository;
import com.liyang.domain.creditcard.CreditcardWorkflow;
import com.liyang.domain.orderwdsjsh.Orderwdsjsh;
import com.liyang.domain.orderwdsjsh.OrderwdsjshRepository;
import com.liyang.domain.person.Person;
import com.liyang.domain.person.PersonRepository;
import com.liyang.domain.person.PersonStateRepository;

@Service
@Order(10043)
public class CreditcardService
		extends AbstractWorkflowService<Creditcard,CreditcardWorkflow, CreditcardAct,CreditcardState,CreditcardLog,CreditcardFile> {

	@Value("${spring.wlqz.wechat.OPEN_ACC_SUCCESS}")
	private String OPEN_ACC_SUCCESS;
	
	@Autowired
	CreditcardActRepository creditcardActRepository;
	@Autowired
	CreditcardLogRepository creditcardLogRepository;
	@Autowired
	CreditcardRepository creditcardRepository;
	@Autowired
	CreditcardStateRepository creditcardStateRepository;
	@Autowired
	WlqzWechatPubService wechatPubService;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	BankRepository bankRepository;
	@Autowired
	PersonStateRepository personStateRepository;
	@Autowired
	OrderwdsjshRepository orderwdsjshRepository;
	@Autowired
	OrderwdsjshService orderwdsjshService;
	@Autowired
	OrdercddRepository ordercddRepository;
	
	@Override
	@PostConstruct
	public void sqlInit() {
		long count  = creditcardActRepository.count();
		if (count==0) {

			CreditcardAct save1 = creditcardActRepository.save(new CreditcardAct("创建", "create", 10, ActGroup.UPDATE));
			CreditcardAct save2 = creditcardActRepository.save(new CreditcardAct("读取", "read", 20, ActGroup.READ));
			CreditcardAct save3 = creditcardActRepository.save(new CreditcardAct("更新", "update", 30, ActGroup.UPDATE));
			CreditcardAct save4 = creditcardActRepository.save(new CreditcardAct("删除", "delete", 40, ActGroup.UPDATE));
			CreditcardAct save5 = creditcardActRepository.save(new CreditcardAct("自己的列表", "listOwn", 50, ActGroup.READ));
			CreditcardAct save6 = creditcardActRepository
					.save(new CreditcardAct("部门的列表", "listOwnDeparment", 60, ActGroup.READ));
			CreditcardAct save7 = creditcardActRepository
					.save(new CreditcardAct("部门和下属部门的列表", "listOwnDeparmentAndChildren", 70, ActGroup.READ));
			CreditcardAct save8 = creditcardActRepository
					.save(new CreditcardAct("通知其他人", "noticeOther", 80, ActGroup.NOTICE));
			CreditcardAct save9 = creditcardActRepository
					.save(new CreditcardAct("通知给操作者", "noticeActUser", 90, ActGroup.NOTICE));
			CreditcardAct save10 = creditcardActRepository
					.save(new CreditcardAct("通知给展示人", "noticeShowUser", 100, ActGroup.NOTICE));

			creditcardStateRepository.save(new CreditcardState("已创建", 0, "CREATED"));
			CreditcardState CreditcardState = new CreditcardState("有效", 100, "ENABLED");
			CreditcardState.setActs(Arrays.asList(save1, save2, save3, save4, save5, save6, save7).stream()
					.collect(Collectors.toSet()));
			creditcardStateRepository.save(CreditcardState);
			creditcardStateRepository.save(new CreditcardState("无效", 200, "DISABLED"));
			creditcardStateRepository.save(new CreditcardState("已删除", 300, "DELETED"));
		}
	}

	@Override
	public LogRepository<CreditcardLog> getLogRepository() {
		return creditcardLogRepository;
	}

	@Override
	public AuditorEntityRepository<Creditcard> getAuditorEntityRepository() {
		return creditcardRepository;
	}

	@Override
	@PostConstruct
	public void injectLogRepository() {
		new Creditcard().setLogRepository(creditcardLogRepository);

	}

	@Override
	public CreditcardLog getLogInstance() {
		return new CreditcardLog();
	}

	@Override
	public ActRepository<CreditcardAct> getActRepository() {
		return creditcardActRepository;
	}

	@Override
	@PostConstruct
	public void injectEntityService() {
		new Creditcard().setService(this);

	}

	@Override
	public CreditcardFile getFileLogInstance() {
		return new CreditcardFile();
	}
	
	
	/**
	 * 开户 orderwdsjsh CREATED/DENIED-->ENABLED     person CREATED--->ENABLED
	 * @param creditcard
	 */
	@Transactional
	public void save(Creditcard creditcard,String beidouMessge) {
//	 	Person person=personRepository.getByTelephoneAndStateCode(creditcard.getCreditcardIdentity()+"","ENABLED");
		Person person=personRepository.findByTelephone(creditcard.getCreditcardIdentity());
	 	person.setState(personStateRepository.findByStateCode("ENABLED"));
	 	orderwdsjshService.adoptApply(creditcard.getCreditcardIdentity()+"");
	 	creditcard.setState(creditcardStateRepository.findByStateCode("ENABLED"));
	 	Orderwdsjsh orderwdsjsh=orderwdsjshRepository.getByTelephoneAndStatCode(creditcard.getCreditcardIdentity()+"", "ENABLED");
	 	
	 	creditcardRepository.save(creditcard);
	 	if (orderwdsjsh!=null) {
			orderwdsjsh.setDebtorCreditcard(creditcard);
		}
		
		wechatPubService.pushOpenAccMessage(orderwdsjsh.getCreatedBy(), "申请通过", person.getName(), person.getTelephone(),OPEN_ACC_SUCCESS,beidouMessge);
	}
	@Transactional(readOnly=true)
	public Creditcard findByCreditcardIdentity(String identity) {
		return creditcardRepository.findByCreditcardIdentity(identity);
	}
	/**
	 * 开户 orderwdsjsh CREATED/DENIED-->ENABLED     person CREATED--->ENABLED
	 * @param creditcard
	 */
	@Transactional//从车抵贷来的保存
	public void saveFromOrdercdd(Creditcard creditcard,Ordercdd ordercdd,String message) {
		Person person=personRepository.findByIdentity(creditcard.getCreditcardIdentity());
		creditcard.setState(creditcardStateRepository.findByStateCode("ENABLED"));
		creditcardRepository.save(creditcard);
		if (ordercdd!=null) {
			ordercdd.setDebtorCreditcard(creditcard);
		}
		wechatPubService.pushOpenAccMessage(ordercdd.getUser(), "申请通过", person.getName(), person.getTelephone(),null,message);
	}
}
