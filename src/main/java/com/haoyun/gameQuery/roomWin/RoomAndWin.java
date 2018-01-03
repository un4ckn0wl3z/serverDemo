package com.haoyun.gameQuery.roomWin;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Administrator
 * @date 2017/12/26
 */
public class RoomAndWin implements Serializable {

    private Long fId; //

    private BigInteger fPid;//玩家ID

    private Integer fDotId;//

    private long fInsTime;//

    private String fNote;//

    private String fExtstr0;//

    private Long fExt640;//房间ID

    private Long fExt641;//游戏次数

    private Long fExt642;//当前局输赢钱数

    private Long fExt643;//是否胜利

    private Long fExt644;//当前局数据钱数

    private Long fExt645;//

    private Long fExt646;//

    private Long fExt647;//

    private Long fExt648;//

    private Long fExt649;//

    private String inserttime;//记录生成时间

    private String gametype;//

    private String iswin;//

    private String ids;//

    private String fRecordId;//游戏回访码



    private static final long serialVersionUID = 1L;

    public Long getfId() {
        return fId;
    }

    public void setfId(Long fId) {
        this.fId = fId;
    }

    public String getIds() {
        return ids;
    }

    public String getIswin() {
        return iswin;
    }

    public void setIswin(String iswin) {
        this.iswin = iswin;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getGametype() {
        return gametype;
    }

    public void setGametype(String gametype) {
        this.gametype = gametype;
    }

    public BigInteger getfPid() {
        return fPid;
    }

    public void setfPid(BigInteger fPid) {
        this.fPid = fPid;
    }

    public Integer getfDotId() {
        return fDotId;
    }

    public void setfDotId(Integer fDotId) {
        this.fDotId = fDotId;
    }

    public long getfInsTime() {
        return fInsTime;
    }

    public void setfInsTime(long fInsTime) {
        this.fInsTime = fInsTime;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public String getfNote() {
        return fNote;
    }

    public void setfNote(String fNote) {
        this.fNote = fNote == null ? null : fNote.trim();
    }

    public String getfExtstr0() {
        return fExtstr0;
    }

    public void setfExtstr0(String fExtstr0) {
        this.fExtstr0 = fExtstr0 == null ? null : fExtstr0.trim();
    }

    public Long getfExt640() {
        return fExt640;
    }

    public void setfExt640(Long fExt640) {
        this.fExt640 = fExt640;
    }

    public Long getfExt641() {
        return fExt641;
    }

    public void setfExt641(Long fExt641) {
        this.fExt641 = fExt641;
    }

    public Long getfExt642() {
        return fExt642;
    }

    public void setfExt642(Long fExt642) {
        this.fExt642 = fExt642;
    }

    public Long getfExt643() {
        return fExt643;
    }

    public void setfExt643(Long fExt643) {
        this.fExt643 = fExt643;
    }

    public Long getfExt644() {
        return fExt644;
    }

    public void setfExt644(Long fExt644) {
        this.fExt644 = fExt644;
    }

    public Long getfExt645() {
        return fExt645;
    }

    public void setfExt645(Long fExt645) {
        this.fExt645 = fExt645;
    }

    public Long getfExt646() {
        return fExt646;
    }

    public void setfExt646(Long fExt646) {
        this.fExt646 = fExt646;
    }

    public Long getfExt647() {
        return fExt647;
    }

    public void setfExt647(Long fExt647) {
        this.fExt647 = fExt647;
    }

    public Long getfExt648() {
        return fExt648;
    }

    public void setfExt648(Long fExt648) {
        this.fExt648 = fExt648;
    }

    public Long getfExt649() {
        return fExt649;
    }

    public void setfExt649(Long fExt649) {
        this.fExt649 = fExt649;
    }

    public String getfRecordId() {
        return fRecordId;
    }

    public void setfRecordId(String fRecordId) {
        this.fRecordId = fRecordId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fId=").append(fId);
        sb.append(", fPid=").append(fPid);
        sb.append(", fDotId=").append(fDotId);
        sb.append(", fInsTime=").append(fInsTime);
        sb.append(", fNote=").append(fNote);
        sb.append(", fExtstr0=").append(fExtstr0);
        sb.append(", fExt640=").append(fExt640);
        sb.append(", fExt641=").append(fExt641);
        sb.append(", fExt642=").append(fExt642);
        sb.append(", fExt643=").append(fExt643);
        sb.append(", fExt644=").append(fExt644);
        sb.append(", fExt645=").append(fExt645);
        sb.append(", fExt646=").append(fExt646);
        sb.append(", fExt647=").append(fExt647);
        sb.append(", fExt648=").append(fExt648);
        sb.append(", fExt649=").append(fExt649);
        sb.append(", fRecordId=").append(fRecordId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
