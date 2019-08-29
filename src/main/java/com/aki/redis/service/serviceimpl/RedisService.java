package com.aki.redis.service.serviceimpl;


import com.aki.redis.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by on 2017/3/1.
 */
@Service
public class RedisService implements IRedisService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;
    @Resource(name = "redisTemplate")
    ValueOperations<Object, Object> valOpsObj;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;


    /**
     * 根据指定key获取String
     * @param key
     * @return
     */
    public String getStr(String key) {
        return valOpsStr.get(key);
    }

    /**
     * 设置Str缓存
     * @param key
     * @param val
     */
    public void setStr(String key, String val) {
        valOpsStr.set(key, val);
    }

    /**
     * 删除指定key
     * @param key
     */
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }


    /**
     * 设置obj缓存
     * @param o1
     * @param o2
     */
    public void setObj(Object o1, Object o2) {
        valOpsObj.set(o1, o2);
    }

    /**
     * 根据指定o获取Object
     * @param o
     * @return
     */
    public Object getObj(Object o) {
        return valOpsObj.get(o);
    }

    /**
     * 删除Obj缓存
     * @param o
     */
    public void delObj(Object o) {
        redisTemplate.delete(o);
    }

}