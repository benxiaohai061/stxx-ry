package com.stxx.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stxx.system.domain.SysLogininfor;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author wangcc
 */
public interface SysLogininforMapper extends BaseMapper<SysLogininfor>
{
    /**
     * 清空系统登录日志
     */
    public int cleanLogininfor();
}
