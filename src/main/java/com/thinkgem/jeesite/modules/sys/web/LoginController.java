/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.modules.pdd.email.model.Email;
import com.thinkgem.jeesite.modules.pdd.email.service.IMailService;
import com.thinkgem.jeesite.modules.pdd.entity.PddVcode;
import com.thinkgem.jeesite.modules.pdd.service.PddVcodeService;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.quartz.util.sms.RandNumUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.CookieUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.security.FormAuthenticationFilter;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private SessionDAO sessionDAO;

	@Autowired
	private SystemService systemService;

	@Autowired
	private OfficeService officeService;

	@Autowired
	private PddVcodeService pddVcodeService;

	@Autowired
	private IMailService iMailService;
	
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();

//		// 默认页签模式
//		String tabmode = CookieUtils.getCookie(request, "tabmode");
//		if (tabmode == null){
//			CookieUtils.setCookie(response, "tabmode", "1");
//		}
		
		if (logger.isDebugEnabled()){
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			CookieUtils.setCookie(response, "LOGINED", "false");
		}
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null && !principal.isMobileLogin()){
			return "redirect:" + adminPath;
		}
//		String view;
//		view = "/WEB-INF/views/modules/sys/sysLogin.jsp";
//		view = "classpath:";
//		view += "jar:file:/D:/GitHub/jeesite/src/main/webapp/WEB-INF/lib/jeesite.jar!";
//		view += "/"+getClass().getName().replaceAll("\\.", "/").replace(getClass().getSimpleName(), "")+"view/sysLogin";
//		view += ".jsp";
		return "modules/sys/sysLogin";
	}

	@RequestMapping(value = "${adminPath}/register/register")
	public String register(HttpServletRequest request, HttpServletResponse response, Model model) {
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String vcode = request.getParameter("vcode");
		if(phone!=null&&password!=null){
			PddVcode pddVcode = pddVcodeService.getByPhone(phone);
			if(pddVcode!=null&&vcode.equals(pddVcode.getVcode())) {
				User user = new User();
				// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
				// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
				user.setCompany(officeService.get("1")); //公司
				user.setOffice(officeService.get("2"));   //部门
				user.setLoginName(phone);

				user.setPassword(password);
				user.setNewPassword(password);

				user.setNo(phone);
				user.setName(phone);
				user.setPhone(phone);
				user.setLoginFlag("1");//可登陆
				user.setDelFlag("0"); //未删除
				//check vcode
				if(new Date().getTime()-pddVcode.getUpdateTime().getTime()<1000*60*10){//已过10分钟，需要重新发送
					// 如果新密码为空，则不更换密码
					if (StringUtils.isNotBlank(user.getNewPassword())) {
						user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
					}

					if (!(systemService.getUserByLoginName(user.getLoginName()) == null)){
						addMessage(model, "注册用户'" + user.getLoginName() + "'失败，手机号已注册，请登陆");
						return "modules/sys/sysRegister";
					}
					// 角色数据有效性验证，过滤不在授权内的角色
					List<Role> roleList = Lists.newArrayList();

					Role role = systemService.getRole("6");
					roleList.add(role);

//			List<String> roleIdList = new ArrayList<String>();
//			roleIdList.add("6");//添加普通用户

//			for (Role r : systemService.findAllRole()){
//				if (roleIdList.contains(r.getId())){
//					roleList.add(r);
//				}
//			}
					user.setRoleList(roleList);
					// 保存用户信息
					systemService.saveRegisterUser(user);
					// 清除当前用户缓存
					if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
						UserUtils.clearCache();
						//UserUtils.getCacheMap().clear();
					}
					addMessage(model, "注册用户'" + user.getLoginName() + "'成功");
					return "modules/sys/sysRegister";
				}else {
					addMessage(model, "注册用户'" + user.getLoginName() + "'失败，验证码已过10分钟，已失效，请重新获取");
					return "modules/sys/sysRegister";
				}
			}
		}addMessage(model, "注册用户失败：验证码不匹配"+phone);
		return "modules/sys/sysRegister";
	}


	@RequestMapping(value = "${adminPath}/register/emailRegister")
	public String emailRegister(HttpServletRequest request, HttpServletResponse response, Model model) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
