package com.haoyun.quotaMgt;

public class QuotaMgt {
    public Integer managerId;
    public String  managerName;
    public Integer rcQuota;
    public Integer goldQuota;

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

    public Integer getRcQuota() {
        return rcQuota;
    }

    public void setRcQuota(Integer rcQuota) {
        this.rcQuota = rcQuota;
    }

    public Integer getGoldQuota() {
        return goldQuota;
    }

    public void setGoldQuota(Integer goldQuota) {
        this.goldQuota = goldQuota;
    }
}
