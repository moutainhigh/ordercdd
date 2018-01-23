package com.liyang.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.creditcard.Creditcard;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.department.DepartmentRepository;
import com.liyang.domain.feebackup.Feebackup;
import com.liyang.domain.feebackup.Feebackup.BACKUPSOURCE;
import com.liyang.domain.loan.LoanAct;
import com.liyang.domain.loan.LoanUtil;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.ordercdd.OrdercddRepository;
import com.liyang.domain.product.Product;
import com.liyang.domain.storebonus.StoreBonus;
import com.liyang.domain.storebonus.StoreBonusAct;
import com.liyang.domain.storebonus.StoreBonusActRepository;
import com.liyang.domain.storebonus.StoreBonusFile;
import com.liyang.domain.storebonus.StoreBonusLog;
import com.liyang.domain.storebonus.StoreBonusLogRepository;
import com.liyang.domain.storebonus.StoreBonusRepository;
import com.liyang.domain.storebonus.StoreBonusState;
import com.liyang.domain.storebonus.StoreBonusStateRepository;
import com.liyang.domain.storebonus.StoreBonusWorkflow;
import com.liyang.domain.user.UserRepository;
import com.liyang.domain.user.userProjection;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject.Level;
import com.liyang.util.SerialNumberUtil;

@Service
public class StoreBonusService extends AbstractWorkflowService<StoreBonus, StoreBonusWorkflow, StoreBonusAct, StoreBonusState, StoreBonusLog, StoreBonusFile> {
	@Resource
	private StoreBonusActRepository storeBonusActRepository;
	@Resource
	private StoreBonusStateRepository storeBonusStateRepository;
	@Resource
	private StoreBonusLogRepository storeBonusLogRepository;
	@Resource
	private StoreBonusRepository storeBonusRepository;
	@Resource
	private OrdercddRepository ordercddRepository;
	@Resource
	private DepartmentRepository departmentRepository;
	@Resource
	private UserRepository userRepository;
    
    @Override
    @PostConstruct
    public void sqlInit() {
		long count  = storeBonusActRepository.count();
        if (count==0) {


        	StoreBonusAct save1 = storeBonusActRepository.save(new StoreBonusAct("创建", "create", 10, ActGroup.UPDATE));
        	StoreBonusAct save2 = storeBonusActRepository.save(new StoreBonusAct("读取", "read", 20, ActGroup.READ));
        	StoreBonusAct save3 = storeBonusActRepository.save(new StoreBonusAct("更新", "update", 30, ActGroup.UPDATE));
        	StoreBonusAct save4 = storeBonusActRepository.save(new StoreBonusAct("删除", "delete", 40, ActGroup.UPDATE));
        	StoreBonusAct save5 = storeBonusActRepository.save(new StoreBonusAct("自己的列表", "listOwn", 50, ActGroup.READ));
        	StoreBonusAct save6 = storeBonusActRepository.save(new StoreBonusAct("部门的列表", "listOwnDepartment", 60, ActGroup.READ));
        	StoreBonusAct save7 = storeBonusActRepository
					.save(new StoreBonusAct("部门和下属部门的列表", "listOwnDepartmentAndChildren", 70, ActGroup.READ));
			
        	StoreBonusState newState = new StoreBonusState("已创建", 0, "CREATED");
            newState = storeBonusStateRepository.save(newState);
            StoreBonusState enableState = new StoreBonusState("已放款", 100, "ENABLED");
            enableState.setActs(Arrays.asList(save1, save2, save3, save4, save5, save6, save7).stream().collect(Collectors.toSet()));
            storeBonusStateRepository.save(enableState);
            storeBonusStateRepository.save(new StoreBonusState("无效", 200, "DISABLED"));
            storeBonusStateRepository.save(new StoreBonusState("删除", 300, "DELETED"));
            StoreBonusState notLend = new StoreBonusState("未放款",101,"NOTLEND");
            notLend.setActs(Arrays.asList(save1,save2,save3,save4, save5, save6, save7).stream().collect(Collectors.toSet()));
        }
    }

    @Override
    public LogRepository<StoreBonusLog> getLogRepository() {
        return storeBonusLogRepository;
    }

    @Override
    public AuditorEntityRepository<StoreBonus> getAuditorEntityRepository() {

        return storeBonusRepository;
    }


    @Override
    @PostConstruct
    public void injectLogRepository() {
        new StoreBonus().setLogRepository(storeBonusLogRepository);

    }

    @Override
    public StoreBonusLog getLogInstance() {
        return new StoreBonusLog();
    }

    @Override
    public ActRepository<StoreBonusAct> getActRepository() {
        return storeBonusActRepository;
    }

    @Override
    @PostConstruct
    public void injectEntityService() {
        new StoreBonus().setService(this);

    }

    @Override
    public StoreBonusFile getFileLogInstance() {
        return new StoreBonusFile();
    }
    
    /**设置门店分红无效，转坏账使用
     * @param ordercddId
     */
    public void disabledState(Integer ordercddId) {
		if (ordercddId == null) {
			return;
		}
		StoreBonus storeBonus = storeBonusRepository.findByOrdercddId(ordercddId);
		if (storeBonus == null) {
			return;
		}
		StoreBonusState state = storeBonusStateRepository.findByStateCode("DISABLED");//徐晓
		storeBonus.setState(state);
		storeBonusRepository.save(storeBonus);
	}

