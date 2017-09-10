/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.pdd.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.pdd.dao.PddLogisticsDao;
import com.thinkgem.jeesite.modules.pdd.entity.PddExpress;
import com.thinkgem.jeesite.modules.pdd.entity.PddLogistics;
import com.thinkgem.jeesite.modules.pdd.entity.PddPlatform;
import com.thinkgem.jeesite.modules.pdd.service.PddPlatformService;
import com.thinkgem.jeesite.modules.quartz.sync.PullThread;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoTrackQueryAPI;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.*;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.pdd.entity.PddOrder;
import com.thinkgem.jeesite.modules.pdd.service.PddOrderService;

import java.util.Date;
import java.util.List;

/**
 * 订单管理Controller
 *
 * @author hhm
 * @version 2017-08-21
 */
@Controller
@RequestMapping(value = "${adminPath}/pdd/pddOrder")
public class PddOrderController extends BaseController {
    private static final String EBusinessID = "1290729";//
//    private static final String EBusinessID = "1301687";//hhm

//    private static final String apiKey = "aece4f2c-bb84-4e5b-87a3-258f090dbae7"; //hhm

    @Autowired
    private PddOrderService pddOrderService;

    @Autowired
    private PddPlatformService pddPlatformService;

    @Autowired
    private PddLogisticsDao pddLogisticsDao;

    @Autowired
    private SystemService systemService;

    @ModelAttribute
    public PddOrder get(@RequestParam(required = false) String id) {
        PddOrder entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = pddOrderService.get(id);
        }
        if (entity == null) {
            entity = new PddOrder();
        }
        return entity;
    }

    @RequiresPermissions("pdd:pddOrder:view")
    @RequestMapping(value = {"list", ""})
    public String list(PddOrder pddOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (pddOrder.getPddPlatform() != null && StringUtils.isNotEmpty(pddOrder.getPddPlatform().getId())) {
            Page<PddOrder> page = pddOrderService.findPage(new Page<PddOrder>(request, response), pddOrder);
            model.addAttribute("page", page);
            model.addAttribute("user", UserUtils.getUser());
            List<PddPlatform> pddPlatformList = pddPlatformService.findList(new PddPlatform(UserUtils.getUser()));
            model.addAttribute("pddPlatformList", pddPlatformList);

            List<PddLogistics> pddLogisticses = pddLogisticsDao.findList(new PddLogistics());
            model.addAttribute("pddLogisticses", pddLogisticses);

        } else {
            PddPlatform pddPlatform = new PddPlatform(UserUtils.getUser());
            pddOrder.setPddPlatform(pddPlatform);
            Page<PddOrder> page = pddOrderService.findPageByUser(new Page<PddOrder>(request, response), pddOrder);
            model.addAttribute("page", page);
            model.addAttribute("user", UserUtils.getUser());
            List<PddPlatform> pddPlatformList = pddPlatformService.findList(new PddPlatform(UserUtils.getUser()));
            model.addAttribute("pddPlatformList", pddPlatformList);

            List<PddLogistics> pddLogisticses = pddLogisticsDao.findList(new PddLogistics());
            model.addAttribute("pddLogisticses", pddLogisticses);
        }

        return "modules/pdd/pddOrderList";
    }

    @RequiresPermissions("pdd:pddOrder:view")
    @RequestMapping(value = "form")
    public String form(PddOrder pddOrder, Model model) {
        model.addAttribute("pddOrder", pddOrder);
        PddPlatform pddPlatform = new PddPlatform(UserUtils.getUser());
        List<PddPlatform> pddPlatformList = pddPlatformService.findList(pddPlatform);
        model.addAttribute("pddPlatformList", pddPlatformList);

        List<PddLogistics> pddLogisticses = pddLogisticsDao.findList(new PddLogistics());
        model.addAttribute("pddLogisticses", pddLogisticses);
        return "modules/pdd/pddOrderForm";
    }

    @RequiresPermissions("pdd:pddOrder:view")
    @RequestMapping(value = "view")
    public String view(PddOrder pddOrder, Model model) {
        model.addAttribute("pddOrder", pddOrder);
        PddPlatform pddPlatform = new PddPlatform(UserUtils.getUser());
        List<PddPlatform> pddPlatformList = pddPlatformService.findList(pddPlatform);
        model.addAttribute("pddPlatformList", pddPlatformList);

        List<PddLogistics> pddLogisticses = pddLogisticsDao.findList(new PddLogistics());
        model.addAttribute("pddLogisticses", pddLogisticses);
        return "modules/pdd/pddOrderView";
    }

    @RequiresPermissions("pdd:pddOrder:edit")
    @RequestMapping(value = "save")
    public String save(PddOrder pddOrder, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, pddOrder)) {
            return form(pddOrder, model);
        }
        pddOrderService.save(pddOrder);
        addMessage(redirectAttributes, "保存订单管理成功");
        return "redirect:" + Global.getAdminPath() + "/pdd/pddOrder/?repage";
    }

    @RequiresPermissions("pdd:pddOrder:edit")
    @RequestMapping(value = "express") //手动同步订单
    public String express(PddOrder pddOrder, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, pddOrder)) {
            return form(pddOrder, model);
        }
