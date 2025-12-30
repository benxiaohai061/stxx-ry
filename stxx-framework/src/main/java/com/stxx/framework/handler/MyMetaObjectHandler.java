package com.stxx.framework.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.stxx.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String createBy = "system";
        if(SecurityUtils.getAuthentication()!=null && !"anonymousUser".equals(SecurityUtils.getAuthentication().getPrincipal())){
            createBy = SecurityUtils.getUsername();
        }
        this.strictInsertFill(metaObject, "createBy", String.class, createBy);
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String updateBy = "system";
        if(SecurityUtils.getAuthentication()!=null && !"anonymousUser".equals(SecurityUtils.getAuthentication().getPrincipal())){
            updateBy = SecurityUtils.getUsername();
        }
        this.strictUpdateFill(metaObject, "updateBy", String.class, updateBy);
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }

}
