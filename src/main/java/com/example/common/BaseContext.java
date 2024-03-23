package com.example.common;

import org.springframework.stereotype.Component;

//基于ThreadLocal封装工具类，用户保存和获取当前登陆用户id
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setThreadLocal(Long id){
        threadLocal.set(id);
    }
    public static Long getThreadLocal(){
        return threadLocal.get();
    }
}
