package com.gdut.behaviordata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liujunliang
 * @date 2025/11/28
 */
@SpringBootApplication
@EnableDiscoveryClient // 启用Nacos服务注册与发现
public class BehaviorDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(BehaviorDataApplication.class, args);
    }
}
