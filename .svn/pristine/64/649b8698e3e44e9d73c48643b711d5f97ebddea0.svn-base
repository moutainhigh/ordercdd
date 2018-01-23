package com.liyang.excel;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
public class ExcelLoan extends ExcelMapUtil {
    public ExcelLoan() {
        initConfig();
    }

    public ExcelLoan(String excelName) {
        super(excelName);
        initConfig();
    }

    public ExcelLoan(String[] objectProperty) {
        super(objectProperty);
        initConfig();
    }


    private void initConfig() {
        String[] objectProperties = new String[]{"id", "loanSn", "ordercdd_realName", "ordercdd_applyMobile",
                "applyBankCardBranch", "applyBankCard", "principal", "oneTimeFee", "retreatFee", "debtorRealityAmount", "state_label", "loanTime",};
        String[] headings = new String[]{"序号", "借款单号", "客户姓名", "联系方式",
                "支行", "银行卡号", "贷款金额(元)", "一次性收费(元)", "可退收费(元)", "实际放款金额(元)", "放款状态", "放贷时间",};
        setObjectProperties(objectProperties);
        setHeadings(headings);
    }
}
