package com.haoyun.playerMgt;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping(value = "playerMgt")
public class PlayerMgtController extends AbstractController<PlayerMgtService> {

	@Override
	@Inject
	@Named("playerMgtService")
	protected void setService(PlayerMgtService service) {
		this.service = service;
	}

	@RequestMapping("/getPlayerInfoList")
	@ResponseBody
	public JSONObject getPlayerInfoList(Integer managerLevelId) {
		 return service.getPlayerInfoList(managerLevelId);
	}

	@RequestMapping(value = "searchPlayerInfo")
	@ResponseBody
	public JSONObject searchPlayerInfo(String encodeData, Integer managerLevelId) throws UnsupportedEncodingException {
		return service.searchPlayerInfo(encodeData, managerLevelId);
	}

	@RequestMapping("/getPropsPoolInfo")
	@ResponseBody
	public JSONObject getPropsPoolInfo() throws ExecutionException, InterruptedException {
		return service.getPropsPoolInfo();
	}

	@RequestMapping("/freezingPids")
	@ResponseBody
	public JSONObject freezingPids(String encodeData, String freezeTime) throws UnsupportedEncodingException {
		return service.freezingPids(encodeData, freezeTime);
	}

	@RequestMapping("/freezingDetail")
	@ResponseBody
	public JSONObject freezingDetail(Integer pid) throws UnsupportedEncodingException {
		return service.freezingDetail(pid);
	}

	@RequestMapping("/unfreeze")
	@ResponseBody
	public JSONObject unfreeze(Integer pid) throws UnsupportedEncodingException {
		return service.unfreeze(pid);
	}

	@RequestMapping("/resetRCode")
	@ResponseBody
	public JSONObject resetRCode(String resetRCodePidListStr, Integer managerLevelId) throws UnsupportedEncodingException {
		return service.resetRCode(resetRCodePidListStr);
	}

}
