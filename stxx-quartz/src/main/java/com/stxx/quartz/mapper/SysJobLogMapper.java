package com.stxx.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stxx.quartz.domain.SysJobLog;

/**
 * 调度任务日志信息 数据层
 *
 * @author wangcc
 */
public interface SysJobLogMapper extends BaseMapper<SysJobLog>
{
    /**
     * 清空任务日志
     */
    public void cleanJobLog();
}
