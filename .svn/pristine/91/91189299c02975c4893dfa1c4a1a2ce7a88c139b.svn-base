package com.liyang.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.liyang.domain.user.ReferralCodeUtil;

/**
 * 应用上下文初始化或刷新
 */
@Component
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//加载用户推荐码
		System.out.println("加载所有推荐码...");
		List<String> all = jdbcTemplate.queryForList("select referral_code from user where referral_code IS NOT NULL",String.class);
		if (all != null) {
			ReferralCodeUtil.refresh(all);
			System.out.println("本次加载推荐码的个数为: "+ all.size());
		}
	}
}
