package com.hy.lwmsbackend.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author arainetion
 * @version 1.0
 * @date 2023/3/13 15:15
 * @Description
 */
@Slf4j
@Configuration
//解决Failed to start bean 'documentationPluginsBootstrapper‘ 但是会遇到No mapping for GET /lwms/doc.html
//@EnableWebMvc
@EnableOpenApi
@EnableKnife4j
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String application;

    @Bean
    public Docket docket() {


        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                //RequestHandlerSelectors配置要扫播接口的方式
                //basePackage指定要扫描的包 RequestHandlerSelectors.basePackage("com.zhao.swagger.controller")
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //paths()过滤什么路径
                .paths(PathSelectors.any())
                .build()
                .groupName("hy");

    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title(application)
                .description(application + "接口文档")
                .contact(new Contact("hy","地址","1052586315@qq.com"))
                .version("1.0")
                .build();
    }


}
