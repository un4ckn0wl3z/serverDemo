package com.haoyun.gameSet.horseRaceLamp;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping(value = "horseRaceLamp")
public class HorseRLController extends AbstractController<HorseRLService> {

    @Override
    @Inject
    @Named("horseRLService")
    protected void setService(HorseRLService service) {
        this.service = service;
    }

    @RequestMapping("/getHorseRLampList")
    @ResponseBody
    public String getHorseRLampList(Integer managerLevelId, String callback) {
        JSONObject jsonObject = service.getHorseRLampList(managerLevelId);
        return callback + "(" + jsonObject + ")";
    }

    @RequestMapping("/submitHorseRLamp")
    @ResponseBody
    public String submitHorseRLamp(String encodeData, Integer managerLevelId, String callback) throws UnsupportedEncodingException, InterruptedException {
        JSONObject jsonObject = service.submitHorseRLamp(encodeData, managerLevelId);

        return callback + "(" + jsonObject + ")";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(String encodeData, String callback) throws UnsupportedEncodingException {
        JSONObject jsonObject = service.delete(encodeData);

        return callback + "(" + jsonObject + ")";
    }

    @RequestMapping("/operaHorse")
    @ResponseBody
    public String operaHorse(String encodeList, String isEnable, String callback) throws UnsupportedEncodingException {
        JSONObject jsonObject = service.operaHorse(encodeList, isEnable);

        return callback + "(" + jsonObject + ")";
    }
}
