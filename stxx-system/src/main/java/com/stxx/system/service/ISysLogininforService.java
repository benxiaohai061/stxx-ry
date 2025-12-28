package com.stxx.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stxx.system.domain.SysLogininfor;

/**
 * 系统访问日志情况信息 服务层
 *
 * @author wangcc
 */
public interface ISysLogininforService extends IService<SysLogininfor>
{

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    /**
     * 清空系统登录日志
     */
    public void cleanLogininfor();
}
