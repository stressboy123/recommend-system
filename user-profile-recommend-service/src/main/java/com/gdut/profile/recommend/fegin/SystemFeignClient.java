package com.gdut.profile.recommend.fegin;

import com.gdut.profile.recommend.entity.BehaviorDetail;
import com.gdut.profile.recommend.entity.Result;
import com.gdut.profile.recommend.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@FeignClient(value = "system-management-service", path = "/api/system/user")//可加fallback降级处理
public interface SystemFeignClient {
    /**
     * 查询所有的用户Id
     * @return 用户Id列表
     */
    @PostMapping("/queryAllId")
    Result<List<String>> getAllSysUserIds();
}
