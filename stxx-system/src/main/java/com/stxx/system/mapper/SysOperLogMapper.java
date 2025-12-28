package com.stxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stxx.system.domain.SysOperLog;

/**
 * 操作日志 数据层
 *
 * @author wangcc
 */
public interface SysOperLogMapper extends BaseMapper<SysOperLog>
{
    /**
     * 清空操作日志
     */
    public void cleanOperLog();
}
