package com.chenli.async;

import java.util.List;

/**
 * Created by chenli
 * 事件处理，只是一个接口，子类去具体实现
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();//只要和EventType有关的都要去处理一下
}
