package com.haoyun.gameQuery.roomWin;

/**
 * Created by songcz on 2017/4/22.
 */
public class MRoomPlayer {
    private Integer pid;
    private String name;
    private String headPortrait; // 本地存储的地址
    private Integer allGoal;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getHeadPortrait() {
        return headPortrait;
    }
    
    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }
    
    public Integer getAllGoal() {
        return allGoal;
    }
    
    public void setAllGoal(Integer allGoal) {
        this.allGoal = allGoal;
    }
}
