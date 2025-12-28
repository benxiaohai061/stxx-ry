package com.stxx.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stxx.common.utils.QueryWrapperUtils;
import com.stxx.common.utils.StringUtils;
import com.stxx.system.domain.SysOperLog;
import com.stxx.system.mapper.SysOperLogMapper;
import com.stxx.system.service.ISysOperLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author wangcc
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService
{
    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        QueryWrapper<SysOperLog> queryWrapper = new QueryWrapper<>();

        // 动态条件查询
        if (StringUtils.isNotEmpty(operLog.getOperIp())) {
            queryWrapper.like("oper_ip", operLog.getOperIp());
        }
        if (StringUtils.isNotEmpty(operLog.getTitle())) {
            queryWrapper.like("title", operLog.getTitle());
        }
        if (operLog.getBusinessType() != null) {
            queryWrapper.eq("business_type", operLog.getBusinessType());
        }
        if (operLog.getStatus() != null) {
            queryWrapper.eq("status", operLog.getStatus());
        }
        if (StringUtils.isNotEmpty(operLog.getOperName())) {
            queryWrapper.like("oper_name", operLog.getOperName());
        }
        if (operLog.getBusinessTypes() != null && operLog.getBusinessTypes().length > 0) {
            queryWrapper.in("business_type", (Object[]) operLog.getBusinessTypes());
        }

        // 时间范围查询
        QueryWrapperUtils.between(queryWrapper, "oper_time", operLog.getParams());

        // 排序
        queryWrapper.orderByDesc("oper_id");

        return this.list(queryWrapper);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        baseMapper.cleanOperLog();
    }
}
