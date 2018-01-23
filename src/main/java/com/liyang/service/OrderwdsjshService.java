package com.liyang.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.department.DepartmentRepository;
import com.liyang.domain.department.Departmenttype;
import com.liyang.domain.department.Departmenttype.DepartmenttypeCode;
import com.liyang.domain.department.DepartmenttypeRepository;
import com.liyang.domain.orderwdsjsh.Orderwdsjsh;
import com.liyang.domain.orderwdsjsh.OrderwdsjshAct;
import com.liyang.domain.orderwdsjsh.OrderwdsjshActRepository;
import com.liyang.domain.orderwdsjsh.OrderwdsjshFile;
import com.liyang.domain.orderwdsjsh.OrderwdsjshLog;
import com.liyang.domain.orderwdsjsh.OrderwdsjshLogRepository;
import com.liyang.domain.orderwdsjsh.OrderwdsjshRepository;
import com.liyang.domain.orderwdsjsh.OrderwdsjshState;
import com.liyang.domain.orderwdsjsh.OrderwdsjshStateRepository;
import com.liyang.domain.orderwdsjsh.OrderwdsjshAct;
import com.liyang.domain.orderwdsjsh.OrderwdsjshActRepository;
import com.liyang.domain.orderwdsjsh.OrderwdsjshWorkflow;
import com.liyang.domain.orderwdsjsh.OrderwdsjshWorkflowRepository;
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
import com.liyang.util.CommonUtil;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject.Level;
import com.liyang.util.WechatImageAsyncFetchEvent;

@Service
@Order(35)
public class OrderwdsjshService extends AbstractWorkflowService<Orderwdsjsh, OrderwdsjshWorkflow, OrderwdsjshAct, OrderwdsjshState, OrderwdsjshLog, OrderwdsjshFile> {

    @Value("${spring.wlqz.wechat.OPEN_ACC_APPLY}")
    private String OPEN_ACC_APPLY;

    @Autowired
    OrderwdsjshActRepository orderwdsjshActRepository;

    @Autowired
    OrderwdsjshStateRepository orderwdsjshStateRepository;

    @Autowired
    OrderwdsjshLogRepository orderwdsjshLogRepository;

    @Autowired
    OrderwdsjshRepository orderwdsjshRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    WlqzWechatPubService wechatPubService;
    @Autowired
    OrderwdsjshWorkflowRepository orderwdsjshWorkflowRepository;
    @Autowired
    PersonStateRepository personStateRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmenttypeRepository departmenttypeRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    @PostConstruct
    public void sqlInit() {
        long count  = orderwdsjshActRepository.count();
        if (count==0) {


            OrderwdsjshAct save1 = orderwdsjshActRepository.save(new OrderwdsjshAct("创建", "create", 10, ActGroup.UPDATE));
            OrderwdsjshAct save2 = orderwdsjshActRepository.save(new OrderwdsjshAct("读取", "read", 20, ActGroup.READ));
            OrderwdsjshAct save3 = orderwdsjshActRepository.save(new OrderwdsjshAct("更新", "update", 30, ActGroup.UPDATE));
            OrderwdsjshAct save4 = orderwdsjshActRepository.save(new OrderwdsjshAct("删除", "delete", 40, ActGroup.UPDATE));
            OrderwdsjshAct save5 = orderwdsjshActRepository.save(new OrderwdsjshAct("自己的列表", "listOwn", 50, ActGroup.READ));
            OrderwdsjshAct save6 = orderwdsjshActRepository.save(new OrderwdsjshAct("部门的列表", "listOwnDepartment", 60, ActGroup.READ));
            OrderwdsjshAct save7 = orderwdsjshActRepository.save(new OrderwdsjshAct("部门和下属部门的列表", "listOwnDepartmentAndChildren", 70, ActGroup.READ));
            OrderwdsjshAct save8 = orderwdsjshActRepository.save(new OrderwdsjshAct("通知其他人", "noticeOther", 80, ActGroup.NOTICE));
            OrderwdsjshAct save9 = orderwdsjshActRepository.save(new OrderwdsjshAct("通知操作者", "noticeActUser", 90, ActGroup.NOTICE));
            OrderwdsjshAct save10 = orderwdsjshActRepository.save(new OrderwdsjshAct("通知展示人", "noticeShowUser", 100, ActGroup.NOTICE));


            OrderwdsjshState newState = new OrderwdsjshState("已创建", 0, "CREATED");
            newState = orderwdsjshStateRepository.save(newState);
            OrderwdsjshState enableState = new OrderwdsjshState("有效", 100, "ENABLED");
            enableState.setActs(Arrays.asList(save1, save2, save3, save4, save5, save6, save7).stream().collect(Collectors.toSet()));
            orderwdsjshStateRepository.save(enableState);
            orderwdsjshStateRepository.save(new OrderwdsjshState("无效", 200, "DISABLED"));
            orderwdsjshStateRepository.save(new OrderwdsjshState("删除", 300, "DELETED"));

        }

    }

