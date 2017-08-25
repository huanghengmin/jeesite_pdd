package com.thinkgem.jeesite.modules.quartz.util.kdniao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkgem.jeesite.modules.pdd.entity.PddOrder;

/**
 * Created by huanghengmin on 2017/8/22.
 */
public class KdniaoUtils {

    public static String sync(String tracking_number) {

        StringBuilder sb = new StringBuilder();
        if (tracking_number != null && tracking_number.length() > 0) {

            KdApiOrderDistinguish api = new KdApiOrderDistinguish();
            try {
                String result = api.getOrderTracesByJson(tracking_number);
                //System.out.println("RESULT:"+result);

                /*String ss = "{  " +
                        "\"EBusinessID\": \"1290729\",  " +
                        "\"Success\": true,  " +
                        "\"LogisticCode\": \"3335894453552\", " +
                        " \"Shippers\": [{      \"ShipperCode\": \"STO\",      \"ShipperName\": \"申通快递\"  } ]}";*/


                JSONObject myObj1 = JSONObject.parseObject(result);
                boolean flag = myObj1.getBoolean("Success");
                //没有物流轨迹的为false
                if (flag) {
                    JSONArray object1 = (JSONArray) myObj1.get("Shippers");
                    JSONObject jsonObject = (JSONObject) object1.get(0);
                    //sb.append("快递公司：" + jsonObject.get("ShipperName")).append("\n");

                    KdniaoTrackQueryAPI qapi = new KdniaoTrackQueryAPI();
                    String qresult = qapi.getOrderTracesByJson(String.valueOf(jsonObject.get("ShipperCode")), tracking_number);
                    //System.out.println("QRESUT:"+qresult);

                    JSONObject result_find = JSONObject.parseObject(qresult);
                    int state = result_find.getInteger("State");


                    //2-在途中,3-签收,4-问题件
                    //sb.append("投递状态：" + state).append("\n");
                    //sb.append("物流信息").append("\n");

                    JSONArray traces = (JSONArray) result_find.get("Traces");
                    for (int y = 0; y < traces.size(); y++) {
                        JSONObject jsonObject1 = (JSONObject) traces.get(y);
                        sb.append(jsonObject1.get("AcceptTime")).append(" ").append(jsonObject1.get("AcceptStation")).append("\n");
                    }

                /*String ss = "{  " +
                    "\"EBusinessID\": \"1290729\",  " +
                    "\"ShipperCode\": \"STO\",  " +
                    "\"Success\": true,  " +
                    "\"LogisticCode\": \"3335894453552\", " +
                    " \"State\": \"3\", " +
                    " \"Traces\": [    " +
                    "{      \"AcceptTime\": \"2017-08-11 22:30:01\",      \"AcceptStation\": \"【收件】【广东揭阳公司】的【广东普宁公司网点】已收件,扫描员是【普宁1】\"    },  " +
                    "{      \"AcceptTime\": \"2017-08-11 22:57:03\",      \"AcceptStation\": \"【装袋】快件在【广东揭阳公司】进行装包发往扫描，发往【广东揭阳中转部】\"    }, " +
                    "{      \"AcceptTime\": \"2017-08-11 23:03:03\",      \"AcceptStation\": \"【发件】快件在【广东揭阳公司】由【普宁刘洪杰】扫描发往【广东揭阳中转部】\"    }, " +
                    "{      \"AcceptTime\": \"2017-08-12 01:40:15\",      \"AcceptStation\": \"【发件】快件在【广东揭阳航空部】由【次日达】扫描发往【广西南宁航空部】\"    }, " +
                    "{      \"AcceptTime\": \"2017-08-14 01:18:56\",      \"AcceptStation\": \"【发件】快件在【广西南宁航空部】由【扫描组1】扫描发往【广西南宁江南公司】\"    }, " +
                    "{      \"AcceptTime\": \"2017-08-14 03:40:27\",      \"AcceptStation\": \"【到件】快件到达【广西南宁江南公司】,上一站是【】,扫描员是【陈姐】\"    }, " +
                    "{      \"AcceptTime\": \"2017-08-14 08:30:19\",      \"AcceptStation\": \"【派件】【广西南宁江南公司】的【陆伟文 手机(13597296314)】正在派件,扫描员是【陈姐】\"    }, " +
                    "{      \"AcceptTime\": \"2017-08-14 13:54:47\",      \"AcceptStation\": \"【签收】已签收,签收人是:【覃】\"    }  ]}";*/


                } else {
                    sb.append("没有物流轨迹");
                }
                //System.out.println(sb.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public static void main(String args[])throws Exception{
        sync("3335894453552");
    }
}
