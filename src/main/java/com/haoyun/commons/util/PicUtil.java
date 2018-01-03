package com.haoyun.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by songcz on 2017/4/21.
 */
public class PicUtil {

    private static final Logger log = LoggerFactory.getLogger(PicUtil.class);

    public static String saveHeadPortraitInServer(String webImgUrl, String fileName) {

        String fileFullPath = "";
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            fileFullPath = PropertiesUtil.getString("headPortrait_url_win");
        } else {
            fileFullPath = PropertiesUtil.getString("headPortrait_url_linux");
        }
        File dir = new File(fileFullPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        fileFullPath = fileFullPath + fileName + ".png";

        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url;
        try {
            url = new URL(webImgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(fileFullPath);
            int size = 0;
            int BUFFER_SIZE = 1024;
            byte[] buf = new byte[BUFFER_SIZE];
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信头像下载失败，返回默认头像");
            return "1";
        } finally {
            try {
                fos.close();
                bis.close();
                httpUrl.disconnect();
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }
        return "https://hygg.3399hy.com/pic_resource/HY_NJ_GD/" + fileName + ".png";
    }
}
