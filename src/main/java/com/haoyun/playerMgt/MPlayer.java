package com.haoyun.playerMgt;

/**
 * Author: songcz
 */
public class MPlayer {
    private String appId; // appId "HY_NJ_GD"
    private String hostId; // hostId 运营者ID "HY"
    private String hostName;
    private String channelId; // 发行渠道ID "APPSTORE"

    private String  uuid; // 设备唯一标识
    private Long    pid;
    private String  name;
    private String  loginType; // 登录类型（NORMAL,YK)
    private String  token;
    private String  openId; // 微信openId
    private String  refreshToken; // 微信refreshToken
    private Integer sex;
    private String  headimgurl;   // 微信给的地址
    private String  headPortrait; // 本地存储的地址
    private Integer roomCardNum; // 房卡数量
    private Integer money;       // 金币
    private Integer diamond;     // 钻石
    private String  phone;
    private String  password;
    private Integer level;    // 等级
    private String  creatTime; // 注册时间
    private String  ip;
    private Integer port;
    private String  lastOnlineTime;  // 最近登录时间
    private String  ipAds;           // 最后登录地址
    private Integer state;          // 0:空闲；1:战斗 2:退出  ---弃用
    private Integer roomCode;       // 正在战斗的房间code
    private Integer accountStatus;  // 账号状态 0：正常；1：冻结
    private String  unfreezeTime;    // 解冻时间 yyyy-MM-dd HH:mm:ss

    private String  totalPlayerTime;    // 玩家总时长 HH:mm:ss

    public String getTotalPlayerTime() {
        return totalPlayerTime;
    }

    public void setTotalPlayerTime(String totalPlayerTime) {
        this.totalPlayerTime = totalPlayerTime;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    // todo
    private String totalGameTime; // 游戏总时长: totalGameTime

    public String getIpAds() {
        return ipAds;
    }

    public void setIpAds(String ipAds) {
        this.ipAds = ipAds;
    }

    public String getTotalGameTime() {
        return totalGameTime;
    }

    public void setTotalGameTime(String totalGameTime) {
        this.totalGameTime = totalGameTime;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Integer getRoomCardNum() {
        return roomCardNum;
    }

    public void setRoomCardNum(Integer roomCardNum) {
        this.roomCardNum = roomCardNum;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(String lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(Integer roomCode) {
        this.roomCode = roomCode;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getUnfreezeTime() {
        return unfreezeTime;
    }

    public void setUnfreezeTime(String unfreezeTime) {
        this.unfreezeTime = unfreezeTime;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }
}