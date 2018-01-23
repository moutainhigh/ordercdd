package com.liyang.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.capitalpayment.Capitalpayment;
import com.liyang.domain.capitalpayment.CapitalpaymentRepository;
import com.liyang.domain.capitalpayment.CapitalpaymentStateRepository;
import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.ordercdd.OrdercddFile;
import com.liyang.domain.ordercdd.OrdercddRepository;
import com.liyang.domain.ordercddloan.Ordercddloan;
import com.liyang.domain.ordercddloan.OrdercddloanAct;
import com.liyang.domain.ordercddloan.OrdercddloanActRepository;
import com.liyang.domain.ordercddloan.OrdercddloanFile;
import com.liyang.domain.ordercddloan.OrdercddloanFileRepository;
import com.liyang.domain.ordercddloan.OrdercddloanLog;
import com.liyang.domain.ordercddloan.OrdercddloanLogRepository;
import com.liyang.domain.ordercddloan.OrdercddloanRepository;
import com.liyang.domain.ordercddloan.OrdercddloanState;
import com.liyang.domain.ordercddloan.OrdercddloanStateRepository;
import com.liyang.domain.ordercddloan.OrdercddloanWorkflow;
import com.liyang.domain.ordercddloan.OrdercddloanWorkflowRepository;
import com.liyang.util.FailReturnObject;
import com.liyang.util.FileRecordCopy;
import com.liyang.util.ReturnObject.Level;
import com.liyang.util.SerialNumberUtil;

@Service
public class OrdercddloanService extends AbstractWorkflowService<Ordercddloan, OrdercddloanWorkflow, OrdercddloanAct, OrdercddloanState, OrdercddloanLog, OrdercddloanFile> {

    @Value("${spring.wlqz.wechat.OPEN_ACC_APPLY}")
    private String OPEN_ACC_APPLY;

    @Autowired
    OrdercddloanLogRepository ordercddloanLogRepository;
    @Autowired
    OrdercddloanRepository ordercddloanRepository;
    @Autowired
    OrdercddloanActRepository ordercddloanActRepository;
    @Autowired
    OrdercddloanStateRepository ordercddloanStateRepository;
    @Resource
    private OrdercddloanFileRepository ordercddloanFileRepository;
    @Resource
    private OrdercddloanWorkflowRepository ordercddloanWorkflowRepository;
    @Resource
    private OrdercddRepository ordercddRepository;
    @Resource
    private CapitalpaymentRepository capitalpaymentRepository;
    @Resource
    private CapitalpaymentStateRepository capitalpaymentStateRepository;

    @Override
    public void sqlInit() {
        long count = ordercddloanActRepository.count();
        if (count == 0) {
            OrdercddloanAct save1 = ordercddloanActRepository.save(new OrdercddloanAct("创建", "create", 10, ActGroup.UPDATE));
            OrdercddloanAct save2 = ordercddloanActRepository.save(new OrdercddloanAct("读取", "read", 20, ActGroup.READ));
            OrdercddloanAct save3 = ordercddloanActRepository.save(new OrdercddloanAct("更新", "update", 30, ActGroup.UPDATE));
            OrdercddloanAct save4 = ordercddloanActRepository.save(new OrdercddloanAct("删除", "delete", 40, ActGroup.UPDATE));
            OrdercddloanAct save5 = ordercddloanActRepository.save(new OrdercddloanAct("自己的列表", "listOwn", 50, ActGroup.READ));
            OrdercddloanAct save6 = ordercddloanActRepository.save(new OrdercddloanAct("部门的列表", "listOwnDepartment", 60, ActGroup.READ));
            OrdercddloanAct save7 = ordercddloanActRepository.save(new OrdercddloanAct("部门和下属部门的列表", "listOwnDepartmentAndChildren", 70, ActGroup.READ));

            OrdercddloanState newState = new OrdercddloanState("已创建", 0, "CREATED");
            newState = ordercddloanStateRepository.save(newState);
            OrdercddloanState enableState = new OrdercddloanState("已放款", 100, "ENABLED");
            enableState.setActs(Arrays.asList(save1, save2, save3, save4, save5, save6, save7).stream().collect(Collectors.toSet()));
            ordercddloanStateRepository.save(enableState);
            ordercddloanStateRepository.save(new OrdercddloanState("无效", 200, "DISABLED"));
            ordercddloanStateRepository.save(new OrdercddloanState("删除", 300, "DELETED"));
            OrdercddloanState notLend = new OrdercddloanState("未放款", 101, "NOTLEND");
            notLend.setActs(Arrays.asList(save1, save2, save3, save4, save5, save6, save7).stream().collect(Collectors.toSet()));
        }
    }