//		String string = pddOrder.getTrackingNumber();
        User user = UserUtils.getUser();
        List<PddExpress> pddExpresses = user.getPddExpressList();
        if (pddExpresses != null && pddExpresses.size() > 0) {
            for (PddExpress pddExpres : pddExpresses) {//需要判断账号是否超过3000次，如超过换下一个几账号查询，没有时提示
//				KdApiOrderDistinguish api = new KdApiOrderDistinguish(pddExpres.getEbusinessid(),pddExpres.getApikey());
                try {
//					String data = api.getOrderTracesByJson(string);
//					KdApiOrderDistinguishEntity kdApiOrderDistinguishEntity = JSON.parseObject(data, KdApiOrderDistinguishEntity.class);//Weibo类在下边定义
                    //没有物流轨迹的为false
//					if (kdApiOrderDistinguishEntity.isSuccess()) {
//						List<Shippers> shippers = kdApiOrderDistinguishEntity.getShippers();
//						for (Shippers shippers1 :shippers){
                    KdniaoTrackQueryAPI qapi = new KdniaoTrackQueryAPI(pddExpres.getEbusinessid(), pddExpres.getApikey());
                    String data1 = qapi.getOrderTracesByJson(pddOrder.getPddLogistics().getLogisticsCode(), pddOrder.getTrackingNumber());
                    if (StringUtils.isNotEmpty(data1)) {
                        KdniaoTrackQueryAPIEntity kdniaoTrackQueryAPIEntity = JSON.parseObject(data1, KdniaoTrackQueryAPIEntity.class);
                        if (kdniaoTrackQueryAPIEntity != null) {
                            if (kdniaoTrackQueryAPIEntity.isSuccess()) {
                                int status = kdniaoTrackQueryAPIEntity.getState();//物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
                                if (status == 3) {//已收状态里改为已签收
                                    if (pddOrder.getOrderStatus() != 3) {//发货状态，1:待发货，2:已发货待签收，3:已签收 5:全部 暂时只开放待发货订单查询
                                        pddOrder.setOrderStatus(3);
                                    }
                                }
                                pddOrder.setPackageStatus(status);
                                List<Traces> traces = kdniaoTrackQueryAPIEntity.getTraces();
                                StringBuilder sb = new StringBuilder();
                                for (Traces traces1 : traces) {
                                    sb.append(traces1.getAcceptTime()).append(traces1.getAcceptStation()).append("\n");
                                }
                                pddOrder.setLogisticInfo(sb.toString());
                                pddOrder.setUpdatedAt(new Date()); //最后更新时间
                                pddOrderService.save(pddOrder);
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
                                //同步快递
                                addMessage(redirectAttributes, "同步订单信息成功");
                                return "redirect:" + Global.getAdminPath() + "/pdd/pddOrder/?repage";
//							}
//						}
                            }
                        }
                        {
                            //同步快递
                            addMessage(redirectAttributes, "同步订单信息失败");
                            return "redirect:" + Global.getAdminPath() + "/pdd/pddOrder/?repage";
                        }
                    } else {
                        //同步快递
                        addMessage(redirectAttributes, "同步订单信息失败");
                        return "redirect:" + Global.getAdminPath() + "/pdd/pddOrder/?repage";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //同步快递
        addMessage(redirectAttributes, "同步订单信息失败");
        return "redirect:" + Global.getAdminPath() + "/pdd/pddOrder/?repage";
    }

    @RequiresPermissions("pdd:pddOrder:edit")
    @RequestMapping(value = "delete")
    public String delete(PddOrder pddOrder, RedirectAttributes redirectAttributes) {
        pddOrderService.delete(pddOrder);
        addMessage(redirectAttributes, "删除订单管理成功");
        return "redirect:" + Global.getAdminPath() + "/pdd/pddOrder/?repage";
    }


    @RequiresPermissions("pdd:pddOrder:edit")
    @RequestMapping("/delSelect")
    public String delSelect(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String items = request.getParameter("ids");
        if (items != null) {
            String[] strs = items.split(",");
            for (int i = 0; i < strs.length; i++) {
                String a = strs[i];
                pddOrderService.delete(new PddOrder(a));
            }
        }
        addMessage(redirectAttributes, "批量删除订单完成");
        return "redirect:" + Global.getAdminPath() + "/pdd/pddOrder/?repage";
    }

    @RequestMapping({"/pull"})
    @ResponseBody
    public String pull(HttpServletRequest request, HttpServletResponse response) {
        String RequestData = request.getParameter("RequestData");
        String RequestType = request.getParameter("RequestType");
        String DataSign = request.getParameter("DataSign");
        //解析数据
        try {
            if (RequestType != null && RequestType.equals(ResponseData.type_101)) { // 推送数据
                PullThread pullThread = new PullThread(RequestData, pddOrderService);
                pullThread.start();
                String requestData = "{\"EBusinessID\":\"" + EBusinessID + "\",\"UpdateTime\":\"" + DateUtils.formatDateTime(new Date()) + "\",\"Success\":" + true + ",\"Reason\":\"\"}";
                return requestData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String requestData = "{\"EBusinessID\":\"" + EBusinessID + "\",\"UpdateTime\":\"" + DateUtils.formatDateTime(new Date()) + "\",\"Success\":" + false + ",\"Reason\":\"\"}";
        return requestData;
    }


}