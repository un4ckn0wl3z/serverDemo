package com.haoyun.gameQuery.roomWin;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * @author Administrator
 *         Created on 2017/12/26
 */
@Controller
@RequestMapping(value = "roomWin")
public class RoomWinController extends AbstractController<RoomWinService> {

    @Override
    @Autowired
    protected void setService(RoomWinService service) {
        this.service = service;
    }

    @RequestMapping("/getRoomList")
    @ResponseBody
    public String getRoomList(Integer managerLevelId, String callback) {
        return callback + "(" + service.getRoomList(managerLevelId) + ")";
    }

    @RequestMapping(value = "searchRoomList")
    @ResponseBody
    public String searchRoomList(String encodeData, Integer managerLevelId, String callback)
            throws UnsupportedEncodingException, ParseException {
        JSONObject jsonObject = service.searchRoomList(encodeData, managerLevelId);

        return callback + "(" + jsonObject + ")";
    }

}
