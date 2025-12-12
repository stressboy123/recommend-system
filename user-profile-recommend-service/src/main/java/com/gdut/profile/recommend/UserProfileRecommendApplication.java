package com.gdut.profile.recommend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liujunliang
 * @date 2025/11/28
 */
@SpringBootApplication
@EnableDiscoveryClient // 启用Nacos服务注册与发现
@EnableFeignClients(basePackages = "com.gdut.profile.recommend.fegin")
public class UserProfileRecommendApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProfileRecommendApplication.class, args);
    }
}
