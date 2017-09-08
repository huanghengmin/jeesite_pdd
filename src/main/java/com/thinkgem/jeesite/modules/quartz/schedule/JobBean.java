package com.thinkgem.jeesite.modules.quartz.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pdd.dao.PddLogisticsDao;
import com.thinkgem.jeesite.modules.pdd.email.queue.OrderQueue;
import com.thinkgem.jeesite.modules.pdd.entity.PddExpress;
import com.thinkgem.jeesite.modules.pdd.entity.PddLogistics;
import com.thinkgem.jeesite.modules.pdd.entity.PddOrder;
import com.thinkgem.jeesite.modules.pdd.entity.PddPlatform;
import com.thinkgem.jeesite.modules.pdd.service.PddOrderService;
import com.thinkgem.jeesite.modules.pdd.service.PddPlatformService;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoSubscribeAPI;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoTrackQueryAPI;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.*;
import com.thinkgem.jeesite.modules.quartz.util.pdd.OrderGet;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
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
    private static final int page_size = 100;

    @Autowired
    private PddPlatformService pddPlatformService;
    @Autowired
    private PddOrderService pddOrderService;
    @Autowired
    private SystemService systemService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void syncPackageStatus(String string) throws Exception {
        PddPlatform pddPlatform = pddPlatformService.get(string);
        if (pddPlatform != null) {
            PddOrder pddOrder_find = new PddOrder(pddPlatform);
            List<PddOrder> pddOrderList = pddOrderService.findListByPlatformNotSignInStatus(pddOrder_find);
            for (PddOrder pddOrder : pddOrderList) {
                User user = UserUtils.get(pddPlatform.getUser().getId());
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
            Thread.sleep(100);
        }

    }

    public void work(PddPlatform pddPlatform, List<PddOrder> pddOrders, /*User user, */String start_updated_at, String end_updated_at, Date date) throws Exception {
        pddPlatform.setLastUpdate(new Date(Long.parseLong(end_updated_at) * 1000));
        String mall_id = pddPlatform.getMallId();
        String secret = pddPlatform.getSecret();
//        List<PddExpress> expresses = user.getPddExpressList();
//        if (expresses != null && expresses.size() > 0) {
        String timestamp = String.valueOf(date.getTime() / 1000);
        int page = 1;
        String response = OrderGet.getOrderAdd(dataType, mall_id, secret, start_updated_at, end_updated_at, is_lucky_flag, refund_status, order_status,
                String.valueOf(page), String.valueOf(page_size), timestamp);

        JSONObject myObj = JSONObject.parseObject(response);
//            logger.info("同步时间:开始" + DateUtils.formatDateTime(new Date(Long.parseLong(start_updated_at) * 1000)) + ",结束:" + DateUtils.formatDateTime(new Date(Long.parseLong(end_updated_at) * 1000)));
        JSONObject object = myObj.getJSONObject("order_sn_increment_get_response");
        logger.error("总量数量：******************************" + object.getInteger("total_count"));

        int totalRecord = object.getInteger("total_count");

        int totalPageNum = (totalRecord + page_size - 1) / page_size;
        if (totalPageNum > 1) {
            for (int i = 2; i <= totalPageNum; i++) {
                String response_second = OrderGet.getOrderAdd(dataType, mall_id, secret, start_updated_at, end_updated_at, is_lucky_flag, refund_status, order_status,
                        String.valueOf(i), String.valueOf(page_size), timestamp);
                JSONObject myObj_second = JSONObject.parseObject(response_second);
                if (myObj_second != null) {
                    JSONObject object_second = myObj_second.getJSONObject("order_sn_increment_get_response");
                    if (object_second != null) {
                        List<PddOrder> pddOrderList = parsePddOrder(object_second/*, expresses*/);
                        if (pddOrderList != null && pddOrderList.size() > 0) {
                            pddOrders.addAll(pddOrderList);
                        }
                    }
                }
            }
        }
        if (object != null) {
            List<PddOrder> pddOrderList = parsePddOrder(object/*,expresses*/);
            if (pddOrderList != null && pddOrderList.size() > 0) {
                pddOrders.addAll(pddOrderList);
            }
        }
//        } else {
//            logger.error("平台：" + pddPlatform.getShopName() + ",未配置快递鸟账号，请配置后同步！");
//        }
    }

    public List<PddOrder> parsePddOrder(JSONObject object/*, List<PddExpress> expresses*/) {
        JSONArray array = object.getJSONArray("order_sn_list");
//        logger.info("数据:" + array.toJSONString());
        if (array != null && array.size() > 0) {
            List<PddOrder> pddOrders = new ArrayList<PddOrder>();
            for (int x = 0; x < array.size(); x++) {
                JSONObject o = array.getJSONObject(x);
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
                PddLogistics pddLogistics = UserUtils.getLogisticsId(o.getString("logistics_id"));
                pddOrder.setPddLogistics(pddLogistics); //int
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
//                if (pddOrder.getOrderStatus() != 3) {//未签收状态
//                    for (PddExpress e : expresses) {
//                        boolean isSync = false;
//                        KdniaoTrackQueryAPI kqapi = new KdniaoTrackQueryAPI(e.getEbusinessid(), e.getApikey());
//                        String ydata1 = null;
//                        try {
//                            ydata1 = kqapi.getOrderTracesByJson(pddOrder.getPddLogistics().getLogisticsCode(), pddOrder.getTrackingNumber());
//                        } catch (Exception e1) {
//                            e1.printStackTrace();
//                        }
//                        if (StringUtils.isNotEmpty(ydata1)) {
//                            KdniaoTrackQueryAPIEntity kdniaoTrackQueryAPIEntity = JSON.parseObject(ydata1, KdniaoTrackQueryAPIEntity.class);
//                            if (kdniaoTrackQueryAPIEntity != null) {
//                                if (kdniaoTrackQueryAPIEntity.isSuccess()) {
//                                    int status = kdniaoTrackQueryAPIEntity.getState(); ////物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
//                                    if (status == 3) {
//                                        if (pddOrder.getOrderStatus() != 3) {//发货状态，1:待发货，2:已发货待签收，3:已签 收 5:全部 暂时只开放待发货订单查询
//                                            pddOrder.setOrderStatus(3);
//                                        }
//                                    }
//                                    pddOrder.setPackageStatus(status);
//                                    List<Traces> traces = kdniaoTrackQueryAPIEntity.getTraces();
//                                    StringBuilder sb_2 = new StringBuilder();
//                                    for (Traces traces1 : traces) {
//                                        sb_2.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
//                                    }
//                                    pddOrder.setLogisticInfo(sb_2.toString());
//                                    pddOrder.setUpdatedAt(new Date()); //最后更新时间
//                                    isSync = true;
//                                    break;
//                                }
//                                if (isSync)
//                                    break;
//                            } else {
//                                pddOrder.setPackageStatus(0);//已签收设置为已签收
//                                pddOrder.setUpdatedAt(new Date()); //最后更新时间
//                            }
//                        } else {
//                            pddOrder.setPackageStatus(0);//已签收设置为已签收
//                            pddOrder.setUpdatedAt(new Date()); //最后更新时间
//                        }
//                    }
//                } else {
                if (pddOrder.getOrderStatus() != 3) {
                    pddOrder.setPackageStatus(0);//已签收设置为已签收
                    pddOrder.setUpdatedAt(new Date()); //最后更新时间
                } else {
                    pddOrder.setPackageStatus(pddOrder.getOrderStatus());//已签收设置为已签收
                    pddOrder.setUpdatedAt(new Date()); //最后更新时间
                }
//                }
                pddOrders.add(pddOrder);
            }
            return pddOrders;
        }
        return null;

    }

    /**
     * 同步方法
     *
     * @param string
     */
    public void syncOrder(String string) throws Exception {
        PddPlatform pddPlatform = pddPlatformService.get(string);
        if (pddPlatform.getLastUpdate() != null) {
            Date date = new Date();
            long now_time = date.getTime();

//            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步开始时间：" + DateUtils.formatDateTime(pddPlatform.getLastUpdate()));

            int second = 1000 * 60 * 30;  //半小时

            long first_t = pddPlatform.getLastUpdate().getTime();

            long cc = now_time - first_t;

            long c = cc % second == 0 ? cc / second : cc / second + 1;

            long first_time = first_t;
            long second_time = first_t + second;

            List<PddOrder> pddOrders = new ArrayList<PddOrder>();
            User user = systemService.getUser(pddPlatform.getUser().getId());
            for (int i = 0; i < c; i++) {
                //work
                work(pddPlatform, pddOrders, /*user,*/ String.valueOf(first_time / 1000), String.valueOf(second_time / 1000), date);
                second_time = second_time + second;
                first_time = first_time + second;
//                Thread.sleep(100);
            }
            if (pddOrders != null && pddOrders.size() > 0) {
                pddPlatformService.importOrder(pddOrders, pddPlatform, user);
            }

//            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步结束时间：" + DateUtils.formatDateTime(date));
        } else {
            //首次同步
            long current = System.currentTimeMillis();
            Date date = new Date(current);

//            Date date = Timestamp.valueOf("2017-09-04 19:00:00");
//            Date date_end = Timestamp.valueOf("2017-09-04 22:00:00");
//            long current = date.getTime();

//            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步开始时间：" + DateUtils.formatDateTime(date.getTime() - 48 * 1000 * 60 * 60));
            int second = 1000 * 60 * 30;  //半小时
            int count = 48;
            long first_time = current - count * second;
            long second_time = first_time + second;

//            long first_time = date.getTime();
//            long second_time = date_end.getTime();
            List<PddOrder> pddOrders = new ArrayList<PddOrder>();
            User user = systemService.getUser(pddPlatform.getUser().getId());
            for (int g = 0; g < count; g++) {
                //work
                work(pddPlatform, pddOrders, /*user,*/ String.valueOf(first_time / 1000), String.valueOf(second_time / 1000), date);
                first_time = first_time + second;
                second_time = second_time + second;
//                Thread.sleep(100);
            }

            if (pddOrders != null && pddOrders.size() > 0) {
                pddPlatformService.importOrder(pddOrders, pddPlatform, user);
            }
//            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步结束时间：" + DateUtils.formatDateTime(date));
        }
    }

    public static void main(String args[]) throws Exception {
        int totalRecord = 242;
        int totalPageNum = (totalRecord + page_size - 1) / page_size;
        System.out.print(totalPageNum);
        /*int second = 1000 * 60 * 30;  //半小时
        long second_time = new Date().getTime();
        Date first_d = DateUtils.parseDate("2017-08-29 15:20:30");
        long first_t = first_d.getTime();
        long cc = second_time - first_t;
        long c = cc % second == 0 ? cc / second : cc / second + 1;
        System.out.println(c);*/


       /* Date date = new Date();
        long now_time = date.getTime();

//            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步开始时间：" + DateUtils.formatDateTime(pddPlatform.getLastUpdate()));

        int second = 1000 * 60 * 30;  //半小时

        long first_t = now_time-8*1000*60*60;

        long cc = now_time - first_t;

        long c = cc % second == 0 ? cc / second : cc / second + 1;

        long first_time = first_t;
        long second_time = first_t + second;

        for (int i = 0; i < c; i++) {
            //work
            //work(pddPlatform, String.valueOf(first_time / 1000), String.valueOf(second_time / 1000), date);
            System.out.println("开始时间:"+DateUtils.formatDateTime(new Date(first_time)));
            System.out.println("结束时间:"+DateUtils.formatDateTime(new Date(second_time)));

            second_time = second_time + second;
            first_time = first_time + second;
        }*/



     /*   long current = System.currentTimeMillis();
        Date date = new Date(current);

//            logger.info("同步平台：" + pddPlatform.getShopName() + ",同步开始时间：" + DateUtils.formatDateTime(date.getTime() - 48 * 1000 * 60 * 60));
        int second = 1000 * 60 * 30;  //半小时
        int count = 48;



        long first_time = current - count*second;
        long second_time = first_time+second;

        for (int g = 0; g < 48; g++) {

            System.out.println("开始时间:"+DateUtils.formatDateTime(new Date(first_time)));
            System.out.println("结束时间:"+DateUtils.formatDateTime(new Date(second_time)));

            //work
//            work(pddPlatform, String.valueOf(first_time / 1000), String.valueOf(second_time / 1000), date);


            first_time = first_time +second;
            second_time = second_time + second;
        }*/
    }
}