    /**
     * 下载所有图片（凭证）
     * @param storeBonus
     */
    public void doDownloadfile(StoreBonus storeBonus) {
        System.out.println("门店分红StoreBonus下载所有图片开始--------------------------------------");
        downloadAllFilesAnduploadToAliyun(storeBonus);
    }
    
    /**
     * 分红确认
     */
    public void doBonus(StoreBonus storeBonus) {
    	storeBonus.setBonusTime(new Date());
    	storeBonusRepository.save(storeBonus);
    }
    
    /**计算单笔订单，门店的分红
     * 实际门店放款金额=产品匹配时的收费（包括一次性和分期的）+贷款金额*（给客户的年利息-给门店的年利息）/12*分期数  -我们向门店收取的费用（不会有分期的情况）
     * 暂时只考虑按月分期
     * @param ordercdd
     */
    public void create(Ordercdd ordercdd) {
		Product product = ordercdd.getProduct();
		//平台给门店的利息
		BigDecimal storeInterest = product.getStoreInterest();
		storeInterest = storeInterest == null?new BigDecimal("0"):storeInterest;
		//门店给客户的利息
		BigDecimal debtorInterest = product.getDebtorInterest();
		debtorInterest = debtorInterest == null?new BigDecimal("0"):debtorInterest;
		//借款总额,授信金额
		BigDecimal applyAmount = ordercdd.getApplyAmount();
		applyAmount = applyAmount == null?new BigDecimal("0"):applyAmount;
		//贷款分期数
		int periodNumber = ordercdd.getProduct().getPeriodNumber();
		//所有收费
		Set<Feebackup> feebackups = ordercdd.getFeebackups();
		//（门店给客户的利息-平台给门店的利息）* 授信金额 
		BigDecimal interest = debtorInterest.subtract(storeInterest).multiply(applyAmount).divide(BigDecimal.valueOf(12),6, BigDecimal.ROUND_UP).multiply(new BigDecimal(periodNumber));
		StoreBonus storeBonus = new StoreBonus();
		storeBonus.setOrdercdd(ordercdd);
		storeBonus.setUser(userRepository.findByDepartmentAndRole(ordercdd.getServiceUser().getDepartment().getId()));
		storeBonus.setDepartment(ordercdd.getServiceUser().getDepartment());
		storeBonus.setRealityAmount(interest);
		storeBonus.setStorebonusSn(SerialNumberUtil.INSTANCE.create(SerialNumberUtil.SerialType.STOREBONUS));
		BigDecimal periodStoreToUser = new BigDecimal(0);//门店向客户的分期收费
		BigDecimal storeToUser = new BigDecimal("0");//门店向客户的各种收费
		BigDecimal companyToStore = new BigDecimal("0");//平台给门店的收费
		if (feebackups!=null) {
			for (Feebackup feebackup : feebackups) {
				if (BACKUPSOURCE.COMPANYFEE.equals(feebackup.getBackupsource())) {
					companyToStore = companyToStore.add(calculateFeebackup(applyAmount, feebackup));
					continue;
				}
				if (BACKUPSOURCE.STOREFEE.equals(feebackup.getBackupsource())) {
					BigDecimal amount = calculateFeebackup(applyAmount, feebackup);
					storeToUser = storeToUser.add(amount);
					if (feebackup.getIsPeriod()) {//分期费用
						periodStoreToUser = periodStoreToUser.add(amount);
					}
					continue;
				}
			}
			BigDecimal storeMoney = storeToUser.add(interest).subtract(companyToStore);
			storeBonus.setRealityAmount(storeMoney);
		}
		storeBonus.setLoanAmount(storeBonus.getRealityAmount());
		if (ordercdd.getLoanToStore()) {//放款给门店
			//平台给门店放款=（原先的两笔放款相加）客户申请金额+门店向客户的分期费用-平台向门店的收费+门店向客户收取的利息-平台向门店收取的利息
			BigDecimal loanAmount = applyAmount.add(periodStoreToUser).subtract(companyToStore).add(interest);
			Boolean isInterestAdvance = ordercdd.getIsInterestAdvance();
			isInterestAdvance = isInterestAdvance==null?false:isInterestAdvance;
			if (isInterestAdvance) {
				LoanUtil util = new LoanUtil();
				loanAmount = loanAmount.subtract(util.getDebtorInterest(applyAmount, debtorInterest));
			}
			storeBonus.setLoanAmount(loanAmount);
		}
		//状态
		StoreBonusState state = storeBonusStateRepository.findByStateCode("NOTLEND");
		storeBonus.setState(state);
		storeBonusRepository.save(storeBonus);
	}
    
    /**获取单个收费的收费金额
     * @param applyAmount
     * @param feebackup
     * @return
     */
    private BigDecimal calculateFeebackup(BigDecimal applyAmount,Feebackup feebackup) {
		if (feebackup==null) {
			return new BigDecimal("0");
		}
		BigDecimal feeRate = feebackup.getFeeRate();
		if (feeRate==null) {
			return feebackup.getFeeAmount();//固定收费
		}
    	return feeRate.multiply(applyAmount);//按比例收费
	}
}
