package com.gdut.profile.recommend.service;

import com.gdut.profile.recommend.entity.Result;
import com.gdut.profile.recommend.entity.UserTag;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
public interface UserProfileService {
    /**
     * 新增单条用户标签
     */
    Result<Integer> saveUserTag(UserTag tag);

    /**
     * 批量新增用户标签
     */
    Result<Integer> saveUserTagBatch(List<UserTag> tags);

    /**
     * 删除单条用户标签
     */
    Result<Integer> removeUserTag(String tagId);

    /**
     * 批量删除用户标签
     */
    Result<Integer> removeUserTagBatch(List<String> tagIds);

    /**
     * 更新单条用户标签
     */
    Result<Integer> updateUserTag(UserTag tag);

    /**
     * 批量更新用户标签
     */
    Result<Integer> updateUserTagBatch(List<UserTag> tags);

    /**
     * 查询单条用户标签
     */
    Result<UserTag> getUserTag(String tagId);

    /**
     * 批量查询用户标签
     */
    Result<List<UserTag>> getUserTagBatch(List<String> tagIds);

    /**
     * 根据用户id实时更新用户标签
     */
    Result<Integer> updateUserProfileIncrementally(String userId);

    /**
     * 根据用户id全量更新用户标签
     */
    Result<Integer> updateUserProfileFull(String userId);

    /**
     * 异步全量更新
     */
    void updateUserProfileFullAsync(String userId);

    /**
     * 查询需要全量更新的用户id
     */
    Result<List<String>> listNeedFullUpdateUserIds();

    /**
     * 批量新增用户标签1000条
     */
    Result<Integer> saveTagBatch();

    /**
     * 根据用户id查询用户标签
     */
    Result<UserTag> getUserTagByUserId(String userId);
}
