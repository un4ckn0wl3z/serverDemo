package com.haoyun.quotaMgt;

public class QuotaRecord {
    public Integer managerId;
    public String  managerName;
    public Integer rcQuota;
    public Integer goldQuota;
    public Integer quotaMgrId;
    public String quotaMgrName;
    public String  operaTime;

    public Integer getQuotaMgrId() {
        return quotaMgrId;
    }

    public void setQuotaMgrId(Integer quotaMgrId) {
        this.quotaMgrId = quotaMgrId;
    }

    public String getQuotaMgrName() {
        return quotaMgrName;
    }

    public void setQuotaMgrName(String quotaMgrName) {
        this.quotaMgrName = quotaMgrName;
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

    public String getOperaTime() {
        return operaTime;
    }

    public void setOperaTime(String operaTime) {
        this.operaTime = operaTime;
    }
}
