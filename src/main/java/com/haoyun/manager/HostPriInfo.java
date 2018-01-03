package com.haoyun.manager;

/**
 * 游戏运营商权限
 * game host privilege info
 */
public class HostPriInfo {
    public Integer managerLevelId;
    public String  hostId;  // 游戏运营商id
    public Integer privilege;   // 权限: 0: 无, 1: 有

    public Integer getManagerLevelId() {
        return managerLevelId;
    }

    public void setManagerLevelId(Integer managerLevelId) {
        this.managerLevelId = managerLevelId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }
}
