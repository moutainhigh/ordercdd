package com.liyang.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.liyang.domain.capitalproduct.Capitalproduct;
import com.liyang.domain.creditrepayplan.Creditrepayplan;
import com.liyang.domain.creditrepayplan.CreditrepayplanRepository;
import com.liyang.domain.creditrepayplan.CreditrepayplanStateRepository;
import com.liyang.domain.department.Departmenttype;
import com.liyang.domain.department.DepartmenttypeRepository;
import com.liyang.domain.loan.Loan;
import com.liyang.domain.loan.LoanRepository;
import com.liyang.domain.loan.Loanoverdue;
import com.liyang.domain.loan.LoanoverdueRepository;
import com.liyang.domain.ordercdd.Ordercdd;
import com.liyang.domain.product.Product;
import com.liyang.util.*;
import com.liyang.util.ReturnObject.Level;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.liyang.domain.base.AbstractAuditorAct.ActGroup;
import com.liyang.domain.creditrepayment.Creditrepayment;
import com.liyang.domain.creditrepayment.CreditrepaymentAct;
import com.liyang.domain.creditrepayment.CreditrepaymentActRepository;
import com.liyang.domain.creditrepayment.CreditrepaymentFile;
import com.liyang.domain.creditrepayment.CreditrepaymentLog;
import com.liyang.domain.creditrepayment.CreditrepaymentLogRepository;
import com.liyang.domain.creditrepayment.CreditrepaymentRepository;
import com.liyang.domain.creditrepayment.CreditrepaymentState;
import com.liyang.domain.creditrepayment.CreditrepaymentStateRepository;
import com.liyang.domain.creditrepayment.CreditrepaymentWorkflow;
import com.liyang.domain.creditrepayment.CreditrepaymentWorkflowRepository;
import com.liyang.domain.base.ActRepository;
import com.liyang.domain.base.AuditorEntityRepository;
import com.liyang.domain.base.LogRepository;
import com.liyang.domain.role.RoleRepository;

@Service
@Order(312)
public class CreditrepaymentService extends AbstractWorkflowService<Creditrepayment, CreditrepaymentWorkflow, CreditrepaymentAct, CreditrepaymentState, CreditrepaymentLog, CreditrepaymentFile> {
    @Autowired
    CreditrepaymentActRepository CreditrepaymentActRepository;

    @Autowired
    CreditrepaymentStateRepository CreditrepaymentStateRepository;

    @Autowired
    CreditrepaymentLogRepository CreditrepaymentLogRepository;

    @Autowired
    CreditrepaymentRepository CreditrepaymentRepository;
    //
    @Autowired
    CreditrepaymentWorkflowRepository creditrepaymentWorkflowRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    WlqzWechatPubService wechatPubService;
    @Autowired
    CreditrepayplanRepository creditrepayplanRepository;
    @Autowired
    CreditrepayplanStateRepository creditrepayplanStateRepository;
    @Autowired
    DepartmenttypeRepository departmenttypeRepository;
    @Autowired
    LoanoverdueRepository loanoverdueRepository;
    @Autowired
    LoanRepository loanRepository;
    
    @Override
    @PostConstruct
    public void sqlInit() {
        long count = CreditrepaymentActRepository.count();
        if (count==0) {
            CreditrepaymentAct save1 = CreditrepaymentActRepository.save(new CreditrepaymentAct("创建", "create", 10, ActGroup.UPDATE));
            CreditrepaymentAct save2 = CreditrepaymentActRepository.save(new CreditrepaymentAct("读取", "read", 20, ActGroup.READ));
            CreditrepaymentAct save3 = CreditrepaymentActRepository.save(new CreditrepaymentAct("更新", "update", 30, ActGroup.UPDATE));
            CreditrepaymentAct save4 = CreditrepaymentActRepository.save(new CreditrepaymentAct("删除", "delete", 40, ActGroup.UPDATE));
            CreditrepaymentAct save5 = CreditrepaymentActRepository.save(new CreditrepaymentAct("自己的列表", "listOwn", 50, ActGroup.READ));
            CreditrepaymentAct save6 = CreditrepaymentActRepository.save(new CreditrepaymentAct("部门的列表", "listOwnDepartment", 60, ActGroup.READ));
            CreditrepaymentAct save7 = CreditrepaymentActRepository.save(new CreditrepaymentAct("部门和下属部门的列表", "listOwnDepartmentAndChildren", 70, ActGroup.READ));
            CreditrepaymentAct save8 = CreditrepaymentActRepository.save(new CreditrepaymentAct("通知其他人", "noticeOther", 80, ActGroup.NOTICE));
            CreditrepaymentAct save9 = CreditrepaymentActRepository.save(new CreditrepaymentAct("通知操作者", "noticeActUser", 90, ActGroup.NOTICE));
            CreditrepaymentAct save10 = CreditrepaymentActRepository.save(new CreditrepaymentAct("通知展示人", "noticeShowUser", 100, ActGroup.NOTICE));


            CreditrepaymentState newState = new CreditrepaymentState("待确认", 0, "CREATED");
            newState = CreditrepaymentStateRepository.save(newState);

            CreditrepaymentState enableState = new CreditrepaymentState("有效", 100, "ENABLED");
            enableState.setActs(Arrays.asList(save1, save2, save3, save4, save5, save6, save7).stream().collect(Collectors.toSet()));
            CreditrepaymentStateRepository.save(enableState);
            CreditrepaymentStateRepository.save(new CreditrepaymentState("无效", 200, "DISABLED"));
            CreditrepaymentStateRepository.save(new CreditrepaymentState("已删除", 300, "DELETED"));
        }
    }

