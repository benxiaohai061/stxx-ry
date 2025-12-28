package com.stxx.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stxx.system.domain.SysOperLog;

/**
 * 操作日志 服务层
 *
 * @author wangcc
 */
public interface ISysOperLogService extends IService<SysOperLog>
{
    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * 清空操作日志
     */
    public void cleanOperLog();
}
