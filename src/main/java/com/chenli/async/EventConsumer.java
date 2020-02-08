package com.chenli.async;

import com.alibaba.fastjson.JSON;
import com.chenli.util.JedisAdapter;
import com.chenli.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenli
 * 事件消费者，即处理事件的对象
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    //把所有有关EvenTpye相关的EventHandler全部组织起来
    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    //全局变量获取所有实现EventHandler接口的类
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    //建立映射表，不同的事件对应不同的处理者处理。初始化之后开始做
    @Override
    public void afterPropertiesSet() throws Exception {
        //把实现了EventHandler的类全部找出来
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {

                //先把EventType取出来
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {//先判断有没有这个事件类型
                        config.put(type, new ArrayList<EventHandler>());//没有就把它添加进去
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        //阻塞队列，开一个线程去处理事件，实现异步的操作
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {//一直不停地取数据然后处理数据
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0, key);//取出事件，从redis里取出来
                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }

                        //解析事件模型，JSON把它解析出来
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {//不存在的，从来就没有注册过的事件
                            logger.error("不能识别的事件");
                            continue;
                        }

                        //通过多态来实现
                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);//找对应的Handler处理
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
