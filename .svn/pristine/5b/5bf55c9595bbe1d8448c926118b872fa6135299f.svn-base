package com.liyang.domain.notice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
//@Transactional
public class NoticeInfoTest {
    @Autowired
    NoticeInfoRepository noticeInfoRepository;
    @Test
    public void testCreatedAt()
    {
        NoticeInfo info = new NoticeInfo();
        info.setContent("");
        info = noticeInfoRepository.save(info);
        assertNotNull(info.getCreatedAt());
    }
}