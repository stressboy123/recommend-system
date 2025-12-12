package com.gdut.system.management.service.impl;

import com.gdut.system.management.entity.Result;
import com.gdut.system.management.entity.SysUser;
import com.gdut.system.management.enums.ResultCode;
import com.gdut.system.management.exception.BusinessException;
import com.gdut.system.management.mapper.SysUserMapper;
import com.gdut.system.management.service.SysUserService;
import com.gdut.system.management.util.ResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    // final 修饰依赖，构造器注入（Spring 5+ 自动注入）
    private final SysUserMapper sysUserMapper;

    public SysUserServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveSysUser(SysUser sysUser) {
        // 1. 基础参数校验
        if (Objects.isNull(sysUser.getUsername())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "用户名不能为空");
        }
        if (Objects.isNull(sysUser.getPassword())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "密码不能为空");
        }

        // 2. 业务规则校验（示例：用户名唯一）
//        SysUser existingUser = sysUserMapper.selectByUsername(sysUser.getUsername());
//        if (existingUser != null) {
//            throw new BusinessException(ResultCode.REPEAT_SUBMIT, "用户名已存在，请勿重复创建");
//        }

        // 3. 设置默认值
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setStatus(1); // 1-正常，0-禁用（默认正常）
        sysUser.setRoleType(Objects.isNull(sysUser.getRoleType()) ? 2 : sysUser.getRoleType()); // 默认普通用户（2）

        // 4. 密码加密（实际项目需添加，示例：BCrypt加密）
        // sysUser.setPassword(BCrypt.hashpw(sysUser.getPassword(), BCrypt.gensalt()));

        int count = sysUserMapper.insertSysUserSingle(sysUser);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveSysUserBatch(List<SysUser> sysUsers) {
        if (sysUsers == null || sysUsers.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        // 批量校验 + 设置默认值
        sysUsers.forEach(user -> {
            if (Objects.isNull(user.getUsername())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "批量新增中存在用户名为空的记录");
            }
            if (Objects.isNull(user.getPassword())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "批量新增中存在密码为空的记录");
            }
            // 校验用户名重复（如需严格校验，需查询数据库去重，此处简化）
//            SysUser existing = sysUserMapper.selectByUsername(user.getUsername());
//            if (existing != null) {
//                throw new BusinessException(ResultCode.REPEAT_SUBMIT, "用户名【" + user.getUsername() + "】已存在");
//            }
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            user.setStatus(1);
            user.setRoleType(Objects.isNull(user.getRoleType()) ? 2 : user.getRoleType());
            // 密码加密：user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        });

        int count = sysUserMapper.insertSysUserMulti(sysUsers);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeSysUser(String userId) {
        if (Objects.isNull(userId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        // 校验用户是否存在
        SysUser sysUser = sysUserMapper.selectSysUserSingle(userId);
        if (sysUser == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "用户不存在，删除失败");
        }

        // 物理删除（如需逻辑删除，改为更新 status=0，对应 XML 需调整）
        int count = sysUserMapper.deleteSysUserSingle(userId);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeSysUserBatch(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        // 批量校验用户是否存在（简化：直接删除，影响行数为实际删除数量）
        int count = sysUserMapper.deleteSysUserMulti(userIds);
        return count > 0 ? ResultUtils.success(count) : ResultUtils.failWithCustomMsg(ResultCode.DATA_NOT_FOUND, "无有效用户可删除");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateSysUser(SysUser sysUser) {
        // 1. 校验ID必填
        if (Objects.isNull(sysUser.getId())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "用户ID不能为空");
        }

        // 2. 校验用户是否存在
        SysUser existingUser = sysUserMapper.selectSysUserSingle(sysUser.getId());
        if (existingUser == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "用户不存在，更新失败");
        }

        // 3. 业务规则：用户名如需唯一，需校验是否与其他用户重复
//        if (Objects.nonNull(sysUser.getUsername()) && !sysUser.getUsername().equals(existingUser.getUsername())) {
//            SysUser repeatUser = sysUserMapper.selectByUsername(sysUser.getUsername());
//            if (repeatUser != null) {
//                throw new BusinessException(ResultCode.REPEAT_SUBMIT, "用户名已被占用");
//            }
//        }

        // 4. 强制刷新更新时间
        sysUser.setUpdateTime(LocalDateTime.now());

        // 5. 密码更新单独处理（如需加密，此处添加逻辑）
        // if (Objects.nonNull(sysUser.getPassword()) && !sysUser.getPassword().equals(existingUser.getPassword())) {
        //     sysUser.setPassword(BCrypt.hashpw(sysUser.getPassword(), BCrypt.gensalt()));
        // }

        int count = sysUserMapper.updateSysUserSingle(sysUser);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateSysUserBatch(List<SysUser> sysUsers) {
        if (sysUsers == null || sysUsers.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        // 批量校验
        sysUsers.forEach(user -> {
            if (Objects.isNull(user.getId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "批量更新中存在用户ID为空的记录");
            }
            SysUser existing = sysUserMapper.selectSysUserSingle(user.getId());
            if (existing == null) {
                throw new BusinessException(ResultCode.DATA_NOT_FOUND, "用户ID【" + user.getId() + "】不存在");
            }
            user.setUpdateTime(LocalDateTime.now());
        });

        int count = sysUserMapper.updateSysUserMulti(sysUsers);
        return ResultUtils.success(count);
    }

    @Override
    public Result<SysUser> getSysUser(String userId) {
        if (Objects.isNull(userId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        SysUser sysUser = sysUserMapper.selectSysUserSingle(userId);
        if (sysUser == null) {
            return ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
        }

        // 密码脱敏（返回给前端时隐藏密码，可选）
        // sysUser.setPassword("******");
        return ResultUtils.success(sysUser);
    }

    @Override
    public Result<List<SysUser>> getSysUserBatch(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        List<SysUser> sysUsers = sysUserMapper.selectSysUserMulti(userIds);
        // 批量密码脱敏
        // sysUsers.forEach(user -> user.setPassword("******"));
        return ResultUtils.success(sysUsers);
    }

    @Override
    public Result<List<String>> getAllSysUserIds() {
        List<String> userIds = sysUserMapper.selectAllSysUserIds();
        return userIds != null ? ResultUtils.success(userIds) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<Integer> saveUserBatch() {
        List<SysUser> sysUserList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            String userId = "user_id" + i;
            SysUser user = SysUser.builder()
                    .id(userId)
                    .username("user_" + i)
                    .password("password" + i)
                    .roleType(2)
                    .phone("138" + String.format("%08d", i))
                    .status(1)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            sysUserList.add(user);
        }
        int count = sysUserMapper.insertSysUserMulti(sysUserList);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }
}
