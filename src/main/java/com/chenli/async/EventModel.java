package com.chenli.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenli
 * 放在队列的事件模型
 * 线程发生的数据都打包到这个model里面
 */
public class EventModel {
    private EventType type;//事件类型
    private int actorId;//事件触发者
    //触发对象用entityType、entityId表示，两者结合可以表示任何东西
    private int entityType;
    private int entityId;
    private int entityOwnerId;//触发对象的属主是谁
    private Map<String, String> exts = new HashMap<String, String>();//触发线程要有什么数据需要保存

    public String getExt(String key) {
        return exts.get(key);
    }

    //为了更快处理数据，把这个对象返回出来，写代码会比较方便
    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public EventModel() {

    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
