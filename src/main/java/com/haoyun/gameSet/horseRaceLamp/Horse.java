package com.haoyun.gameSet.horseRaceLamp;

/**
 * 跑马灯
 */
public class Horse {
    public String  horseId;      // 跑马灯id
    public String  sender;       // 发起者
    public String  enableTime;   // 启用时间
    public String  ineffectTime; // 失效时间
    public String  sendTime;     // 创建时间
    public Integer perWaveInteval; // 每波间隔
    public Integer intervalTime;   // 每个跑马灯间隔秒数
    public Integer perWaveNumber;  // 每波次数
    public Integer isEnable;     // 是否启用
    public String  content;      // 跑马灯内容

    public String getHorseId() {
        return horseId;
    }

    public void setHorseId(String horseId) {
        this.horseId = horseId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(String enableTime) {
        this.enableTime = enableTime;
    }

    public String getIneffectTime() {
        return ineffectTime;
    }

    public void setIneffectTime(String ineffectTime) {
        this.ineffectTime = ineffectTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getPerWaveInteval() {
        return perWaveInteval;
    }

    public void setPerWaveInteval(Integer perWaveInteval) {
        this.perWaveInteval = perWaveInteval;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public Integer getPerWaveNumber() {
        return perWaveNumber;
    }

    public void setPerWaveNumber(Integer perWaveNumber) {
        this.perWaveNumber = perWaveNumber;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
