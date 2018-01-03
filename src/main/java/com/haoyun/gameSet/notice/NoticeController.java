package com.haoyun.gameSet.notice;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;


/**
 * @author Administrator
 */
@Controller
@RequestMapping(value = "notice")
public class NoticeController extends AbstractController<NoticeService> {

    @Override
    @Inject
    @Named("noticeService")
    protected void setService(NoticeService service) {
        this.service = service;
    }

    @RequestMapping("/getNoticeList")
    @ResponseBody
    public String getNoticeList(Integer managerLevelId, String callback) {
        JSONObject jsonObject = service.getNoticeList(managerLevelId);

        return callback + "(" + jsonObject + ")";
    }

    @RequestMapping("/submitNotice")
    @ResponseBody
    public String submitNotice(String encodeData, Integer managerLevelId, String callback)
            throws UnsupportedEncodingException, InterruptedException {
        JSONObject jsonObject = service.submitNotice(encodeData, managerLevelId);

        return callback + "(" + jsonObject + ")";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(String noticeId, String callback) {
        JSONObject jsonObject = service.delete(noticeId);

        return callback + "(" + jsonObject + ")";
    }
}
