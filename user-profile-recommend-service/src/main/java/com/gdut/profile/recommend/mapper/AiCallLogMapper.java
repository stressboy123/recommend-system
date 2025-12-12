package com.gdut.profile.recommend.mapper;

import com.gdut.profile.recommend.entity.AiCallLog;
import com.gdut.profile.recommend.entity.RecommendResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Mapper
public interface AiCallLogMapper {
    /**
     * 插入单条ai日志
     * @param log ai日志详细信息
     * @return 成功次数
     */
    int insertAiCallLogSingle(AiCallLog log);
    /**
     * 插入多条ai日志
     * @param logs ai日志详细信息
     * @return 成功次数
     */
    int insertAiCallLogMulti(List<AiCallLog> logs);
    /**
     * 删除单条ai日志
     * @param logId ai日志ID
     * @return 成功次数
     */
    int deleteAiCallLogSingle(String logId);
    /**
     * 删除多条ai日志
     * @param logIds 多条ai日志ID
     * @return 成功次数
     */
    int deleteRecommendResultMulti(List<String> logIds);
    /**
     * 更新单条ai日志
     * @param log ai日志详细信息
     * @return 成功次数
     */
    int updateAiCallLogSingle(AiCallLog log);
    /**
     * 更新多条ai日志
     * @param logs 多条ai日志详细信息
     * @return 成功次数
     */
    int updateAiCallLogMulti(List<AiCallLog> logs);
    /**
     * 查询单条ai日志
     * @param logId ai日志ID
     * @return 单条ai日志详细信息
     */
    AiCallLog selectAiCallLogSingle(String logId);
    /**
     * 查询多条ai日志
     * @param logIds 多条ai日志ID
     * @return 多条ai日志详细信息
     */
    List<AiCallLog> selectAiCallLogMulti(List<String> logIds);
}
