package com.haoyun.login;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
@RequestMapping(value = "login")
public class LoginController extends AbstractController<LoginService> {

	@Override
	@Inject
	@Named("loginService")
	protected void setService(LoginService service) {
		this.service = service;
	}

	@RequestMapping("/login")
	@ResponseBody
	public String login(HttpSession session, String encodeUsername, String encodePasswd, String secCode, String callback)
			throws UnsupportedEncodingException {
		String username = URLDecoder.decode(encodeUsername, "utf-8");

		JSONObject json = service.login(username, encodePasswd, secCode);
		if (json.getInteger("state") == 1) {
			session.setAttribute("username", username);
		}

		return callback +"("+ json.toJSONString() + ")";
	}

	@RequestMapping("/getManagerLevelId")
	@ResponseBody
	public int getManagerLevelId(String managerId) {
		return service.getManagerLevelId(managerId);
	}

	@RequestMapping(value = "getSystemState")
	@ResponseBody
	public JSONObject getSystemState() throws UnsupportedEncodingException {
		return service.getSystemState();
	}

	@RequestMapping(value = "getManagerInfo")
	@ResponseBody
	public String getManagerInfo(String encodeManagerName, String callback) throws UnsupportedEncodingException {

		JSONObject jsonObject = service.getManagerInfo(encodeManagerName);
		return callback + "(" + jsonObject + ")";
	}
}
