package com.stxx.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stxx.system.domain.SysUserRole;

/**
 * 用户与角色关联表 数据层
 *
 * @author wangcc
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole>
{
    /**
     * 批量新增用户角色信息
     *
     * @param userRoleList 用户角色列表
     * @return 结果
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

}
