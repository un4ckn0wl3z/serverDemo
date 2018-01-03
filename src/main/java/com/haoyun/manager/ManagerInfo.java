package com.haoyun.manager;

public class ManagerInfo {
    public Integer managerId;
    public String managerName;
    public String nickName;
    public String seed;
    public String password;
    public Integer managerLevelId;
    public String managerLevelName;


    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getManagerLevelId() {
        return managerLevelId;
    }

    public void setManagerLevelId(Integer managerLevelId) {
        this.managerLevelId = managerLevelId;
    }

    public String getManagerLevelName() {
        return managerLevelName;
    }

    public void setManagerLevelName(String managerLevelName) {
        this.managerLevelName = managerLevelName;
    }
}
