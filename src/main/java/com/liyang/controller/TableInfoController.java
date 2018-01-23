package com.liyang.controller;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.monitor.dao.MonitorDaoJdbcImpl.FieldInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liyang.annotation.Info;
import com.liyang.domain.base.BaseEntity;
import com.liyang.domain.role.Role;
import com.liyang.domain.user.User;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObjectImpl;
import com.liyang.util.ReturnObject.Level;

@RestController
public class TableInfoController {
	@Autowired
	private List<Class> listClass;// 所有的实体类

	@RequestMapping("getEntityInfo")
	public Map<String, Object> getAllInfo()
	{

		return getInfo();

	}

	/**
	 * 组成最终map
	 * 
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @throws InfoException
	 */
	
	private String firstLower(String str){
		String lowerCase = str.substring(0, 1).toLowerCase();
		return lowerCase + str.substring(1);
	}
	
	public Map<String, Object> getInfo(){
		Map<String, Object> map = new HashMap<>();

		for (Class clazz : listClass) {

			Map<String, Object> fieldInfoMap = new HashMap<>();
			List<Field> fieldList = new ArrayList<>();
			map.put(firstLower(clazz.getSimpleName()), fieldInfoMap);


			if(clazz.getAnnotation(Info.class)!=null)
			{
				Info info = (Info) clazz.getAnnotation(Info.class);
				fieldInfoMap.put("self", buildInfo(info, firstLower(clazz.getSimpleName())));
			}else{
				fieldInfoMap.put("self", buildInfo(null, firstLower(clazz.getSimpleName())));
			}
			
			fieldList = getAllField(clazz, fieldList);// 获取该类中的所有Field

			for (Field field : fieldList) {
				// 枚举
				if (field.getType().isEnum()) {
					Map<String, Object> enumFieldMap = new HashMap<>();
					fieldInfoMap.put(field.getName(), enumFieldMap);
				
					enumFieldMap.put("self", buildInfo(field.getAnnotation(Info.class),field.getName()));
					
					Map<String, Object> valueMap = new HashMap<>();
					enumFieldMap.put("values",valueMap);
					
					Object[] enums = field.getType().getEnumConstants();
					for (Object object : enums) {
						
						
						// 获取枚举内部字段上的注解
						Info enumFieldInfo;
						try {
							enumFieldInfo = object.getClass().getField(object.toString()).getAnnotation(Info.class);
							valueMap.put(object.toString(), buildInfo(enumFieldInfo,object.getClass().getField(object.toString()).getName()));
						} catch (NoSuchFieldException e) {
							throw new FailReturnObject(4671, object.getClass().getSimpleName()+"没有field"+object.toString(),Level.ERROR);
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							throw new FailReturnObject(4691, object.getClass().getSimpleName()+"的"+object.toString()+"无法访问",Level.ERROR);
						}
						
						

					}
					// boolean
				} else if ("Boolean".equals(field.getType().getSimpleName()) || "boolean".equals(field.getType().getSimpleName())) {
					Map<String, Object> booleanMap = new HashMap<>();
					booleanMap.put("values", new HashMap<String,Object>());
					fieldInfoMap.put(field.getName(), booleanMap);
					
					Annotation[] annotations = field.getAnnotationsByType(Info.class);
					for (Annotation annotation : annotations) {
						Info info2 = (Info) annotation;
						setBooealnInfo(info2, booleanMap,field.getName());
					}

				} else {
					// 普通

					Info fieldInfo = field.getAnnotation(Info.class);
					fieldInfoMap.put(field.getName(), buildInfo(fieldInfo, field.getName()));
				}
			}

		}
		return map;
	}

	private void setBooealnInfo(Info info, Map<String, Object> map,String defaultLabel) {
		if (info.flag().toString().equals("self")) {
			map.put("self", buildInfo(info,defaultLabel));
		} else if (info.flag().toString().equals("True")) {
			((HashMap<String,Object>)map.get("values")).put("true", buildInfo(info,"true"));
		} else if (info.flag().toString().equals("False")) {
			((HashMap<String,Object>)map.get("values")).put("false", buildInfo(info,"false"));
		}
	}

	// 将info字段值注入到Description中
	private Description buildInfo(Info info, String defaultLabel) {
		Description description = new Description();

		if (info == null) {
			description.setLabel(defaultLabel);
			return description;
		} else {
			if (info.label() != null) {
				description.setLabel(info.label());
			} else {
				description.setLabel(defaultLabel);
			}
			description.setHelp(info.help());
			description.setPlaceholder(info.placeholder());
			description.setTip(info.tip());
			description.setSecret(info.secret());
			return description;
		}
	}

	/**
	 * 用于判断类或字段上是否有注解
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @throws InfoException
	 */
	public void checkInfoAnnotation(Object object) {

		if (object instanceof AnnotatedElement) {
			AnnotatedElement element = (AnnotatedElement) object;

			if (object instanceof Field) {
				Field field = (Field) object;
				if (field.getType().getSimpleName().equals("Boolean")
						|| field.getType().getSimpleName().equals("boolean")) {
					if (element.getAnnotationsByType(Info.class) != null
							&& element.getAnnotationsByType(Info.class).length != 2) {
						throw new FailReturnObject(8741, "boolean型变量需要两个注解",Level.ERROR);
					}

				}
			}

		}
	}

	/**
	 * 获取所有的字段
	 * 
	 * @param clazz
	 * @param list
	 * @return
	 */
	public List<Field> getAllField(Class clazz, List<Field> list) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if(field.getAnnotation(Transient.class) == null){
				list.add(field);
			}
		}
		if ("Object".equals(clazz.getSuperclass().getSimpleName())) {
			return list;
		} else {
			return getAllField(clazz.getSuperclass(), list);
		}
	}

	public static class Description {
		String label;
		String placeholder;
		String tip;
		String help;
		String secret;

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getPlaceholder() {
			return placeholder;
		}

		public void setPlaceholder(String placeholder) {
			this.placeholder = placeholder;
		}

		public String getTip() {
			return tip;
		}

		public void setTip(String tip) {
			this.tip = tip;
		}

		public String getHelp() {
			return help;
		}

		public void setHelp(String help) {
			this.help = help;
		}

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

	}
}
