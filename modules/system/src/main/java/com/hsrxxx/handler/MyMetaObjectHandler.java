package com.hsrxxx.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hsrxxx.common.core.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据插入更新时的自动操作
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createBy", getUserName(), metaObject);
        this.setFieldValByName("updateBy", getUserName(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateBy", getUserName(), metaObject);
    }

    private String getUserName(){
        return SecurityUtils.getUsername();
    }
}
