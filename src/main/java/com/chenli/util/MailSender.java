package com.chenli.util;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeUtility;

import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

/**
 * Created by chenli
 * 邮件服务
 */
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("力哥大帅比");//邮件的昵称，谁发的
            InternetAddress from = new InternetAddress(nick + "<954357018@qq.com>");//邮件地址
            MimeMessage mimeMessage = mailSender.createMimeMessage();//构造一份邮件
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            //这个是邮件的模板，Velocity去渲染（重点）
            String result = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    //修改这个格式就可以实现各种邮箱的配置
    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("*****");//用户名（邮箱）
        mailSender.setPassword("****");//（密码）应该从数据库里读取出来然后写进去
        mailSender.setHost("smtp.qq.com");//发邮件的地址
        mailSender.setPort(465);//端口号
        mailSender.setProtocol("smtp");//协议
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
