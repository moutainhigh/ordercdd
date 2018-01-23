package com.liyang.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.liyang.domain.base.AbstractWorkflowEntity;
import com.liyang.domain.base.AbstractWorkflowState;
import com.liyang.domain.department.Department;
import com.liyang.domain.user.User;

/**
 *工具类，用于将一个bean转换为另外一个bean或map
 */
public class BeanUtil {
	
	/**
	 * 通用的转换，将一些关联对象转换成map，而不使用原生对象，防止返回页面报错
	 */
	public static void commonConvert(Map<String, Object> map, AbstractWorkflowEntity bean) {
		map.remove("logRepository");
		map.remove("service");
		//补充state、files、logs
		map.put("state", bean.getState());
		map.put("createAt", DateFormatUtils.format(bean.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
//		map.put("files", bean.getFiles());
//		map.put("logs", bean.getLogs());
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof User) {
				User user = (User) value;
				Map<String, Object> userMap = new HashMap<>();
				userMap.put("id", user.getId());
				userMap.put("nickname", user.getNickname());
				userMap.put("username", user.getUsername());
				map.put(entry.getKey(), userMap);
				continue;
			}
			if (value instanceof Department) {
				Department department = (Department) value;
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("id", department.getId());
				departmentMap.put("label", department.getLabel());
				map.put(entry.getKey(), departmentMap);
				continue;
			}
			if (value instanceof AbstractWorkflowState) {//状态
				AbstractWorkflowState state = (AbstractWorkflowState) value;
				Map<String, Object> stateMap = new HashMap<>();
				stateMap.put("id", state.getId());
				stateMap.put("label", state.getLabel());
				stateMap.put("stateCode", state.getStateCode());
				stateMap.put("sort", state.getSort());
				map.put(entry.getKey(), stateMap);
				continue;
			}
		}
	}
	
	/**将一个bean转换为一个map
	 * 其父类的getter和字段无法自动获取到，只能手动补齐，又或者指明字段名，也可以获取到值
	 * @return
	 */
	public static Map<String, Object> beanToMap(Object bean) {
		Map<String, Object> result = new HashMap<>();
		if (bean == null) {
			return result;
		}
		Class<?> cls = bean.getClass();
		// 取出bean里的所有方法  
        Method[] methods = cls.getDeclaredMethods();
        Field[] fields = cls.getDeclaredFields();  
   
        for (Field field : fields) {
            try {  
                String fieldGetName = parGetName(field.getName());  
                if (!checkGetMet(methods, fieldGetName)) {  
                    continue;  
                }  
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});  
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});  
                result.put(field.getName(), fieldVal);
            } catch (Exception e) {
            	result.put(field.getName(), null);
                continue;  
            }  
        }
		return result;
	}
	
	private static boolean checkGetMet(Method[] methods, String fieldGetMet) {  
        for (Method met : methods) {  
            if (fieldGetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }
	
	private static String parGetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        return "get" + fieldName.substring(0, 1).toUpperCase()  
                + fieldName.substring(1);  
    } 
}