    @Override
    public LogRepository<OrdercddloanLog> getLogRepository() {
        return ordercddloanLogRepository;
    }

    @Override
    public AuditorEntityRepository<Ordercddloan> getAuditorEntityRepository() {

        return ordercddloanRepository;
    }


    @Override
    @PostConstruct
    public void injectLogRepository() {
        new Ordercdd().setLogRepository(ordercddloanLogRepository);

    }

    @Override
    public OrdercddloanLog getLogInstance() {
        return new OrdercddloanLog();
    }

    @Override
    public ActRepository<OrdercddloanAct> getActRepository() {
        return ordercddloanActRepository;
    }

    @Override
    @PostConstruct
    public void injectEntityService() {
        new Ordercddloan().setService(this);

    }

    @Override
    public OrdercddloanFile getFileLogInstance() {
        return new OrdercddloanFile();
    }

    /**
     * 匹配资金产品
     *
     * @param ordercddloan
     */
    @Transactional
    public void doCreate(Ordercddloan ordercddloan) {
        Ordercdd ordercdd = ordercddloan.getOrdercdd();
        Capitalproduct capitalproduct = ordercddloan.getCapitalproduct();
        BigDecimal applyAmount = ordercddloan.getApplyAmount();
        if (ordercdd == null) {
            throw new FailReturnObject(1, "ordercdd的不能为null", Level.ERROR);
        }
        if (capitalproduct == null) {
            throw new FailReturnObject(2, "capitalproduct的不能为null", Level.ERROR);
        }
        if (ordercdd.getId() == null) {
            throw new FailReturnObject(3, "ordercdd的ID不能为空", Level.ERROR);
        }
        if (capitalproduct.getId() == null) {
            throw new FailReturnObject(4, "capitalproduct的ID不能为空", Level.ERROR);
        }
        if (applyAmount == null || (applyAmount.compareTo(new BigDecimal("0")) <= 0)) {
            throw new FailReturnObject(5, "申请金额不能为0", Level.ERROR);
        }
        if (applyAmount.scale() > 2) {
            throw new FailReturnObject(6, "申请金额小数点后最多为两位，请重新填写", Level.ERROR);
        }
//		ordercdd = ordercddRepository.findOne(ordercdd.getId());
        BigDecimal leftMatchAmount = ordercdd.getLeftMatchAmount();
        if (applyAmount.compareTo(leftMatchAmount) > 0) {
            throw new FailReturnObject(7, "申请金额不能大于剩余匹配金额", Level.ERROR);
        }
        OrdercddloanState cddloanState = ordercddloanStateRepository.findByStateCode("CREATED");//确定的状态，写死
        ordercddloan.setState(cddloanState);
        OrdercddloanWorkflow cddloanWorkflow = ordercddloanWorkflowRepository.findOne(4);//确定的工作流，写死
        ordercddloan.setWorkflow(cddloanWorkflow);
        ordercddloan.setOrdercddloanSn(SerialNumberUtil.INSTANCE.create(SerialNumberUtil.SerialType.ORDERCDDLOAN));
        Ordercddloan save = ordercddloanRepository.save(ordercddloan);
        //复制文件
        copyOrdercddFile(ordercdd.getFiles(), save);
        //修改剩余金额
        ordercdd.setLeftMatchAmount(leftMatchAmount.subtract(applyAmount));
        ordercddRepository.save(ordercdd);
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

    /**
     * 审核不通过
     * 1.修改状态,框架自动会修改可能
     * 2.将申请金额加到关联的ordercdd剩余匹配金额中
     */
    @Transactional
    public void doFailed(Ordercddloan ordercddloan) {
        //1.将状态设为驳回
        OrdercddloanState newState = ordercddloanStateRepository.findByStateCode("DISABLED");
        ordercddloan.setState(newState);
        ordercddloanRepository.save(ordercddloan);

        //2.匹配金额
        Ordercdd ordercdd = ordercddloan.getOrdercdd();
        if (ordercdd == null) {
            throw new FailReturnObject(1, "ordercdd的不能为null", Level.ERROR);
        }
        if (ordercddloan.getId() == null) {
            throw new FailReturnObject(1, "ordercddloan的ID不能为空", Level.ERROR);
        }
        BigDecimal applyAmount = ordercddloan.getApplyAmount();
        applyAmount = applyAmount.add(ordercdd.getLeftMatchAmount());
        ordercdd.setLeftMatchAmount(applyAmount);
        ordercddRepository.save(ordercdd);
    }

    /**
     * 审核通过
     *
     * @param
     */
    @Transactional
    public void doAdoptAll(Ordercddloan ordercddloan) {
        //1.将状态设置为通过
        OrdercddloanState newState = ordercddloanStateRepository.findByStateCode("ENABLED");
        ordercddloan.setState(newState);
        ordercddloanRepository.save(ordercddloan);
        //2.生成放款记录
        Capitalpayment ment = new Capitalpayment();
        ment.setOrdercdd(ordercddloan.getOrdercdd());
        ment.setOrdercddloan(ordercddloan);
        ment.setCapitalproduct(ordercddloan.getCapitalproduct());
        ment.setCreditorDepartment(ordercddloan.getCapitalproduct().getCreditorDepartment());
        ment.setApplyAmount(ordercddloan.getApplyAmount());
        ment.setState(capitalpaymentStateRepository.findByStateCode("NOTLEND"));
        capitalpaymentRepository.save(ment);
    }

    /**
     * 部分审核通过
     *
     * @param
     */
    @Transactional
    public void doAdopt(Ordercddloan ordercddloan) {
        //1.获取参数，校验参数
        BigDecimal acceptAmount = ordercddloan.getAcceptAmount();
        BigDecimal applyAmount = ordercddloan.getApplyAmount();
        if (acceptAmount == null) {
            throw new FailReturnObject(4, "同意申请金额为空", Level.ERROR);
        }
        if (acceptAmount.compareTo(BigDecimal.ZERO)==-1||acceptAmount.compareTo(applyAmount)==1){
            throw new FailReturnObject(3,"同意申请金额大于申请额度或者小于0",Level.ERROR);
        }

        //审核全部不通过
        if (acceptAmount.compareTo(BigDecimal.ZERO)==0) {
            doFailed(ordercddloan);
            return;
        }

        //全部同意
        if (acceptAmount.compareTo(applyAmount) == 0) {
            doAdoptAll(ordercddloan);
            return;
        }
        //2.逻辑处理
        //2.1将申请的金额重新生成一个订单
        Ordercddloan newOrdercddLoan = new Ordercddloan();
        newOrdercddLoan.setApplyAmount(acceptAmount);
        //生成唯一的setOrdercddloanSn
        newOrdercddLoan.setOrdercddloanSn(SerialNumberUtil.INSTANCE.create(SerialNumberUtil.SerialType.ORDERCDDLOAN));

        //jpa不能使用同一个集合
        newOrdercddLoan.setCapitalproduct(ordercddloan.getCapitalproduct());
        newOrdercddLoan.setOrdercdd(ordercddloan.getOrdercdd());
        newOrdercddLoan.setCreatedByDepartment(ordercddloan.getCreatedByDepartment());
        newOrdercddLoan.setService(ordercddloan.getService());
        newOrdercddLoan.setLogRepository(ordercddloan.getLogRepository());
        newOrdercddLoan.setLastAct(ordercddloan.getLastAct());
        newOrdercddLoan.setWorkflow(ordercddloan.getWorkflow());

        ordercddloanRepository.save(newOrdercddLoan);

        //2.2走同意申请申请流程
        doAdoptAll(newOrdercddLoan);

        //2.3剩余金额驳回
        Ordercddloan leftOrdercddLoan = new Ordercddloan();
        try {
            BeanUtils.copyProperties(leftOrdercddLoan, ordercddloan);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        leftOrdercddLoan.setApplyAmount(applyAmount.subtract(acceptAmount));
        doFailed(leftOrdercddLoan);

        //2.4将原来的订单设置为驳回
        OrdercddloanState ordercddloanState = ordercddloanStateRepository.findByStateCode("DISABLED");
        ordercddloan.setState(ordercddloanState);
        ordercddloanRepository.save(ordercddloan);
    }

    public void doDownloadfile(Ordercddloan Ordercddloan) {
        downloadAllFilesAnduploadToAliyun(Ordercddloan);
    }

    //深度克隆
    @SuppressWarnings("unchecked")
    public static <T> T deepCloneObject(T obj) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(obj);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);

            return (T) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