    @Override
    public LogRepository<OrderwdsjshLog> getLogRepository() {
        // TODO Auto-generated method stub
        return orderwdsjshLogRepository;
    }

    @Override
    public AuditorEntityRepository<Orderwdsjsh> getAuditorEntityRepository() {

        return orderwdsjshRepository;
    }


    @Override
    @PostConstruct
    public void injectLogRepository() {
        new Orderwdsjsh().setLogRepository(orderwdsjshLogRepository);

    }

    @Override
    public OrderwdsjshLog getLogInstance() {
        // TODO Auto-generated method stub
        return new OrderwdsjshLog();
    }

    @Override
    public ActRepository<OrderwdsjshAct> getActRepository() {
        // TODO Auto-generated method stub
        return orderwdsjshActRepository;
    }

    @Override
    @PostConstruct
    public void injectEntityService() {
        new Orderwdsjsh().setService(this);

    }

    @Override
    public OrderwdsjshFile getFileLogInstance() {
        // TODO Auto-generated method stub
        return new OrderwdsjshFile();
    }

    public void changeStateNotpass(String telephone, String beidouComment) {
        System.out.println("denited:" + telephone);
        Orderwdsjsh orderwdsjsh = orderwdsjshRepository.getByTelephoneAndStatCode(telephone, "ADOPT");
        if (orderwdsjsh != null) {
            orderwdsjsh.setBeidouComment(beidouComment);
            orderwdsjsh.setState(orderwdsjshStateRepository.findByStateCode("NOTPASS"));
            orderwdsjshRepository.save(orderwdsjsh);
        }
    }

    /**
     * 提交申请动作  一次只能允许一次提交，待提交成功后才能进行下一次申请
     *
     * @param orderwdsjsh
     */
    public void doCreate(Orderwdsjsh orderwdsjsh) {
        orderwdsjsh.setWorkflow(orderwdsjshWorkflowRepository.findOne(1));

        Orderwdsjsh findByApplyMobile = orderwdsjshRepository.findByApplyMobile(orderwdsjsh.getApplyMobile());
        if (null != findByApplyMobile) {
            throw new FailReturnObject(6657, "随借随还订单相同手机号" + orderwdsjsh.getApplyMobile() + "已存在，请使用不同手机号", Level.WARNING);
        }
        Orderwdsjsh findByApplyIdentityNo = orderwdsjshRepository.findByApplyIdentityNo(orderwdsjsh.getApplyIdentityNo());
        if (null != findByApplyIdentityNo) {
            throw new FailReturnObject(6657, "随借随还订单相同身份证号" + orderwdsjsh.getApplyIdentityNo() + "已存在，请使用不同身份证", Level.WARNING);
        }

        Page<Orderwdsjsh> orderwdsjshes = orderwdsjshRepository.listOwn(null);
        List<Orderwdsjsh> list = orderwdsjshes.getContent();

        if (orderwdsjshes != null && list.size() != 0) {
            for (Orderwdsjsh exitOrderwdsjsh : list) {

                if (!(exitOrderwdsjsh.isStateAdopt() || exitOrderwdsjsh.isStateEnable())) {
                    System.out.println(exitOrderwdsjsh.getApplyIdentityNo() + ":" + exitOrderwdsjsh.getState().getStateCode() + ":" + exitOrderwdsjsh.getId());
                    throw new FailReturnObject(199, "前一个申请还在审核中！", Level.WARNING);
                }
            }
        }
    }

