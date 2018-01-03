package com.haoyun.manager;

/**
 * game privilege info
 */
public class GamePriInfo {
    public Integer managerLevelId;
    public String  gameId;
    public Integer privilege;

    public Integer getManagerLevelId() {
        return managerLevelId;
    }

    public void setManagerLevelId(Integer managerLevelId) {
        this.managerLevelId = managerLevelId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }
}
