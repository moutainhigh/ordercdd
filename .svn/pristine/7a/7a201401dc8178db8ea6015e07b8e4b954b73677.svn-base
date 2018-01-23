package com.liyang;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.liyang.util.FreeMarkerUtil;

public class FreeMarkerUtilTest {
	public static void test1() throws Exception {
		FreeMarkerUtil util = new FreeMarkerUtil();
		util.processConsole(null);
	}
	
	public static void test2() throws Exception {
		Map<String,Object> params = new HashMap<>();
		params.put("userName", "测试");
		params.put("userPhone", "123456");
		params.put("plateNumber", "浙A12345");
		params.put("serviceUserName", "测试服务员");
		params.put("vehicleType", "ABC");
		params.put("yearExpirationDate", "2017-11-17");
		params.put("insuranceExpirationDate", "2017-11-18");
		params.put("customerService", "测试客服");
		params.put("targetProperty", "不知道");
		params.put("mortgageProperty", "不知道");
		params.put("monthInterestRate", "0.001");
		params.put("managementExpense", "100");
		params.put("serviceCharge", "10");
		params.put("notarizationFee", "10");
		params.put("vehiclePremium", "10");
		params.put("GPSInstallationFee", "10");
		params.put("mortgageRegistrationFee", "10");
		params.put("abcd", "10");
		params.put("monthInterestRate", "0.01");
		params.put("otherFee", "10");
		params.put("lumpSumFee", "1000");
		params.put("bond", "100");
		params.put("IllegalDeposit", "100");
		params.put("otherDeposit", "100");
		params.put("TotalRefundableCharges", "300");
		params.put("CreditedAmount", "700");
		FreeMarkerUtil util = new FreeMarkerUtil();
		util.processConsole(params);
	}
	
	public static void main(String[] args) throws Exception {
		test2();
	}
}
