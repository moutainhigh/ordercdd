package com.liyang.controller;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.liyang.util.CommonUtil;
import com.liyang.util.FailReturnObject;
import com.liyang.util.FreeMarkerUtil;
import com.liyang.util.ReturnObject.Level;

@Controller
public class TemplateController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/template/{entityName}/{viewType}", method = RequestMethod.GET)
	public String view(@PathVariable String entityName , @PathVariable String viewType) {
		String template = CommonUtil.template(entityName, viewType);
		return template;
	}
		
	/**合同模板内容替换，生成具体合同
	 * @param templateName
	 * @param params
	 * @return 填好数据的HTML字符串
	 */
	@ResponseBody
	@RequestMapping("/contract/template/create/{templateName}")
	public String contractCreate(@PathVariable String templateName,@RequestParam(required=false) Map<String,Object> params) {
		FreeMarkerUtil util = null;
		try {
			if (templateName.contains(".")) {
				int pos = templateName.lastIndexOf(".");
				templateName = templateName.substring(0, pos);
			}
			templateName = templateName +".html";
			util = new FreeMarkerUtil(templateName);
			return util.process(params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FailReturnObject(1, "模板引擎初始化出错："+e, Level.ERROR);
		}
	}
	
	/**跳转到新打开的窗口，展示合同内容
	 * @param templateName
	 * @param params
	 * @return
	 */
	@RequestMapping("/contract/template/forward/{templateName}")
	public ModelAndView contractForward(@PathVariable String templateName,@RequestParam(required=false) Map<String,Object> params) {
		FreeMarkerUtil util = null;
		try {
			if (templateName.contains(".")) {
				int pos = templateName.lastIndexOf(".");
				templateName = templateName.substring(0, pos);
			}
			templateName = templateName +".html";
			util = new FreeMarkerUtil(templateName);
			String content = util.process(params);
			ModelAndView view = new ModelAndView();
			view.addObject("content", content);
			view.setViewName("contract/templateContent");
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FailReturnObject(1, "模板引擎初始化出错："+e, Level.ERROR);
		}
	}
}
