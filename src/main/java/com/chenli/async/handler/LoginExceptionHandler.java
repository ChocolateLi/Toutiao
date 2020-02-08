package com.chenli.async.handler;

import com.chenli.async.EventHandler;
import com.chenli.async.EventModel;
import com.chenli.async.EventType;
import com.chenli.model.Message;
import com.chenli.service.MessageService;
import com.chenli.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by chenli
 * 登录异常的处理
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;


    @Override
    public void doHandle(EventModel model) {
        // 判断是否有异常登陆,这里没写，只是简单的发送一些信息
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("欢迎登陆力哥资讯网站，没有你看不到的资讯，只有你想不到的资讯");
        message.setFromId(3);//这里可以设置系统id，默认3是系统Id
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "欢迎登陆", "mails/welcome.html",
                map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
