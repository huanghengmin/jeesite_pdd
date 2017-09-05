package com.thinkgem.jeesite.test;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Data;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.ResponseData;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Traces;

import java.util.List;

public class Test {

	public static void main(String args[])throws Exception{
		String ss = "{\"Count\":5,\"Data\":[{\"CallBack\":\"\",\"EBusinessID\":\"1301687\",\"LogisticCode\":\"00000000201709020\",\"OrderCode\":\"\",\"Reason\":\"\",\"ShipperCode\":\"YD\",\"State\":\"0\",\"Success\":true,\"Traces\":[]},{\"CallBack\":\"\",\"EBusinessID\":\"1301687\",\"LogisticCode\":\"00000000201709021\",\"OrderCode\":\"\",\"Reason\":\"\",\"ShipperCode\":\"YTO\",\"State\":\"0\",\"Success\":true,\"Traces\":[]},{\"CallBack\":\"\",\"EBusinessID\":\"1301687\",\"LogisticCode\":\"00000000201709022\",\"OrderCode\":\"\",\"Reason\":\"\",\"ShipperCode\":\"EMS\",\"State\":\"3\",\"Success\":true,\"Traces\":[{\"AcceptStation\":\"深圳市横岗速递营销部已收件，（揽投员姓名：钟某某;联系电话：18000000000）\",\"AcceptTime\":\"2017-09-02 08:48:05\"},{\"AcceptStation\":\"离开深圳市 发往广州市\",\"AcceptTime\":\"2017-09-02 12:48:05\"},{\"AcceptStation\":\"呼和浩特市邮政速递物流分公司金川揽投部安排投递（投递员姓名：安某;联系电话：18800000000）\",\"AcceptTime\":\"2017-09-03 05:48:05\"},{\"AcceptStation\":\"快件已被签收，签收人：本人\",\"AcceptTime\":\"2017-09-03 06:48:05\"}]},{\"CallBack\":\"\",\"EBusinessID\":\"1301687\",\"LogisticCode\":\"00000000201709023\",\"OrderCode\":\"\",\"Reason\":\"\",\"ShipperCode\":\"HTKY\",\"State\":\"0\",\"Success\":true,\"Traces\":[]},{\"CallBack\":\"\",\"EBusinessID\":\"1301687\",\"LogisticCode\":\"00000000201709024\",\"OrderCode\":\"\",\"Reason\":\"\",\"ShipperCode\":\"HTKY\",\"State\":\"2\",\"Success\":true,\"Traces\":[{\"AcceptStation\":\"深圳市横岗速递营销部已收件，（揽投员姓名：钟某某;联系电话：18000000000）\",\"AcceptTime\":\"2017-09-02 08:48:05\"},{\"AcceptStation\":\"离开深圳市 发往广州市\",\"AcceptTime\":\"2017-09-02 12:48:05\"},{\"AcceptStation\":\"呼和浩特市邮政速递物流分公司金川揽投部安排投递（投递员姓名：安某;联系电话：18800000000）\",\"AcceptTime\":\"2017-09-03 05:48:05\"}]}],\"EBusinessID\":\"1301687\",\"PushTime\":\"2017-09-03 08:48:05\"}";
		ResponseData data = JSON.parseObject(ss, ResponseData.class);//Weibo类在下边定义
//        logger.info("推送返回数据:"+data);
//        if (data != null && data.getData() != null) {
		List<Data> data1 = data.getData();
		for (Data d : data1) {
			if (d.isSuccess()) {//成功
				// PddOrder pddOrder = pddOrderService.getByLogisticCode(d.getLogisticCode());
				//if (pddOrder != null) {
				// int status = d.getState();//物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
//                        if (status == 3) {
//                            if (pddOrder.getOrderStatus() != 3) {//发货状态，1:待发货，2:已发货待签收，3:已签 收 5:全部 暂时只开放待发货订单查询
//                                pddOrder.setOrderStatus(3);
//                            }
//                        }
//                        pddOrder.setPackageStatus(status);
				List<Traces> traces = d.getTraces();
				StringBuilder sb = new StringBuilder();
				for (Traces traces1 : traces) {
					sb.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
				}
//                        pddOrder.setLogisticInfo(sb.toString());
//                        pddOrder.setUpdatedAt(new Date()); //最后更新时间
//                        pddOrderService.save(pddOrder);

//                        if (status != 3) { //物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
				//订阅订单信息
//                            OrderQueue.getOrderQueue().produce(pddOrder);
                                    /*KdniaoSubscribeAPI qapi = new KdniaoSubscribeAPI(EBusinessID, apiKey);
                                    String data2 = qapi.orderTracesSubByJson(pddOrder.getPddLogistics().getLogisticsCode(), pddOrder.getTrackingNumber());
                                    Result result = JSON.parseObject(data2, Result.class);
                                    if (result.isSuccess()) {
                                        logger.info("注册推送：结果："+result+",快递编码：" + pddOrder.getPddLogistics().getLogisticsCode() + ",单号：" + pddOrder.getTrackingNumber() + "时间：" + DateUtils.formatDateTime(new Date()));
                                    }*/
//                        }
//                    }
//                }
			}
		}
	}
}
