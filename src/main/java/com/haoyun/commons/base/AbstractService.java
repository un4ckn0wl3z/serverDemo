package com.haoyun.commons.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.jdbc.JdbcDao;
import com.haoyun.commons.mongodb.MongoDao;
import com.haoyun.commons.redis.RedisDao;
import com.haoyun.manager.Game;
import com.haoyun.manager.GameHost;
import com.haoyun.manager.GamePriInfo;
import com.haoyun.manager.ManagerInfo;
import com.haoyun.playerMgt.MPlayer;
import com.haoyun.quotaMgt.QuotaMgt;
import com.haoyun.commons.util.DoubleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * @author Administrator
 * @date 2017/12/21
 */
public abstract class AbstractService {
    private static Logger log = LoggerFactory.getLogger(AbstractService.class);

    @Autowired
    protected JdbcDao jdbcDao;

    @Autowired
    protected RedisDao redisDao;

    @Autowired
    protected MongoDao mongoDao;

    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.DEFAULT, readOnly=true)
    public List<ManagerInfo> getManagerList() {
        String sql = "select managerId, managerName from manager order by managerId";
        List<ManagerInfo> managerList = jdbcDao.queryForList(sql, new Object[]{}, ManagerInfo.class);

        return managerList;
    }

    /**
     * 判断是否对该游戏拥有分析权限
     * @param managerLevelId
     * @param gameId
     * @return
     */
    public boolean judgeGamePrivilege(Integer managerLevelId, String gameId) {
        if ((managerLevelId == null) || (gameId == null)) {
            return false;
        }

        List<Game> gameList = getGamesList(managerLevelId);
        if (gameList == null) { // 无游戏权限
            return false;
        }
        int flag = 0;
        for (Game game : gameList) {
            if (gameId.equals(game.gameId)) {
                flag = 1;
                break;
            }
        }
        if (flag == 0) {  // 没有权限
            return false;
        }

        return true;
    }

    /**
     * 获取游戏列表
     * @param managerLevelId
     * @return
     */
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.DEFAULT, readOnly=true)
    public List<Game> getGamesList(Integer managerLevelId) {
        String sql = "select a.gameId, a.gameName from game a, managerLGPrivilege b where a.gameId=b.gameId and "
                + "b.managerLevelId=? and b.privilege=1";
        List<Game> gameList = jdbcDao.queryForList(sql, new Object[]{managerLevelId}, Game.class);

        return gameList;
    }

    /**
     * 获取管理员等级游戏权限信息
     * @return gamePriInfo: {gameId : privilege}
     */
    public JSONObject getMLGamePriInfoList(Integer managerLevelId) {
        JSONObject json = new JSONObject();

        String sql = "select gameId, privilege from managerLGPrivilege where managerLevelId=?";
        List<GamePriInfo> gamePriInfoList = jdbcDao.queryForList(sql, new Object[]{managerLevelId}, GamePriInfo.class);
        if (gamePriInfoList == null) {
            return null;
        }

        for (GamePriInfo gamePriInfo : gamePriInfoList) {
            json.put(String.valueOf(gamePriInfo.gameId), gamePriInfo.privilege);
        }

        return json;
    }

    /**
     * 授权额度变化
     *
     * @param emailJson
     */
    public void subQuota(JSONObject emailJson) {
        // sender  managerName
        // goldNum
        // rcNum

        String managerName = emailJson.getString("sender");
        Integer goldNum = emailJson.getInteger("goldNum");
        Integer rcNum = emailJson.getInteger("rcNum");

        String sql_m = "select managerId from manager where managerName=?";
        Integer managerId = jdbcDao.queryForSimpleObject(sql_m, new Object[]{managerName}, Integer.class);
        if (managerId == null) {
            return;
        }

        if (goldNum == null) {
            goldNum = 0;
        }

        if (rcNum == null) {
            rcNum = 0;
        }

        String sql_01 = "select * from quotaMgt where managerId=?";
        QuotaMgt quotaMgt = jdbcDao.queryForObject(sql_01, new Object[]{managerId}, QuotaMgt.class);

        if (quotaMgt.goldQuota == null) {
            quotaMgt.goldQuota = 0;
        }

        if (quotaMgt.rcQuota == null) {
            quotaMgt.rcQuota = 0;
        }

        goldNum = (int) DoubleUtil.sub(quotaMgt.goldQuota, goldNum);
        rcNum = (int) DoubleUtil.sub(quotaMgt.rcQuota, rcNum);

        String sql_1 = "update quotaMgt set goldQuota=?,rcQuota=? where managerId=?";
        int count = jdbcDao.update(sql_1, new Object[]{goldNum, rcNum, managerId});

        log.info("count is :---------------------" + count);
    }

    /**
     * 获取事件名称
     * @param eventId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public String getEventName(Integer eventId) {
        String sql = "select a.eventName from event a where a.eventId=?";
        String eventName = jdbcDao.queryForSimpleObject(sql, new Object[]{eventId}, String.class);

        return eventName;
    }

    /**
     * 运营商权限过滤
     *
     * @param list
     * @param managerLevelId
     */
    public void handelList(List<Map<String, String>> list, Integer managerLevelId) {
        if (list == null) {
            return;
        }

        List<GameHost> hostList = getHostList(managerLevelId);
        if ((hostList == null) || (hostList.size() < 1)) {
            list.clear();
            return;
        }

        Iterator<Map<String, String>> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map<String, String> map = iterator.next();
            JSONArray ja = JSONArray.parseArray(map.get("hostId"));
            int flag = 0;
            if ((ja != null) && (ja.size() > 0)) {
                for (GameHost host : hostList) {
                    if (host == null) {
                        break;
                    }
                    if ((host.hostId != null) && isContain(ja, host.hostId)) {
                        flag = 1;
                        break;
                    }
                }
            }

            if (flag == 0) {
                iterator.remove(); // 没有该运营商权限, 删除
            }
        }
    }

    private boolean isContain(JSONArray ja, String hostId) {
        for (Object o : ja) {
            if (hostId.equals(o)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 添加或编辑公告, 跑马灯等时, 验证并添加(编辑)运营商权限
     *
     * @param json
     * @param objJson
     * @param managerLevelId
     * @return
     */
    public boolean vldAndAddHostId(JSONObject json, JSONObject objJson, Integer managerLevelId) {
        List<GameHost> hostList = getHostList(managerLevelId);
        if ((hostList == null) || (hostList.size() < 1)) {
            json.put("msg", "无运营商权限!");
            return false;
        }

        JSONArray jsonArray = new JSONArray();
        for (GameHost h : hostList) {
            jsonArray.add(h.hostId);
        }

        objJson.put("hostId", jsonArray.toString());

        return true;
    }

    /**
     * 获取运营商列表
     * @param managerLevelId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public List<GameHost> getHostList(Integer managerLevelId) {
        String sql = "select a.hostId, a.hostName from host a, managerLHPrivilege b where a.hostId=b.hostId and "
                + "b.managerLevelId=? and b.privilege=1";
        List<GameHost> hostList = jdbcDao.queryForList(sql, new Object[]{managerLevelId}, GameHost.class);

        return hostList;
    }

    public List<Map<String, String>> addEmailList(Set<String> keys, List<GameHost> hostList) {
        List<Map<String, String>> emailList = new ArrayList<>();
        int count = 0;
        for (String key : keys) {
            Map<String, String> map = redisDao.getMap(key);
            JSONArray ja = JSONArray.parseArray(map.get("hostId"));

            if (!vdHostPvg(hostList, ja)) {
                continue;
            }

            emailList.add(map);
            count++;
            if (count > 99) {
                break;
            }
        }

        return emailList;
    }

    /**
     * 验证运营商权限 - validate host privilege
     *
     * @param hostList
     * @param ja
     * @return
     */
    public boolean vdHostPvg(List<GameHost> hostList, JSONArray ja) {
        if ((ja == null) || (ja.size() < 1)) {
            return false;
        }

        for (GameHost gh : hostList) {
            for (Object o : ja) {
                gh.hostId.equals(o);
                return true;
            }
        }

        return false;
    }

    /**
     * 运营商权限过滤
     *
     * @param list
     * @return
     */
    public void handelPlayerList(List<MPlayer> list, List<GameHost> hostList) {
        if (list == null) {
            return;
        }

        Iterator<MPlayer> iterator = list.iterator();
        while (iterator.hasNext()) {
            MPlayer player = iterator.next();
            int flag = 0;
            for (GameHost host : hostList) {
                if (host == null) {
                    break;
                }
                if ((host.hostId != null) && host.hostId.equals(player.getHostId())) {
                    player.setHostName(host.hostName);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                iterator.remove(); // 没有该游戏权限, 删除
            }
        }
    }
}
