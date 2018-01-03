package com.haoyun.playerMgt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractService;
import com.haoyun.manager.GameHost;
import com.haoyun.commons.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Service
public class PlayerMgtService extends AbstractService {
    Logger log = LoggerFactory.getLogger(getClass());

    public JSONObject getPlayerInfoList(Integer managerLevelId) {
        JSONObject json = new JSONObject();
        json.put("playerInfoList", null);

        if (managerLevelId == null) {
            return json;
        }

        List<GameHost> hostList = getHostList(managerLevelId);
        if ((hostList == null) || (hostList.size() < 1)) {
            return json;
        }

        Query query = new Query();
        String collectionName = "player";

        query.limit(1000);
        String columnName = "lastOnlineTime";
        query.with(new Sort(Sort.Direction.DESC, columnName));
        query.fields().exclude("_id").exclude("_class").exclude("appId").exclude("channelId")
                .exclude("uuid").exclude("loginType").exclude("token").exclude("sex")
                .exclude("headPortrait").exclude("password").exclude("state");

        List<MPlayer> playerInfoList = mongoDao.find(query, MPlayer.class, collectionName);
        handelPlayerList(playerInfoList, hostList);

        log.info(playerInfoList.size() + "");
        json.put("playerInfoList", playerInfoList);

        return json;
    }

    public JSONObject searchPlayerInfo(String encodeData, Integer managerLevelId) throws UnsupportedEncodingException {
        JSONObject json = new JSONObject();
        json.put("playerInfoList", null);

        if ((encodeData == null) || (managerLevelId == null)) {
            return json;
        }

        List<GameHost> hostList = getHostList(managerLevelId);
        if ((hostList == null) || (hostList.size() < 1)) {
            return json;
        }

        String queryContentStr = URLDecoder.decode(encodeData, "utf-8");
        JSONObject queryContent = JSONObject.parseObject(queryContentStr);

        // $scope.queryContent = {
        // 		pid: '',
        // 		name: '',
        // 		notLoginDays: '',
        // 		phone: '',
        // 		state: '',
        // 		isBindPhone: '',
        // 		roomCode: '',
        // }
        Integer pid          = queryContent.getInteger("pid");
        Integer roomCode     = queryContent.getInteger("roomCode");
        String name          = queryContent.getString("name");
        String hostId        = queryContent.getString("hostId");
        Integer notLoginDays = queryContent.getInteger("notLoginDays");
        String phone         = queryContent.getString("phone");
        Integer state        = queryContent.getInteger("state");
        Integer isBindPhone  = queryContent.getInteger("isBindPhone");

        Criteria criteria = new Criteria();
        // Query query = new Query();

        // ne: !=; gt: >; lt: <; lte: <=; like: /bc/; gt: >; gte: >=;
        // if (pid != null) {
        // 	Criteria cri = Criteria.where("pid").is(pid);
        // 	query.addCriteria(Criteria.where("players").elemMatch(cri));
        // }
        if (pid != null) {
            criteria.and("pid").is(pid);
        }

        if ((name != null) && (!name.equals(""))) {
            criteria.and("name").is(name);
        }

        if (hostId != null) {
            criteria.and("hostId").is(hostId);
        }

        if (roomCode != null) {
            criteria.and("roomCode").is(roomCode);
        }

        if (notLoginDays != null) {
            String notLoginDate = getLineDate(notLoginDays);
            criteria.and("lastOnlineTime").lte(notLoginDate);
        }

        if (phone != null) {
            criteria.and("phone").is(phone);
        }

        if (isBindPhone != null) {
            if (isBindPhone == 0) {
                // criteria.and("phone").regex("^.{0,10}$");
                criteria.and("phone").is(null);
            } else {
                criteria.and("phone").regex("^.{11}$");
            }
        }

        // {stateId: 0, stateName: '正常'},
        // {stateId: 1, stateName: '战斗'},
        // {stateId: 2, stateName: '空闲/离线'},
        // {stateId: 3, stateName: '冻结'},
        if (state != null) {
            // accountStatus;  账号状态 0：正常； 1：冻结
            // roomCode:       战斗状态: 0:空闲/离线: >0: 战斗
            switch (state) {
                case 0:
                    criteria.and("accountStatus").is(0);
                    break;
                case 1:
                    criteria.and("accountStatus").is(0);
                    criteria.and("roomCode").gt(0);
                    break;
                case 2:
                    criteria.and("accountStatus").is(0);
                    criteria.and("roomCode").is(0);
                    break;
                case 3:
                    criteria.and("accountStatus").is(1);
                    break;
                default:
                    criteria.and("accountStatus").is(0);
                    break;
            }
        }

        Query query = new Query();
        query.addCriteria(criteria);

        query.limit(1000);
        String columnName = "lastOnlineTime";
        query.with(new Sort(Sort.Direction.DESC, columnName));
        String collectionName = "player";
        query.fields().exclude("_id").exclude("_class").exclude("appId").exclude("channelId")
                .exclude("uuid").exclude("loginType").exclude("token").exclude("sex")
                .exclude("headPortrait").exclude("password").exclude("state");

        List<MPlayer> playerInfoList = mongoDao.find(query, MPlayer.class, collectionName);
        handelPlayerList(playerInfoList, hostList);

        log.info("playerInfoList.size: " + playerInfoList.size());
        json.put("playerInfoList", playerInfoList);

        return json;
    }

