package com.chenli.model;

import org.springframework.stereotype.Component;

/**
 * Created by chenli
 * 这个类表示当前用户是谁，用户的信息存储到这个类中
 * 以spring依赖注入的方式，把它放到容器里。加@Component
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
