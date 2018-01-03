package com.haoyun.manager;

public class ManagerLPriInfo {
    public Integer managerLevelId;
    public String  privilegeName;
    public Integer privilege;

    public Integer getManagerLevelId() {
        return managerLevelId;
    }

    public void setManagerLevelId(Integer managerLevelId) {
        this.managerLevelId = managerLevelId;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }
}
