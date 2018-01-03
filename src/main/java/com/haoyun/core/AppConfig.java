package com.haoyun.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * spring context 的配置文件
 *
 * Created by Green Lei on 2015/10/20 10:07.
 */
@Configuration
@EnableWebMvc
@ImportResource({"classpath*:/applicationContext.xml"})
public class AppConfig {
}
