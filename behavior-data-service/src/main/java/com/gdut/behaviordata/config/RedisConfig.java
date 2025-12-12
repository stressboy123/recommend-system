package com.gdut.behaviordata.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Configuration
public class RedisConfig {

    // 从yml读取Redis基础配置
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.database}")
    private int redisDatabase;

    /**
     * 创建RedissonClient Bean（单机模式）
     */
    @Bean(destroyMethod = "shutdown") // 容器销毁时关闭RedissonClient
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 1. 单机模式配置（基础场景）
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort) // Redis地址（必须以redis://开头）
                .setDatabase(redisDatabase) // 数据库索引
                .setConnectionPoolSize(10) // 连接池大小（默认64，按需调整）
                .setConnectionMinimumIdleSize(5); // 最小空闲连接数

        // 有密码则设置
        if (!redisPassword.isEmpty()) {
            singleServerConfig.setPassword(redisPassword);
        }

        // ========== 集群模式配置（按需替换单机配置） ==========
        // config.useClusterServers()
        //         .addNodeAddress("redis://192.168.1.1:6379", "redis://192.168.1.2:6379")
        //         .setPassword(redisPassword)
        //         .setScanInterval(2000); // 集群节点扫描间隔

        // ========== 哨兵模式配置（按需替换） ==========
        // config.useSentinelServers()
        //         .setMasterName("mymaster")
        //         .addSentinelAddress("redis://192.168.1.3:26379", "redis://192.168.1.4:26379")
        //         .setPassword(redisPassword);

        return Redisson.create(config);
    }
}
