package com.haoyun.quotaMgt;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractService;
import com.haoyun.manager.ManagerInfo;
import com.haoyun.commons.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuotaRService extends AbstractService {
	Logger log = LoggerFactory.getLogger(getClass());

	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT, readOnly=true, rollbackFor = Exception.class)
	public JSONObject getQuotaRecordList() {
		JSONObject json = new JSONObject();
		json.put("result", false);
		json.put("quotaRecordList", null);

		// 20170318 18141245
		String todayTime = DateUtil.getStringDate().substring(0, 8);
		String sql = "select a.quotaRId, a.managerId, a.goldQuota, a.rcQuota, a.quotaMgrId,"
				+ "CONCAT(SUBSTR(a.operaTime, 1, 4),'-',SUBSTR(a.operaTime, 5, 2),'-',SUBSTR(a.operaTime, 7, 2)"
				+ ", ' ', SUBSTR(a.operaTime, 9, 2), ':',SUBSTR(a.operaTime, 11, 2),':',SUBSTR(a.operaTime, 13, 2)) as operaTime "
				+ " from quotaMgtRecord a where a.operaTime between ? and ? limit 1000";
		List<QuotaRecord> quotaRecordList = jdbcDao.queryForList(sql, new Object[]{todayTime.concat("000000"),
				todayTime.concat("999999")}, QuotaRecord.class);

		handelQuotaRecordList(quotaRecordList);
		json.put("quotaRecordList", quotaRecordList);
		json.put("result", true);

		return json;
	}

	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT, readOnly=true)
	public JSONObject searchQuotaMgtRecord(String encodeData)
			throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);
		json.put("quotaRecordList", null);

		if (encodeData == null) {
			return json;
		}

		String dataStr = URLDecoder.decode(encodeData, "utf-8");
		JSONObject data = JSONObject.parseObject(dataStr);

		// $scope.queryContent = {
		// 		managerName: '',
		// 		startTime: '',
		// 		endTime: '',
		// 	}
		String managerName = data.getString("managerName");
		String startTime = data.getString("startTime");
		String endTime = data.getString("endTime");

		String sql = "select a.quotaRId, a.managerId, b.managerName, a.goldQuota, a.rcQuota, a.quotaMgrId,"
				+ "CONCAT(SUBSTR(a.operaTime, 1, 4),'-',SUBSTR(a.operaTime, 5, 2),'-',SUBSTR(a.operaTime, 7, 2)"
				+ ", ' ', SUBSTR(a.operaTime, 9, 2), ':',SUBSTR(a.operaTime, 11, 2),':',SUBSTR(a.operaTime, 13, 2)) "
				+ "as operaTime from quotaMgtRecord a, manager b where a.managerId = b.managerId ";
		List<Object> params = new ArrayList<>();
		if ((managerName != null) && (!managerName.equals(""))) {
			sql += "and b.managerName=? ";
			params.add(managerName);
		}

		if ((!startTime.equals("")) && (!endTime.equals(""))) {
			sql += "and a.operaTime between ? and ? ";

			if (Integer.valueOf(startTime) <= Integer.valueOf(endTime)) {
				params.add(startTime + "000000");
				params.add(endTime + "999999");
			} else {
				params.add(endTime + "000000");
				params.add(startTime + "999999");
			}
		}

		List<QuotaRecord> quotaRecordList = jdbcDao.queryForList(sql, params.toArray(), QuotaRecord.class);
		handelQuotaRecordList(quotaRecordList);

		json.put("result", true);
		json.put("quotaRecordList", quotaRecordList);

		return json;
	}


	/**
	 * 处理管理员信息
	 * @param quotaRecordList
	 * @return
	 */
	private void handelQuotaRecordList(List<QuotaRecord> quotaRecordList) {
		if ((quotaRecordList == null) || (quotaRecordList.size() < 1)) {
			return;
		}

		String sql = "select managerId, managerName from manager";
		List<ManagerInfo> managerInfoList = jdbcDao.queryForList(sql, new Object[]{}, ManagerInfo.class);

		for (int i = 0; i < quotaRecordList.size(); i++) {
			QuotaRecord quotaRecord = quotaRecordList.get(i);

			for (ManagerInfo manager : managerInfoList) {
				if (quotaRecord.managerId == manager.managerId) {
					quotaRecord.setManagerName(manager.managerName);
				}
				if (quotaRecord.quotaMgrId == manager.managerId) {
					quotaRecord.setQuotaMgrName(manager.managerName);
				}
			}
		}
	}
}
