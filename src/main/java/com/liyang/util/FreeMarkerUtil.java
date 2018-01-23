package com.liyang.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 使用FreeMarker处理HTML模板
 * 默认模板目录template/contract
 * 默认合同模板contract.html
 * http://blog.csdn.net/chen413203144/article/details/78284237
 * https://www.cnblogs.com/lunatic/p/FreemarkerUtil.html
 * https://www.cnblogs.com/sloveling/p/4560824.html
 * 处理变量不存在的情况：
 * 1. <h1>Welcome ${user!"Anonymous"}!</h1>在变量名后面跟着一个!和默认值
 * 2. <#if user??><h1>Welcome ${user}!</h1></#if> 在变量名后面放置??来询问FreeMarker一个变量是否存在。将它和if指令合并，那么如果user变量不存在的话将会忽略整个问候代码段
 */
public class FreeMarkerUtil {
	private Configuration cfg;
    private String templateFolder = "/template/contract";
    private String templateFullName = "contract.html";
    
    public FreeMarkerUtil() throws Exception{
        init();
    }
    public FreeMarkerUtil(String templateFullName) throws Exception{
        this.templateFullName = templateFullName;
        init();
    }
    public FreeMarkerUtil(String templateFolder,String templateFullName) throws Exception{
        this.templateFolder = templateFolder;
        this.templateFullName = templateFullName;
        init();
    }
    
    //初始化FreeMarker配置
    private void init() throws IOException{
        //创建一个Configuration实例
        cfg = new Configuration(Configuration.VERSION_2_3_26);
        //基于文件系统,不能将模板文件放入jar包中，否则找不到，只能是实际存在的绝对路径
//        cfg.setDirectoryForTemplateLoading(new File(templateFolder));
        //基于类路径,貌似是类的根路径，即class文件夹下的目录
        cfg.setClassForTemplateLoading(FreeMarkerUtil.class, templateFolder);
        //基于Servlet Context，指的是基于WebRoot下的template下的framemaker.ftl文件
//        cfg.setServletContextForTemplateLoading(null, File.separator+templateFolder);
        cfg.setDefaultEncoding("utf-8");
        cfg.setLocale(Locale.CHINA);
    }
    
    private void removeAnyNull(Map<String,Object> root) {
    	if (null == root) {
			root = new HashMap<>();
			return;
		}
    	Map<String,Object> result = new HashMap<>();
    	for (Map.Entry<String,Object> entry : root.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (key==null||"".equals(key)) {
				key = "null";
			}
			if (value==null) {
				value = "";
			}
			result.put(key, value);
		}
    	root = result;
    }
        
    public String process(Map<String,Object> root)throws Exception{
        removeAnyNull(root);
        Template t = cfg.getTemplate(templateFullName);
        StringWriter writer = new StringWriter(2048);
        t.process(root,writer);
        return writer.toString();
    
    }
    
    public String process(Map<String,Object> root,Writer writer)throws Exception{
    	removeAnyNull(root);
        Template t = cfg.getTemplate(templateFullName);
        if (null == writer) {
            writer = new StringWriter(2048);
        }
        t.process(root,writer);
        return writer.toString();
    
    }
    
    public void processConsole(Map<String,Object> root)throws Exception{
    	removeAnyNull(root);
        Template t = cfg.getTemplate(templateFullName);
        t.process(root, new OutputStreamWriter(System.out));
    }
}
