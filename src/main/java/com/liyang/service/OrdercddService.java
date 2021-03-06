package com.liyang.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.creditcard.Creditcard;
import com.liyang.domain.department.Department;
import com.liyang.domain.feebackup.Feebackup;
import com.liyang.domain.feebackup.Feebackup.BACKUPSOURCE;
import com.liyang.domain.feebackup.FeebackupRepository;
import com.liyang.domain.feetype.Feetype;
import com.liyang.domain.feetype.FeetypeRepository;
import com.liyang.domain.loan.LoanStateRepository;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.ordercdd.OrdercddAct;
import com.liyang.domain.ordercdd.OrdercddActRepository;
import com.liyang.domain.ordercdd.OrdercddFile;
import com.liyang.domain.ordercdd.OrdercddFileRepository;
import com.liyang.domain.ordercdd.OrdercddLog;
import com.liyang.domain.ordercdd.OrdercddLogRepository;
import com.liyang.domain.ordercdd.OrdercddRepository;
import com.liyang.domain.ordercdd.OrdercddState;
import com.liyang.domain.ordercdd.OrdercddStateRepository;
import com.liyang.domain.ordercdd.OrdercddWorkflow;
import com.liyang.domain.ordercddloan.Ordercddloan;
import com.liyang.domain.ordercddloan.OrdercddloanFile;
import com.liyang.domain.ordercddloan.OrdercddloanFileRepository;
import com.liyang.domain.ordercddloan.OrdercddloanRepository;
import com.liyang.domain.ordercddloan.OrdercddloanState;
import com.liyang.domain.ordercddloan.OrdercddloanStateRepository;
import com.liyang.domain.ordercddloan.OrdercddloanWorkflow;
import com.liyang.domain.ordercddloan.OrdercddloanWorkflowRepository;
import com.liyang.domain.person.Person;
import com.liyang.domain.person.PersonFile;
import com.liyang.domain.person.PersonFileRepository;
import com.liyang.domain.person.PersonRepository;
import com.liyang.domain.person.PersonStateRepository;
import com.liyang.domain.product.Product;
import com.liyang.domain.product.ProductRepository;
import com.liyang.domain.productcompanyfee.Productcompanyfee;
import com.liyang.domain.user.User;
import com.liyang.domain.user.UserRepository;
import com.liyang.domain.user.UserStateRepository;
import com.liyang.domain.vehicle.Vehicle;
import com.liyang.service.check.cdd.CddCheckSRV;
import com.liyang.util.CommonUtil;
import com.liyang.util.FailReturnObject;
import com.liyang.util.FileRecordCopy;
import com.liyang.util.ReturnObject;
import com.liyang.util.SerialNumberUtil;

@Service
public class OrdercddService extends AbstractWorkflowService<Ordercdd, OrdercddWorkflow, OrdercddAct, OrdercddState, OrdercddLog, OrdercddFile> {

    @Value("${spring.wlqz.wechat.OPEN_ACC_APPLY}")
    private String OPEN_ACC_APPLY;

    @Autowired
    OrdercddLogRepository ordercddLogRepository;
    @Autowired
    OrdercddRepository ordercddRepository;
    @Autowired
    OrdercddActRepository ordercddActRepository;
    @Autowired
    OrdercddStateRepository ordercddStateRepository;
    @Autowired
    OrdercddFileRepository ordercddFileRepository;
    @Autowired
    OrdercddloanStateRepository ordercddloanStateRepository;
    @Autowired
    OrdercddloanWorkflowRepository ordercddloanWorkflowRepository;
    @Autowired
    OrdercddloanFileRepository ordercddloanFileRepository;
    @Autowired
    OrdercddloanRepository ordercddloanRespository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonFileRepository personFileRepository;
    @Autowired
    WlqzWechatPubService wechatPubService;
    @Autowired
    PersonStateRepository personStateRepository;
    @Autowired
    UserService userService;
    @Autowired
    WlqzWechatPubService wlqzWechatPubService;
    @Autowired
    CreditcardService creditcardService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserStateRepository userStateRepository;
    @Autowired
    LoanStateRepository loanStateRepository;
    @Autowired
    LoanService loanService;
    @Autowired
    StoreBonusService storeBonusService;
    @Autowired
    FeebackupRepository feebackupRepository;
    @Autowired
    FeetypeRepository feetypeRepository;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    CddCheckSRV cddCheckSRV;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    BankcardService bankcardService;

