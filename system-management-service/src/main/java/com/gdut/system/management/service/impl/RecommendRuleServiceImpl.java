package com.gdut.system.management.service.impl;

import com.alibaba.fastjson.JSON;
import com.gdut.system.management.entity.RecommendRule;
import com.gdut.system.management.entity.Result;
import com.gdut.system.management.enums.ResultCode;
import com.gdut.system.management.exception.BusinessException;
import com.gdut.system.management.mapper.RecommendRuleMapper;
import com.gdut.system.management.service.RecommendRuleService;
import com.gdut.system.management.util.ResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Service
public class RecommendRuleServiceImpl implements RecommendRuleService {
    private final RecommendRuleMapper recommendRuleMapper;

    public RecommendRuleServiceImpl(RecommendRuleMapper recommendRuleMapper) {
        this.recommendRuleMapper = recommendRuleMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveRecommendRule(RecommendRule rule) {
        // 1. 基础参数校验
        if (Objects.isNull(rule.getRuleName())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "规则名称不能为空");
        }
        if (Objects.isNull(rule.getScene())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "应用场景（scene）不能为空");
        }
        if (Objects.isNull(rule.getCreateUserId())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "创建人ID（createUserId）不能为空");
        }

        // 2. 业务规则校验
        // 2.1 场景值合法性（示例：1-首页推荐，2-详情页推荐，3-搜索推荐，可按需调整）
        if (rule.getScene() < 1 || rule.getScene() > 5) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "应用场景值不合法（支持1-5）");
        }
        // 2.2 规则名称唯一
//        RecommendRule existing = recommendRuleMapper.selectByRuleName(rule.getRuleName());
//        if (existing != null) {
//            throw new BusinessException(ResultCode.REPEAT_SUBMIT, "规则名称已存在，请勿重复创建");
//        }
        // 2.3 JSON字段格式校验（tagWeightJson/modeRatioJson）
        validateJsonField(rule.getTagWeightJson(), "标签权重JSON（tagWeightJson）");
        validateJsonField(rule.getModeRatioJson(), "推荐模式占比JSON（modeRatioJson）");

        // 3. 设置默认值
        rule.setCreateTime(LocalDateTime.now());
        rule.setUpdateTime(LocalDateTime.now());
        rule.setIsEnable(Objects.isNull(rule.getIsEnable()) ? 1 : rule.getIsEnable()); // 1-启用，0-禁用
        rule.setCacheExpire(Objects.isNull(rule.getCacheExpire()) ? 3600 : rule.getCacheExpire()); // 默认缓存1小时

        int count = recommendRuleMapper.insertRecommendRuleSingle(rule);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveRecommendRuleBatch(List<RecommendRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        rules.forEach(rule -> {
            // 批量参数校验
            if (Objects.isNull(rule.getRuleName())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "批量新增中存在规则名称为空的记录");
            }
            if (Objects.isNull(rule.getScene()) || rule.getScene() < 1 || rule.getScene() > 5) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "规则【" + rule.getRuleName() + "】场景值不合法");
            }
            if (Objects.isNull(rule.getCreateUserId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "规则【" + rule.getRuleName() + "】创建人ID不能为空");
            }
            // 名称唯一校验
