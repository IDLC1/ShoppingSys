package com.taotao.common.pojo;


import java.util.HashMap;

/**
 * 存储返回给前端的数据内容
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
    }

    public static R error(String msg) {
        R r = new R();
        r.put("code", 500);
        r.put("msg", msg);
        return r;
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R success(Object data) {
        R r = new R();
        r.put("data", data);
        return r;
    }

    public static R success(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R success(String msg, Object data) {
        R r = new R();
        r.put("code", 0);
        r.put("msg", msg);
        r.put("data", data);
        return r;
    }

    public static R success(HashMap<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R success() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}