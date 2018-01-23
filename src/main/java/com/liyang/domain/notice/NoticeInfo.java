package com.liyang.domain.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


//@RestResource(exported = false)
@Entity
@Table(name = "notice_info")
public class NoticeInfo implements Serializable{
    private static final long serialVersionUID = 7015252822164677702L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Info(label = "id")
    private Integer id;
    @Column(name = "created_at")
    @Info(label = "创建时间")
    private Date createdAt;
    @Lob
    @Column(name = "content",nullable = false)
    private String content;
    @JsonIgnore
    @OneToOne(mappedBy = "noticeInfo")
    private Notice notice;
    @PrePersist
    public void perPersist()
    {
        if(createdAt==null){
            createdAt=new Date();
        }
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