    @Override
    @PostConstruct
    public void sqlInit() {
        long count = ordercddActRepository.count();
        if (count == 0) {


            OrdercddAct save1 = ordercddActRepository.save(new OrdercddAct("创建", "create", 10, ActGroup.UPDATE));
            OrdercddAct save2 = ordercddActRepository.save(new OrdercddAct("读取", "read", 20, ActGroup.READ));
            OrdercddAct save3 = ordercddActRepository.save(new OrdercddAct("更新", "update", 30, ActGroup.UPDATE));
            OrdercddAct save4 = ordercddActRepository.save(new OrdercddAct("删除", "delete", 40, ActGroup.UPDATE));
            OrdercddAct save5 = ordercddActRepository.save(new OrdercddAct("自己的列表", "listOwn", 50, ActGroup.READ));
            OrdercddAct save6 = ordercddActRepository.save(new OrdercddAct("部门的列表", "listOwnDepartment", 60, ActGroup.READ));
            OrdercddAct save7 = ordercddActRepository.save(new OrdercddAct("部门和下属部门的列表", "listOwnDepartmentAndChildren", 70, ActGroup.READ));
            OrdercddAct save8 = ordercddActRepository.save(new OrdercddAct("通知其他人", "noticeOther", 80, ActGroup.NOTICE));
            OrdercddAct save9 = ordercddActRepository.save(new OrdercddAct("通知操作者", "noticeActUser", 90, ActGroup.NOTICE));
            OrdercddAct save10 = ordercddActRepository.save(new OrdercddAct("通知展示人", "noticeShowUser", 100, ActGroup.NOTICE));


            OrdercddState newState = new OrdercddState("已创建", 0, "CREATED");
            newState = ordercddStateRepository.save(newState);
            OrdercddState enableState = new OrdercddState("已放款", 100, "ENABLED");
            enableState.setActs(Arrays.asList(save1, save2, save3, save4, save5, save6, save7).stream().collect(Collectors.toSet()));
            ordercddStateRepository.save(enableState);
            ordercddStateRepository.save(new OrdercddState("无效", 200, "DISABLED"));
            ordercddStateRepository.save(new OrdercddState("删除", 300, "DELETED"));
            OrdercddState notLend = new OrdercddState("未放款", 101, "NOTLEND");
            notLend.setActs(Arrays.asList(save1, save2, save3, save4, save5, save6, save7).stream().collect(Collectors.toSet()));

        }
    }

    @Override
    public LogRepository<OrdercddLog> getLogRepository() {
        return ordercddLogRepository;
    }

    @Override
    public AuditorEntityRepository<Ordercdd> getAuditorEntityRepository() {

        return ordercddRepository;
    }


    @Override
    @PostConstruct
    public void injectLogRepository() {
        new Ordercdd().setLogRepository(ordercddLogRepository);

    }

    @Override
    public OrdercddLog getLogInstance() {
        return new OrdercddLog();
    }

    @Override
    public ActRepository<OrdercddAct> getActRepository() {
        return ordercddActRepository;
    }

    @Override
    @PostConstruct
    public void injectEntityService() {
        new Ordercdd().setService(this);
    }

    @Override
    public OrdercddFile getFileLogInstance() {
        return new OrdercddFile();
    }

    /**
     * 分配客户经理
     *
     * @param ordercdd
     */
    public void doDistribution(Ordercdd ordercdd) {
        User serviceUser = userRepository.findOne(ordercdd.getServiceId());
        ordercdd.setServiceName(serviceUser.getNickname());
        ordercdd.setServiceUser(serviceUser);
        ordercdd.setDistribution(true);
    }
    
    /**
     * 重新分配客户经理
     *
     * @param ordercdd
     */
    public void doRedistribution(Ordercdd ordercdd) {
    	doDistribution(ordercdd);
    }


    /**
     * 签约
     *
     * @param ordercdd
     */
    public void doSign(Ordercdd ordercdd) {

    }

