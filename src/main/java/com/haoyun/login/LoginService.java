package com.haoyun.login;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractService;
import com.haoyun.commons.util.MD5Util;
import com.haoyun.manager.ManagerInfo;
import ft.otp.verify.OTPVerify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author lpf
 */
@Service
public class LoginService extends AbstractService {
	Logger log = LoggerFactory.getLogger(getClass());

	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT)
	public JSONObject login(String managerName, String encodePasswd, String secCode)
			throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();

		// 0:该用户不存在 1:成功登录 2: 密码错误 3: 请输入密保 4: 密保错误
		json.put("state", 0);
		json.put("systemState", 1);

		String sql = "select a.managerName, a.managerLevelId,a.managerId,a.password, a.seed, b.managerLevelName " +
				"from manager a, managerLevel b where a.managerName=? and a.managerLevelId = b.managerLevelId";
		ManagerInfo manager = jdbcDao.queryForObject(sql, new Object[] { managerName }, ManagerInfo.class);

		if(manager == null){
			return json;
		}

		// 登录账号--登录时 大小写敏感
		if(!manager.managerName.equals(managerName)){
			return json;
		}

		// 判断前端系统是否被关闭
		String sql_11 = "select state from foregroundControl where type = 1";
		Integer systemState = jdbcDao.queryForSimpleObject(sql_11, new Object[]{}, Integer.class);

		json.put("systemState", String.valueOf(systemState));
		if ((systemState == 0) && (manager.managerId != 1)) {
			return json;
		}

		if ((manager.seed != null) && (manager.seed.length() > 1)) {
			if ((secCode == null) || (secCode.length() < 1)) { // 6位密保数
				json.put("state", 3);
				return json;
			}

			if (!JudgeSecurityCode(secCode, manager.seed)) {
				json.put("state", 4);
				return json;
			}
		}

		json.put("managerId", manager.managerId);
		json.put("managerLevelName", manager.managerLevelName);
		json.put("managerLevelId", manager.managerLevelId);

		String key = "=yrwPo&3l?123";
		String dataStr = URLDecoder.decode(encodePasswd, "utf-8");
		String vKey = dataStr.substring(0, key.length());

		// 0:没有该用户 1:成功登录 2:密码错误
		if (!vKey.equals(key)) {
			return json;
		}

		String password = dataStr.substring(key.length());
		password = MD5Util.md5(password);

		// state: 0:没有该用户 1:成功登录 2:密码错误
		if (manager.password == null) {
			json.put("state", 0);
			return json;
		} else if (manager.password.equals(password)) {
			json.put("state", 1);
			// json.put("managerId", manager.managerId);
			return json;
		} else {
			json.put("state", 2);
			return json;
		}
	}

	private boolean JudgeSecurityCode(String secCode, String seed) {
		if ((secCode == null) || (secCode.length() != 6)) {
			return false;
		}

		Map hashMap;
		int iDrift = 0;
		long lSucc = 0;
		Long nReturn;

		hashMap = OTPVerify.ET_CheckPwdz201(
				seed,                                    //令牌密钥
				System.currentTimeMillis() / 1000,        //调用本接口计算机的当前时间
				0,                                        //给0
				60,                                        //给60，因为每60秒变更新的动态口令
				iDrift,                                //漂移值，用于调整硬件与服务器的时间偏差，见手册说明
				20,                                        //认证窗口，见手册说明
				lSucc,                                    //成功值，用于调整硬件与服务器的时间偏差，见手册说明
				secCode);                                    //要认证的动态口令OTP

		nReturn = (Long) hashMap.get("returnCode");
		// System.out.println("returnCode = " + nReturn);

		if (nReturn == OTPVerify.OTP_SUCCESS) {
		//  System.out.println("check success");
		//  System.out.println("currentSucc = " + hashMap.get("currentUTCEpoch"));
		//  System.out.println("currentDrift = " + hashMap.get("currentDrift"));

		//  iDrift = ((Long) hashMap.get("currentDrift")).intValue();
		//  lSucc = ((Long) hashMap.get("currentUTCEpoch")).longValue();

			return true;
		} else {
			// System.out.println("check fail");
			return false;
		}
	}

	/**
	 * 获取管理员等级
	 *
	 * @param managerId
	 * @return
	 */
	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT, readOnly=true, rollbackFor = Exception.class)
	public int getManagerLevelId(String managerId) {
		if (managerId == null) {
			return 0;
		}

		String sql = "select managerLevelId from manager where managerId=?";
		int managerLevelId = jdbcDao.queryForSimpleObject(sql, new Object[]{managerId}, Integer.class);

		return managerLevelId;
	}

	// 获取后台系统状态-systemState
	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT, readOnly=true, rollbackFor = Exception.class)
	public JSONObject getSystemState() {
		JSONObject json = new JSONObject();
		json.put("systemState", 0);

		String sql_1 = "select state from foregroundControl where type = 1";
		Integer systemState = jdbcDao.queryForSimpleObject(sql_1, new Object[]{}, Integer.class);

		json.put("systemState", systemState);

		return json;
	}

	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT, rollbackFor = Exception.class)
	public JSONObject getManagerInfo(String encodeManagerName) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);

		if (encodeManagerName == null) {
			return json;
		}
		String managerName = URLDecoder.decode(encodeManagerName, "utf-8");

		String sql = "select a.managerId,b.managerLevelName,b.managerLevelId from manager a, managerLevel b " +
				"where a.managerName=? and a.managerLevelId = b.managerLevelId";
		ManagerInfo manager = jdbcDao.queryForObject(sql, new Object[]{managerName}, ManagerInfo.class);

		if (manager == null) {
			return json;
		} else {
			json.put("result", true);
			json.put("managerLevelName", manager.managerLevelName);
			json.put("managerLevelId", manager.managerLevelId);
			return json;
		}
	}
}
