package com.thinkgem.jeesite.modules.pdd.email.service.impl;

import com.thinkgem.jeesite.modules.pdd.email.model.Email;
import com.thinkgem.jeesite.modules.pdd.email.queue.MailQueue;
import com.thinkgem.jeesite.modules.pdd.email.service.IMailService;
import com.thinkgem.jeesite.modules.pdd.email.utils.Constants;
import com.thinkgem.jeesite.modules.pdd.email.utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceImpl implements IMailService {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;//执行者

    //@Value("${mail.username}")
//    public String USER_NAME="15107847@qq.com";//发送者
    public String USER_NAME="laidongzhou@pddcall.com";//发送者

    @Override
    public void send(Email mail) throws Exception {
        logger.info("发送邮件：{}", mail.getContent());
        MailUtil mailUtil = new MailUtil();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(USER_NAME);
        message.setTo(mail.getEmail());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        mailUtil.start(mailSender, message);
    }

    @Override
    public void sendHtml(Email mail) throws Exception {
        MailUtil mailUtil = new MailUtil();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
        helper.setFrom(USER_NAME);
        helper.setTo(mail.getEmail());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent(),true);
     /*   helper.setText(
                "<html><body><img src=\"cid:springcloud\" ></body></html>",
                true);*/
        // 发送图片
      /*  File file = ResourceUtils.getFile("classpath:static"
                + Constants.SF_FILE_SEPARATOR + "image"
                + Constants.SF_FILE_SEPARATOR + "springcloud.png");
        helper.addInline("springcloud", file);*/
        // 发送附件
      /*  file = ResourceUtils.getFile("classpath:static"
                + Constants.SF_FILE_SEPARATOR + "file"
                + Constants.SF_FILE_SEPARATOR + "关注科帮网获取更多源码.zip");
        helper.addAttachment("科帮网", file);*/
        mailUtil.startHtml(mailSender, message);
    }

    @Override
    public void sendQueue(Email mail) throws Exception {
        MailQueue.getMailQueue().produce(mail);
    }
}
