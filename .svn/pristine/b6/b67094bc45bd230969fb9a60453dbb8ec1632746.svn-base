package com.liyang.domain.notice;

import com.jpa.query.expression.generic.SpecificationQueryRepository;
import com.liyang.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface NoticeRepository extends JpaRepository<Notice,Integer>,SpecificationQueryRepository<Notice> {
    @Override
    @Query("select n from  Notice n where n.noticeUser=?#{principal}")
    Page<Notice> findAll(Pageable pageable);
}
