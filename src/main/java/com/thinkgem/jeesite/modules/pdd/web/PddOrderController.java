/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.pdd.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.pdd.entity.PddPlatform;
import com.thinkgem.jeesite.modules.pdd.service.PddPlatformService;
import com.thinkgem.jeesite.modules.quartz.util.kdniao.KdniaoUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
 * @author hhm
 * @version 2017-08-21
 */
@Controller
@RequestMapping(value = "${adminPath}/pdd/pddOrder")
public class PddOrderController extends BaseController {

	@Autowired
	private PddOrderService pddOrderService;

	@Autowired
	private PddPlatformService pddPlatformService;
	
	@ModelAttribute
	public PddOrder get(@RequestParam(required=false) String id) {
		PddOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pddOrderService.get(id);
		}
		if (entity == null){
			entity = new PddOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("pdd:pddOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(PddOrder pddOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		PddPlatform pddPlatform = new PddPlatform(UserUtils.getUser());
		pddOrder.setPddPlatform(pddPlatform);
		List<PddPlatform> pddPlatformList = pddPlatformService.findList(pddPlatform);
		model.addAttribute("pddPlatformList", pddPlatformList);
		Page<PddOrder> page = pddOrderService.findPage(new Page<PddOrder>(request, response), pddOrder);
		model.addAttribute("page", page);
		return "modules/pdd/pddOrderList";
	}

	@RequiresPermissions("pdd:pddOrder:view")
	@RequestMapping(value = "form")
	public String form(PddOrder pddOrder, Model model) {
		model.addAttribute("pddOrder", pddOrder);
		PddPlatform pddPlatform = new PddPlatform(UserUtils.getUser());
        List<PddPlatform> pddPlatformList = pddPlatformService.findList(pddPlatform);
		model.addAttribute("pddPlatformList", pddPlatformList);
		return "modules/pdd/pddOrderForm";
	}

	@RequiresPermissions("pdd:pddOrder:view")
	@RequestMapping(value = "view")
	public String view(PddOrder pddOrder, Model model) {
		model.addAttribute("pddOrder", pddOrder);
		PddPlatform pddPlatform = new PddPlatform(UserUtils.getUser());
		List<PddPlatform> pddPlatformList = pddPlatformService.findList(pddPlatform);
		model.addAttribute("pddPlatformList", pddPlatformList);
		return "modules/pdd/pddOrderView";
	}

	@RequiresPermissions("pdd:pddOrder:edit")
	@RequestMapping(value = "save")
	public String save(PddOrder pddOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pddOrder)){
			return form(pddOrder, model);
		}
		pddOrderService.save(pddOrder);
		addMessage(redirectAttributes, "保存订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/pdd/pddOrder/?repage";
	}

	@RequiresPermissions("pdd:pddOrder:edit")
	@RequestMapping(value = "express")
	public String express(PddOrder pddOrder, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pddOrder)){
			return form(pddOrder, model);
		}
		String string = pddOrder.getTrackingNumber();
		String msg = KdniaoUtils.sync(string);
		pddOrder.setLogisticInfo(msg);
		pddOrder.setEndUpdatedAt(new Date());
		pddOrderService.save(pddOrder);
		//同步快递
		addMessage(redirectAttributes, "同订单信息成功");
		return "redirect:"+Global.getAdminPath()+"/pdd/pddOrder/?repage";
	}
	
	@RequiresPermissions("pdd:pddOrder:edit")
	@RequestMapping(value = "delete")
	public String delete(PddOrder pddOrder, RedirectAttributes redirectAttributes) {
		pddOrderService.delete(pddOrder);
		addMessage(redirectAttributes, "删除订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/pdd/pddOrder/?repage";
	}

}