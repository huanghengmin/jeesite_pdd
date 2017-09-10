package com.thinkgem.jeesite.modules.quartz.schedule;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pdd.entity.*;
import com.thinkgem.jeesite.modules.pdd.service.PddOrderService;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoTrackQueryAPI;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.KdniaoTrackQueryAPIEntity;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Traces;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时任务
 * <p>
 * jobBean bean的名称
 *
 * @author hhm
 * @email
 * @date
 */
@Component("jobSyncBean")

public class JobSyncBean {
    private Logger logger = Logger.getLogger(JobSyncBean.class);

    @Autowired
    private PddOrderService pddOrderService;

    public void job(String string) throws Exception {
        logger.info(string);
        List<PddOrder> pddOrderList = pddOrderService.findListByNotPullExpress(new PddOrder());
        if (pddOrderList != null && pddOrderList.size() > 0) {
            for (PddOrder pddOrder : pddOrderList) {
                PddPlatform pddPlatform = UserUtils.getPlatform(pddOrder.getPddPlatform().getId());
                User user = UserUtils.get(pddPlatform.getUser().getId());
                if (user != null) {
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
                                            }
                                        }
                                        pddOrder.setPackageStatus(status_query);
                                        List<Traces> traces = kdniaoTrackQueryAPIEntity.getTraces();
                                        StringBuilder sb = new StringBuilder();
                                        for (Traces traces1 : traces) {
                                            sb.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
                                        }
                                        pddOrder.setLogisticInfo(sb.toString());
                                        pddOrder.setUpdatedAt(new Date()); //最后更新时间
                                        pddOrderService.save(pddOrder);
                                    }
                                }
                            }
                        }

                    }
                }
                Thread.sleep(100);
            }
        }
    }
}
