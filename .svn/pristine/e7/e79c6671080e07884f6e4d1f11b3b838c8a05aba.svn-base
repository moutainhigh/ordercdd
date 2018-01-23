package com.liyang.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.liyang.domain.base.AbstractWorkflowFile;

public class FileRecordCopy {
	
	/**复制文件记录公用方法
     * @param from
     * @param to
     */
    public static void copyFileRecord(AbstractWorkflowFile from, AbstractWorkflowFile to) {
    	try {
			BeanUtils.copyProperties(to, from);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return;
		}
    	to.setId(null);
    	to.setAct(null);
    	to.setLog(null);
    	to.setLastModifiedBy(CommonUtil.getPrincipal());
    	to.setCreatedBy(CommonUtil.getPrincipal());
    	to.setCreatedByDepartment(CommonUtil.getCurrentUserDepartment());
	}
}
