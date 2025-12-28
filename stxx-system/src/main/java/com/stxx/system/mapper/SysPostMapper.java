package com.stxx.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stxx.system.domain.SysPost;

/**
 * 岗位信息 数据层
 *
 * @author wangcc
 */
public interface SysPostMapper extends BaseMapper<SysPost>
{
    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    List<SysPost> selectPostsByUserName(String userName);
}