    @Override
    public LogRepository<CreditrepaymentLog> getLogRepository() {
        return CreditrepaymentLogRepository;
    }

    @Override
    public AuditorEntityRepository<Creditrepayment> getAuditorEntityRepository() {
        return CreditrepaymentRepository;
    }


    @Override
    @PostConstruct
    public void injectLogRepository() {
        new Creditrepayment().setLogRepository(CreditrepaymentLogRepository);
    }

    @Override
    public CreditrepaymentLog getLogInstance() {
        return new CreditrepaymentLog();
    }

    @Override
    public ActRepository<CreditrepaymentAct> getActRepository() {
        return CreditrepaymentActRepository;
    }

    @Override
    @PostConstruct
    public void injectEntityService() {
        new Creditrepayment().setService(this);
    }

    @Override
    public CreditrepaymentFile getFileLogInstance() {
        return new CreditrepaymentFile();
    }

    public void save(Creditrepayment Creditrepayment) {
        //wechatPubService.pushOpenAccMessage(toUser, url, templateId, childMap);
        CreditrepaymentRepository.save(Creditrepayment);
    }

    public void doCreate(Creditrepayment creditrepayment) {
//        String planSN = creditrepayment.getPlanSn();
//        if (planSN == null) {
//            throw new FailReturnObject(1634, "请选择还款的期号", ReturnObject.Level.INFO);
//        }
//        Creditrepayplan creditrepayplan = creditrepayplanRepository.findByPlanSn(planSN);
//        if (creditrepayplan == null) {
//            throw new FailReturnObject(1842, "请输入正确的还款计划", ReturnObject.Level.INFO);
//        }
//        Loan loan = creditrepayplan.getLoan();

//        Capitalproduct capitalproduct = loan.getOrdercdd().getProduct().getCapitalproduct();
//
//        Calendar calendar = Calendar.getInstance();
//        double nowMills = calendar.getTimeInMillis();
//
//        double repaymentdate = creditrepayplan.getDebtorRepaymentDate().getTime();
//        if (repaymentdate < nowMills) {//逾期了
//            creditrepayplan.setIsOverdue(1);
//            int ov = (int) Math.ceil((nowMills - repaymentdate) / 1000 / 60 / 60 / 24);
//            if (nowMills > creditrepayplan.getDebtorExtentedRepaymentDate().getTime()) {//逾期天数大于宽限天数
//                creditrepayplan.setOverdueDays(ov);//逾期天数
//                int ovDay = (int) Math.ceil((nowMills - creditrepayplan.getDebtorExtentedRepaymentDate().getTime()) / 1000 / 60 / 60 / 24);//实际逾期天数
//
//                BigDecimal overdueAmount=creditrepayplan.getOverdueAmount().add(loan.getPrincipal().multiply(BigDecimal.valueOf(ovDay)).multiply(capitalproduct.getOverduePenaltyInterestRate()));
//                creditrepayplan.setOverdueAmount(overdueAmount);
//                creditrepayplan.setLeftAmount(creditrepayplan.getLeftAmount().add(overdueAmount));
//            }
//
//        }
        Loan loan = null;
        Creditrepayplan creditrepayplan = null;
        /**
         *$feels = creditrepayment.getFees();
         * $loanOverdue =Loan.getOverdue();
         * $loan=null;
         * if($loanOverDue){
         *      $overdueAmount = $loanOverdue.getOverdueAmount();
         *      if($feels.compareTo($overdueAmount)<0)
         *      {
         *          HashMap map=new HashMap<>();
         *          map.put("overdueAmount",$overdueAmount);
         *          map.put("message","您逾期了");
         *          ObjectMapper mapper =new ObjectMapper();
         *          String mapStr = mapper.writeValueAsString(map);
         *          throw new FailObject(1532,mapStr,Level.INFO);
         *      }
         *
         *$loan=payment.getLoan();//还款记录的loan谁还谁传
         *      loanOverdue.serOverdue=false;//财务确认后进行相关修改
         *      loan.setOverdue=false;//
         *
         * }else{
         *   String planSN = creditrepayment.getPlanSn();
         //        if (planSN == null) {
         //            throw new FailReturnObject(1634, "请选择还款的期号", ReturnObject.Level.INFO);
         //        }
         //        Creditrepayplan creditrepayplan = creditrepayplanRepository.findByPlanSn(planSN);
         //        if (creditrepayplan == null) {
         //            throw new FailReturnObject(1842, "请输入正确的还款计划", ReturnObject.Level.INFO);
         //        }
         *
         * }
         */
        BigDecimal fees = creditrepayment.getFees();
        Assert.notNull(fees, "还款费用不能为空");

        loan = creditrepayment.getLoan();
        Assert.notNull(loan, "loan不能为空");

        Ordercdd ordercdd = loan.getOrdercdd();

        creditrepayment.setDebtorName(ordercdd.getRealName());
        creditrepayment.setLoan(loan);
        creditrepayment.setLoanSn(loan.getLoanSn());
        creditrepayment.setOrderSn(ordercdd.getCddSn());
//        creditrepayment.setCreditrepayplan(creditrepayplan);
//        creditrepayment.setDebtorRepaymentDate(creditrepayplan.getDebtorRepaymentDate());
        creditrepayment.setDebtorActualRepaymentDate(new Date());

        creditrepayment.setWorkflow(creditrepaymentWorkflowRepository.findOne(1));//工作流1


        //李斌那边查询
        Product product = ordercdd.getProduct();
        creditrepayment.setCreatedByDepartment(product.getDepartment());
        creditrepayment.setMentSn(SerialNumberUtil.INSTANCE.create(SerialNumberUtil.SerialType.LOANCREDITREPAYMENT));
//        creditrepayplanRepository.save(creditrepayplan);
    }

