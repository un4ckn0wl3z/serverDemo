package com.haoyun.core;

import javax.servlet.ServletException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AppInit {

    public static Properties config = new Properties();

    public static void main(String[] args) throws InterruptedException, ServletException {

        try {
            // 读取jar外的配置文件
            InputStream in = new BufferedInputStream(new FileInputStream("conf/conf.properties"));
            config.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new HttpServer().start();
    }
}