package com.gdut.system.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author liujunliang
 * @date 2025/11/28
 */
@SpringBootApplication
@EnableDiscoveryClient // 启用Nacos服务注册与发现
public class SystemManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemManagementApplication.class, args);
    }
}
