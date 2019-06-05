package com.chenli.async;

import com.alibaba.fastjson.JSONObject;
import com.chenli.util.JedisAdapter;
import com.chenli.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenli
 * 事件产生者，就是把事件放进队列里
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    //把事件放进队列里的方法
    public boolean fireEvent(EventModel model) {
        try {
            String json = JSONObject.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();//获取队列里事件的key
            jedisAdapter.lpush(key, json);//通过redis，把事件放进redis事件里
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
