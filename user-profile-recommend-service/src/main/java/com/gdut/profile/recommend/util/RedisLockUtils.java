package com.gdut.profile.recommend.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author liujunliang
 * @date 2025/12/6
 */
@Component
public class RedisLockUtils {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 尝试获取分布式锁（核心方法）
     * @param lockKey 业务锁键（如：userId:actionType）
     * @param waitTime 最大等待时间（超时则放弃获取锁）
     * @param leaseTime 锁自动释放时间（Redisson看门狗会自动续期，建议设30s+）
     * @param timeUnit 时间单位
     * @return true=获取锁成功，false=获取失败
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit) {
        if (lockKey == null || lockKey.isEmpty()) {
            throw new IllegalArgumentException("锁键不能为空");
        }
        // 获取Redisson可重入锁
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 尝试加锁：waitTime内获取不到则返回false，获取到则持有leaseTime（看门狗自动续期）
            // 注：若leaseTime设为-1，Redisson会启用默认看门狗（30s过期，每10s续期一次）
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            // 恢复线程中断状态，避免丢失中断信号
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 重载方法：默认时间单位（秒），简化调用
     * @param lockKey 业务锁键
     * @param waitTime 最大等待时间（秒）
     * @param leaseTime 锁自动释放时间（秒）
     * @return true=获取成功
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        return tryLock(lockKey, waitTime, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * 释放分布式锁（安全释放，避免误删其他线程的锁）
     * @param lockKey 业务锁键
     */
    public void unlock(String lockKey) {
        if (lockKey == null || lockKey.isEmpty()) {
            return;
        }
        RLock lock = redissonClient.getLock(lockKey);

        // 仅释放当前线程持有的锁（核心：防止误解锁）
        if (lock.isHeldByCurrentThread()) {
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                // 忽略“未持有锁却解锁”的异常（如锁已自动过期）
                e.printStackTrace();
            }
        }
    }
}
