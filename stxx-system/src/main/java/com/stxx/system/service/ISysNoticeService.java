package com.stxx.system.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stxx.system.domain.SysNotice;

/**
 * 公告 服务层
 *
 * @author wangcc
 */
public interface ISysNoticeService extends IService<SysNotice>
{
    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);


}
