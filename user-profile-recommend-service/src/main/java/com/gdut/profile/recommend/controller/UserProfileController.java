package com.gdut.profile.recommend.controller;

import com.gdut.profile.recommend.entity.Result;
import com.gdut.profile.recommend.entity.UserTag;
import com.gdut.profile.recommend.service.UserProfileService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@RestController
@RequestMapping("/api/recommend/userprofile") // 接口路径：用户标签模块
public class UserProfileController {

    // final 修饰依赖，构造器注入
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * 新增单条用户标签
     */
    @PostMapping("/add")
    public Result<Integer> addUserTag(@Valid @RequestBody UserTag tag) {
        return userProfileService.saveUserTag(tag);
    }

    /**
     * 批量新增用户标签
     */
    @PostMapping("/addBatch")
    public Result<Integer> addUserTagBatch(@Valid @RequestBody List<UserTag> tags) {
        return userProfileService.saveUserTagBatch(tags);
    }

    /**
     * 删除单条用户标签
     */
    @DeleteMapping("/delete/{id}")
    public Result<Integer> deleteUserTag(@PathVariable("id") String tagId) {
        return userProfileService.removeUserTag(tagId);
    }

    /**
     * 批量删除用户标签
     */
    @DeleteMapping("/deleteBatch")
    public Result<Integer> deleteUserTagBatch(@RequestBody List<String> tagIds) {
        return userProfileService.removeUserTagBatch(tagIds);
    }

    /**
     * 更新单条用户标签
     */
    @PutMapping("/update")
    public Result<Integer> updateUserTag(@Valid @RequestBody UserTag tag) {
        return userProfileService.updateUserTag(tag);
    }

    /**
     * 批量更新用户标签
     */
    @PutMapping("/updateBatch")
    public Result<Integer> updateUserTagBatch(@Valid @RequestBody List<UserTag> tags) {
        return userProfileService.updateUserTagBatch(tags);
    }

    /**
     * 查询单条用户标签
     */
    @GetMapping("/query/{id}")
    public Result<UserTag> getUserTag(@PathVariable("id") String tagId) {
        return userProfileService.getUserTag(tagId);
    }

    /**
     * 批量查询用户标签
     */
    @PostMapping("/queryBatch")
    public Result<List<UserTag>> getUserTagBatch(@RequestBody List<String> tagIds) {
        return userProfileService.getUserTagBatch(tagIds);
    }

    /**
     * 批量新增用户标签1000条
     */
    @PostMapping("/addTagBatch")
    public Result<Integer> addTagBatch() {
        return userProfileService.saveTagBatch();
    }

    /**
     * 根据用户id查询用户标签
     */
    @GetMapping("/queryByUserId/{userId}")
    public Result<UserTag> getUserTagByUserId(@PathVariable("userId") String userId) {
        return userProfileService.getUserTagByUserId(userId);
    }
}