    /**
     * 主管 为贷款人分配业务人员 notice id=1  将这个用户分配到id为1的业务员下
     *
     * @param
     */
    public void doDistribution(Orderwdsjsh orderwdsjsh) {
        orderwdsjsh.setServiceName(userRepository.findOne(orderwdsjsh.getServiceId()).getNickname());
        orderwdsjsh.setServiceUser(userRepository.findOne(orderwdsjsh.getServiceId()));
        orderwdsjsh.setDistribution(true);
        System.out.println("--------------" + orderwdsjsh.getServiceId());
    }

    /**
     * 通过背篼申请
     *
     * @param telephone
     */
    public void adoptApply(String telephone) {
        Orderwdsjsh orderwdsjsh = orderwdsjshRepository.getByTelephoneAndStatCode(telephone, "SIGNED");

        if (orderwdsjsh == null) {
            orderwdsjsh = orderwdsjshRepository.getByTelephoneAndStatCode(telephone, "DENIED");

        }
        System.out.println(orderwdsjsh + ":" + telephone);
        if (orderwdsjsh != null) {
            orderwdsjsh.setState(orderwdsjshStateRepository.findByStateCode("ENABLED"));
            System.out.println(orderwdsjsh.getState().getStateCode());
            orderwdsjshRepository.save(orderwdsjsh);
        }
    }

    /**
     * 一个微信用户只能有一个person  身份证是唯一标识
     * 一次只能允许一次提交，待提交成功后才能进行下一次申请
     * <p>
     * 申请通过我方后建立person 与 orderWdsjs，user建立关联
     */
    public void doAdopt(Orderwdsjsh orderwdsjsh) {
        User user = orderwdsjsh.getCreatedBy();
        if (user == null) {
            throw new FailReturnObject(499, "没有创建人", Level.WARNING);//lhg
        }

        System.out.println("doAdopt");

//        List<User> list = userRepository.findByUnionid(user.getUnionid());
//        if (list != null && list.size() > 0) {
//            for (User exitUser : list) {
//                if (exitUser.getPerson() != null) {
//                    user.setPerson(exitUser.getPerson());
//                    orderwdsjsh.setPerson(exitUser.getPerson());
//                    return;
//                }
//            }
//        }
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

    public void doFailed(Orderwdsjsh orderwdsjsh) {
        wechatPubService.pushOpenAccMessage(orderwdsjsh.getCreatedBy(), "初步筛选未通过", orderwdsjsh.getRealName(), orderwdsjsh.getApplyMobile(), OPEN_ACC_APPLY, orderwdsjsh.getComment());
    }

    public void doDownloadFile(Orderwdsjsh orderwdsjsh) {
        downloadAllFilesAnduploadToAliyun(orderwdsjsh);
    }

    @Override
    public OrderwdsjshLog fillmultiWechatImageToLog(Orderwdsjsh entity, OrderwdsjshLog log) {
        // TODO Auto-generated method stub
        String[] wechatFiles = entity.getWechatFiles();
        for (String fileName : wechatFiles) {
            OrderwdsjshFile fileLogInstance = getFileLogInstance();
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
            Departmenttype departmentType = departmenttypeRepository.findByDepartmenttypeCode(DepartmenttypeCode.DEBTOR);
            fileLogInstance.setUploaderDepartmenttype(departmentType);
            OrderwdsjshFile file = fileRepository.save(fileLogInstance);
            applicationContext.publishEvent(new WechatImageAsyncFetchEvent(entity, file));
        }
        return log;
    }

    public void doChangeid(Orderwdsjsh orderwdsjsh) {
        if (orderwdsjshRepository.findByApplyIdentityNo(orderwdsjsh.getApplyIdentityNo()) != null) {
            throw new FailReturnObject(4561, "身份证号已经存在", Level.INFO);
        }

        Person person = orderwdsjsh.getPerson();
        if (person != null) {
            person.setIdentity(orderwdsjsh.getApplyIdentityNo());
            personRepository.save(person);
        }
    }

    public void doSign(Orderwdsjsh orderwdsjsh) {

    }

}