//		String vcode = request.getParameter("vcode");
		if(email!=null&&password!=null){
//			PddVcode pddVcode = pddVcodeService.getByEmail(email);
//			if(pddVcode!=null&&vcode.equals(pddVcode.getVcode())) {
			User user = new User();
			// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
			// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
			user.setCompany(officeService.get("1")); //公司
			user.setOffice(officeService.get("2"));   //部门
			user.setLoginName(email);
			//String code = String.valueOf(UUID.randomUUID());
			User u = systemService.getUserByLoginName(user.getLoginName());
			if (u != null){
				if(u.getLoginFlag().equals("1")){
					addMessage(model, "用户'" + user.getLoginName() + "'已激活，请登陆");
					return "modules/sys/sysLogin";
				}else {
					Email mail = new Email();
					mail.setEmail(new String[]{email});
					mail.setSubject("激活邮件");
					mail.setContent("<h1>此邮件为官方激活邮件！请点击下面链接完成激活操作！</h1><h3>" +
//							"<a href=\"http://localhost:8181/jeesite/a/register/emailRegisterCode?email=" + email + "\">" +
							"<a href=\"http://123.207.58.71:8080/jeesite/a/register/emailRegisterCode?email=" + email + "\">" +
							"点击激活</a></h3>");
//					mail.setTemplate("welcome");

					try {
						iMailService.sendHtml(mail);
					} catch (Exception e) {
						e.printStackTrace();
					}
					systemService.saveUser(u);
					addMessage(model, "已发送激活链接到邮箱"+user.getLoginName()+"，请激活后登陆");
					return "modules/sys/sysEmailRegister";
				}
			}else {
				user.setName(email);
				user.setPassword(password);
				user.setNewPassword(password);

				user.setNo(email);
				user.setLoginFlag("0");//不可登陆需要激活
				user.setDelFlag("0"); //未删除
				//check vcode
//				if(new Date().getTime()-pddVcode.getUpdateTime().getTime()<1000*60*3){//已过1分钟，需要重新发送
				// 如果新密码为空，则不更换密码
				if (StringUtils.isNotBlank(user.getNewPassword())) {
					user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
				}


				// 角色数据有效性验证，过滤不在授权内的角色
				List<Role> roleList = Lists.newArrayList();

				Role role = systemService.getRole("6");
				roleList.add(role);

//			List<String> roleIdList = new ArrayList<String>();
//			roleIdList.add("6");//添加普通用户

//			for (Role r : systemService.findAllRole()){
//				if (roleIdList.contains(r.getId())){
//					roleList.add(r);
//				}
//			}

//				user.setNo(code);
				user.setRoleList(roleList);
				// 保存用户信息
				systemService.saveRegisterUser(user);
				// 清除当前用户缓存
				if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
					UserUtils.clearCache();
					//UserUtils.getCacheMap().clear();
				}


				Email mail = new Email();
				mail.setEmail(new String[]{email});
				mail.setSubject("激活邮件");
				mail.setContent("<h1>此邮件为官方激活邮件！请点击下面链接完成激活操作！</h1><h3>" +
//						"<a href=\"http://localhost:8181/jeesite/a/register/emailRegisterCode?email=" + email + "\">" +
						"<a href=\"http://123.207.58.71:8080/jeesite/a/register/emailRegisterCode?email=" + email + "\">" +
						"点击激活</a></h3>");
//					mail.setTemplate("welcome");

				try {
//					iMailService.send(mail);
					iMailService.sendHtml(mail);
				} catch (Exception e) {
					e.printStackTrace();
				}

				addMessage(model, "注册用户'" + user.getLoginName() + "'成功,已发送激活链接到邮箱, 请激活后登陆！");
				return "modules/sys/sysEmailRegister";
				/*}else {
					addMessage(model, "注册用户'" + user.getLoginName() + "'失败，验证码已过3分钟，已失效，请重新获取");
					return "modules/sys/sysRegister";*/
//				}
				//}
			}
		}
		addMessage(model, "注册用户失败");
		return "modules/sys/sysEmailRegister";
	}


	@RequestMapping(value = "${adminPath}/register/emailRegisterCode")
	public String emailRegisterCode(HttpServletRequest request, HttpServletResponse response, Model model) {
		String email = request.getParameter("email");
		User u = systemService.getUserByLoginName(email);
		if(u!=null){
			if(new Date().getTime()-u.getUpdateDate().getTime()<10*60*1000) {
				u.setLoginFlag("1");
				systemService.saveUser(u);
				addMessage(model, "激活用户成功：请登陆");
				return "modules/sys/sysLogin";
			}else {
				addMessage(model, "激活失败：激活码链接未激活已超过10分钟，激活无效！");
				return "modules/sys/sysEmailRegister";
			}
		}
		addMessage(model, "激活用户失败：验证码不匹配");
		return "modules/sys/sysEmailRegister";
	}

	@RequestMapping(value = "${adminPath}/register/emailIndex")
	public String emailIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/sysEmailRegister";
	}

	@RequestMapping(value = "${adminPath}/register/index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/sysRegister";
	}

	@ResponseBody
	@RequestMapping(value = "${adminPath}/register/vcode")
	public String registerVCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String phone = request.getParameter("phone");
		if(phone!=null&&phone.length()>0){
			int code = RandNumUtils.getRandNum(1,999999);
			PddVcode pddVcode = pddVcodeService.getByPhone(phone);
			if(pddVcode!=null){
				pddVcode.setVcode(String.valueOf(code));
				pddVcode.setUpdateTime(new Date());
				boolean flag = pddVcodeService.updateAndSendCode(pddVcode);
				if(flag){
					String msg = "已发送短信，请注意查收";
					return "{success:true,msg:'"+msg+"'}";
				}
			}else {
				User user = new User();
				user.setId(IdGen.uuid());
				pddVcode = new PddVcode();
				pddVcode.setPhone(phone);
				pddVcode.setVcode(String.valueOf(code));
				pddVcode.setUpdateTime(new Date());
				boolean flag = pddVcodeService.insertAndSendCode(pddVcode,user);
				if(flag){
					String msg = "已发送短信，请注意查收";
					return "{success:true,msg:'"+msg+"'}";
				}
			}
		}
