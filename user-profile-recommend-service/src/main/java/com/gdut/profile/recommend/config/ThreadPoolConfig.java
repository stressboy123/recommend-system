package com.gdut.profile.recommend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liujunliang
 * @date 2025/12/7
 */
@Configuration
@EnableAsync // 开启异步支持
public class ThreadPoolConfig {

    /**
     * 用户画像更新专用线程池
     */
    @Bean(name = "userProfileExecutor")
    public Executor userProfileExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数：IO密集型任务（调用DB/远程接口）设为 2*CPU核心数+1
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        executor.setCorePoolSize(corePoolSize);
        // 最大线程数：根据业务峰值调整（如200）
        executor.setMaxPoolSize(200);
        // 队列容量：缓冲待执行任务（避免瞬间打满线程池）
        executor.setQueueCapacity(1000);
        // 线程名前缀：便于日志排查
        executor.setThreadNamePrefix("user-profile-");
        // 线程空闲超时时间：60秒
        executor.setKeepAliveSeconds(60);
        // 拒绝策略：超出最大线程+队列时，由调用线程执行（避免任务直接丢弃）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化线程池
        executor.initialize();
        return executor;
    }
}
