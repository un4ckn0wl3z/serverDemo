package com.haoyun.gameQuery.roomWin;

import java.util.List;

/**
 * Author: Administrator
 * Created by: ModelGenerator on 2017/4/14
 */
public class MRoom {
    private Integer roomCode;
    private String  roomId;  // 房间Id, 唯一, 回放码
    private String  hostId;  // hostId 运营者ID "HY"
    private String  hostName;
    private String  startTime;
    private String  endTime;
    private Integer gameNum;
    private Integer gameLastNum;// 房间剩余局数
    private Integer payType;
    private List<MRoomPlayer> players;
    private Integer createrPay;
    private Integer otherPay;
    private List<Long> readyPlayer;
    // private Long    createrPid;
    private List<MGame> games;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public List<MGame> getGames() {
        return games;
    }

    public void setGames(List<MGame> games) {
        this.games = games;
    }

    public List<Long> getReadyPlayer() {
        return readyPlayer;
    }

    public void setReadyPlayer(List<Long> readyPlayer) {
        this.readyPlayer = readyPlayer;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(Integer roomCode) {
        this.roomCode = roomCode;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getGameNum() {
        return gameNum;
    }

    public void setGameNum(Integer gameNum) {
        this.gameNum = gameNum;
    }

    public Integer getGameLastNum() {
        return gameLastNum;
    }

    public void setGameLastNum(Integer gameLastNum) {
        this.gameLastNum = gameLastNum;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<MRoomPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<MRoomPlayer> players) {
        this.players = players;
    }

    public Integer getCreaterPay() {
        return createrPay;
    }

    public void setCreaterPay(Integer createrPay) {
        this.createrPay = createrPay;
    }

    public Integer getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(Integer otherPay) {
        this.otherPay = otherPay;
    }
}