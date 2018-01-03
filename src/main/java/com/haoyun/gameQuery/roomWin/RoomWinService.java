package com.haoyun.gameQuery.roomWin;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractService;
import com.haoyun.manager.GameHost;
import com.haoyun.commons.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * @author Administrator
 *         Created on 2017/12/26
 */
@Service
public class RoomWinService extends AbstractService {
    private static Logger log = LoggerFactory.getLogger(RoomWinService.class);

    public JSONObject getRoomList(Integer managerLevelId) {
        JSONObject json = new JSONObject();

        if (managerLevelId == null) {
            return json;
        }

        List<GameHost> hostList = getHostList(managerLevelId);
        if ((hostList == null) || (hostList.size() < 1)) {
            return json;
        }

        Query query = new Query();
        String collectionName = "room";

        query.limit(50);
        String columnName = "startTime";
        query.with(new Sort(Sort.Direction.DESC, columnName));
        // .exclude("roomId")
        query.fields().exclude("_id").exclude("appId").exclude("games");
        List<MRoom> roomList = mongoDao.find(query, MRoom.class, collectionName);
        handelRoomList(roomList, hostList);

        log.info(roomList.size() + "");

        List<RoomAndWin> roomAndWins = transFormData(roomList);
        json.put("roomList", roomAndWins);

        return json;
    }

    public JSONObject searchRoomList(String encodeData, Integer managerLevelId)
            throws UnsupportedEncodingException, ParseException {
        JSONObject json = new JSONObject();
        json.put("roomList", null);

        if ((encodeData == null) || (managerLevelId == null)) {
            return json;
        }

        List<GameHost> hostList = getHostList(managerLevelId);
        if ((hostList == null) || (hostList.size() < 1)) {
            return json;
        }

        String queryContentStr = URLDecoder.decode(encodeData, "utf-8");
        JSONObject queryContent = JSONObject.parseObject(queryContentStr);

        Integer pid = queryContent.getInteger("pid");
        Integer roomCode = queryContent.getInteger("roomCode");
        String hostId = queryContent.getString("hostId");
        String startTime = queryContent.getString("startTime");
        String endTime = queryContent.getString("endTime");

        Query query = new Query();
        if ((pid != null) && (!pid.equals(""))) {
            Criteria cri = Criteria.where("pid").is(pid);
            query.addCriteria(Criteria.where("players").elemMatch(cri));
        }

        if (hostId != null) {
            query.addCriteria(Criteria.where("hostId").is(hostId));
        }

        if ((roomCode != null) && (!roomCode.equals(""))) {
            query.addCriteria(Criteria.where("roomCode").is(roomCode));
        }

        if ((!startTime.equals("")) && (!endTime.equals(""))) {
            if (Integer.valueOf(startTime.replace("-", "")) <= Integer.valueOf(endTime.replace("-", ""))) {
                startTime = new StringBuffer(startTime).append(" 00:00:00").toString();
                endTime = new StringBuffer(endTime).append(" 23:59:59").toString();
            } else {
                startTime = new StringBuffer(endTime).append(" 00:00:00").toString();
                endTime = new StringBuffer(startTime).append(" 23:59:59").toString();
            }
        } else { // 不设条件, 只查询最近一周的房卡情况
            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.DATE, -7);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String startTime1 = sdf1.format(calendar1.getTime()).substring(0, 10);
            String todayTime = DateUtil.getSystemDate().substring(0, 10);

            startTime = new StringBuffer(startTime1).append(" 00:00:00").toString();
            endTime = new StringBuffer(todayTime).append(" 23:59:59").toString();
        }

        Criteria c = Criteria.where("startTime").gte(startTime).lte(endTime);
        query.addCriteria(c);

        String collectionName = "room";
        String columnName = "startTime";
        query.with(new Sort(Sort.Direction.DESC, columnName));
        query.fields().exclude("_id").exclude("appId").exclude("games"); // .exclude("roomId")

        List<MRoom> roomList = mongoDao.find(query, MRoom.class, collectionName);
        handelRoomList(roomList, hostList);

        log.info(roomList.size() + "");

        // 数据结构转换
        List<RoomAndWin> roomAndWins = transFormData(roomList);

        // 过滤掉该房间内的其他玩家
        if ((pid != null) && (!"".equals(pid))) {
            Iterator<RoomAndWin> iterator = roomAndWins.iterator();

            while (iterator.hasNext()) {
                RoomAndWin roomAndWin = iterator.next();
                if (!String.valueOf(roomAndWin.getfPid()).equals(String.valueOf(pid))) {
                    iterator.remove();
                    continue;
                }
            }
        }

        json.put("roomList", roomAndWins);

        return json;
    }

    /**
     * 运营商权限过滤
     *
     * @param roomList
     * @return
     */
    private void handelRoomList(List<MRoom> roomList, List<GameHost> hostList) {
        if (roomList == null) {
            return;
        }

        Iterator<MRoom> iterator = roomList.iterator();
        while (iterator.hasNext()) {
            MRoom room = iterator.next();
            int flag = 0;
            for (GameHost host : hostList) {
                if (host == null) {
                    break;
                }
                if ((host.hostId != null) && host.hostId.equals(room.getHostId())) {
                    room.setHostName(host.hostName);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                iterator.remove(); // 没有该游戏权限, 删除
            }
        }
    }

    //  类转换
    private List<RoomAndWin> transFormData(List<MRoom> roomList) {
        List<RoomAndWin> roomAndWinList = new ArrayList<>();

        if (roomList == null) {
            return roomAndWinList;
        }

        for (MRoom room : roomList) {
            if (room.getPlayers() == null) {
                return roomAndWinList;
            }

            List<MGame> mGames = showGameDetail(room.getRoomId());

            for (MRoomPlayer roomPlayer : room.getPlayers()) {
                RoomAndWin roomAndWin = new RoomAndWin();

                // 赋值
                roomAndWin.setfPid(BigInteger.valueOf(roomPlayer.getPid()));    // 玩家id
                roomAndWin.setfExt640(Long.valueOf(room.getRoomCode()));        // 房间id
                roomAndWin.setfExt641(Long.valueOf(mGames.size()));             // 游戏次数
                roomAndWin.setfExt642(Long.valueOf(roomPlayer.getAllGoal()));   // 当前局数输赢钱数
                roomAndWin.setGametype(room.getHostName());         // 游戏类型
                roomAndWin.setInserttime(room.getStartTime());      // 记录生成时间

                roomAndWinList.add(roomAndWin);
            }
        }

        return roomAndWinList;
    }

    public List<MGame> showGameDetail(String roomId) {
        List<MGame> gameDetail = new ArrayList<>();

        if ((roomId == null)) {
            return gameDetail;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId));

        String collectionName = "room";
        query.fields().include("games.goals.pid");
        query.fields().include("games.goals.goal");
        MRoom room = mongoDao.findOne(query, collectionName, MRoom.class);

        gameDetail = room.getGames();

        log.info(gameDetail.toString());

        return gameDetail;
    }

}
