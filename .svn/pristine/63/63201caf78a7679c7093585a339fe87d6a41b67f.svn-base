package com.liyang.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liyang.domain.base.AbstractAuditorAct;
import com.liyang.domain.base.AbstractAuditorEntity;
import com.liyang.domain.base.AbstractAuditorLog;
import com.liyang.domain.base.AbstractWorkflowEntity;
import com.liyang.domain.department.Department;
import com.liyang.domain.department.Departmenttype;
import com.liyang.domain.exception.Exception;
import com.liyang.domain.exception.ExceptionRepository;
import com.liyang.domain.notice.Notice;
import com.liyang.domain.notice.NoticeInfo;
import com.liyang.domain.notice.NoticeInfoRepository;
import com.liyang.domain.notice.NoticeRepository;
import com.liyang.domain.role.Role;
import com.liyang.domain.role.RoleRepository;
import com.liyang.domain.user.User;
import com.liyang.domain.user.UserRepository;
import com.liyang.message.CustomContent;
import com.liyang.message.Message;
import com.liyang.util.*;
import com.liyang.websocket.MyHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ExceptionRepository exceptionRepository;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MyHandle websocket;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    NoticeInfoRepository noticeInfoRepository;

    public void doNotice(User fromUser, AbstractAuditorEntity entity, AbstractAuditorAct act) {
        if (request.getParameter("notice") != null && !"".equals(request.getParameter("notice"))) {
            entity.setNotice(request.getParameter("notice"));
        }

        if (act.getNoticeSelfTemplate() != null && !act.getNoticeSelfTemplate().trim().equals("")) {
            AbstractAuditorAct actNotice = entity.getService().getActRepository().findByActCode("noticeActUser");
            if (actNotice == null) {
                throw new FailReturnObject(1800, "noticeActUser动作不存在", com.liyang.util.ReturnObject.Level.FATAL);
            }
            String sendCustomElemMessage = sendCustomElemMessage(CommonUtil.getPrincipal(), entity, act,
                    act.getNoticeSelfTemplate());
            if (sendCustomElemMessage != null) {

                noticeLog(CommonUtil.getPrincipal(), entity, actNotice, sendCustomElemMessage);
            }
        }
        if (act.getNoticeShowTemplate() != null && !act.getNoticeShowTemplate().trim().equals("")) {
            AbstractAuditorAct actNotice = entity.getService().getActRepository().findByActCode("noticeShowUser");
            if (actNotice == null) {
                throw new FailReturnObject(1810, "noticeShowUser动作不存在", com.liyang.util.ReturnObject.Level.ERROR);
            }
            String sendCustomElemMessage = sendCustomElemMessage(fromUser, entity, act, act.getNoticeShowTemplate());
            if (sendCustomElemMessage != null) {
                noticeLog(fromUser, entity, actNotice, sendCustomElemMessage);
            }
        }
        if (act.getNoticeOtherTemplate() != null && !act.getNoticeOtherTemplate().trim().equals("")) {
            AbstractAuditorAct actNotice = entity.getService().getActRepository().findByActCode("noticeOther");
            if (actNotice == null) {
                throw new FailReturnObject(1820, "noticeOther动作不存在", com.liyang.util.ReturnObject.Level.FATAL);
            }
            if (entity.getNotice() != null && !entity.getNotice().equals("")) {

                String[] split = entity.getNotice().split(",");

                for (String u : split) {
                    //User findByUnionid = userRepository.findByUnionid(u);
                    User findOne = userRepository.findOne(Integer.valueOf(u));//unionid 该 openid
                    String sendCustomElemMessage = sendCustomElemMessage(findOne, entity, act,
                            act.getNoticeOtherTemplate());
                    if (sendCustomElemMessage != null) {
                        noticeLog(findOne, entity, actNotice, sendCustomElemMessage);
                    }
                }
            }
//            if (act.getNoticeDepartmenttype() != null && act.getNoticeRole() != null) {
//            if (act.getNoticeDepartmenttype() != null && act.getNoticeRoles() != null) {
            if (act.getNoticeRoles() != null) {

//                List<User> users = walkByDepartmentTypeAndRole(entity, act.getNoticeDepartmenttype(), act.getNoticeRole());
                List<User> users = walkByDepartmentTypeAndRole(entity, act.getNoticeDepartmenttype(), act.getNoticeRoles());

                if (users == null || users.isEmpty()) {
                    Exception exception = new Exception();
                    exception.setActionStatus("FAIL");
                    exception.setErrorCode(3102);
                    StringBuilder sb2 = new StringBuilder();
                    exception.setErrorInfo(sb2.append(entity.getClass().getSimpleName()).append("的动作").append(act.getLabel()).append("的自动通知没有用户接收").toString());
                    exceptionRepository.save(exception);

                } else {
                    for (User user : users) {
                        String sendCustomElemMessage = sendCustomElemMessage(user, entity, act,
                                act.getNoticeOtherTemplate());
                        if (sendCustomElemMessage != null) {
                            noticeLog(user, entity, actNotice, sendCustomElemMessage);
                        }
                    }
                }
            }
        }
    }

    private List<User> walkByDepartmentTypeAndRole(AbstractAuditorEntity entity, Departmenttype noticeDepartmentType,
                                                   String noticeRoleStr) {

        if (entity.getCreatedByDepartment() != null) {
            Department department = searchDepartment(entity.getCreatedByDepartment(), noticeDepartmentType);
            if (department == null) {
                Exception exception = new Exception();
                exception.setActionStatus("FAIL");
                exception.setErrorCode(3114);
                StringBuilder sb3 = new StringBuilder();
                exception.setErrorInfo(sb3.append(entity.getClass().getSimpleName()).append(entity.toString()).append("通知的部门类型")
                        .append(noticeDepartmentType.getDepartmenttypeCode()).append("没有部门").toString());
                exceptionRepository.save(exception);
            } else {
//                Set<Role> deptRoles = department.getDepartmenttype().getRoles();

                String[] noticeRoleStrArr = noticeRoleStr.split("-");
                List<Integer> roleIdList = new ArrayList<>();
                for (String noticeRoleID : noticeRoleStrArr) {
                    roleIdList.add(Integer.valueOf(noticeRoleID));
                }
                List<Role> noticeRoles = roleRepository.findAllByIdIn(roleIdList);
//                noticeRoles.retainAll(deptRoles);
//                if (!noticeRoles.isEmpty()) {
//                    StringBuilder roleCodes=new StringBuilder();
//                    noticeRoles.forEach(item->{
//                        roleCodes.append(item.getRoleCode()).append(',');
//                    });
//                    Exception exception = new Exception();
//                    exception.setActionStatus("FAIL");
//                    exception.setErrorCode(3116);
//                    StringBuilder sb4 = new StringBuilder();
//                    exception.setErrorInfo(sb4.append("通知的部门类型").append(noticeDepartmentType.getDepartmenttypeCode()).append("没有角色")
//                            .append(roleCodes).toString());
//                    exceptionRepository.save(exception);
//                } else {
//                    return userRepository.findAllByDepartmentAndRoleIn(department, noticeRoles);
                return userRepository.findAllByDepartmentAndRoleIn(department, noticeRoles);
//                }
            }
        } else {
            Exception exception = new Exception();
            exception.setActionStatus("FAIL");
            exception.setErrorCode(3112);
            StringBuilder sb6 = new StringBuilder();
            exception.setErrorInfo(sb6.append(entity.getClass().getSimpleName()).append(":").append(entity.toString()).append("没有创建部门").toString());
            exceptionRepository.save(exception);
        }


        return null;
    }


    private Department searchDepartment(Department createdByDepartment, Departmenttype noticeDepartmentType) {
        if (createdByDepartment.getDepartmenttype().equals(noticeDepartmentType)) {
            return createdByDepartment;
        } else {
            if (createdByDepartment.getParent() != null) {
                return searchDepartment(createdByDepartment.getParent(), noticeDepartmentType);
            }
            return null;
        }
    }


    private void noticeLog(User user, AbstractAuditorEntity entity, AbstractAuditorAct act, String message) {
        AbstractAuditorLog logInstance = entity.getLogInstance();
        logInstance.setAct(act);
        logInstance.setActGroup(act.getActGroup());
        logInstance.setEntity(entity);
        logInstance.setLabel(act.getLabel());
        logInstance.setNoticeTo(user);
        logInstance.setNotice(message);
        entity.getLogRepository().save(logInstance);
    }

    public String sendCustomElemMessage(User user, AbstractAuditorEntity entity, AbstractAuditorAct act,
                                        String template) {
        if (act.getMessageSender() == null) {
            throw new FailReturnObject(6361, "发送的账户不存在", com.liyang.util.ReturnObject.Level.ERROR);
        }


        if (user != null) {
//            String url = String.format(urlTemplate, "openim", "sendmsg", new Random().nextInt(700000000));
            String dataFromTemplate = getDataFromTemplate(user, entity, act, template);
            if (dataFromTemplate == null) {
                throw new FailReturnObject(1900, "没有设置行为消息模板", com.liyang.util.ReturnObject.Level.FATAL);
            }

            CustomContent.Ext ext = new CustomContent.Ext();
            ext.setEntityId(entity.getId());
            ext.setEntityName(entity.getClass().getSimpleName());
            ext.setType("notice");
            if (entity instanceof AbstractWorkflowEntity) {
                ext.setEntityType("workflowEntity");
                AbstractWorkflowEntity entity1 = (AbstractWorkflowEntity) entity;//2017-10-19 12:04:27 ---Jax
                if (entity1.getWorkflow() != null) {
                    ext.setWorkflowId(entity1.getWorkflow().getId());//
                }
            } else if (entity instanceof AbstractAuditorEntity) {
                ext.setEntityType("auditEntity");
            }

            Message message = CommonUtil.customElemMessageWrapper(dataFromTemplate, ext, act.getMessageSender().getId().toString(), user.getId().toString(), 2);
            sendMessageByUser(user, message);
            return dataFromTemplate;
        } else {
            return null;
        }
    }

    private void sendMessageByUser(User user, Message message) {
        Assert.notNull(user, "user not be must null");
        String jsonMessage = null;
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (jsonMessage != null) {
            Notice notice = new Notice();
            notice.setNoticeUser(user);

            NoticeInfo noticeInfo = new NoticeInfo();
            noticeInfo.setContent(jsonMessage);
            notice.setNoticeInfo(noticeInfo);
            notice.setNoticeUser(user);
            noticeInfoRepository.save(noticeInfo);
            noticeRepository.save(notice);
            websocket.sendNoticeByUser(user, notice);
        }

    }

    private String getDataFromTemplate(User user, AbstractAuditorEntity entity, AbstractAuditorAct act,
                                       String template) {
        NoticeService.SpelContext spelContext = new NoticeService.SpelContext(user, entity, act);
        ExpressionParser parser = new SpelExpressionParser();

        String result = parser.parseExpression(template, new TemplateParserContext()).getValue(spelContext,
                String.class);
        return result;

    }

    private ReturnObject restTemplatePost(String url, Object message) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String ret = restTemplate.postForObject(url, message, String.class);
        ObjectMapper mapper = new ObjectMapper();
        ReturnObjectImpl readValue;
        try {
            readValue = mapper.readValue(ret, ReturnObjectImpl.class);
        } catch (IOException e) {
            throw new FailReturnObject(1800, "解析tim信息错误", com.liyang.util.ReturnObject.Level.FATAL);
        }
        return readValue;
    }


    private class SpelContext {
        private User user;
        private AbstractAuditorEntity entity;
        private AbstractAuditorAct act;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public AbstractAuditorEntity getEntity() {
            return entity;
        }

        public void setEntity(AbstractAuditorEntity entity) {
            this.entity = entity;
        }

        public AbstractAuditorAct getAct() {
            return act;
        }

        public void setAct(AbstractAuditorAct act) {
            this.act = act;
        }

        public SpelContext(User user, AbstractAuditorEntity entity, AbstractAuditorAct act) {
            super();
            this.user = user;
            this.entity = entity;
            this.act = act;
        }

    }
}
