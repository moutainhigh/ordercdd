package com.liyang.controller;

import com.liyang.service.ExceptionService;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject;
import com.liyang.util.ReturnObjectImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.webmvc.OKException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Profile("cdd")
public class GlobalExceptionHandler {

	@Autowired
	ExceptionService exceptionService;

	@ExceptionHandler(java.lang.Exception.class)
	@ResponseBody
	public ReturnObject handleBizExp(java.lang.Exception ex) throws java.lang.Exception {

		if (ex instanceof FailReturnObject) {
			exceptionService.log((ReturnObject)ex);
			ReturnObjectImpl returnObjectImpl = new ReturnObjectImpl();
			returnObjectImpl.setActionStatus(((FailReturnObject) ex).getActionStatus());
			returnObjectImpl.setErrorCode(((FailReturnObject) ex).getErrorCode());
			returnObjectImpl.setErrorInfo(((FailReturnObject) ex).getErrorInfo());
			return returnObjectImpl;
		} else if(ex instanceof OKException){
			return null;
		}else{
			exceptionService.log(ex);
			ReturnObjectImpl returnObjectImpl = new ReturnObjectImpl();
			returnObjectImpl.setActionStatus("FAIL");
			returnObjectImpl.setErrorCode(500);
			returnObjectImpl.setErrorInfo(ex.getMessage());
			return returnObjectImpl;
		}

	}
}