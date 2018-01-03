package com.haoyun.manager;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping(value = "manager")
public class ManagerController extends AbstractController<ManagerService> {

    @Override
    @Inject
    @Named("managerService")
    protected void setService(ManagerService service) {
        this.service = service;
    }

    @RequestMapping("/getMenuInfo")
    @ResponseBody
    public JSONObject getMenuInfo() {
        return service.getMenuInfo();
    }

    @RequestMapping("/getGameHostInfoList")
    @ResponseBody
    public JSONObject getGameHostInfoList() {
        return service.getGameHostInfoList();
    }

    @RequestMapping("/getTableInfoList")
    @ResponseBody
    public JSONObject getTableInfoList() {
        return service.getTableInfoList();
    }

    @RequestMapping("/managerInfoList")
    @ResponseBody
    public String getManagerInfoList(String callback) {
        List<ManagerInfo> managerInfoList = service.getManagerInfoList();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("managerInfoList",managerInfoList);

        return callback + "(" + jsonObject.toJSONString() + ")";
    }

    @RequestMapping("/submit")
    @ResponseBody
    public JSONObject submit(String encodeManagerInfo) throws UnsupportedEncodingException {
        return service.submit(encodeManagerInfo);
    }

    @RequestMapping("/deleteManager")
    @ResponseBody
    public JSONObject deleteManager(String encodeManagerId) throws UnsupportedEncodingException {
        return service.deleteManager(encodeManagerId);
    }

    @RequestMapping("/getManagerLevelInfoList")
    @ResponseBody
    public JSONObject getManagerLevelInfoList() {
        return service.getManagerLevelInfoList();
    }

    @RequestMapping("/setManagerLevelInfo")
    @ResponseBody
    public JSONObject setManagerLevelInfo(String encodeManagerLevelInfo) throws UnsupportedEncodingException {
        return service.setManagerLevelInfo(encodeManagerLevelInfo);
    }

    @RequestMapping("/deleteManagerLevel")
    @ResponseBody
    public JSONObject deleteManagerLevel(Integer managerLevelId) {
        return service.deleteManagerLevel(managerLevelId);
    }

    @RequestMapping("/getLevelPrivilege")
    @ResponseBody
    public JSONObject getLevelPrivilege(Integer managerLevelId) throws UnsupportedEncodingException {
        return service.getLevelPrivilege(managerLevelId);
    }

    @RequestMapping(value = "/getPriMenuInfoList")
    @ResponseBody
    public String getPriMenuInfoList(String managerLevelId, String callback) throws UnsupportedEncodingException,
            IllegalAccessException, SQLException, InstantiationException {
        JSONObject jsonObject = service.getPriMenuInfoList(managerLevelId);

        return callback + "(" + jsonObject.toJSONString() + ")";
    }

    @RequestMapping("/getGamesInfoList")
    @ResponseBody
    public List<Game> getGamesInfoList() {
        return service.getGamesInfoList();
    }

    @RequestMapping("/submitLMenuPriInfo")
    @ResponseBody
    public JSONObject submitLMenuPriInfo(Integer managerLevelId, String encodeMenuPriInfo,
                                         String encodeGamePriInfo, String encodeGameHostPriInfo,
                                         String encodeTablePriInfo)
            throws UnsupportedEncodingException {
        return service.submitLMenuPriInfo(managerLevelId, encodeMenuPriInfo, encodeGamePriInfo,
                encodeGameHostPriInfo, encodeTablePriInfo);
    }

    @RequestMapping("/getTablePriInfo")
    @ResponseBody
    public JSONObject getTablePriInfo(Integer managerLevelId) throws UnsupportedEncodingException {
        return service.getTablePriInfo(managerLevelId);
    }
}
