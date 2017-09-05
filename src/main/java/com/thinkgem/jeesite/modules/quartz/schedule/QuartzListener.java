package com.thinkgem.jeesite.modules.quartz.schedule;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.pdd.email.queue.ConsumeMailQueue;
import com.thinkgem.jeesite.modules.pdd.entity.*;
import com.thinkgem.jeesite.modules.pdd.service.PddQuartzService;
import org.apache.log4j.Logger;
import org.quartz.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Date;
import java.util.List;


public class QuartzListener implements ServletContextListener {
    private Logger logger = Logger.getLogger(QuartzListener.class);


    private Scheduler quartzScheduler;
    private ConsumeMailQueue consumeMailQueue;
    private PddQuartzService pddQuartzService;

    public void contextDestroyed(ServletContextEvent arg0) {
        //停止时停止所有任务
        try {
            // Get a reference to the Scheduler and shut it down
            quartzScheduler.shutdown(true);
            // Sleep for a bit so that we don't get any errors
            Thread.sleep(2000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void contextInitialized(ServletContextEvent arg0) {
        //加载时查询数据库任务并启动
        quartzScheduler = SpringContextHolder.getBean("quartzScheduler");
        consumeMailQueue= SpringContextHolder.getBean("consumeMailQueue");
        pddQuartzService= SpringContextHolder.getBean("pddQuartzService");

        PddQuartz scheduleJobEntity = new PddQuartz();
        scheduleJobEntity.setBeanName("jobStartupBean");
        scheduleJobEntity.setMethodName("job");
        scheduleJobEntity.setCreateDate(new Date());
        scheduleJobEntity.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
        scheduleJobEntity.setCronExpression("0 0/40 * * * ?");//40分钟执行一次
        scheduleJobEntity.setId("1111111111111111");
        scheduleJobEntity.setRemarks("检测订单状态服务");
        scheduleJobEntity.setParams("自启动检测订单状态服务");

        Trigger.TriggerState state = ScheduleUtils.getJobStatus(quartzScheduler,scheduleJobEntity.getId());
        if(state.name().equals("NONE")){//NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED;
            logger.info("createScheduleJob");
            ScheduleUtils.createScheduleJob(quartzScheduler,scheduleJobEntity);
        }else if(state.name().equals("PAUSED")||state.equals("BLOCKED")){
            logger.info("resumeJob");
            ScheduleUtils.resumeJob(quartzScheduler,scheduleJobEntity.getId());
        }else {
            logger.info("updateScheduleJob");
            ScheduleUtils.updateScheduleJob(quartzScheduler,scheduleJobEntity);
        }
        ScheduleUtils.run(quartzScheduler,scheduleJobEntity);

        List<PddQuartz> pddQuartzList = pddQuartzService.findList(new PddQuartz());
        if(pddQuartzList!=null&&pddQuartzList.size()>0){
            for (PddQuartz pddQuartz:pddQuartzList){
                Trigger.TriggerState state_temp = ScheduleUtils.getJobStatus(quartzScheduler, pddQuartz.getId());
                if (state_temp.name().equals("NONE")) {//NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED;
                    logger.info("createScheduleJob");
                    ScheduleUtils.createScheduleJob(quartzScheduler, pddQuartz);
                } else if (state_temp.name().equals("PAUSED") || state_temp.equals("BLOCKED")) {
                    logger.info("resumeJob");
                    ScheduleUtils.resumeJob(quartzScheduler, pddQuartz.getId());
                } else {
                    logger.info("updateScheduleJob");
                    ScheduleUtils.updateScheduleJob(quartzScheduler, pddQuartz);
                }
                ScheduleUtils.run(quartzScheduler, pddQuartz);
                logger.info("运行任务数："+ScheduleUtils.queryAllJob(quartzScheduler).size());
            }
        }
    }
}