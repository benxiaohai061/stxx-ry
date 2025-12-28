package com.stxx.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stxx.system.domain.SysRoleDept;

/**
 * 角色与部门关联表 数据层
 *
 * @author wangcc
 */
public interface SysRoleDeptMapper extends BaseMapper<SysRoleDept>
{
    /**
     * 批量新增角色部门信息
     *
     * @param roleDeptList 角色部门列表
     * @return 结果
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
