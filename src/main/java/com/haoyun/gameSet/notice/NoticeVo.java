package com.haoyun.gameSet.notice;

/**
 * 公告展示类
 *
 * @author Administrator
 *         Created on 2018/1/3
 */
public class NoticeVo {
    private String fId;

    private Long fEnable;

    private String fAppName;

    private String fText;

    private String fViewType;

    private String fOkJump;

    private String fInserttime;

    private String startTime;

    private String endTime;

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public Long getfEnable() {
        return fEnable;
    }

    public void setfEnable(Long fEnable) {
        this.fEnable = fEnable;
    }

    public String getfAppName() {
        return fAppName;
    }

    public void setfAppName(String fAppName) {
        this.fAppName = fAppName;
    }

    public String getfText() {
        return fText;
    }

    public void setfText(String fText) {
        this.fText = fText;
    }

    public String getfViewType() {
        return fViewType;
    }

    public void setfViewType(String fViewType) {
        this.fViewType = fViewType;
    }

    public String getfOkJump() {
        return fOkJump;
    }

    public void setfOkJump(String fOkJump) {
        this.fOkJump = fOkJump;
    }

    public String getfInserttime() {
        return fInserttime;
    }

    public void setfInserttime(String fInserttime) {
        this.fInserttime = fInserttime;
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
}
