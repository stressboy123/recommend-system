package com.gdut.profile.recommend.xxljob;

import com.gdut.profile.recommend.entity.Result;
import com.gdut.profile.recommend.fegin.SystemFeignClient;
import com.gdut.profile.recommend.service.UserProfileService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Component
public class ProfileUpdateJob {
    @Resource
    UserProfileService userProfileService;
    /**
     * 画像更新Job执行方法：XXL-JOB 配置定时任务（每小时执行），批量处理近 1 小时无行为更新的用户画像，避免 “冷用户” 画像过期。
     */
    @XxlJob("profileUpdateJobHandler")
    public void executeProfileUpdate() {
        XxlJobHelper.log("===== 开始执行用户画像更新任务 =====");
        // 存储异步任务结果，用于等待全部完成
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        try {
            // 1. 获取需要更新的用户列表
            Result<List<String>> result = userProfileService.listNeedFullUpdateUserIds();
            if (!result.isSuccess()) {
                XxlJobHelper.log("===== 用户画像更新任务执行失败 =====");
                XxlJobHelper.handleFail("画像更新失败：" + result.getMsg());
                return;
            }
            List<String> userIds = result.getData();
            if (userIds.isEmpty()) {
                XxlJobHelper.log("定时任务执行完成，暂无用户需要更新");
                XxlJobHelper.handleSuccess("画像更新完成");
                return;
            }
            XxlJobHelper.log("本次需更新用户数：{}", userIds.size());

            // 2. 分批提交异步任务（每50个一批，避免瞬间打满线程池）
            int batchSize = 50;
            for (int i = 0; i < userIds.size(); i += batchSize) {
                int endIndex = Math.min(i + batchSize, userIds.size());
                List<String> batchUserIds = userIds.subList(i, endIndex);
                // 提交当前批次的异步任务
                for (String userId : batchUserIds) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(
                            () -> userProfileService.updateUserProfileFullAsync(userId)
                    );
                    futureList.add(future);
                }
            }

            // 3. 等待所有异步任务完成（核心：避免任务提前结束）
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

            // 4. 检查是否有任务失败
            boolean hasFail = false;
            for (CompletableFuture<Void> future : futureList) {
                try {
                    future.get(); // 触发异常捕获
                } catch (InterruptedException | ExecutionException e) {
                    hasFail = true;
                    XxlJobHelper.log("异步任务失败：{}", e.getCause().getMessage());
                }
            }

            // 5. 标记任务最终状态
            if (hasFail) {
                XxlJobHelper.log("定时任务执行完成，共处理{}个用户，部分更新失败", userIds.size());
                XxlJobHelper.handleFail("画像更新完成，但部分用户更新失败");
            } else {
                XxlJobHelper.log("定时任务执行完成，共处理{}个用户，全部更新成功", userIds.size());
                XxlJobHelper.handleSuccess("画像更新完成，全部用户更新成功");
            }

        } catch (Exception e) {
            XxlJobHelper.log("===== 用户画像更新任务执行失败 =====", e);
            XxlJobHelper.handleFail("画像更新失败：" + e.getMessage());
        } finally {
            futureList.clear(); // 释放资源
        }
    }
}
