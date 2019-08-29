package com.aki.redis.service;

import com.aki.redis.po.UserRedis;

import java.util.List;

public interface IRedisGsonService {
    void add(String key, UserRedis user, Long time);

    void addList(String key, List<UserRedis> users, Long time);

    UserRedis get(String key);

    List<UserRedis> getUserList(String key);

    void delete(String key);
}
