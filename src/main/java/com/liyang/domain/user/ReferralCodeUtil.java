package com.liyang.domain.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 生成并保存一个推荐码
 */
public class ReferralCodeUtil {
	private static Set<String> referralCodeSet = new HashSet<>();
	
	public static void refresh(Collection<String> data) {
		referralCodeSet.clear();
		referralCodeSet.addAll(data);
	}
	
	public static String create() {
		while (true) {
			String code = RandomStringUtils.random(4, true, true);
			if (referralCodeSet.contains(code)) {
				continue;
			}
			referralCodeSet.add(code);
			return code;
		}
	}
}