//            if (recommendRuleMapper.selectByRuleName(rule.getRuleName()) != null) {
//                throw new BusinessException(ResultCode.REPEAT_SUBMIT, "规则名称【" + rule.getRuleName() + "】已存在");
//            }
            // JSON格式校验
            validateJsonField(rule.getTagWeightJson(), "规则【" + rule.getRuleName() + "】的标签权重JSON");
            validateJsonField(rule.getModeRatioJson(), "规则【" + rule.getRuleName() + "】的推荐模式占比JSON");
            // 默认值设置
            rule.setCreateTime(LocalDateTime.now());
            rule.setUpdateTime(LocalDateTime.now());
            rule.setIsEnable(Objects.isNull(rule.getIsEnable()) ? 1 : rule.getIsEnable());
            rule.setCacheExpire(Objects.isNull(rule.getCacheExpire()) ? 3600 : rule.getCacheExpire());
        });

        int count = recommendRuleMapper.insertRecommendRuleMulti(rules);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeRecommendRule(String ruleId) {
        if (Objects.isNull(ruleId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        RecommendRule rule = recommendRuleMapper.selectRecommendRuleSingle(ruleId);
        if (rule == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "推荐规则不存在，删除失败");
        }

        int count = recommendRuleMapper.deleteRecommendRuleSingle(ruleId);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeRecommendRuleBatch(List<String> ruleIds) {
        if (ruleIds == null || ruleIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        int count = recommendRuleMapper.deleteRecommendRuleMulti(ruleIds);
        return count > 0 ? ResultUtils.success(count) : ResultUtils.failWithCustomMsg(ResultCode.DATA_NOT_FOUND, "无有效规则可删除");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateRecommendRule(RecommendRule rule) {
        // 1. ID校验
        if (Objects.isNull(rule.getId())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "规则ID不能为空");
        }

        // 2. 规则存在性校验
        RecommendRule existing = recommendRuleMapper.selectRecommendRuleSingle(rule.getId());
        if (existing == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "推荐规则不存在，更新失败");
        }

        // 3. 业务规则校验
//        if (Objects.nonNull(rule.getRuleName()) && !rule.getRuleName().equals(existing.getRuleName())) {
//            // 规则名称修改时，校验新名称唯一
//            if (recommendRuleMapper.selectByRuleName(rule.getRuleName()) != null) {
//                throw new BusinessException(ResultCode.REPEAT_SUBMIT, "新规则名称已存在");
//            }
//        }
        if (Objects.nonNull(rule.getScene()) && (rule.getScene() < 1 || rule.getScene() > 5)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "应用场景值不合法（支持1-5）");
        }
        // JSON格式校验（仅当字段不为空时）
        if (Objects.nonNull(rule.getTagWeightJson())) {
            validateJsonField(rule.getTagWeightJson(), "标签权重JSON（tagWeightJson）");
        }
        if (Objects.nonNull(rule.getModeRatioJson())) {
            validateJsonField(rule.getModeRatioJson(), "推荐模式占比JSON（modeRatioJson）");
        }

        // 4. 强制刷新更新时间
        rule.setUpdateTime(LocalDateTime.now());

        int count = recommendRuleMapper.updateRecommendRuleSingle(rule);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateRecommendRuleBatch(List<RecommendRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        rules.forEach(rule -> {
            if (Objects.isNull(rule.getId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "批量更新中存在规则ID为空的记录");
            }
            if (recommendRuleMapper.selectRecommendRuleSingle(rule.getId()) == null) {
                throw new BusinessException(ResultCode.DATA_NOT_FOUND, "规则ID【" + rule.getId() + "】不存在");
            }
            // 场景值校验
            if (Objects.nonNull(rule.getScene()) && (rule.getScene() < 1 || rule.getScene() > 5)) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "规则ID【" + rule.getId() + "】场景值不合法");
            }
            // JSON格式校验
            if (Objects.nonNull(rule.getTagWeightJson())) {
                validateJsonField(rule.getTagWeightJson(), "规则ID【" + rule.getId() + "】的标签权重JSON");
            }
            if (Objects.nonNull(rule.getModeRatioJson())) {
                validateJsonField(rule.getModeRatioJson(), "规则ID【" + rule.getId() + "】的推荐模式占比JSON");
            }
            // 刷新更新时间
            rule.setUpdateTime(LocalDateTime.now());
        });

        int count = recommendRuleMapper.updateRecommendRuleMulti(rules);
        return ResultUtils.success(count);
    }

    @Override
    public Result<RecommendRule> getRecommendRule(String ruleId) {
        if (Objects.isNull(ruleId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        RecommendRule rule = recommendRuleMapper.selectRecommendRuleSingle(ruleId);
        return rule != null ? ResultUtils.success(rule) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<List<RecommendRule>> getRecommendRuleBatch(List<String> ruleIds) {
        if (ruleIds == null || ruleIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        List<RecommendRule> rules = recommendRuleMapper.selectRecommendRuleMulti(ruleIds);
        return ResultUtils.success(rules);
    }

    /**
     * 辅助方法：校验JSON字符串格式是否合法
     */
    private void validateJsonField(String jsonStr, String fieldDesc) {
        if (Objects.isNull(jsonStr)) {
            return; // 允许为空，如需必填可在此处抛异常
        }
        try {
            JSON.parse(jsonStr); // 用FastJSON解析校验
        } catch (Exception e) {
            throw new BusinessException(ResultCode.PARAM_ERROR, fieldDesc + "格式不合法");
        }
    }
}
