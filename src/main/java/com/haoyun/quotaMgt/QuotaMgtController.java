package com.haoyun.quotaMgt;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 */
@Controller
@RequestMapping(value = "quotaMgt")
public class QuotaMgtController extends AbstractController<QuotaMgtService> {

	@Override
	@Inject
	@Named("quotaMgtService")
	protected void setService(QuotaMgtService service) {
		this.service = service;
	}

	@RequestMapping("/getMgrQuotaList")
	@ResponseBody
	public JSONObject getMgrQuotaList() {
		return service.getMgrQuotaList();
	}

	@RequestMapping("/submitQuota")
	@ResponseBody
	public JSONObject submitQuota(String encodeQuota) throws UnsupportedEncodingException {
		return service.submitQuota(encodeQuota);
	}

	@RequestMapping("/getMgrQuota")
	@ResponseBody
	public JSONObject getMgrQuota(Integer managerId) {
		return service.getMgrQuota(managerId);
	}
}