    private void copytoOrdercddloan(Ordercdd ordercdd) {
        if (ordercdd == null) {
            return;
        }
        Ordercddloan cddloan = new Ordercddloan();
        OrdercddloanState cddloanState = ordercddloanStateRepository.findByStateCode("CREATED");//确定的状态，写死
        cddloan.setState(cddloanState);
        OrdercddloanWorkflow cddloanWorkflow = ordercddloanWorkflowRepository.findOne(4);//确定的工作流，写死
        cddloan.setWorkflow(cddloanWorkflow);
        cddloan.setOrdercdd(ordercdd);
        cddloan.setApplyAmount(ordercdd.getApplyAmount());//申请金额
        cddloan.setCapitalproduct(ordercdd.getProduct().getCapitalproduct());
        cddloan.setOrdercddloanSn(SerialNumberUtil.INSTANCE.create(SerialNumberUtil.SerialType.ORDERCDDLOAN));
        Ordercddloan save = ordercddloanRespository.save(cddloan);
        copyOrdercddFile(ordercdd.getFiles(), save);
    }

    private void copyOrdercddFile(Set<OrdercddFile> list, Ordercddloan save) {
        if (list == null) {
            return;
        }
        Set<OrdercddloanFile> cddloanFilelist = new HashSet<>();
        for (OrdercddFile file : list) {
            OrdercddloanFile cddloanFile = new OrdercddloanFile();
            FileRecordCopy.copyFileRecord(file, cddloanFile);
            cddloanFile.setEntity(save);
            //没有日志关联
            cddloanFilelist.add(cddloanFile);
        }
        ordercddloanFileRepository.save(cddloanFilelist);
    }
    
    @Transactional
    public void doCreate(Ordercdd ordercdd) {
        System.out.println("--------doCreate");
        User principal=CommonUtil.getPrincipal();
        if(principal!=null&&"STORE_SERVICE".equals(principal.getRole().getRoleCode())){
            userService.verifySmsCode(ordercdd.getApplyMobile(), ordercdd.getSmsCode());
        }
        //验证手机号等
        cddCheckSRV.checkCreate(ordercdd);
        
        Collection<String> noSuccessCode = new ArrayList<>();
        noSuccessCode.add("ENABLED");//签约完成，二审通过
//        noSuccessCode.add("SECONDINSTANCE");//废弃
        ///二审没有完结之前不能再申请下一单
        List<OrdercddState> states = ordercddStateRepository.findByStateCodeNotIn(noSuccessCode);
        List<Ordercdd> notSuccessOrdercdds = ordercddRepository.findAllByApplyMobileAndStateIn(ordercdd.getApplyMobile(), states);
        if (notSuccessOrdercdds!=null && (!notSuccessOrdercdds.isEmpty())) {
        	Department curDepartment = CommonUtil.getCurrentUserDepartment();
        	//一般应该只有一个元素或没有
        	if (notSuccessOrdercdds.size() > 1) {
        		throw new FailReturnObject(1534, "该用户在多个门店已存在未完成的申请单，请在签约完成之后重试", ReturnObject.Level.INFO);
			}
        	Ordercdd not = notSuccessOrdercdds.get(0);
			Department cddDepartment = not.getCreatedByDepartment();
			String realName = not.getRealName();
			if (!realName.equals(ordercdd.getRealName())) {
				throw new FailReturnObject(1534, "该手机号已被其他用户使用", ReturnObject.Level.INFO);
			}
			if (curDepartment!=null&&cddDepartment.getId().equals(curDepartment.getId())) {//本门店
				throw new FailReturnObject(1534, "该用户在本门店已存在未完成的申请单，请在签约完成之后重试", ReturnObject.Level.INFO);
			}else {
				throw new FailReturnObject(1534, "该用户在其它门店已存在未完成的申请单，请在签约完成之后重试", ReturnObject.Level.INFO);
			}
		}
        //验证用户是否存在
        User user = userRepository.findByUsername(ordercdd.getApplyMobile());
        if (user == null) {

            user = userService.baseRegister(ordercdd.getApplyMobile(), "123456", "APPLIED");
            if (ordercdd.getWechatCode() != null) {
                wlqzWechatPubService.weixinBind(ordercdd.getWechatCode(), user);
            }
            if (user.getNickname() == null) {
                user.setNickname(ordercdd.getRealName());
            }
            userRepository.save(user);
        }

        ordercdd.setCreatedBy(user);
        ordercdd.setUser(user);
        ordercdd.setCddSn(SerialNumberUtil.INSTANCE.create(SerialNumberUtil.SerialType.ORDERCDD));
        ordercdd.setProduct(productRepository.findFirstByLabel("汽车抵押贷款"));
        
        ordercddRepository.save(ordercdd);
        copyFromPerson(ordercdd);
        
        wechatPubService.pushOpenAccMessage(ordercdd.getUser(), "申请提交成功", ordercdd.getRealName(), ordercdd.getApplyMobile(), null, ordercdd.getComment());
    }
    
