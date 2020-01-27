package com.chenli.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenli
 *
 * 定义一个ViewObject对象来存储关联的数据
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
