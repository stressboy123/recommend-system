package com.gdut.profile.recommend.mapper;

import com.gdut.profile.recommend.entity.RecommendResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Mapper
public interface RecommendResultMapper {
    /**
     * 插入单条推荐结果
     * @param result 推荐结果详细信息
     * @return 成功次数
     */
    int insertRecommendResultSingle(RecommendResult result);
    /**
     * 插入多条推荐结果
     * @param results 推荐结果详细信息
     * @return 成功次数
     */
    int insertRecommendResultMulti(@Param("results") List<RecommendResult> results);
    /**
     * 删除单条推荐结果
     * @param resultId 推荐结果ID
     * @return 成功次数
     */
    int deleteRecommendResultSingle(@Param("resultId") String resultId);
    /**
     * 删除多条推荐结果
     * @param resultIds 多条推荐结果ID
     * @return 成功次数
     */
    int deleteRecommendResultMulti(@Param("resultIds") List<String> resultIds);
    /**
     * 更新单条推荐结果
     * @param result 推荐结果详细信息
     * @return 成功次数
     */
    int updateRecommendResultSingle(RecommendResult result);
    /**
     * 更新多条推荐结果
     * @param results 多条推荐结果详细信息
     * @return 成功次数
     */
    int updateRecommendResultMulti(@Param("results") List<RecommendResult> results);
    /**
     * 查询单条推荐结果
     * @param resultId 推荐结果ID
     * @return 单条推荐结果详细信息
     */
    RecommendResult selectRecommendResultSingle(@Param("resultId") String resultId);
    /**
     * 查询多条推荐结果
     * @param resultIds 多条推荐结果ID
     * @return 多条推荐结果详细信息
     */
    List<RecommendResult> selectRecommendResultMulti(@Param("resultIds") List<String> resultIds);
}
