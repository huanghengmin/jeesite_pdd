package com.thinkgem.jeesite.modules.quartz.schedule;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pdd.email.model.Email;
import com.thinkgem.jeesite.modules.pdd.email.queue.MailQueue;
import com.thinkgem.jeesite.modules.pdd.email.queue.OrderQueue;
import com.thinkgem.jeesite.modules.pdd.entity.*;
import com.thinkgem.jeesite.modules.pdd.service.PddOrderService;
import com.thinkgem.jeesite.modules.pdd.service.PddPlatformService;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoTrackQueryAPI;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.KdniaoTrackQueryAPIEntity;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Traces;
import com.thinkgem.jeesite.modules.quartz.util.sms.PropertiesUtils;
import com.thinkgem.jeesite.modules.quartz.util.sms.SMSLZUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 定时任务
 * <p>
 * jobBean bean的名称
 *
 * @author hhm
 * @email
 * @date
 */
@Component("jobStartupBean")

public class JobStartupBean {
    private Logger logger = Logger.getLogger(JobStartupBean.class);

    @Autowired
    private PddOrderService pddOrderService;

    @Autowired
    private SystemService systemService;

    public boolean sendPullRemand(User user, PddOrder pddOrder) {
        //邮件预警
        int emailRemand = user.getEmailRemand();
        if (emailRemand == 1) {//启用
            List<PddEmail> pddEmails = user.getPddEmailList();
            if (pddEmails != null && pddEmails.size() > 0) {

                String[] emails = new String[pddEmails.size()];
                for (int i = 0; i < pddEmails.size(); i++) {
                    PddEmail pddEmail = pddEmails.get(i);
                    emails[i] = pddEmail.getEmail();
                }
                logger.info("邮件提醒订单：" + pddOrder.getOrderSn() + ",快递单号:" + pddOrder.getTrackingNumber() + ",超过预警时间未揽件！请注意，规避虚假发货！");
                Email mail = new Email();
                mail.setEmail(emails);
                mail.setSubject("揽件预警");
                mail.setContent("订单：" + pddOrder.getOrderSn() + ",快递单号:" + pddOrder.getTrackingNumber() + ",超过预警时间未揽件！请注意，规避虚假发货！");
                mail.setTemplate("welcome");
                try {
                    MailQueue.getMailQueue().produce(mail);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        //短信预警
        int phoneRemand = user.getPhoneRemand();
        if (phoneRemand == 1) {//启用
            List<PddPhone> pddPhones = user.getPddPhoneList();
            if (pddPhones != null && pddPhones.size() > 0) {

                String[] phones = new String[pddPhones.size()];
                for (int i = 0; i < pddPhones.size(); i++) {
                    PddPhone pddPhone = pddPhones.get(i);
                    phones[i] = pddPhone.getPhone();
                }
                logger.info("短信提醒订单：" + pddOrder.getOrderSn() + ",快递单号:" + pddOrder.getTrackingNumber() + ",超过预警时间未揽件！请注意，规避虚假发货！");
                if (user.getNoteNumber() > 0) {
                    Properties p = PropertiesUtils.getProperties();
                    String VCode = p.getProperty("sms.pull");
                    String FreeSignName = p.getProperty("sms.FreeSignName");
                    try {
                        SMSLZUtils.sendSms(phones.toString(), FreeSignName, VCode, "{\"order_sn\":\"" + pddOrder.getOrderSn() + "\"}");
                    } catch (ClientException e) {
                        e.printStackTrace();
                        return false;
                    }
                    user.setNoteNumber(user.getNoteNumber() - 1);
                    systemService.updateUserSet(user);
                }
            }
        }
        return true;
    }

    public boolean sendSecondRemand(User user, PddOrder pddOrder) {
        //邮件预警
        int emailRemand = user.getEmailRemand();
        if (emailRemand == 1) {//启用
            List<PddEmail> pddEmails = user.getPddEmailList();
            if (pddEmails != null && pddEmails.size() > 0) {

                String[] emails = new String[pddEmails.size()];
                for (int i = 0; i < pddEmails.size(); i++) {
                    PddEmail pddEmail = pddEmails.get(i);
                    emails[i] = pddEmail.getEmail();
                }
                logger.info("邮件提醒订单：" + pddOrder.getOrderSn() + ",快递单号:" + pddOrder.getTrackingNumber() + ",超过预警时间二次未揽件！请注意，规避虚假发货！");
                Email mail = new Email();
                mail.setEmail(emails);
                mail.setSubject("揽件预警");
                mail.setContent("订单：" + pddOrder.getOrderSn() + ",快递单号:" + pddOrder.getTrackingNumber() + ",超过预警时间二次未揽件！请注意，规避虚假发货！");
                mail.setTemplate("welcome");
                try {
                    MailQueue.getMailQueue().produce(mail);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        //短信预警
        int phoneRemand = user.getPhoneRemand();
        if (phoneRemand == 1) {//启用
            List<PddPhone> pddPhones = user.getPddPhoneList();
            if (pddPhones != null && pddPhones.size() > 0) {

                String[] phones = new String[pddPhones.size()];
                for (int i = 0; i < pddPhones.size(); i++) {
                    PddPhone pddPhone = pddPhones.get(i);
                    phones[i] = pddPhone.getPhone();
                }
                logger.info("短信提醒订单：" + pddOrder.getOrderSn() + ",快递单号:" + pddOrder.getTrackingNumber() + ",超过预警时间二次未揽件！请注意，规避虚假发货！");
                if (user.getNoteNumber() > 0) {
                    Properties p = PropertiesUtils.getProperties();
                    String VCode = p.getProperty("sms.second");
                    String FreeSignName = p.getProperty("sms.FreeSignName");
                    try {
                        SMSLZUtils.sendSms(phones.toString(), FreeSignName, VCode, "{\"order_sn\":\"" + pddOrder.getOrderSn() + "\"}");
                    } catch (ClientException e) {
                        e.printStackTrace();
                        return false;
                    }
                    user.setNoteNumber(user.getNoteNumber() - 1);
                    systemService.updateUserSet(user);
                }
            }
        }
        return true;
    }

    public void job(String string) throws Exception {

        System.out.println("任务进行中。。。" + string);
        PddOrder pddOrder_status = new PddOrder();
        pddOrder_status.setPackageStatus(3);
        List<PddOrder> pddOrderList = pddOrderService.findListByNotSignInStatus(pddOrder_status);
        if (pddOrderList != null && pddOrderList.size() > 0) {
            for (PddOrder pddOrder : pddOrderList) {
                if (pddOrder.getPackageStatus() != null) {
                    int status = pddOrder.getPackageStatus();
                    if (status == 0) {//未揽件
                        PddPlatform pddPlatform = UserUtils.getPlatform(pddOrder.getPddPlatform().getId());
                        User user = UserUtils.get(pddPlatform.getUser().getId());
                        if (user != null) {
                            if (user.isEnablePullRemand()) {
                                if (user.getPullRemand() != null) {
                                    int pullRemand = user.getPullRemand();
                                    if (new Date().getTime() - pddOrder.getUpdatedAt().getTime() >= pullRemand * 60 * 60 * 1000) {
                                        //此时应该检测一下快递
                                        List<PddExpress> pddExpresses = user.getPddExpressList();
                                        if (pddExpresses != null && pddExpresses.size() > 0) {
                                            for (PddExpress pddExpres : pddExpresses) {//需要判断账号是否超过3000次，如超过换下一个几账号查询，没有时提示
                                                KdniaoTrackQueryAPI qapi = new KdniaoTrackQueryAPI(pddExpres.getEbusinessid(), pddExpres.getApikey());
                                                String data1 = qapi.getOrderTracesByJson(pddOrder.getPddLogistics().getLogisticsCode(), pddOrder.getTrackingNumber());
                                                if (StringUtils.isNotEmpty(data1)) {
                                                    KdniaoTrackQueryAPIEntity kdniaoTrackQueryAPIEntity = JSON.parseObject(data1, KdniaoTrackQueryAPIEntity.class);
                                                    if (kdniaoTrackQueryAPIEntity != null) {
                                                        if (kdniaoTrackQueryAPIEntity.isSuccess()) {
                                                            int status_query = kdniaoTrackQueryAPIEntity.getState();//物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
                                                            if (status_query == 3) {
                                                                if (pddOrder.getOrderStatus() != 3) {//发货状态，1:待发货，2:已发货待签收，3:已签 收 5:全部 暂时只开放待发货订单查询
                                                                    pddOrder.setOrderStatus(3);
                                                                    pddOrder.setPackageStatus(status_query);
                                                                    List<Traces> traces = kdniaoTrackQueryAPIEntity.getTraces();
                                                                    StringBuilder sb = new StringBuilder();
                                                                    for (Traces traces1 : traces) {
                                                                        sb.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
                                                                    }
                                                                    pddOrder.setLogisticInfo(sb.toString());
                                                                    pddOrder.setUpdatedAt(new Date()); //最后更新时间
                                                                    pddOrderService.save(pddOrder);
                                                                    break;
                                                                }
                                                            } else {//状态为运输中
                                                                List<Traces> traces = kdniaoTrackQueryAPIEntity.getTraces();
                                                                StringBuilder sb = new StringBuilder();
                                                                for (Traces traces1 : traces) {
                                                                    sb.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
                                                                }
                                                                if ((sb.toString()).equals(pddOrder.getLogisticInfo())) {//未更新
                                                                    //发送提醒
                                                                    sendPullRemand(user, pddOrder);
                                                                    //修改为预警
                                                                    if (pddOrder.getPackageStatus() == 0) {
                                                                        pddOrder.setPackageStatus(5); //设置状态为5
                                                                        pddOrder.setUpdatedAt(new Date());
                                                                        pddOrderService.save(pddOrder);
                                                                        break;
                                                                    } else if (pddOrder.getPackageStatus() == 1) {
                                                                        pddOrder.setPackageStatus(6); //设置状态为5
                                                                        pddOrder.setUpdatedAt(new Date());
                                                                        pddOrderService.save(pddOrder);
                                                                        break;
                                                                    }
                                                                } else {
                                                                    pddOrder.setPackageStatus(status_query);
                                                                    pddOrder.setLogisticInfo(sb.toString());
                                                                    pddOrder.setUpdatedAt(new Date()); //最后更新时间
                                                                    pddOrderService.save(pddOrder);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    } else if (status != 3) {//中转途中
                        PddPlatform pddPlatform = UserUtils.getPlatform(pddOrder.getPddPlatform().getId());
                        User user = UserUtils.get(pddPlatform.getUser().getId());
                        if (user != null) {
                            if (user.isEnableSecondRemand()) {
                                if (user.getSecondRemand() != null) {
                                    int secondRemand = user.getSecondRemand();
                                    if (new Date().getTime() - pddOrder.getUpdatedAt().getTime() >= secondRemand * 60 * 60 * 1000) {
                                        //此时应该检测一下快递
                                        List<PddExpress> pddExpresses = user.getPddExpressList();
                                        if (pddExpresses != null && pddExpresses.size() > 0) {
                                            for (PddExpress pddExpres : pddExpresses) {//需要判断账号是否超过3000次，如超过换下一个几账号查询，没有时提示
                                                KdniaoTrackQueryAPI qapi = new KdniaoTrackQueryAPI(pddExpres.getEbusinessid(), pddExpres.getApikey());
                                                String data1 = qapi.getOrderTracesByJson(pddOrder.getPddLogistics().getLogisticsCode(), pddOrder.getTrackingNumber());
                                                if (StringUtils.isNotEmpty(data1)) {
                                                    KdniaoTrackQueryAPIEntity kdniaoTrackQueryAPIEntity = JSON.parseObject(data1, KdniaoTrackQueryAPIEntity.class);
                                                    if (kdniaoTrackQueryAPIEntity != null) {
                                                        if (kdniaoTrackQueryAPIEntity.isSuccess()) {
                                                            int status_query = kdniaoTrackQueryAPIEntity.getState();//物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
                                                            if (status_query == 3) {
                                                                if (pddOrder.getOrderStatus() != 3) {//发货状态，1:待发货，2:已发货待签收，3:已签 收 5:全部 暂时只开放待发货订单查询
                                                                    pddOrder.setOrderStatus(3);
                                                                    pddOrder.setPackageStatus(status_query);
                                                                    List<Traces> traces = kdniaoTrackQueryAPIEntity.getTraces();
                                                                    StringBuilder sb = new StringBuilder();
                                                                    for (Traces traces1 : traces) {
                                                                        sb.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
                                                                    }
                                                                    pddOrder.setLogisticInfo(sb.toString());
                                                                    pddOrder.setUpdatedAt(new Date()); //最后更新时间
                                                                    pddOrderService.save(pddOrder);
                                                                    break;
                                                                }
                                                            } else {//状态为运输中
                                                                List<Traces> traces = kdniaoTrackQueryAPIEntity.getTraces();
                                                                StringBuilder sb = new StringBuilder();
                                                                for (Traces traces1 : traces) {
                                                                    sb.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
                                                                }
                                                                if ((sb.toString()).equals(pddOrder.getLogisticInfo())) {//未更新
                                                                    //发送提醒
                                                                    sendSecondRemand(user, pddOrder);

                                                                    if (pddOrder.getPackageStatus() == 0) {
                                                                        pddOrder.setPackageStatus(5); //设置状态为5
                                                                        pddOrder.setUpdatedAt(new Date());
                                                                        pddOrderService.save(pddOrder);
                                                                        break;
                                                                    } else if (pddOrder.getPackageStatus() == 1) {
                                                                        pddOrder.setPackageStatus(6); //设置状态为5
                                                                        pddOrderService.save(pddOrder);
                                                                        pddOrder.setUpdatedAt(new Date());
                                                                        break;
                                                                    }
                                                                    break;
                                                                } else {
                                                                    pddOrder.setPackageStatus(status_query);
                                                                    pddOrder.setLogisticInfo(sb.toString());
                                                                    pddOrder.setUpdatedAt(new Date()); //最后更新时间
                                                                    pddOrderService.save(pddOrder);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Thread.sleep(200);
            }

        }
    }
}
