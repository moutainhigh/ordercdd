package com.liyang.domain.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface NoticeInfoRepository extends JpaRepository<NoticeInfo,Integer> {
}