    private void paymentOverdue(Loan loan, BigDecimal fees, Creditrepayment creditrepayment)//还逾期的
    {

        Loanoverdue loanoverdue = getOverdueByLoan(loan);
        if(loanoverdue==null||(fees.compareTo(BigDecimal.valueOf(0))<=0)){
            return;
        }

        BigDecimal subOverdue = loanoverdue.getOverdueAmount().subtract(fees);
        int resultSub = subOverdue.compareTo(BigDecimal.valueOf(0));
        if (resultSub <= 0) {
            loanoverdue.setOverdueAmount(subOverdue);
            loanoverdue.setStatus(1);
            loanoverdue.setFinishAt(new Date());
            loan.setOverdue(false);
            loanRepository.save(loan);

        }
        if (resultSub > 0) {
            loanoverdue.setOverdueAmount(subOverdue);
        }

        loanoverdue.setCreditrepayment(creditrepayment);


        loanoverdueRepository.save(loanoverdue);
    }

    private void paymentPlan(Creditrepayment creditrepayment) {//还还款计划
        //先还已出账的，再还逾期
        Assert.notNull(creditrepayment.getFees(), "客户还款的费用不能为空");
        Assert.notNull(creditrepayment.getRealAmount(), "客户还款实际到账金额不能为空");
        Loan loan = creditrepayment.getLoan();
        BigDecimal realAmount = creditrepayment.getRealAmount();//实际到账金额
        BigDecimal leftoverFees= realAmount;//还过剩下的钱
        BigDecimal paid=new BigDecimal(0);//已还的钱
        List<Creditrepayplan> creditrepayplans = creditrepayplanRepository.getAccountedPlanByLoansn(loan.getLoanSn());//获取已出账的还款计划
        for (Creditrepayplan creditrepayplan : creditrepayplans) {
            BigDecimal leftAmount = creditrepayplan.getLeftAmount();
            leftoverFees = leftoverFees.subtract(leftAmount);
            int subResult = leftoverFees.compareTo(BigDecimal.valueOf(0));
            //如果实际还款金额比剩余应还金额多
            if (subResult >= 0) {
                paid = paid.add(creditrepayplan.getLeftAmount());
                creditrepayplan.setLeftAmount(BigDecimal.valueOf(0));
                creditrepayplan.setState(creditrepayplanStateRepository.findByStateCode("CLOSED"));
                creditrepayplanRepository.save(creditrepayplan);
                continue;
            }
            paid=paid.add(creditrepayplan.getLeftAmount().add(leftoverFees));
            creditrepayplan.setLeftAmount(leftoverFees.abs());
            creditrepayplanRepository.save(creditrepayplan);
            break;
        }

        Loanoverdue loanoverdue = getOverdueByLoan(loan);
        if (loanoverdue != null) {
            loanoverdue.setOverdueAmount(loanoverdue.getOverdueAmount().subtract(paid));//
            if(loanoverdue.getOverdueAmount().compareTo(BigDecimal.valueOf(0))<=0){//已结请
                loanoverdue.setStatus(1);
                loan.setOverdue(false);
                loan=loanRepository.save(loan);
            }
            loanoverdueRepository.save(loanoverdue);
        }
        if (leftoverFees.compareTo(BigDecimal.valueOf(0)) > 0) {//能还的还款计划还完了还有余额
            paymentOverdue(loan, leftoverFees, creditrepayment);
        }
    }