    /**
     * 获取最近notLoginDays天之前的时间
     *
     * @param notLoginDays
     * @return yyyy-MM-dd HH:mm:ss
     */
    private String getLineDate(Integer notLoginDays) {
        if ((notLoginDays == null) || (notLoginDays < 1)) {
            return DateUtil.getStringDate();
        }

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar1.add(Calendar.DATE, -notLoginDays);
        String startTime = sdf1.format(calendar1.getTime());

        return startTime;
    }

    /**
     * 获取金币池和房卡池数量
     *
     * @return
     */
    public JSONObject getPropsPoolInfo() throws ExecutionException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("roomCardNum", null);

        Query query = new Query();
        query.fields().include("roomCardNum");
        query.fields().include("money");
        String collectionName = "player";
        List<MPlayer> playerList = mongoDao.find(query, MPlayer.class, collectionName);

        ForkJoinPool forkjoinPool = new ForkJoinPool();
        ForkJoinCalcPoolNum task  = new ForkJoinCalcPoolNum(playerList);

        Future<Map<String, Long>> future = forkjoinPool.submit(task);
        forkjoinPool.awaitTermination(1, TimeUnit.SECONDS);

        Map<String, Long> map = future.get();
        json.put("roomCardNum", map.get("sum_rcNum"));
        json.put("money", map.get("sum_goldNum"));
        forkjoinPool.shutdown();

        return json;
    }

    /**
     *
     * 批量冻结玩家
     * @return
     */
    public JSONObject freezingPids(String encodeData, String freezeTime) throws UnsupportedEncodingException {
        JSONObject json = new JSONObject();
        json.put("result", false);

        if ((encodeData == null) || (freezeTime == null)) {
            return json;
        }

        String freezingPidArrayStr = URLDecoder.decode(encodeData, "utf-8");
        JSONArray freezingPidArray = JSONArray.parseArray(freezingPidArrayStr);

        Criteria criteria = new Criteria();
        Criteria[] ca = new Criteria[freezingPidArray.size()];
        for (int i = 0; i < freezingPidArray.size(); i++) {
            Criteria c = Criteria.where("pid").is(freezingPidArray.get(i));
            ca[i] = c;
        }
        criteria.orOperator(ca);

        Query query = new Query();
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("accountStatus", 1);
        update.set("unfreezeTime", freezeTime);

        String collectionName = "player";
        int count = mongoDao.updateMulti(query, update, collectionName);

        if (count > 0) {
            json.put("result", true);
            return json;
        }

        return json;
    }

    /**
     * 冻结详情
     *
     * @return
     */
    public JSONObject freezingDetail(Integer pid){
        JSONObject json = new JSONObject();
        json.put("result", false);
        json.put("unfreezeTime", null);

        if (pid == null) {
            return json;
        }

        Criteria criteria = new Criteria();
        criteria.and("pid").is(pid);

        Query query = new Query();
        query.addCriteria(criteria);

        String collectionName = "player";
        query.fields().include("unfreezeTime");
        MPlayer player = mongoDao.findOne(query, collectionName, MPlayer.class);

        log.info(player.getUnfreezeTime());
        json.put("unfreezeTime", player.getUnfreezeTime());
        json.put("result", true);
        return json;
    }

    /**
     *
     * 解冻玩家
     * @param pid
     * @return
     */
    public JSONObject unfreeze(Integer pid){
        JSONObject json = new JSONObject();
        json.put("result", false);

        if (pid == null) {
            return json;
        }

        Criteria criteria = new Criteria();
        criteria.and("pid").is(pid);

        Query query = new Query();
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("accountStatus", 0);

        String collectionName = "player";
        int count = mongoDao.updateFirst(query, update, collectionName);

        if (count > 0) {
            json.put("result", true);
            return json;
        }

        return json;
    }

    /* =============== 重置玩家房间 =============== */
    /**
     * 重置玩家房间
     *
     * @return
     */
    public JSONObject resetRCode(String resetRCodePidListStr) {
        JSONObject json = new JSONObject();
        json.put("result", false);

        if (resetRCodePidListStr == null) {
            return json;
        }

        JSONArray resetRCodePidArray = JSONArray.parseArray(resetRCodePidListStr);

        Criteria criteria = new Criteria();
        Criteria[] ca = new Criteria[resetRCodePidArray.size()];
        for(int i = 0; i < resetRCodePidArray.size(); i++){
            Criteria c = Criteria.where("pid").is(resetRCodePidArray.get(i));
            ca[i] = c;
        }
        criteria.orOperator(ca);

        Query query = new Query();
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("roomCode", 0);

        String collectionName = "player";
        int count = mongoDao.updateMulti(query, update, collectionName);

        if (count > 0) {
            json.put("result", true);
            return json;
        }

        return json;
    }
}
