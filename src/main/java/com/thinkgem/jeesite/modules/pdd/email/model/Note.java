package com.thinkgem.jeesite.modules.pdd.email.model;

import com.thinkgem.jeesite.modules.sys.entity.User;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Email封装类 
 * 创建者 科帮网 
 * 创建时间 2017年7月20日
 *
 */
public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	private String phones;
	private String signName;
	private String templateCode;
	private String json_params;
	private User user;

	public Note(String phones, String signName, String templateCode, String json_params, User user) {
		this.phones = phones;
		this.signName = signName;
		this.templateCode = templateCode;
		this.json_params = json_params;
		this.user = user;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getJson_params() {
		return json_params;
	}

	public void setJson_params(String json_params) {
		this.json_params = json_params;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
