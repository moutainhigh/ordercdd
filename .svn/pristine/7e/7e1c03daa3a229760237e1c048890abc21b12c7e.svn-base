//package com.liyang.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.liyang.Application;
//import com.liyang.domain.creditrepayment.Creditrepayment;
//import com.liyang.domain.creditrepayment.CreditrepaymentRepository;
//import com.liyang.domain.creditrepayment.CreditrepaymentState;
//import com.liyang.domain.creditrepayplan.Creditrepayplan;
//import com.liyang.domain.creditrepayplan.CreditrepayplanRepository;
//import com.liyang.domain.creditrepayplan.CreditrepayplanState;
//import com.liyang.domain.creditrepayplan.CreditrepayplanStateRepository;
//import com.liyang.domain.loan.*;
//import com.liyang.domain.user.User;
//import com.liyang.domain.user.UserRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
//import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestComponent;
//import org.springframework.security.authentication.TestingAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
//import org.springframework.test.annotation.TestAnnotationUtils;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//public class CreditrepaymentServiceTest {
//    @Autowired
//    CreditrepayplanRepository creditrepayplanRepository;
//    @Autowired
//    OrdercddService ordercddService;
//    @Autowired
//    LoanoverdueRepository loanoverdueRepository;
//    @Autowired
//    CreditrepaymentRepository creditrepaymentRepository;
//    @Autowired
//    LoanRepository loanRepository;
//    @Autowired
//    CreditrepaymentService creditrepaymentService;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    LoanStateRepository loanStateRepository;
//    @Autowired
//    CreditrepayplanStateRepository creditrepayplanStateRepository;
//    @Test
//    public void testPayment() throws JsonProcessingException {
//
//        Loan loan = new Loan();
//        loan.setDebtorInterest(BigDecimal.valueOf(0.1));
//        loan.setOverdue(true);
//        loan.setPeriodNumber(2);
//        loan.setLoanSn("1231234lhg");
//        loan.setPrincipal(BigDecimal.valueOf(20));
//        loanRepository.save(loan);
//
//        CreditrepayplanState accounted =creditrepayplanStateRepository.findByStateCode("ACCOUNTED");//已出账状态
//        Creditrepayplan creditrepayplan1 = new Creditrepayplan();
//        creditrepayplan1.setLoan(loan);
//        creditrepayplan1.setState(accounted);
//        creditrepayplan1.setLoanSn(loan.getLoanSn());
//        creditrepayplan1.setLeftAmount(BigDecimal.valueOf(100));//------------------------
//
//        Creditrepayplan creditrepayplan2 = new Creditrepayplan();
//        creditrepayplan2.setLoan(loan);
//        creditrepayplan2.setState(accounted);
//        creditrepayplan2.setLoanSn(loan.getLoanSn());
//        creditrepayplan2.setLeftAmount(BigDecimal.valueOf(120));//------------------------
//
//        creditrepayplanRepository.save(creditrepayplan1);
//        creditrepayplanRepository.save(creditrepayplan2);
//
//
//        Loanoverdue loanoverdue = new Loanoverdue();
//        loanoverdue.setOverdueAmount(BigDecimal.valueOf(220));//---------------------------
//        loanoverdue.setHistoryAmount(BigDecimal.valueOf(10));
//        loanoverdue.setPenalSum(BigDecimal.valueOf(10));
//        loanoverdue.setStatus(0);
//        loanoverdue.setLoan(loan);
//
//        loanoverdue=loanoverdueRepository.save(loanoverdue);
//        assertNotNull(loanoverdueRepository.findOne(loanoverdue.getId()).getLoan().getLoanoverdues());
//
//        //begin 第一次还款
//        Creditrepayment creditrepayment1 = new Creditrepayment();
//        creditrepayment1.setFees(BigDecimal.valueOf(20));
//        creditrepayment1.setLoan(loan);//只还到第一期
//
//        creditrepaymentRepository.save(creditrepayment1);
//
//        creditrepaymentService.doConfirm(creditrepaymentRepository.findById(creditrepayment1.getId()));
//        System.out.println(creditrepayplanRepository.findById(creditrepayplan1.getId()).getLeftAmount().longValue());
//        assertEquals(80,creditrepayplanRepository.findById(creditrepayplan1.getId()).getLeftAmount().intValue());//第一期变少了
//        assertEquals(120,creditrepayplanRepository.findById(creditrepayplan2.getId()).getLeftAmount().intValue());//第二期没变
//        assertEquals(200,loanoverdueRepository.findOne(loanoverdue.getId()).getOverdueAmount().intValue());//逾期表少20
//        assertEquals(loanRepository.findById(loan.getId()).getOverdue(),true);// loan 仍然逾期
//        //end
//
//        //第二次还款
//        Creditrepayment creditrepayment2 = new Creditrepayment();
//        creditrepayment2.setFees(BigDecimal.valueOf(90));
//        creditrepayment2.setLoan(loan);//还到第二期
//
//        creditrepaymentRepository.save(creditrepayment2);
//
//        creditrepaymentService.doConfirm(creditrepaymentRepository.findById(creditrepayment2.getId()));
//
//        assertEquals(0,creditrepayplanRepository.findById(creditrepayplan1.getId()).getLeftAmount().intValue());//第一期0
//        assertEquals(creditrepayplanRepository.findById(creditrepayplan1.getId()).getState().getStateCode(),"CLOSED");//第一期完成
//        assertEquals(110,creditrepayplanRepository.findById(creditrepayplan2.getId()).getLeftAmount().intValue());//第二90
//        assertEquals(loanRepository.findById(loan.getId()).getOverdue(),true);// loan 仍然逾期
//        //end
//
//        //第三次次
//        Creditrepayment creditrepayment3 = new Creditrepayment();
//        creditrepayment3.setFees(BigDecimal.valueOf(110));
//        creditrepayment3.setLoan(loan);//还到第二期
//
//        creditrepaymentRepository.save(creditrepayment3);
//
//        creditrepaymentService.doConfirm(creditrepaymentRepository.findById(creditrepayment3.getId()));
//
//        assertEquals(0,creditrepayplanRepository.findById(creditrepayplan1.getId()).getLeftAmount().intValue());//第一期0
//        assertEquals(creditrepayplanRepository.findById(creditrepayplan1.getId()).getState().getStateCode(),"CLOSED");//第一期完成
//        assertEquals(0,creditrepayplanRepository.findById(creditrepayplan2.getId()).getLeftAmount().intValue());//第二期0
//        assertEquals(creditrepayplanRepository.findById(creditrepayplan2.getId()).getState().getStateCode(),"CLOSED");//第二期完成
//        assertEquals(0,loanoverdueRepository.findOne(loanoverdue.getId()).getOverdueAmount().intValue());//逾期表少10
//        assertEquals(loanoverdueRepository.findOne(loanoverdue.getId()).getStatus().intValue(),1);//逾期结清
//        assertEquals(loanRepository.findById(loan.getId()).getOverdue(),false);// loan 已经不逾期了
//        //end
//
//        //begin还完计划还逾期
//        Loan loan2 = new Loan();
//        loan2.setDebtorInterest(BigDecimal.valueOf(0.1));
//        loan2.setOverdue(true);
//        loan2.setPeriodNumber(2);
//        loan2.setLoanSn("1231234lhg123");
//        loan2.setPrincipal(BigDecimal.valueOf(20));
//        loanRepository.save(loan2);
//
//        Creditrepayplan creditrepayplanOne = new Creditrepayplan();
//        creditrepayplanOne.setLoan(loan2);
//        creditrepayplanOne.setState(accounted);
//        creditrepayplanOne.setLoanSn(loan2.getLoanSn());
//        creditrepayplanOne.setLeftAmount(BigDecimal.valueOf(100));//------------------------
//
//        creditrepayplanRepository.save(creditrepayplanOne);
//
//
//        Loanoverdue loanoverdueTwo = new Loanoverdue();
//        loanoverdueTwo.setOverdueAmount(BigDecimal.valueOf(110));//---------------------------
//        loanoverdueTwo.setHistoryAmount(BigDecimal.valueOf(10));
//        loanoverdueTwo.setPenalSum(BigDecimal.valueOf(10));
//        loanoverdueTwo.setStatus(0);
//        loanoverdueTwo.setLoan(loan2);
//
//        loanoverdueTwo=loanoverdueRepository.save(loanoverdueTwo);
//        assertNotNull(loanoverdueRepository.findOne(loanoverdueTwo.getId()).getLoan().getLoanoverdues());
//
//
//        Creditrepayment creditrepaymentTWO = new Creditrepayment();
//        creditrepaymentTWO.setFees(BigDecimal.valueOf(110));
//        creditrepaymentTWO.setLoan(loan2);
//
//        creditrepaymentRepository.save(creditrepaymentTWO);
//
//        creditrepaymentService.doConfirm(creditrepaymentRepository.findById(creditrepaymentTWO.getId()));
//        assertEquals(0,creditrepayplanRepository.findById(creditrepayplanOne.getId()).getLeftAmount().intValue());//第一期0
//        assertEquals(creditrepayplanRepository.findById(creditrepayplanOne.getId()).getState().getStateCode(),"CLOSED");//第一期完成
//        assertEquals(0,loanoverdueRepository.findOne(loanoverdueTwo.getId()).getOverdueAmount().intValue());//逾期表少10
//        assertEquals(loanoverdueRepository.findOne(loanoverdueTwo.getId()).getStatus().intValue(),1);//逾期结清
//        assertEquals(loanRepository.findById(loan.getId()).getOverdue(),false);// loan 已经不逾期了
//    }
//
//}