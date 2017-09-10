package com.thinkgem.jeesite.modules.quartz.sync;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.modules.pdd.entity.PddOrder;
import com.thinkgem.jeesite.modules.pdd.service.PddOrderService;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Data;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.ResponseData;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Traces;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class PullThread extends Thread {
    private Logger logger = Logger.getLogger(PullThread.class);

    private String RequestData;
    private PddOrderService pddOrderService;

    public PullThread(String RequestData, PddOrderService pddOrderService) {
        // 通过构造函数从外部引入
        this.RequestData = RequestData;
        this.pddOrderService = pddOrderService;
    }

    @Override
    public void run() {
        //此时应该检测一下快递
        ResponseData data = JSON.parseObject(RequestData, ResponseData.class);//Weibo类在下边定义
        logger.info("推送返回数据:" + data.toString());
        if (data != null && data.getData() != null) {
            List<Data> data1 = data.getData();
            for (Data d : data1) {
                if (d.isSuccess()) {//成功
                    PddOrder pddOrder = UserUtils.getPddOrderByLogisticsCode(d.getLogisticCode());
                    if (pddOrder != null) {
                        int status = d.getState();//物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
                        if (status == 3) {
                            if (pddOrder.getOrderStatus() != 3) {//发货状态，1:待发货，2:已发货待签收，3:已签 收 5:全部 暂时只开放待发货订单查询
                                pddOrder.setOrderStatus(3);
                            }
                        }
                        pddOrder.setPackageStatus(status);
                        List<Traces> traces = d.getTraces();
                        StringBuilder sb = new StringBuilder();
                        for (Traces traces1 : traces) {
                            sb.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
                        }
                        pddOrder.setLogisticInfo(sb.toString());
                        pddOrder.setUpdatedAt(new Date()); //最后更新时间
                        pddOrderService.save(pddOrder);
                        logger.info("推送数据保存成功" + pddOrder.getTrackingNumber() + ",状态" + status);
                    }
                }
            }
        }
    }

}
