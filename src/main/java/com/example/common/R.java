package com.example.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R<T> {
    private String msg;
    private Integer code;
    private T data;
    private Map map = new HashMap();
    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.msg = "Success";
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg){
        R<T> r = new R<T>();
        r.msg = msg;
        r.code = 0;
        return r;
    }
}
