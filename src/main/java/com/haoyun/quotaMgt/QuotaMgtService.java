package com.haoyun.quotaMgt;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractService;
import com.haoyun.commons.util.DateUtil;
import com.haoyun.commons.util.DoubleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Service
public class QuotaMgtService extends AbstractService {
	Logger log = LoggerFactory.getLogger(getClass());

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public JSONObject getMgrQuotaList() {
		JSONObject json = new JSONObject();
		String sql = "select a.managerId, a.managerName, b.rcQuota, b.goldQuota from manager a left join quotaMgt b " +
				"on a.managerId=b.managerId ";
		List<QuotaMgt> mgrQuotaList = jdbcDao.queryForList(sql, new Object[]{}, QuotaMgt.class);
		json.put("mgrQuotaList", mgrQuotaList);

		return json;
	}

	/**
	 * 编辑配额
	 * @param encodeQuota
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Transactional(timeout = 15, propagation= Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
			rollbackFor=Exception.class)
	public JSONObject submitQuota(String encodeQuota) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);

		if (encodeQuota == null) {
			return json;
		}

		String encodeQuotaStr = URLDecoder.decode(encodeQuota, "utf-8");
		JSONObject quotaJson = JSONObject.parseObject(encodeQuotaStr);
        Integer managerId  = quotaJson.getInteger("managerId");
		Integer goldQuota = quotaJson.getInteger("goldQuota");
		Integer rcQuota   = quotaJson.getInteger("rcQuota");

		String sql_0 = "select count(*) from quotaMgt where managerId=?";
		int num = jdbcDao.queryForInt(sql_0, new Object[]{managerId});

		int count;
		if (num > 0) {
            String sql_01 = "select * from quotaMgt where managerId=?";
            QuotaMgt quotaMgt = jdbcDao.queryForObject(sql_01, new Object[]{managerId}, QuotaMgt.class);

            if(quotaMgt.goldQuota == null){
                quotaMgt.goldQuota = 0;
            }

            if(quotaMgt.rcQuota == null){
                quotaMgt.rcQuota = 0;
            }

            if (DoubleUtil.add(quotaMgt.goldQuota, goldQuota) < 0) {
                goldQuota = 0;
            } else {
                goldQuota = (int) DoubleUtil.add(quotaMgt.goldQuota, goldQuota);
            }

            if (DoubleUtil.add(quotaMgt.rcQuota, rcQuota) < 0) {
                rcQuota = 0;
            } else {
                rcQuota = (int) DoubleUtil.add(quotaMgt.rcQuota, rcQuota);
            }

			String sql_1 = "update quotaMgt set goldQuota=?,rcQuota=? where managerId=?";
			count = jdbcDao.update(sql_1, new Object[]{goldQuota, rcQuota, managerId});
		} else {
			String sql = "insert into quotaMgt(managerId,goldQuota,rcQuota) values (?,?,?)";
			count = jdbcDao.update(sql, new Object[]{managerId, goldQuota, rcQuota});
		}

		if (count > 0) {
            writeQuotaRecord(quotaJson);
            json.put("result", true);
		}

		return json;
	}

	private void writeQuotaRecord(JSONObject quotaJson) {
		String managerId  = quotaJson.getString("managerId");
		String quotaMgrId = quotaJson.getString("quotaMgrId");
		Integer goldQuota = quotaJson.getInteger("goldQuota");
		Integer rcQuota   = quotaJson.getInteger("rcQuota");

        // 20170318 18141245
        String operaTime = DateUtil.getStringDate().substring(0, 14);

		String sql = "insert into quotaMgtRecord (managerId,goldQuota,rcQuota,quotaMgrId,operaTime) values (?,?,?,?,?)";
		int count = jdbcDao.update(sql, new Object[]{managerId, goldQuota, rcQuota, quotaMgrId, operaTime});
		log.info("insert into quotaMgtRecord is:----------------" + count);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public JSONObject getMgrQuota(Integer managerId) {
		JSONObject json = new JSONObject();
		String sql = "select a.goldQuota, a.rcQuota from quotaMgt a where a.managerId=?";
		QuotaMgt quota = jdbcDao.queryForObject(sql, new Object[]{managerId}, QuotaMgt.class);
		json.put("quota", quota);

		return json;
	}
}