    /**
     * 从Person复制信息到Ordercdd
     */
    private void copyFromPerson(Ordercdd ordercdd) {
    	//复制person中的信息到当前ordercdd
        Person person = personRepository.findByTelephone(ordercdd.getApplyMobile());
        if (person != null) {
			ordercdd.setRealName(person.getName());
			ordercdd.setBirthday(person.getBirthday());
			ordercdd.setMaritalStatus(person.getMaritalStatus());
			ordercdd.setEmail(person.getEmail());
			ordercdd.setCompanyAddress(
					toEmpty(person.getCompanyProvince())+
					toEmpty(person.getCompanyCity())+
					toEmpty(person.getCompanyDistrict())+
					toEmpty(person.getCompanyTown())+
					toEmpty(person.getCompanyAddress()));
			ordercdd.setCompanyName(person.getCompanyName());
			ordercdd.setCompanyTelephone(person.getCompanyTelephone());
			ordercdd.setCompanyProvince(person.getCompanyProvince());
			ordercdd.setPersonalAddress(
					toEmpty(person.getHomeProvince())+
					toEmpty(person.getHomeCity())+
					toEmpty(person.getHomeDistrict())+
					toEmpty(person.getHomeTown())+
					toEmpty(person.getHomeAddress()));
			ordercdd.setApplyIdentityNo(person.getIdentity());
			Set<PersonFile> personFiles = person.getFiles();
			if (personFiles!=null) {
				Set<OrdercddFile> ordercddFiles = new HashSet<>();
				for (PersonFile personFile : personFiles) {
					if (personFile == null) {
						continue;
					}
					OrdercddFile file = new OrdercddFile();
					FileRecordCopy.copyFileRecord(personFile, file);
					file.setEntity(ordercdd);
					ordercddFiles.add(file);
				}
				ordercdd.setFiles(ordercddFiles);
				ordercddFileRepository.save(ordercddFiles);
			}
 		}
	}
    
    private String toEmpty(String str) {
    	return StringUtils.isBlank(str) ? "" : str;
	}

    /**
     * 一审通过
     *
     * @param ordercdd
     */
    @Transactional
    public void doAdopt(Ordercdd ordercdd) {

    }

    /**
     * 二审通过
     *
     * @param ordercdd
     */
    @Transactional
    public void doTwoadopt(Ordercdd ordercdd) {
        User user = ordercdd.getUser();
        if (user == null) {
            throw new FailReturnObject(1499, "没有申请的用户", ReturnObject.Level.WARNING);//lhg
        }
        //1 授信
        Creditcard creditcard = creditcardService
                .findByCreditcardIdentity(ordercdd.getApplyIdentityNo());
        if (creditcard == null) {
            creditcard = new Creditcard();
            creditcard.setSort(new Random().nextInt(1000));
            creditcard.setCreditcardIdentity(ordercdd.getApplyIdentityNo());// 贷款人身份证号
            creditcard.setCreditGrant(ordercdd.getApplyAmount());// 额度
            creditcard.setCreditBalance(ordercdd.getApplyAmount());
            creditcard.setPerson(personRepository.getByTelephoneAndStateCode(creditcard.getCreditcardIdentity(), "ENABLED"));
            creditcard.setProduct(productRepository.findFirstByLabel("汽车抵押贷款"));///李斌说一定是 "汽车抵押贷款"
            creditcardService.saveFromOrdercdd(creditcard, ordercdd, "已同意");

        }
        //2 生成贷款
        loanService.save(ordercdd, creditcard);
        //生成向门店打款的记录store_bonus
        storeBonusService.create(ordercdd);
        //修改剩余金额
        ordercdd.setLeftMatchAmount(new BigDecimal("0"));
        ordercddRepository.save(ordercdd);
        //拷贝数据到ordercddloan
        copytoOrdercddloan(ordercdd);
    }

    /**
     * 一审驳回
     *
     * @param Ordercdd
     */
    public void doFailed(Ordercdd Ordercdd) {
//        wechatPubService.pushOpenAccMessage(Ordercdd.getUser(), "申请被驳回", Ordercdd.getRealName(), Ordercdd.getApplyMobile(), null, Ordercdd.getComment());
    }

    /**
     * 二审驳回
     *
     * @param Ordercdd
     */
    public void doTwofailed(Ordercdd Ordercdd) {

    }

