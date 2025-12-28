package com.stxx.system.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stxx.common.utils.StringUtils;
import com.stxx.system.domain.SysNotice;
import com.stxx.system.mapper.SysNoticeMapper;
import com.stxx.system.service.ISysNoticeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公告 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        QueryWrapper<SysNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(notice.getNoticeTitle()), "notice_title", notice.getNoticeTitle())
                    .eq(StringUtils.isNotEmpty(notice.getNoticeType()), "notice_type", notice.getNoticeType())
                    .eq(StringUtils.isNotEmpty(notice.getStatus()), "status", notice.getStatus())
                    .orderByDesc("create_time");
        return this.list(queryWrapper);
    }

}
