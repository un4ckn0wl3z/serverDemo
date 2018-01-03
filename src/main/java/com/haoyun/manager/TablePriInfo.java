package com.haoyun.manager;

/**
 * 表字段权限
 * game table privilege info
 */
public class TablePriInfo {
    public Integer managerLevelId;
    public String  tableId;  //
    public Integer privilege;   // 权限: 0: 无, 1: 有

    public Integer getManagerLevelId() {
        return managerLevelId;
    }

    public void setManagerLevelId(Integer managerLevelId) {
        this.managerLevelId = managerLevelId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }
}
