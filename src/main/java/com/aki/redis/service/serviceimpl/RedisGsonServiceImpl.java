package com.aki.redis.service.serviceimpl;

import com.aki.redis.po.UserRedis;
import com.aki.redis.service.IRedisGsonService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisGsonServiceImpl implements IRedisGsonService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void add(String key, UserRedis user, Long time) {
        Gson gson = new Gson();
        String src = gson.toJson(user);
        stringRedisTemplate.opsForValue().set(key, src, time, TimeUnit.MINUTES);
    }

    @Override
    public void addList(String key, List<UserRedis> users, Long time) {
        Gson gson = new Gson();
        String src = gson.toJson(users);
        stringRedisTemplate.opsForValue().set(key, src, time, TimeUnit.MINUTES);
    }

    @Override
    public UserRedis get(String key) {
        String source = stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(source)) {
            return new Gson().fromJson(source, UserRedis.class);
        }
        return null;
    }

    @Override
    public List<UserRedis> getUserList(String key) {
        String source = stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(source)) {
            return new Gson().fromJson(source, new TypeToken<List<UserRedis>>() {
            }.getType());
        }
        return null;
    }

    @Override
    public void delete(String key) {
        stringRedisTemplate.opsForValue().getOperations().delete(key);
    }

}
