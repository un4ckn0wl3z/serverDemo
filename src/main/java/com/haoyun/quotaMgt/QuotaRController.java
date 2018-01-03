package com.haoyun.quotaMgt;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping(value = "quotaRecord")
public class QuotaRController extends AbstractController<QuotaRService> {

	@Override
	@Inject
	@Named("quotaRService")
	protected void setService(QuotaRService service) {
		this.service = service;
	}

	@RequestMapping("/getQuotaRecordList")
	@ResponseBody
	public JSONObject getQuotaRecordList() {
		 return service.getQuotaRecordList();
	}

	@RequestMapping(value = "searchQuotaMgtRecord", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject searchQuotaMgtRecord(String encodeData)
			throws UnsupportedEncodingException {
		return service.searchQuotaMgtRecord(encodeData);
	}
}
