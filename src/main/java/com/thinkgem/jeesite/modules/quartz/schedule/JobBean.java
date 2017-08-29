package com.thinkgem.jeesite.modules.quartz.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.pdd.entity.PddExpress;
import com.thinkgem.jeesite.modules.pdd.entity.PddOrder;
import com.thinkgem.jeesite.modules.pdd.entity.PddPlatform;
import com.thinkgem.jeesite.modules.pdd.service.PddPlatformService;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdApiOrderDistinguish;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoSubscribeAPI;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoTrackQueryAPI;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.*;
import com.thinkgem.jeesite.modules.quartz.util.pdd.OrderGet;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
@Component("jobBean")
public class JobBean {
    private static final String dataType = "JSON";//返回数据类型 json/xml
    private static final String is_lucky_flag = "1";//抽奖订单
    private static final String refund_status = "5";//退款状态
    private static final String order_status = "2"; //订单状态 必填 发货状态，1:待发货，2:已发货待签收，3:已签收 5:全部

    @Autowired
    private PddPlatformService pddPlatformService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void work(PddPlatform pddPlatform, User user, String start_updated_at, String end_updated_at, Date date) {
        pddPlatform.setLastUpdate(date);
        String mall_id = pddPlatform.getMallId();
        String secret = pddPlatform.getSecret();
        List<PddExpress> expresses = user.getPddExpressList();
        String timestamp = String.valueOf(date.getTime() / 1000);
        String response = OrderGet.getOrderAdd(dataType, mall_id, secret, start_updated_at, end_updated_at, is_lucky_flag, refund_status, order_status, timestamp);
        JSONObject myObj = JSONObject.parseObject(response);
        logger.info("同步时间:开始" + DateUtils.formatDateTime(Long.parseLong(start_updated_at)) + ",结束:" + DateUtils.formatDateTime(Long.parseLong(start_updated_at)));
        logger.info("同步结果:" + response);
        JSONObject object = (JSONObject) myObj.get("order_sn_increment_get_response");
        if(object!=null) {
            JSONArray array = (JSONArray) object.get("order_sn_list");
            if(array!=null&&array.size()>0) {
                List<PddOrder> pddOrders = new ArrayList<PddOrder>();
                for (int i = 0; i < array.size(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    PddOrder pddOrder = new PddOrder();
                    pddOrder.setOrderSn(String.valueOf(o.get("order_sn")));
                    pddOrder.setConfirmTime(DateUtils.parseDate(o.getString("confirm_time")));
                    pddOrder.setReceiverName(o.getString("receiver_name"));
                    pddOrder.setCreatedTime(DateUtils.parseDate(o.getString("created_time")));
                    pddOrder.setCountry(o.getString("country"));
                    pddOrder.setProvince(o.getString("province"));
                    pddOrder.setCity(o.getString("city"));
                    pddOrder.setTown(o.getString("town"));
                    pddOrder.setAddress(o.getString("address"));
                    pddOrder.setReceiverPhone(o.getString("receiver_phone"));
                    pddOrder.setPayAmount(o.getDouble("pay_amount")); //number(4,1)
                    pddOrder.setGoodsAmount(o.getDouble("goods_amount"));//number(4,1)
                    pddOrder.setDiscountAmount(o.getDouble("discount_amount"));//number(4,1)
                    pddOrder.setPostage(o.getDouble("postage"));//number(4,1)
                    pddOrder.setPayNo(o.getString("pay_no"));
                    pddOrder.setPayType(o.getString("pay_type"));
                    pddOrder.setIdCardNum(o.getString("id_card_num"));
                    pddOrder.setIdCardName(o.getString("id_card_name"));
                    pddOrder.setLogisticsId(o.getInteger("logistics_id")); //int
                    pddOrder.setTrackingNumber(o.getString("tracking_number"));
                    pddOrder.setShippingTime(DateUtils.parseDate(o.getString("shipping_time")));
                    pddOrder.setOrderStatus(o.getInteger("order_status"));//int
                    pddOrder.setIsLucky(o.getInteger("is_lucky_flag"));//int
                    pddOrder.setRefundStatus(o.getInteger("refund_status"));//int
                    pddOrder.setUpdatedAt(DateUtils.parseDate(o.getString("updated_at")));//datetime
                    pddOrder.setLastShipTime(DateUtils.parseDate(o.getString("last_ship_time")));
                    pddOrder.setRemark(o.getString("remark"));
                    pddOrder.setPlatformDiscount(o.getString("platform_discount"));
                    pddOrder.setSellerDiscount(o.getString("seller_discount"));
                    pddOrder.setCapitalFreeDiscount(o.getString("capital_free_discount"));

                    StringBuilder sb = new StringBuilder();
                    JSONArray jsonArray = (JSONArray) o.get("item_list");
                    for (int y = 0; y < jsonArray.size(); y++) {
                        JSONObject so = (JSONObject) jsonArray.get(y);
//				sb.append("商品:"+(y+1));
                        sb.append("商品名称:").append(so.getString("goods_name")).append("\n");
                        sb.append("商品规格:").append(so.getString("goods_spec")).append("\n");
                        sb.append("商品单价:").append(so.getString("goods_price")).append("\n");
//				sb.append("商品编号:").append(so.getString("goods_id")).append("\n");
                        //sb.append("Sku 编号:").append(so.getString("sku_id")).append("\n");
                        //sb.append("商家编码-SKU维度:").append(so.getString("outer_id")).append("\n");
//				sb.append("商家编码-商品维度:").append(so.getString("outer_goods_id")).append("\n");
//				sb.append("商品图片:").append(so.getString("goods_img")).append("\n");
                    }
                    pddOrder.setGoodInfo(sb.toString());
                    if (!pddOrder.getOrderStatus().equals("3")) {//未签收状态
                        for (PddExpress e : expresses) {
                            KdApiOrderDistinguish api_ = new KdApiOrderDistinguish(e.getEbusinessid(), e.getApikey());
                            String data = null;
                            try {
                                data = api_.getOrderTracesByJson(pddOrder.getTrackingNumber());
                                KdApiOrderDistinguishEntity kdApiOrderDistinguishEntity = JSON.parseObject(data, KdApiOrderDistinguishEntity.class);//Weibo类在下边定义
                                if (kdApiOrderDistinguishEntity.isSuccess()) {
                                    kdApiOrderDistinguishEntity.getShippers();
                                    List<Shippers> shippers = kdApiOrderDistinguishEntity.getShippers();
                                    if (shippers != null && shippers.size() > 0) {
                                        for (Shippers shippers1 : shippers) {
                                            //查询订单信息
                                            KdniaoTrackQueryAPI kqapi = new KdniaoTrackQueryAPI(e.getEbusinessid(), e.getApikey());
                                            String ydata1 = kqapi.getOrderTracesByJson(shippers1.getShipperCode(), pddOrder.getTrackingNumber());

                                            KdniaoTrackQueryAPIEntity kdniaoTrackQueryAPIEntity = JSON.parseObject(ydata1, KdniaoTrackQueryAPIEntity.class);

                                            if (kdniaoTrackQueryAPIEntity.isSuccess()) {
                                                int status = kdniaoTrackQueryAPIEntity.getState(); ////物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
                                                if (status == 3) {
                                                    if (pddOrder.getOrderStatus() != null && pddOrder.getOrderStatus() != 3) {//发货状态，1:待发货，2:已发货待签收，3:已签 收 5:全部 暂时只开放待发货订单查询
                                                        pddOrder.setOrderStatus(3);
                                                    }
                                                }
                                                pddOrder.setPackageStatus(status);
                                                List<Traces> traces = kdniaoTrackQueryAPIEntity.getTraces();
                                                StringBuilder sb_2 = new StringBuilder();
                                                for (Traces traces1 : traces) {
                                                    sb_2.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
                                                }
                                                pddOrder.setLogisticInfo(sb_2.toString());
                                                pddOrder.setUpdatedAt(new Date()); //最后更新时间

                                                if (!(status == 3 || status == 4)) { //物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
                                                    //订阅订单信息
                                                    KdniaoSubscribeAPI qapi = new KdniaoSubscribeAPI(e.getEbusinessid(), e.getApikey());
                                                    String data1 = qapi.orderTracesSubByJson(shippers1.getShipperName(), pddOrder.getTrackingNumber());
                                                    Result result = JSON.parseObject(data1, Result.class);
                                                    if (result.isSuccess()) {
                                                        logger.info("注册推送：" + shippers1.getShipperName() + ",单号：" + shippers1.getShipperCode() + "时间：" + DateUtils.formatDateTime(new Date()));
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                        }
                    }
                    pddOrders.add(pddOrder);
                }
                pddPlatform.setPddOrderList(pddOrders);
                pddPlatformService.importOrder(pddOrders, pddPlatform, user);
                pddPlatformService.updateByUser(pddPlatform, user);
            }
        }
    }

    /**
     * 同步方法
     *
     * @param pddPlatform
     * @param user
     */
    public void syncOrder(PddPlatform pddPlatform, User user) {
        if (pddPlatform.getLastUpdate() != null) {
            Date date = new Date();
            long now_time = date.getTime();

            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步开始时间：" + DateUtils.formatDateTime(pddPlatform.getLastUpdate()));

            int second = 1000 * 60 * 30;  //半小时

            long first_t = pddPlatform.getLastUpdate().getTime();

            long cc = now_time - first_t;

            long c = cc % second == 0 ? cc / second : cc / second + 1;

            long first_time = first_t ;
            long second_time = first_t + second;

            for (int i = 0; i < c; i++) {
                //work
                work(pddPlatform, user, String.valueOf(first_time / 1000), String.valueOf(second_time / 1000), date);
                second_time = second_time - second;
                first_time = first_time - second;
            }
            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步结束时间：" + DateUtils.formatDateTime(date));
        } else {
            //首次同步
            long current = System.currentTimeMillis();
            Date date = new Date(current);

            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步开始时间：" + DateUtils.formatDateTime(date.getTime()- 48 * 1000 * 60 * 60));
            int second = 1000 * 60 * 30;  //半小时
            long second_time = current;
            long first_time = current - second;

            for (int i = 0; i < 48; i++) {
                //work
                work(pddPlatform, user, String.valueOf(first_time / 1000), String.valueOf(second_time / 1000), date);
                second_time = second_time - second;
                first_time = first_time - second;
            }
            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步结束时间：" + DateUtils.formatDateTime(date));
        }
    }

    public static void main(String args[]) throws Exception {
        int second = 1000 * 60 * 30;  //半小时
        long second_time = new Date().getTime();
        Date first_d = DateUtils.parseDate("2017-08-29 15:20:30");
        long first_t = first_d.getTime();
        long cc = second_time - first_t;
        long c = cc % second == 0 ? cc / second : cc / second + 1;
        System.out.println(c);
    }
}
