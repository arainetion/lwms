package com.hy.lwmsbackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * @author arainetion
 * @version 1.0
 * @date 2023/3/13 21:22
 * @Description 控制台输出Swagger
 */

@Slf4j
@Component
public class SwaggerPrintConfig implements ApplicationListener<WebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {

        try {
            //获取ip
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            //获取端口号
            int port = event.getWebServer().getPort();
            //获取应用名
            String applicationName = event.getApplicationContext().getApplicationName();
            //打印swagger文档地址
            log.info("项目启动成功！swagger接口文档地址：http://"+hostAddress+":"+port+applicationName+"/"+"doc.html");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