    /**
     * 保存收费项目
     */
    @Transactional
    public void doSavefeebackup(Ordercdd ordercdd) {
        Set<Object> feebackups = ordercdd.getFees();
        if (feebackups == null) {
            return;
        }
        //保存现有
        Map<Integer, Feetype> feetypeMap = mapAllFeetype();
        List<Feebackup> saves = new ArrayList<>();
        for (Object obj : feebackups) {
            if (!Map.class.isInstance(obj)) {
            	continue;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            if (map.get("feetype") == null) {
                continue;
            }
            Integer feetypeId = Integer.valueOf(String.valueOf(map.get("feetype")));
            Feetype feetype = feetypeMap.get(feetypeId);
            map.remove("fee");
            map.remove("createdAt");
            map.remove("_links");
            map.remove("backupsource");
            map.remove("feetype");
            Map<String, Object> valueMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() == null) {
					continue;
				}
				valueMap.put(entry.getKey(), entry.getValue());
			}
            if (feetype.getType() == 0) {//门店向客户收费
                Feebackup feebackup = new Feebackup();
                try {
					BeanUtils.populate(feebackup, valueMap);
					feebackup.setFeetype(feetype);
					feebackup.setOrdercdd(ordercdd);
					feebackup.setBackupsource(BACKUPSOURCE.STOREFEE);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
                saves.add(feebackup);
            }
        }
        //删除原有所有门店收费项目
        feebackupRepository.deleteByOrdercdd(ordercdd.getId(), BACKUPSOURCE.STOREFEE);
        //删除之后保存
        feebackupRepository.save(saves);
        ordercddRepository.save(ordercdd);
    }

    /**
     * 提交资料
     * 1. 将product关联的平台向门店收费的项目复制到Feebackup
     * 2. 将person中的历史数据、信息和图片复制到当前订单
     *
     * @param ordercdd
     */
    public void doApplication(Ordercdd ordercdd) {
    	//验证
    	cddCheckSRV.checkApplication(ordercdd);
    	
        Product product = ordercdd.getProduct();
        Set<Productcompanyfee> productcompanyfees = product.getProductcompanyfees();
        if (productcompanyfees == null) {
            return;
        }
        List<Feebackup> feebackups = new ArrayList<>();
        for (Productcompanyfee productcompanyfee : productcompanyfees) {
            if (productcompanyfee == null) {
                continue;
            }
            Feebackup feebackup = new Feebackup();
            feebackup.setBackupsource(BACKUPSOURCE.COMPANYFEE);
            feebackup.setComment(productcompanyfee.getComment());
            feebackup.setDescription(productcompanyfee.getDescription());
            feebackup.setFeeAmount(productcompanyfee.getFeeAmount());
            feebackup.setFeeRate(productcompanyfee.getFeeRate());
            feebackup.setFeetype(productcompanyfee.getFeetype());
            feebackup.setFeetypeCode(productcompanyfee.getFeetypeCode());
            feebackup.setIsPeriod(productcompanyfee.getIsPeriod());
            feebackup.setIsRefund(productcompanyfee.getIsRefund());
            feebackup.setOrdercdd(ordercdd);
            feebackups.add(feebackup);
        }
        //删除原有所有门店收费项目
        feebackupRepository.deleteByOrdercdd(ordercdd.getId(), BACKUPSOURCE.COMPANYFEE);
        //删除之后保存
        feebackupRepository.save(feebackups);
        //2017年12月15日10:48:01  ID：1000406
        generatePerson(ordercdd);
        //复制订单信息到person, 需要ordercdd关联person之后
        copyToPerson(ordercdd);
        //生成汽车和银行卡//2017年12月25日09:51:02
        Vehicle vehicle = vehicleService.saveByOrdercdd(ordercdd);
        ordercdd.setVehicle(vehicle);

        bankcardService.saveByOrdercdd(ordercdd);

    }
    private void generatePerson(Ordercdd ordercdd)
    {
        //申请单提交一审资料时，该客户就会进入person表中  ID： 1000406
        String identity = ordercdd.getApplyIdentityNo();//身份证号
        if (identity == null) {
            throw new FailReturnObject(1543, "身份证号不能为空,请补填信息", ReturnObject.Level.INFO);
        }

        User user=ordercdd.getUser();
        Person person = personRepository.findByIdentity(identity);
        if (person == null) {
            person = new Person();
            person = wechatPubService.setPersonField(person, ordercdd);
            //如果身份证号不一样，手机号一样

            person.setState(personStateRepository.findByStateCode("ENABLED"));
            wechatPubService.pushOpenAccMessage(user, "已同意申请", person.getName(), person.getTelephone(), null, ordercdd.getComment());
        }
        personRepository.save(person);
        user.setPerson(person);
        user.setState(userStateRepository.findByStateCode("ENABLED"));
        userRepository.save(user);
        ordercdd.setPerson(person);
    }
    
