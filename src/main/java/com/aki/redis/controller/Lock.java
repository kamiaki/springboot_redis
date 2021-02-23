package com.aki.redis.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class Lock {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @RequestMapping("run")
    @ResponseBody
    public String run() {
        String lockKey = "UDPGetThunderDataSaveToDB";
        String clientId = UUID.randomUUID().toString();
        try {
            Boolean lockFlag = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 1, TimeUnit.SECONDS);
            if (!lockFlag) {
                return "锁住了";
            }
        } finally {
            if (clientId.equals(redisTemplate.opsForValue().get(lockKey))) {
//                redisTemplate.delete(lockKey);
            }
        }
        return "没锁柱";
    }

    @RequestMapping("run2")
    public void run2() {
        String lockKey = "UDPGetThunderDataSaveToDB";
        String clientId = UUID.randomUUID().toString();
        try {
            Boolean lockFlag = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
            if (!lockFlag) {
                return;
            }
        } finally {
            if (clientId.equals(redisTemplate.opsForValue().get(lockKey))) {
                redisTemplate.delete(lockKey);
            }
        }
    }
}
