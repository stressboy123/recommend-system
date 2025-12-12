package com.gdut.gateway;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liujunliang
 * @date 2025/11/28
 */
@SpringBootApplication
@EnableDiscoveryClient // 启用Nacos服务发现（网关需找到后端微服务）
@SentinelResource // 启用Sentinel网关限流
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
