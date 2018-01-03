package com.haoyun.gameSet.notice;

import com.alibaba.fastjson.JSONObject;
import com.haoyun.commons.base.AbstractService;
import com.haoyun.commons.util.DateUtil;
import com.haoyun.commons.util.IDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/12.
 */
@Service
public class NoticeService extends AbstractService {
    Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 获取公告列表
     *
     * @return
     */
    public JSONObject getNoticeList(Integer managerLevelId) {
        JSONObject json = new JSONObject();
        json.put("noticeList", null);

        Set<String> keys = redisDao.getKey("notice:*");

        List<Map<String, String>> noticeList = new ArrayList<>();
        int count = 0;
        for (String key : keys) {
            Map<String, String> map = redisDao.getMap(key);
            noticeList.add(map);

            count++;
            if (count > 99) {
                break;
            }
        }

        handelList(noticeList, managerLevelId);

        log.info(noticeList.size() + "");
        List<NoticeVo> noticeVoList = tranformData(noticeList);

        json.put("noticeList", noticeVoList);

        return json;
    }

    /**
     * 提交公告
     *
     * @param encodeData
     * @return
     */
    public JSONObject submitNotice(String encodeData, Integer managerLevelId) throws UnsupportedEncodingException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("result", false);

        if ((encodeData == null) || (managerLevelId == null)) {
            json.put("msg", "参数为空!");
            return json;
        }

        String emailDataStr = URLDecoder.decode(encodeData, "utf-8");
        JSONObject noticeJson = JSONObject.parseObject(emailDataStr);

        if (!vldAndAddHostId(json, noticeJson, managerLevelId)) {
            return json;
        }

        // 新增公告
        String noticeId = noticeJson.getString("noticeId");
        if (noticeId == null) {
            noticeId = IDUtil.gen("");
            noticeJson.put("noticeId", noticeId);
        }

        // 20170318 18141245
        String todayTime = DateUtil.getStringDate();
        todayTime = todayTime.substring(0, 4) + "-" + todayTime.substring(4, 6) + "-" +
                todayTime.substring(6, 8) + " " + todayTime.substring(8, 10) + ":" +
                todayTime.substring(10, 12) + ":" + todayTime.substring(12, 14);
        noticeJson.put("createTime", todayTime);

        redisDao.addHash("notice:" + noticeId, noticeJson);

        json.put("result", true);
        return json;
    }

    /**
     * 删除公告
     *
     * @param noticeId
     * @return
     */
    public JSONObject delete(String noticeId) {
        JSONObject json = new JSONObject();
        json.put("result", false);

        if (noticeId == null) {
            return json;
        }
        redisDao.delete("notice:" + noticeId);
        log.info("delete noticeId为： " + noticeId);

        json.put("result", true);
        return json;
    }

    private List<NoticeVo> tranformData(List<Map<String, String>> noticeList) {
        List<NoticeVo> noticeVoList = new ArrayList<>();

        if (noticeList == null || noticeList.size() == 0) {
            return noticeVoList;
        }

        for (Map notice : noticeList) {
            NoticeVo noticeVo = new NoticeVo();

            noticeVo.setfId((String) notice.get("noticeId"));
            noticeVo.setStartTime((String) notice.get("enableTime"));
            noticeVo.setEndTime((String) notice.get("ineffectTime"));
            noticeVo.setfEnable(Long.valueOf((String) notice.get("isEnable")));
            noticeVo.setfText((String) notice.get("content"));
            noticeVo.setfOkJump((String) notice.get("noticeUrl"));
            noticeVo.setfInserttime((String) notice.get("createTime"));

            noticeVoList.add(noticeVo);
        }

        return noticeVoList;
    }
}
