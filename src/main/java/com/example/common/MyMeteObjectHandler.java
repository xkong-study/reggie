package com.example.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMeteObjectHandler implements MetaObjectHandler {
    //插入时更新
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("create_time", LocalDateTime.now());
        metaObject.setValue("update_time", LocalDateTime.now());
    }
    //更新数据时更新
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("update_user", BaseContext.getThreadLocal());
        metaObject.setValue("update_time", LocalDateTime.now());
    }
}
