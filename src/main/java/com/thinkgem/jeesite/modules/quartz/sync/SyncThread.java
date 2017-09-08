package com.thinkgem.jeesite.modules.quartz.sync;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pdd.entity.PddExpress;
import com.thinkgem.jeesite.modules.pdd.entity.PddOrder;
import com.thinkgem.jeesite.modules.pdd.service.PddOrderService;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoTrackQueryAPI;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.KdniaoTrackQueryAPIEntity;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Traces;
import com.thinkgem.jeesite.modules.sys.entity.User;

import java.util.Date;
import java.util.List;

public class SyncThread extends Thread {

    private PddOrderService pddOrderService;
    private List<PddOrder> pddOrderList;
    private User user;

    public SyncThread(PddOrderService pddOrderService, List<PddOrder> pddOrderList, User user) {
        // 通过构造函数从外部引入
        this.pddOrderService = pddOrderService;
        this.pddOrderList = pddOrderList;
        this.user = user;
    }

    @Override
    public void run() {
        if (pddOrderList != null && pddOrderList.size() > 0) {
            for (PddOrder pddOrder : pddOrderList) {
                //此时应该检测一下快递
                List<PddExpress> pddExpresses = user.getPddExpressList();
                if (pddExpresses != null && pddExpresses.size() > 0) {
                    for (PddExpress pddExpres : pddExpresses) {//需要判断账号是否超过3000次，如超过换下一个几账号查询，没有时提示
                        KdniaoTrackQueryAPI qapi = new KdniaoTrackQueryAPI(pddExpres.getEbusinessid(), pddExpres.getApikey());
                        String data1 = null;
                        try {
                            data1 = qapi.getOrderTracesByJson(pddOrder.getPddLogistics().getLogisticsCode(), pddOrder.getTrackingNumber());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                                    break;
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
