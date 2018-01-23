package com.liyang.websocket;

import com.liyang.domain.notice.Notice;
import com.liyang.domain.notice.NoticeRepository;
import com.liyang.domain.user.User;
import com.liyang.util.FailReturnObject;
import com.liyang.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public class MyHandle extends TextWebSocketHandler {
    private static Map<Integer, WebSocketSession> sessions = new HashMap<>();
    @Autowired
    NoticeRepository noticeRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
       UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) session.getPrincipal();
        if (auth == null) {
            session.sendMessage(new TextMessage("没有认证的链接，已关闭"));
            session.close();
            return;
        }
        User user = (User)auth.getPrincipal();
        sessions.put(user.getId(), session);
//        Set<User> userList = new HashSet<>();
//        userList.add(user);
//        List<Notice> noticeList= noticeRepository.findAllUnreadByUsers(userList);

//        sendNoticeByUser(user,noticeList);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) session.getPrincipal();
        if (auth != null) {
            User user=(User)auth.getPrincipal();
            sessions.remove(user.getId());
        }
    }

    @SuppressWarnings("ThrowableNotThrown")
    public void sendNoticeByUser(User user, Notice notice) {
        Assert.notNull(user, "user not be must null");
        Assert.notNull(notice, "notice not be must null");
        Assert.notNull(notice.getNoticeInfo().getContent(), "content not be must null");
        WebSocketSession session = sessions.get(user.getId());
        if (session == null) {
            return;
        }
        TextMessage message = new TextMessage(notice.getNoticeInfo().getContent());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            notice.setStatus(Notice.Status.UNSENT);
            noticeRepository.save(notice);
        }
    }
    public void sendNoticeByUser(Collection<User> users, Notice notice) {
        Assert.notNull(users,"users not be must null");
        users.forEach(user -> {
            sendNoticeByUser(user,notice);
        });
    }
    public void sendNoticeByUser(User user, List<Notice> notices) {
        Assert.notNull(user,"users not be must null");
        Assert.notNull(notices,"user not be must null");
        notices.forEach(notice -> {
            sendNoticeByUser(user,notice);
        });
    }
}
