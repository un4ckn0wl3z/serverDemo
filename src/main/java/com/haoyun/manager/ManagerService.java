package com.haoyun.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractService;
import com.haoyun.commons.util.DBUtil;
import com.haoyun.commons.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 */
@Service
public class ManagerService extends AbstractService {
	Logger log = LoggerFactory.getLogger(getClass());

	private static Connection conn;

	private static Statement state;

	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT, readOnly=true)
	public List<ManagerInfo> getManagerInfoList() {
		String sql = "select * from manager";
		List<ManagerInfo> managerInfoList = jdbcDao.queryForList(sql, new Object[]{}, ManagerInfo.class);

		handelMnagerInfo(managerInfoList);

		return managerInfoList;
	}

	/**
	 * 处理管理员信息
	 * @param managerInfoList
	 * @return
	 */
	public List<ManagerInfo> handelMnagerInfo(List<ManagerInfo> managerInfoList) {
		if (managerInfoList == null) {
			return null;
		}

		String sql = "select * from managerLevel";
		List<ManagerInfo> managerLevelInfoList = jdbcDao.queryForList(sql, new Object[]{}, ManagerInfo.class);

		for (int i = 0; i < managerInfoList.size(); i++) {
			ManagerInfo managerInfo = managerInfoList.get(i);
			managerInfo.setPassword(""); // 去除密码

			for (ManagerInfo managerLevelInfo : managerLevelInfoList) {
				if (managerLevelInfo.managerLevelId == managerInfo.managerLevelId) {
					managerInfo.setManagerLevelName(managerLevelInfo.managerLevelName);
					break;
				}
			}
		}

		return managerInfoList;
	}

	/**
	 * 添加或编辑管理员
	 * @param encodeManagerInfo
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Transactional(timeout = 10, propagation= Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
			rollbackFor=Exception.class)
	public JSONObject submit(String encodeManagerInfo) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);

		if (encodeManagerInfo == null) {
			return json;
		}

		String encodeManageInfoStr = URLDecoder.decode(encodeManagerInfo, "utf-8");
		JSONObject manageInfo      = JSONObject.parseObject(encodeManageInfoStr);
		String managerId           = manageInfo.getString("managerId");
		String managerName         = manageInfo.getString("managerName");
		String nickName            = manageInfo.getString("nickName");
		String password            = manageInfo.getString("password");
		String managerLevelId      = manageInfo.getString("managerLevelId");
		String seed      		   = manageInfo.getString("seed");

		if (password != null) {
			String key = "=yrwPo&3l?123";
			String vKey = password.substring(0, key.length());

			if (!vKey.equals(key)) {
				return json;
			}
			password = password.substring(key.length());
			password = MD5Util.md5(password);
		}

		if (managerId == null) {
			// insert
			String sql = "insert into manager (managerName,nickName,password,seed,managerLevelId) values (?,?,?,?,?)";
			int count = jdbcDao.update(sql, new Object[]{managerName, nickName, password, seed, managerLevelId});
			if (count > 0) {
				json.put("result", true);
			}
		} else {
			// update
			int count;
			if (password == null) {
				String sql_1 = "update manager set managerName=?,nickName=?,seed=?,managerLevelId=? where managerId=?";
				count = jdbcDao.update(sql_1, new Object[]{managerName, nickName, seed, managerLevelId, managerId});

			} else {
				String sql_1 = "update manager set managerName=?,nickName=?,password=?,seed=?, managerLevelId=? " +
						"where managerId=?";
				count = jdbcDao.update(sql_1, new Object[]{managerName, nickName, password, seed,  managerLevelId,
						managerId});
			}
			if (count > 0) {
				json.put("result", true);
			}
		}

		return json;
	}

	/**
	 * 删除管理员
	 * @param encodeManagerId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Transactional(timeout = 10, propagation= Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public JSONObject deleteManager(String encodeManagerId) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);

		if (encodeManagerId == null) {
			return json;
		}

		String managerId = URLDecoder.decode(encodeManagerId, "utf-8");

		// 获取并删除管理员权限设置信息
		String sql_0 = "select count(*) from managerPrivilege where managerId=?";
		int num = jdbcDao.queryForInt(sql_0, new Object[]{managerId});
		if (num > 0) {
			String sql_1 = "delete from managerPrivilege where managerId=?";
			int count = jdbcDao.update(sql_1, new Object[]{managerId});

			if (count == 0) {
				json.put("result", false);
				return json;
			}
		}

		String sql = "delete from manager where managerId=?";
		int count = jdbcDao.update(sql, new Object[]{managerId});
		if (count > 0) {
			json.put("result", true);
		}

		return json;
	}

	/**
	 * 获取管理员等级模板信息
	 * @return
	 */
	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT, readOnly=true)
	public JSONObject getManagerLevelInfoList() {
		JSONObject json = new JSONObject();
		json.put("result", false);
		json.put("managerLevelInfoList", null);

		String sql = "select * from managerLevel";
		List<ManagerInfo> managerLevelInfoList = jdbcDao.queryForList(sql, new Object[]{}, ManagerInfo.class);

		if (managerLevelInfoList == null) {
			return json;
		}

		json.put("result", true);
		json.put("managerLevelInfoList", managerLevelInfoList);

		return json;
	}

	/**
	 * 添加或编辑管理员等级模板
	 * @param encodeManagerLevelInfo
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Transactional(timeout = 10, propagation= Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public JSONObject setManagerLevelInfo(String encodeManagerLevelInfo) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);

		if (encodeManagerLevelInfo == null) {
			return json;
		}

		String managerLevelInfoStr = URLDecoder.decode(encodeManagerLevelInfo, "utf-8");
		JSONObject managerLevelInfo = JSONObject.parseObject(managerLevelInfoStr);
		Integer managerLevelId = managerLevelInfo.getInteger("managerLevelId");
		String managerLevelName = managerLevelInfo.getString("managerLevelName");

        if ((managerLevelName == null) || (managerLevelName.length() > 10)) {
            return json;
        }

        if (managerLevelId == null) {
            // insert
            String sql = "insert into managerLevel(managerLevelName) values (?)";
            int count = jdbcDao.update(sql, new Object[]{managerLevelName});
            if (count > 0) {
                json.put("result", true);
            }
        } else {
			// update
			int count;
			String sql_1 = "update managerLevel set managerLevelName=? where managerLevelId=?";
			count = jdbcDao.update(sql_1, new Object[]{managerLevelName, managerLevelId});

			if (count > 0) {
				json.put("result", true);
			}
		}

		return json;
	}

	/**
	 * 删除管理员等级模板
	 * @param managerLevelId
	 * @return
	 */
	@Transactional(timeout = 10, propagation= Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public JSONObject deleteManagerLevel(Integer managerLevelId){
		JSONObject json = new JSONObject();
		json.put("result", false);

		if (managerLevelId == null) {
			return json;
		}

		// 获取并删除管理员等级权限设置信息
		String sql_0 = "select count(*) from managerLevelPrivilege where managerLevelId=?";
		int num = jdbcDao.queryForInt(sql_0, new Object[]{managerLevelId});
		if (num > 0) {
			String sql_1 = "delete from managerLevelPrivilege where managerLevelId=?";
			int count = jdbcDao.update(sql_1, new Object[]{managerLevelId});

			if (count == 0) {
				json.put("result", false);
				return json;
			}
		}

		String sql = "delete from managerLevel where managerLevelId=?";
		int count = jdbcDao.update(sql, new Object[]{managerLevelId});
		if (count > 0) {
			json.put("result", true);
		}

		return json;
	}

	/**
	 * 获取管理员等级权限信息
	 * @param managerLevelId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT, readOnly=true)
	public JSONObject getLevelPrivilege(Integer managerLevelId) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);

		if (managerLevelId == null) {
			return json;
		}

		String sql = "select * from managerLevelPrivilege where managerLevelId=?";
		List<ManagerLPriInfo> managerLPInfoList = jdbcDao.queryForList(sql, new Object[]{managerLevelId}, ManagerLPriInfo.class);
		if (managerLPInfoList != null) {
			json.put("menuPriInfo", translateMLPrivilege(managerLPInfoList));
		}

		JSONObject gamePriInfo = getMLGamePriInfoList(managerLevelId);
		json.put("gamePriInfo", gamePriInfo);

		JSONObject gameHostPriInfo = getMLGHostPriInfoList(managerLevelId);
		json.put("gameHostPriInfo", gameHostPriInfo);

		JSONObject tablePriInfo = getMLTablePriInfo(managerLevelId);
		json.put("tablePriInfo", tablePriInfo);

		json.put("result", true);

		return json;
	}

	/**
	 * [{privilegeName: privilegeName, privilege: 1}] --> {privilegeName: 1}
	 * @param managerLPInfoList
	 * @return
	 */
	private JSONObject translateMLPrivilege(List<ManagerLPriInfo> managerLPInfoList){
		JSONObject json = new JSONObject();

		if(managerLPInfoList == null){
			return json;
		}

		for (ManagerLPriInfo m : managerLPInfoList) {
			if ((m.privilegeName != null) && (m.privilegeName.length() > 1)) {
				json.put(m.privilegeName, m.privilege);
			}
		}

		return json;
	}

	/**
	 * 获取管理员等级 游戏运营商 权限信息
	 * game host privilege info
	 * @return hostIdPrivilegeInfo: {hostId : privilege}
	 */
	private JSONObject getMLGHostPriInfoList(Integer managerLevelId) {
		JSONObject json = new JSONObject();

		String sql = "select hostId, privilege from managerLHPrivilege where managerLevelId=?";
		List<HostPriInfo> hostPriInfoList = jdbcDao.queryForList(sql, new Object[]{managerLevelId},
				HostPriInfo.class);
		if (hostPriInfoList == null) {
			return null;
		}

		for (HostPriInfo g : hostPriInfoList) {
			json.put(g.hostId, g.privilege);
		}

		return json;
	}

	public JSONObject getTablePriInfo(Integer managerLevelId){
		JSONObject json = new JSONObject();
		json.put("result", false);

		if (managerLevelId == null) {
			return json;
		}

		JSONObject tablePriInfo = getMLTablePriInfo(managerLevelId);
		json.put("tablePriInfo", tablePriInfo);
		json.put("result", true);

		return json;
	}

	/**
	 * 获取管理员等级 表 权限信息
	 * table privilege info
	 * @return tablePrivilegeInfo: {tableId : privilege}
	 */
	public JSONObject getMLTablePriInfo(Integer managerLevelId) {
		JSONObject json = new JSONObject();

		String sql = "select tableId, privilege from tablePrivilege where managerLevelId=?";
		List<TablePriInfo> tablePriInfoList = jdbcDao.queryForList(sql, new Object[]{managerLevelId},
				TablePriInfo.class);
		if (tablePriInfoList == null) {
			return json;
		}

		for (TablePriInfo t : tablePriInfoList) {
			json.put(t.tableId, t.privilege);
		}

		return json;
	}

	@Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.DEFAULT, readOnly=true)
	public List<Game> getGamesInfoList() {
		String sql = "select * from game";
		List<Game> gameList = jdbcDao.queryForList(sql, new Object[]{}, Game.class);
		return gameList;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public JSONObject getGameHostInfoList() {
		JSONObject json = new JSONObject();
		json.put("gameHostInfoList", null);

		String sql = "select hostId, hostName from host";
		List<GameHost> gameHostInfoList = jdbcDao.queryForList(sql, new Object[]{}, GameHost.class);

		json.put("gameHostInfoList", gameHostInfoList);

		return json;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public JSONObject getTableInfoList() {
		JSONObject json = new JSONObject();
		json.put("tableInfoList", null);

		String sql = "select * from tableInfo";
		List<TableInfo> tableInfoList = jdbcDao.queryForList(sql, new Object[]{}, TableInfo.class);

		json.put("tableInfoList", tableInfoList);

		return json;
	}

	/**
	 * 管理等级权限提交
	 * @param encodeMenuPriInfo ''
	 * @return ''
	 * @throws UnsupportedEncodingException ''
	 */
	@Transactional(timeout = 30, propagation= Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public JSONObject submitLMenuPriInfo(Integer managerLevelId, String encodeMenuPriInfo,
                                         String encodeGamePriInfo, String encodeGameHostPriInfo,
                                         String encodeTablePriInfo) throws UnsupportedEncodingException {
		JSONObject json = new JSONObject();
		json.put("result", false);

		if ((managerLevelId == null) || (encodeMenuPriInfo == null)
				|| (encodeGamePriInfo == null) || (encodeGameHostPriInfo == null)) {
			return json;
		}

		// 游戏运营商---菜单权限
		String menuPriInfoStr = URLDecoder.decode(encodeMenuPriInfo, "utf-8");
		JSONObject menuPriInfo =  JSONObject.parseObject(menuPriInfoStr);

		// 游戏运营商---游戏权限
		String gamePriInfoStr = URLDecoder.decode(encodeGamePriInfo, "utf-8");
		JSONObject gamePriInfo =  JSONObject.parseObject(gamePriInfoStr);

		// 游戏运营商---运营商权限
		String gameHostPriInfoStr = URLDecoder.decode(encodeGameHostPriInfo, "utf-8");
		JSONObject gameHostPriInfo =  JSONObject.parseObject(gameHostPriInfoStr);

		// 设置管理员等级---菜单权限
		boolean setMLMenuResult = setMLMenuPriInfo(managerLevelId, menuPriInfo);
		if (setMLMenuResult) {
			json.put("result", true);
		}

		// 设置管理员等级---游戏权限
		boolean setMLGPResult = setMLGamePriInfo(gamePriInfo, managerLevelId);
		if (setMLGPResult) {
			json.put("result", true);
		} else {
			json.put("result", false);
		}

		// 设置管理员等级---运营商权限
		boolean setMLGHostPResult = setMLGameHostPriInfo(gameHostPriInfo, managerLevelId);
		if (setMLGHostPResult) {
			json.put("result", true);
		} else {
			json.put("result", false);
		}

		// 游戏运营商---表权限
		String tablePriInfoStr = URLDecoder.decode(encodeTablePriInfo, "utf-8");
		JSONObject tablePriInfo =  JSONObject.parseObject(tablePriInfoStr);

		// 设置管理员等级---表权限
		boolean setTablePriResult = setTablePriInfo(tablePriInfo, managerLevelId);
		if (setTablePriResult) {
			json.put("result", true);
		} else {
			json.put("result", false);
		}

		return json;
	}

	/**
	 * set manager level menu privilege Info
	 * 设置管理员等级菜单权限
	 * @param levelPrivilegeInfo {privilegeName : privilege}
	 */
	private boolean setMLMenuPriInfo(Integer managerLevelId, JSONObject levelPrivilegeInfo) {
		if ((levelPrivilegeInfo == null) || (managerLevelId == null)) {
			return false;
		}

		boolean result = true;
		Set<String> keySet = levelPrivilegeInfo.keySet();
		for (String privilegeName : keySet) {
			Integer privilege = levelPrivilegeInfo.getInteger(privilegeName);

			// 获取管理员权限设置信息
			String sql = "select count(*) from managerLevelPrivilege where managerLevelId=? and privilegeName=?";
			Integer num = jdbcDao.queryForInt(sql, new Object[]{managerLevelId, privilegeName});

			int count;
			if (num > 0) {
				String sql_1 = "update managerLevelPrivilege set privilege=? where managerLevelId=? and privilegeName=?";
				count = jdbcDao.update(sql_1, new Object[]{privilege, managerLevelId, privilegeName});
			} else {
				String sql_1 = "insert into managerLevelPrivilege(managerLevelId,privilegeName,privilege) values (?,?,?)";
				count = jdbcDao.update(sql_1, new Object[]{managerLevelId, privilegeName, privilege});
			}
			if (count <= 0) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * set manager level game privilege Info
	 * 设置管理员等级游戏权限
	 * @param gamePriInfo {gameId : privilege}
	 */
	private boolean setMLGamePriInfo(JSONObject gamePriInfo, Integer managerLevelId) {
		if ((gamePriInfo == null) || (managerLevelId == null)) {
			return false;
		}

		boolean result = true;
		Set<String> keySet = gamePriInfo.keySet();
		for (String gameId : keySet) {
			Integer privilege = gamePriInfo.getInteger(gameId);

			// 获取管理员权限设置信息
			String sql = "select count(*) from managerLGPrivilege where managerLevelId=? and gameId=?";
			Integer num = jdbcDao.queryForInt(sql, new Object[]{managerLevelId, gameId});

			int count;
			if (num > 0) {
				String sql_1 = "update managerLGPrivilege set privilege=? where managerLevelId=? and gameId=?";
				count = jdbcDao.update(sql_1, new Object[]{privilege, managerLevelId, gameId});
			} else {
				String sql_1 = "insert into managerLGPrivilege(managerLevelId,gameId,privilege) values (?,?,?)";
				count = jdbcDao.update(sql_1, new Object[]{managerLevelId, gameId, privilege});
			}
			if (count <= 0) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * set manager level game host privilege Info
	 * 设置管理员等级 游戏运营商 权限
	 * @param gameHostPriInfo {hostId : privilege}
	 */
	private boolean setMLGameHostPriInfo(JSONObject gameHostPriInfo, Integer managerLevelId) {
		if ((gameHostPriInfo == null) || (managerLevelId == null)) {
			return false;
		}

		boolean result = true;
		Set<String> keySet = gameHostPriInfo.keySet();
		for (String hostId : keySet) {
			Integer privilege = gameHostPriInfo.getInteger(hostId);

			// 获取管理员权限设置信息
			String sql = "select count(*) from managerLHPrivilege where managerLevelId=? and hostId=?";
			Integer num = jdbcDao.queryForInt(sql, new Object[]{managerLevelId, hostId});

			int count;
			if (num > 0) {
				String sql_1 = "update managerLHPrivilege set privilege=? where managerLevelId=? and hostId=?";
				count = jdbcDao.update(sql_1, new Object[]{privilege, managerLevelId, hostId});
			} else {
				String sql_1 = "insert into managerLHPrivilege(managerLevelId,hostId,privilege) values (?,?,?)";
				count = jdbcDao.update(sql_1, new Object[]{managerLevelId, hostId, privilege});
			}
			if (count <= 0) {
				result = false;
			}
		}

		return result;
	}


	/**
	 * set manager level table privilege Info
	 * 设置管理员等级 游戏运营商 表权限
	 * @param tablePriInfo {tableId : privilege}
	 */
	private boolean setTablePriInfo(JSONObject tablePriInfo, Integer managerLevelId) {
		if (managerLevelId == null) {
			return false;
		}

		boolean result = true;
		Set<String> keySet = tablePriInfo.keySet();
		for (String tableId : keySet) {
			Integer privilege = tablePriInfo.getInteger(tableId);

			// 获取管理员权限设置信息
			String sql = "select count(*) from tablePrivilege where managerLevelId=? and tableId=?";
			Integer num = jdbcDao.queryForInt(sql, new Object[]{managerLevelId, tableId});

			int count;
			if (num > 0) {
				String sql_1 = "update tablePrivilege set privilege=? where managerLevelId=? and tableId=?";
				count = jdbcDao.update(sql_1, new Object[]{privilege, managerLevelId, tableId});
			} else {
				String sql_1 = "insert into tablePrivilege(managerLevelId,tableId,privilege) values (?,?,?)";
				count = jdbcDao.update(sql_1, new Object[]{managerLevelId, tableId, privilege});
			}
			if (count <= 0) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * 获取菜单列表
	 * @return
	 */
	public JSONObject getMenuInfo() {
		JSONObject json = new JSONObject();
		json.put("menuInfoList", null);

		String sql = "select * from menu order by ord";
		List<Menu> menuInfoList = jdbcDao.queryForList(sql, new Object[]{}, Menu.class);
		if (menuInfoList == null) {
			return json;
		}

		JSONArray firstJArray = genMenuList(menuInfoList);
		json.put("menuInfoList", firstJArray);

		return json;
	}

	/**
	 * 按权限获取菜单列表
	 *
	 * @param managerLevelId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true, rollbackFor = Exception.class)
	public JSONObject getPriMenuInfoList(String managerLevelId) throws UnsupportedEncodingException,
			IllegalAccessException, SQLException, InstantiationException {
		JSONObject json = new JSONObject();
		json.put("result", false);
		json.put("priMenuInfoList", null);

		if (managerLevelId == null) {
			return json;
		}

		// 获取菜单信息
		List<Menu> menuInfoList = getMenuList();
		if (menuInfoList == null) {
			return json;
		}

		// 获取权限信息
		List<ManagerLPriInfo> managerLPInfoList = getManagerLPriInfo(managerLevelId);
		if ((managerLPInfoList == null) || (managerLPInfoList.size() < 1)) {
			return json;
		}

		// 菜单信息按权限过滤
		filterMenu(menuInfoList, managerLPInfoList);
		if (menuInfoList.size() < 1) {
			return json;
		}

		JSONArray firstJArray = genMenuList(menuInfoList);
		json.put("priMenuInfoList", firstJArray);
		json.put("result", true);

		return json;
	}

	/**
	 *
	 * 菜单信息按权限过滤
	 * @param menuInfoList
	 * @param managerLPInfoList
	 */
	private void filterMenu(List<Menu> menuInfoList, List<ManagerLPriInfo> managerLPInfoList) {
		Iterator<Menu> iterator = menuInfoList.iterator();
		while (iterator.hasNext()) {
			Menu menu = iterator.next();

			int flag = 0;
			for (ManagerLPriInfo p : managerLPInfoList) {
				if ((p.privilegeName != null) && p.privilegeName.equals(menu.menuId)) {
					flag = 1;
					if ((p.privilege == null) || (p.privilege != 1)) {
						iterator.remove();
						break;
					}
				}
			}

			if (flag == 0) {
				iterator.remove();
			}
		}
	}

	/**
	 * 构造菜单列表
	 * @param menuInfoList
	 * @return
	 */
	private JSONArray genMenuList(List<Menu> menuInfoList) {
		JSONArray firstJArray = new JSONArray(); // 一级菜单
		JSONArray jArray = new JSONArray();
		for (Menu menu : menuInfoList) {
			JSONObject j = new JSONObject();

			j.put("menuId", menu.menuId);
			j.put("menuName", menu.menuName);
			j.put("superMenuId", menu.superMenuId);
			j.put("juniorMenu", new JSONArray());

			jArray.add(j);

			if ((menu.superMenuId == null) || menu.superMenuId.equals("")) {
				firstJArray.add(j);
			}
		}

		for (Object o : firstJArray) {
			generateMenu((JSONObject) o, jArray);
		}

		return firstJArray;
	}

	/**
	 * 构造上下级菜单
	 * @param menuJson
	 * @param jArray
	 */
	private void generateMenu(JSONObject menuJson, JSONArray jArray) {
		for (Object o : jArray) {
			JSONObject j = (JSONObject) o;
			String sMenuId = j.getString("superMenuId");

			if ((sMenuId == null) || sMenuId.equals("")) {
				continue;
			}

			if (menuJson.getString("menuId").equals(sMenuId)) {
				JSONArray juniorMArray = menuJson.getJSONArray("juniorMenu");
				juniorMArray.add(j);

				generateMenu(j, jArray);
			}
		}
	}

	private List<ManagerLPriInfo> getManagerLPriInfo(String managerLevelId) throws SQLException, IllegalAccessException, InstantiationException {
		List<ManagerLPriInfo> managerLPriInfos = new ArrayList<>();

		if (managerLevelId == null) {
			return managerLPriInfos;
		}

		String dataSourceName = "serverGm";
		conn = DBUtil.getConnection(dataSourceName);

		if (conn == null) {
			return managerLPriInfos;
		}

		String sql = "select * from managerLevelPrivilege where managerLevelId=?";

		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1,Integer.valueOf(managerLevelId));

		ResultSet rs = pst.executeQuery();
		managerLPriInfos = DBUtil.populate(rs, ManagerLPriInfo.class);
		DBUtil.release(conn, pst, rs);

		return managerLPriInfos;
	}

	private List<Menu> getMenuList() throws SQLException, IllegalAccessException, InstantiationException {
		List<Menu> menuList = new ArrayList<>();

		String dataSourceName = "serverGm";
		conn = DBUtil.getConnection(dataSourceName);

		if (conn == null) {
			return menuList;
		}

		String sql = "select * from menu order by ord";

		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		menuList = DBUtil.populate(rs, Menu.class);
		DBUtil.release(conn, pst, rs);

		return menuList;
	}
}
