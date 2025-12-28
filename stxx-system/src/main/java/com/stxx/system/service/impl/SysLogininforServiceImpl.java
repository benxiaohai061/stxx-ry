package com.stxx.system.service.impl;

import java.util.Arrays;
import java.util.List;

import com.stxx.common.utils.QueryWrapperUtils;
import com.stxx.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stxx.system.domain.SysLogininfor;
import com.stxx.system.mapper.SysLogininforMapper;
import com.stxx.system.service.ISysLogininforService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author wangcc
 */
@RequiredArgsConstructor
@Service
public class SysLogininforServiceImpl extends ServiceImpl<SysLogininforMapper, SysLogininfor> implements ISysLogininforService
{
    private final SysLogininforMapper logininforMapper;

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor)
    {
        QueryWrapper<SysLogininfor> queryWrapper = new QueryWrapper<>();

        // 动态条件查询
        if (StringUtils.isNotEmpty(logininfor.getIpaddr())) {
            queryWrapper.like("ipaddr", logininfor.getIpaddr());
        }
        if (StringUtils.isNotEmpty(logininfor.getStatus())) {
            queryWrapper.eq("status", logininfor.getStatus());
        }
        if (StringUtils.isNotEmpty(logininfor.getUserName())) {
            queryWrapper.like("user_name", logininfor.getUserName());
        }

        // 时间范围查询
        QueryWrapperUtils.between(queryWrapper, "login_time", logininfor.getParams());

        // 排序
        queryWrapper.orderByDesc("info_id");

        return this.list(queryWrapper);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
