/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *//*

package com.thinkgem.jeesite.modules.pdd.web;


import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.pdd.service.PddOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

*/
/**
 * 留言板Controller
 * @author ThinkGem
 * @version 2013-3-15
 *//*

@Controller
@RequestMapping(value = "${frontPath}/login")
public class LoginController extends BaseController{

	@Autowired
	private PddOrderService pddOrderService;


*/
/**
 * 管理登录
 *//*

	@RequestMapping(value = "${frontPath}/login/register", method = RequestMethod.GET)
	public String register(HttpServletRequest request, HttpServletResponse response, Model model) {
//		String view;
//		view = "/WEB-INF/views/modules/sys/sysLogin.jsp";
//		view = "classpath:";
//		view += "jar:file:/D:/GitHub/jeesite/src/main/webapp/WEB-INF/lib/jeesite.jar!";
//		view += "/"+getClass().getName().replaceAll("\\.", "/").replace(getClass().getSimpleName(), "")+"view/sysLogin";
//		view += ".jsp";
		return "modules/sys/sysRegister";
	}


	*/
/**
	 * 管理登录
	 *//*

	@RequestMapping(value = "${frontPath}/login/forget", method = RequestMethod.GET)
	public String forget(HttpServletRequest request, HttpServletResponse response, Model model) {
//		String view;
//		view = "/WEB-INF/views/modules/sys/sysLogin.jsp";
//		view = "classpath:";
//		view += "jar:file:/D:/GitHub/jeesite/src/main/webapp/WEB-INF/lib/jeesite.jar!";
//		view += "/"+getClass().getName().replaceAll("\\.", "/").replace(getClass().getSimpleName(), "")+"view/sysLogin";
//		view += ".jsp";
		return "modules/sys/sysForget";
	}

}
*/
