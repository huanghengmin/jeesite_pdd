package com.thinkgem.jeesite.modules.pdd.email.queue;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.pdd.email.model.Email;
import com.thinkgem.jeesite.modules.pdd.email.service.IMailService;
import com.thinkgem.jeesite.modules.pdd.entity.PddOrder;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoSubscribeAPI;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消费队列
 * 创建者 科帮网
 * 创建时间	2017年8月4日
 */
@Component
public class ConsumeMailQueue {
    private static final Logger logger = LoggerFactory.getLogger(ConsumeMailQueue.class);
    @Autowired
    IMailService mailService;

//    private static final String EBusinessID = "1301687";//hhm
//    private static final String apiKey = "aece4f2c-bb84-4e5b-87a3-258f090dbae7"; //hhm

    private static final String EBusinessID = "1290729";//
    private static final String apiKey = "c662a7b7-6669-455e-8860-3b1e55b41222";

    @PostConstruct
    public void startThread() {
        ExecutorService e = Executors.newFixedThreadPool(5);// 两个大小的固定线程池
        e.submit(new PollMail(mailService));
        e.submit(new PollMail(mailService));
        e.submit(new PullOrder());
        e.submit(new PullOrder());
        e.submit(new PullOrder());
    }

    class PollMail implements Runnable {
        IMailService mailService;

        public PollMail(IMailService mailService) {
            this.mailService = mailService;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Email mail = MailQueue.getMailQueue().consume();
                    if (mail != null) {
                        logger.info("剩余邮件总数:{}", MailQueue.getMailQueue().size());
                        mailService.send(mail);
                        Thread.sleep(1000 * 2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class PullOrder implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    PddOrder pddOrder = OrderQueue.getOrderQueue().consume();
                    KdniaoSubscribeAPI qapi = new KdniaoSubscribeAPI(EBusinessID, apiKey);
                    String data1 = qapi.orderTracesSubByJson(pddOrder.getPddLogistics().getLogisticsCode(), pddOrder.getTrackingNumber());
                    Result result = JSON.parseObject(data1, Result.class);
                    if (result.isSuccess()) {
                        logger.info("注册推送：结果：" + result + ",快递编码：" + pddOrder.getPddLogistics().getLogisticsCode() + ",单号：" + pddOrder.getTrackingNumber() + "时间：" + DateUtils.formatDateTime(new Date()));
                    }
                    Thread.sleep(1000 * 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @PreDestroy
    public void stopThread() {
        logger.info("destroy");
    }
}
