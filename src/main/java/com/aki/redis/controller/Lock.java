package com.aki.redis.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class Lock {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private Redisson redisson;

    /**
     * 普通锁
     *
     * @return
     */
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
            if (lockFlag) {
                // 模拟执行了两秒
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (clientId.equals(redisTemplate.opsForValue().get(lockKey))) {
                redisTemplate.delete(lockKey);
            }
        }
        return "没锁柱";
    }

    /**
     * redisson锁
     */
    @RequestMapping("run2")
    @ResponseBody
    public String run2() {
        // 设置锁定资源名称
        RLock disLock = redisson.getLock("DISLOCK");
        boolean isLock;
        try {
            //  拿到锁的就执行，没拿到锁的就阻塞 超时事件500毫秒
//            isLock = disLock.lock(500, TimeUnit.MILLISECONDS);
            //  尝试获取分布式锁 超时事件
            isLock = disLock.tryLock(15000, 2000, TimeUnit.MILLISECONDS);
            if (isLock) {
                // 模拟执行了两秒
                Thread.sleep(5000);
            } else {
                return "没拿到锁";
            }
        } catch (Exception e) {
        } finally {
            // 无论如何, 最后都要解锁
            disLock.unlock();
        }
        return "拿到锁，执行完了";
    }
}