    private Loanoverdue getOverdueByLoan(Loan loan) {
        Assert.notNull(loan, "Loan must not be null");
        Set<Loanoverdue> loanoverdueSet=loan.getLoanoverdues();
        if(loanoverdueSet==null){
            return null;
        }
        Optional<Loanoverdue> loanoverdueOptional = loan.getLoanoverdues().stream().filter(o -> o.getStatus() == 0).findFirst();
        return loanoverdueOptional.orElse(null);
    }


    /**确认到账
     * @param creditrepayment
     */
    public void doConfirm(Creditrepayment creditrepayment) {
        assertRepaymentType(creditrepayment.getRepaymentType(),0,1,2,3,4);
        paymentPlan(creditrepayment);
        //发送确认还款消息
        //逾期
        BigDecimal planFees = new BigDecimal(0);
        BigDecimal leftAmount = new BigDecimal(0);
        Loanoverdue loanoverdue = loanoverdueRepository.findEnable(creditrepayment.getLoan().getId());
        if (loanoverdue != null) {
			planFees = loanoverdue.getHistoryAmount();
			planFees = loanoverdue.getOverdueAmount();
		}else {
			Creditrepayplan plan = creditrepayment.getCreditrepayplan();
			if (plan == null) {
				plan = creditrepayplanRepository.findNormalPlan(creditrepayment.getLoan().getId());
			}
			if (plan != null) {
				planFees = plan.getFees();
				leftAmount = plan.getLeftAmount();
			}
		}
        Loan loan = creditrepayment.getLoan();
        wechatPubService.pushCreditrepaymentMsg(loan.getDebtorUser(), null, 
        		loan.getPrincipal(), planFees, creditrepayment.getRealAmount(), leftAmount, 
        		DateFormatUtils.format(creditrepayment.getCreatedAt(), "yyyy年MM月dd日  HH:mm"));
    }

    /**
     *
     * @param types 允许的类型
     */
    private void assertRepaymentType(int repaymentType,int ...types){
        boolean result = false;
        for(int type : types)
        {
            if(type==repaymentType){
                result=true;
                break;
            }
        }
        if(!result){
            throw new FailReturnObject(1654,"还款类型错误", ReturnObject.Level.INFO);
        }
    }
    //一次还清
    public void doOnce(Creditrepayment creditrepayment)
    {
        //2 一次还清 3 门店代付还清
        assertRepaymentType(creditrepayment.getRepaymentType(),2,3);
        Loan loan = loanRepository.findOne(creditrepayment.getLoan().getId());
        Assert.notNull(loan, "没有对应的loan");
        if(!"ENABLED".equals(loan.getState().getStateCode())){
            throw new FailReturnObject(1523,"贷款的状态不是已放款，请联系管理人员", ReturnObject.Level.INFO);
        }
        //获取已出账和未出账的
        List<Creditrepayplan> creditrepayplans = creditrepayplanRepository.getAccountedAndAccountPlanByLoansn(loan.getLoanSn());
        creditrepayplans.forEach(creditrepayplan -> {
            creditrepayplan.setLeftAmount(BigDecimal.valueOf(0));
            creditrepayplan.setState(creditrepayplanStateRepository.findByStateCode("CLOSED"));
            creditrepayplanRepository.save(creditrepayplan);
        });

        Loanoverdue loanoverdue = getOverdueByLoan(loan);
        if(loanoverdue!=null){
            loanoverdue.setStatus(1);
            loanoverdue.setFinishAt(new Date());
            loanoverdue.setCreditrepayment(creditrepayment);
            loanoverdueRepository.save(loanoverdue);
        }

        loan.setOverdue(false);
        loanRepository.save(loan);
    }

    @Override
    public CreditrepaymentLog fillmultiWechatImageToLog(Creditrepayment entity, CreditrepaymentLog log) {
        // TODO Auto-generated method stub
        String[] wechatFiles = entity.getWechatFiles();
        for (String fileName : wechatFiles) {
            CreditrepaymentFile fileLogInstance = getFileLogInstance();
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
            CreditrepaymentFile file = fileRepository.save(fileLogInstance);
            applicationContext.publishEvent(new WechatImageAsyncFetchEvent(entity, file));
        }
        return log;
    }

    public void doBalance(Creditrepayment creditrepayment)//全部结算
    {
        //plan 获取出账和未出账的所有
        //loanOverdue 逾期的
    }
}