//		return "modules/sys/sysRegister";
		String msg = "发送短信异常，phone:"+phone;
		return "{success:false,msg:"+msg+"}";
	}


	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/forget", method = RequestMethod.GET)
	public String forget(HttpServletRequest request, HttpServletResponse response, Model model) {
//		String view;
//		view = "/WEB-INF/views/modules/sys/sysLogin.jsp";
//		view = "classpath:";
//		view += "jar:file:/D:/GitHub/jeesite/src/main/webapp/WEB-INF/lib/jeesite.jar!";
//		view += "/"+getClass().getName().replaceAll("\\.", "/").replace(getClass().getSimpleName(), "")+"view/sysLogin";
//		view += ".jsp";
		return "modules/sys/sysForget";
	}
	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null){
			return "redirect:" + adminPath;
		}

		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
		
		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
		
		if (logger.isDebugEnabled()){
			logger.debug("login fail, active session size: {}, message: {}, exception: {}", 
					sessionDAO.getActiveSessions(false).size(), message, exception);
		}
		
		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)){
			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		}
		
		// 验证失败清空验证码
		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());
		
		// 如果是手机登录，则返回JSON字符串
		if (mobile){
	        return renderString(response, model);
		}
		
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();

		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);
		
		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
			}else if (StringUtils.equals(logined, "true")){
				UserUtils.getSubject().logout();
				return "redirect:" + adminPath + "/login";
			}
		}
		
		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()){
			if (request.getParameter("login") != null){
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null){
				return "modules/sys/sysIndex";
			}
			return "redirect:" + adminPath + "/login";
		}
		
//		// 登录成功后，获取上次登录的当前站点ID
//		UserUtils.putCache("siteId", StringUtils.toLong(CookieUtils.getCookie(request, "siteId")));

//		System.out.println("==========================a");
//		try {
//			byte[] bytes = com.thinkgem.jeesite.common.utils.FileUtils.readFileToByteArray(
//					com.thinkgem.jeesite.common.utils.FileUtils.getFile("c:\\sxt.dmp"));
//			UserUtils.getSession().setAttribute("kkk", bytes);
//			UserUtils.getSession().setAttribute("kkk2", bytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		for (int i=0; i<1000000; i++){
////			//UserUtils.getSession().setAttribute("a", "a");
////			request.getSession().setAttribute("aaa", "aa");
////		}
//		System.out.println("==========================b");
		return "modules/sys/sysIndex";
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
}
