package com.liyang.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liyang.scheduled.CreditrepayplanScheduled;
import com.liyang.scheduled.LoanoverdueScheduled;

/**
 * 临时测试接口
 */
@RestController
@RequestMapping("/test")
public class ATestTemp {
	@Resource
	private CreditrepayplanScheduled planScheduled;
	@Resource
	private LoanoverdueScheduled loanoverdueScheduled;
	
	@RequestMapping("/plan")
	public String plan() {
		planScheduled.activePlan();
		return "OK";
	}
	
	@RequestMapping("/loanoverdue")
	public String loanoverdue() {
		loanoverdueScheduled.scheduled();
		return "OK";
	}
	
	@RequestMapping("/run")
	public String run() {
		planScheduled.activePlan();
		loanoverdueScheduled.scheduled();
		return "OK";
	}
	
	@RequestMapping("/test")
	public String test() {
		return "test OK";
	}
}
