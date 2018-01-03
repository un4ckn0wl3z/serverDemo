package com.haoyun.gameSet.horseRaceLamp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractService;
import com.haoyun.commons.util.DateUtil;
import com.haoyun.commons.util.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 跑马灯
 */
@Service
public class HorseRLService extends AbstractService {
	public static final String CHANNEL = "horse:topic";

	Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 获取跑马灯列表
	 * @return
	 */
	public JSONObject getHorseRLampList(Integer managerLevelId) {
		JSONObject json = new JSONObject();
		json.put("horseRLampList", null);

		Set<String> keys = redisDao.getKey("horseRLamp:*");

		List<Map<String, String>> horseRLampList = new ArrayList<>();
		int count = 0;
		for (String key : keys) {
			Map<String, String> map = redisDao.getMap(key);
			horseRLampList.add(map);

			count++;
			if (count > 99) {
				break;
			}
		}

		handelList(horseRLampList, managerLevelId);

		log.info(horseRLampList.size() + "");
		json.put("horseRLampList", horseRLampList);

		return json;
	}

	/**
	 * 添加或编辑跑马灯
	 * @param encodeData
	 * @param managerLevelId
	 * @return
	 */
	public JSONObject submitHorseRLamp(String encodeData, Integer managerLevelId) throws UnsupportedEncodingException, InterruptedException {
		JSONObject json = new JSONObject();
		json.put("result", false);

		if ((encodeData == null) || (managerLevelId == null)) {
			json.put("msg", "参数为空!");
			return json;
		}

		String emailDataStr = URLDecoder.decode(encodeData, "utf-8");
		JSONObject horseJson = JSONObject.parseObject(emailDataStr);

		if (!vldAndAddHostId(json, horseJson, managerLevelId)) {
			return json;
		}

		// 新增跑马灯
		String horseId = horseJson.getString("horseId");
		if (horseId == null) {  // 新增
			horseId = IDUtil.gen("");
			horseJson.put("horseId", horseId);

			if (horseJson.getInteger("isEnable") == 1) {
				// 发布订阅
				redisDao.publish(CHANNEL, horseId);
			}
		} else {  // 修改
			// 发布订阅
			redisDao.publish(CHANNEL, horseId);
		}

		// 20170318 18141245
		String todayTime = DateUtil.getStringDate();
		todayTime = todayTime.substring(0, 4) + "-" + todayTime.substring(4, 6) + "-" +
				todayTime.substring(6, 8) + " " + todayTime.substring(8, 10) + ":" +
				todayTime.substring(10, 12) + ":" + todayTime.substring(12, 14);
		horseJson.put("sendTime", todayTime);

		redisDao.addHash("horseRLamp:" + horseId, horseJson);

		json.put("result", true);
		return json;
	}

	/**
	 * 删除跑马灯
	 * @param encodeData
	 * @return
	 */
	public JSONObject delete(String encodeData) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);
		json.put("msg", "参数为空");

		if (encodeData == null) {
			return json;
		}

		String horseRLampStr = URLDecoder.decode(encodeData, "utf-8");
		JSONObject horseRLamp = JSONObject.parseObject(horseRLampStr);

		String horseId   = horseRLamp.getString("horseId");
		Integer isEnable = horseRLamp.getInteger("isEnable");

		if ((horseId == null) || (isEnable == null)) {
			return json;
		}

		// 判断是否停用
		if (isEnable == 1) {
			json.put("msg", "请先关闭跑马灯");
			return json;
		}
		redisDao.delete("horseRLamp:" + horseId);
		log.info("delete horseRLamp的Id为:" + horseId);

		json.put("result", true);
		return json;
	}

	/**
	 * 全部停止/启用跑马灯
	 * @param isEnable
	 * @return
	 */
	public JSONObject operaHorse(String encodeList, String isEnable) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);

		if ((encodeList == null) || (isEnable == null)) {
			return json;
		}

		String listStr = URLDecoder.decode(encodeList, "utf-8");
		JSONArray jsonArray = JSONArray.parseArray(listStr);

		if (!validate(jsonArray)) {
			return json;
		}

		for (Object o : jsonArray) {
			JSONObject j = (JSONObject) o;
			Iterator<String> iterator = j.keySet().iterator();
			while (iterator.hasNext()) {
				// 获得key
				String horseId = iterator.next();
				// 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
				String isEnable_t = j.getString(horseId);

				if (!isEnable_t.equals(isEnable)) {
					// 发布订阅
					redisDao.publish(CHANNEL, horseId);

					boolean result = redisDao.hSet("horseRLamp:" + horseId, "isEnable", isEnable);
					log.info("operaHorse is:---------------" + result);

					// // 睡眠2s
					// try {
					// 	Thread.sleep(2000);
					// } catch (InterruptedException e) {
					// 	e.printStackTrace();
					// }
				}
			}
		}

		json.put("result", true);
		return json;
	}

	private boolean validate(JSONArray jsonArray) {
		if (jsonArray == null) {
			return false;
		}

		for (Object o : jsonArray) {
			JSONObject j = (JSONObject) o;
			Iterator<String> iterator = j.keySet().iterator();
			while (iterator.hasNext()) {
				// 获得key
				String horseId = iterator.next();
				if (horseId == null) {
					return false;
				}

				// 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
				String isEnable_t = j.getString(horseId);
				if (isEnable_t == null) {
					return false;
				}
			}
		}

		return true;
	}
}
