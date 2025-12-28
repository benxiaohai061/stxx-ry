package com.stxx.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色和菜单关联 sys_role_menu
 *
 * @author wangcc
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu
{
    /** 角色ID */
    private Long roleId;

    /** 菜单ID */
    private Long menuId;
}
