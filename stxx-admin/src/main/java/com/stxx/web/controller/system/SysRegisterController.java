package com.stxx.web.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.stxx.common.core.controller.BaseController;
import com.stxx.common.core.domain.AjaxResult;
import com.stxx.common.core.domain.model.RegisterBody;
import com.stxx.common.utils.StringUtils;
import com.stxx.framework.web.service.SysRegisterService;
import com.stxx.system.service.ISysConfigService;

/**
 * 注册验证
 *
 * @author wangcc
 */
@RequiredArgsConstructor
@RestController
public class SysRegisterController extends BaseController
{
    private final SysRegisterService registerService;

    private final ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
