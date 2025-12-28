package com.stxx.quartz.service.impl;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stxx.common.utils.QueryWrapperUtils;
import com.stxx.common.utils.StringUtils;
import com.stxx.quartz.domain.SysJobLog;
import com.stxx.quartz.mapper.SysJobLogMapper;
import com.stxx.quartz.service.ISysJobLogService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务调度日志信息 服务层
 *
 * @author wangcc
 */
@RequiredArgsConstructor
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements ISysJobLogService
{
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog)
    {
        QueryWrapper<SysJobLog> queryWrapper = new QueryWrapper<>();

        // 动态条件查询
        if (StringUtils.isNotEmpty(jobLog.getJobName())) {
            queryWrapper.like("job_name", jobLog.getJobName());
        }
        if (StringUtils.isNotEmpty(jobLog.getJobGroup())) {
            queryWrapper.eq("job_group", jobLog.getJobGroup());
        }
        if (StringUtils.isNotEmpty(jobLog.getStatus())) {
            queryWrapper.eq("status", jobLog.getStatus());
        }
        if (StringUtils.isNotEmpty(jobLog.getInvokeTarget())) {
            queryWrapper.like("invoke_target", jobLog.getInvokeTarget());
        }

        // 时间范围查询
        QueryWrapperUtils.between(queryWrapper, "create_time", jobLog.getParams());

        // 排序
        queryWrapper.orderByDesc("create_time");

        return this.list(queryWrapper);
    }

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog selectJobLogById(Long jobLogId)
    {
        return this.getById(jobLogId);
    }

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(SysJobLog jobLog)
    {
        this.save(jobLog);
    }

    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteJobLogByIds(Long[] logIds)
    {
        return this.removeByIds(Arrays.asList(logIds)) ? 1 : 0;
    }

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteJobLogById(Long jobId)
    {
        return this.removeById(jobId) ? 1 : 0;
    }

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog()
    {
        baseMapper.cleanJobLog();
    }
}
