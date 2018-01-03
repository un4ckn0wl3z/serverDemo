package com.haoyun.gameSet.notice;

/**
 * Created by Administrator on 2017/5/12.
 */
public class Notice {
    public String  noticeId;     // 公告id
    public String  content;      // 公告内容
    public String  noticeUrl;    // 公告链接
    public String  sender;       // 发起者

    public String  enableTime;   // 启用时间
    public String  ineffectTime; // 失效时间
    public String  createTime;   // 创建时间

    public Integer isEnable;     // 是否启用

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }
}
