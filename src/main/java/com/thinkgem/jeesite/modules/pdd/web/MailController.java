/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.pdd.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pdd.email.model.Email;
import com.thinkgem.jeesite.modules.pdd.email.service.IMailService;
import com.thinkgem.jeesite.modules.pdd.email.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证码Controller
 * @author hhm
 * @version 2017-08-27
 */
@Controller
@RequestMapping(value = "${adminPath}/pdd/pddMail")
public class MailController extends BaseController {

	@Autowired
	private IMailService iMailService;

	@RequestMapping(value = {"send", ""})
	@ResponseBody
	public String send(HttpServletRequest request, HttpServletResponse response) {
		Email mail = new Email();
		mail.setEmail(new String[]{"281843699@qq.com"});
		mail.setSubject("你个小逗比");
		mail.setContent("科帮网欢迎您");
		mail.setTemplate("welcome");

		// sends the e-mail
		try {
			iMailService.send(mail);
//			iMailService.sendQueue(mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "已发送";
	}



}