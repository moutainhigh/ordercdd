package com.liyang.domain.base;

import java.util.ArrayList;
import java.util.List;

import com.liyang.domain.base.AbstractAuditorEntity.BaseAct;

public class FilterActUtil {
	public static List<BaseAct> ignoreAct(Object actList, List<String> ignoreActCodes) {
		List<BaseAct> obj = (List<BaseAct>) actList;
		List<BaseAct> newActList = new ArrayList<>();
		for (BaseAct act : obj) {
			String actCode = act.getActCode();
			if (ignoreActCodes.contains(actCode)) {
				continue;
			}
			newActList.add(act);
		}
		return newActList;
	}
}
