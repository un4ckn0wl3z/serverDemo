package com.haoyun.gameQuery.roomWin;

import java.util.List;

/**
 * Author: Administrator
 * Created by: ModelGenerator on 2017/4/14
 */
public class MGame {
    private String gameId;
    private List<MGoal> goals;
    private String fightingLog;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public List<MGoal> getGoals() {
        return goals;
    }

    public void setGoals(List<MGoal> goals) {
        this.goals = goals;
    }

    public String getFightingLog() {
        return fightingLog;
    }

    public void setFightingLog(String fightingLog) {
        this.fightingLog = fightingLog;
    }

}