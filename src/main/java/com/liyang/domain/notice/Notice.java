package com.liyang.domain.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyang.annotation.Info;
import com.liyang.domain.user.User;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "notice")
public class Notice implements Serializable {
    private static final long serialVersionUID = -2736989566304200813L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Info(label = "id")
    private Integer id;
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.SENT;
    private Boolean isRead = false;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notice_info_id", nullable = false)
    private NoticeInfo noticeInfo;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User noticeUser;

    public NoticeInfo getNoticeInfo() {
        return noticeInfo;
    }

    public void setNoticeInfo(NoticeInfo noticeInfo) {
        this.noticeInfo = noticeInfo;
    }

    public User getNoticeUser() {
        return noticeUser;
    }

    public void setNoticeUser(User noticeUser) {
        this.noticeUser = noticeUser;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        //        READ,//已读
//        UNREAD//未读
        SENT,//
        UNSENT,
    }
}