    /**
     * 将ordercdd中的信息复制到person中
     */
    private void copyToPerson(Ordercdd ordercdd) {
    	Person person = ordercdd.getPerson();
    	if (person == null) {
    		person = personRepository.findByIdentity(ordercdd.getApplyIdentityNo());
		}
    	if (person == null) {
			return;
		}
    	//保存信息
    	person.setName(ordercdd.getRealName());
    	person.setBirthday(ordercdd.getBirthday());
    	person.setMaritalStatus(ordercdd.getMaritalStatus());
    	person.setEmail(ordercdd.getEmail());
    	person.setCompanyName(ordercdd.getCompanyName());
    	person.setCompanyTelephone(ordercdd.getCompanyTelephone());
//    	person.setCompanyProvince(ordercdd.getCompanyProvince());
    	
    	Set<OrdercddFile> ordercddFiles = ordercdd.getFiles();
		if (ordercddFiles!=null) {
			Set<PersonFile> personFiles = new HashSet<>();
			for (OrdercddFile ordercddFile : ordercddFiles) {
				if (ordercddFile == null) {
					continue;
				}
				PersonFile file = new PersonFile();
				FileRecordCopy.copyFileRecord(ordercddFile, file);
				file.setEntity(person);
				personFiles.add(file);
			}
			person.setFiles(personFiles);
			//删除当前person的附件
	    	personRepository.deleteFilesByEntityId(person.getId());
			personFileRepository.save(personFiles);
			personRepository.save(person);
		}
	}
    
    /**
     * 将所有收费类型转换成一个map
     *
     * @return
     */
    private Map<Integer, Feetype> mapAllFeetype() {
        Map<Integer, Feetype> result = new HashMap<>();
        List<Feetype> list = feetypeRepository.findAll();
        if (list == null) {
            return result;
        }
        for (Feetype feetype : list) {
            if (feetype == null) {
                continue;
            }
            result.put(feetype.getId(), feetype);
        }
        return result;
    }

    public void doDownloadfile(Ordercdd ordercdd) {
        System.out.println("汽车贷款Ordercdd下载所有图片开始--------------------------------------");
        Set<OrdercddFile> files = ordercdd.getFiles();
        if (files == null) {
            return;
        }
        String originalFileName = "";
        String newName = "";
        int count = 1;
        for (OrdercddFile ordercddFile : files) {
            originalFileName = ordercddFile.getNewFileName();
            int pos = originalFileName.lastIndexOf(".");
            newName = ordercdd.getRealName() + "_" + ordercdd.getServiceUser().getDepartment().getLabel()
                    + "_" + ordercddFile.getSubcategory() + "_" + (count++) + originalFileName.substring(pos);
            ordercddFile.setOriginalFileName(newName);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String zipFileName = ordercdd.getRealName() + "_" + ordercdd.getServiceUser().getDepartment().getLabel()
                + "_" + format.format(new Date()) + ".zip";
        ordercdd.setZipFileName(zipFileName);
        downloadAllFilesAnduploadToAliyun(ordercdd);
    }

    public int getNotCloneByServiceUser(User serviceUser, List<OrdercddState> states)//每个客户经理未结束的CDD订单
    {

//        User serviceUser = userRepository.findOne(serviceUserId);
//        if(serviceUser==null){
//            throw new FailReturnObject(1623,"客户经理不存在!", ReturnObject.Level.INFO);
//        }


        return ordercddRepository.findAllByServiceUserAndStateIn(serviceUser, states).size();
    }

    //    @Cacheable(value = "ordercddNoCloneCodes",key = "'0'")
    public List<OrdercddState> getNoCloneCode() {
        System.out.println("---------noCloneCodes");
        Collection<String> noCloneCode = new ArrayList<>();
        noCloneCode.add("PENDING");//待提交
        noCloneCode.add("MATCH");//待匹配
        noCloneCode.add("AUDIT");//待审核
        noCloneCode.add("CONTRACT");//待签约
        return ordercddStateRepository.findAllByStateCodeIn(noCloneCode);
    }

}
