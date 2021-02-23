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

    /**
     * 集群实现原理，就是好多个锁同意判断，用RedissonRedLock，
     * 需要3/2+1=2，即至少2个sentinel集群响应成功，就是说一半以上上锁了，就是上锁了
     *
     * Config config1 = new Config();
     * config1.useSingleServer().setAddress("redis://172.29.1.180:5378")
     *         .setPassword("a123456").setDatabase(0);
     * RedissonClient redissonClient1 = Redisson.create(config1);
     *
     * Config config2 = new Config();
     * config2.useSingleServer().setAddress("redis://172.29.1.180:5379")
     *         .setPassword("a123456").setDatabase(0);
     * RedissonClient redissonClient2 = Redisson.create(config2);
     *
     * Config config3 = new Config();
     * config3.useSingleServer().setAddress("redis://172.29.1.180:5380")
     *         .setPassword("a123456").setDatabase(0);
     * RedissonClient redissonClient3 = Redisson.create(config3);
     *
     * String resourceName = "REDLOCK";
     * RLock lock1 = redissonClient1.getLock(resourceName);
     * RLock lock2 = redissonClient2.getLock(resourceName);
     * RLock lock3 = redissonClient3.getLock(resourceName);
     *
     * RedissonRedLock redLock = new RedissonRedLock(lock1, lock2, lock3);
     * boolean isLock;
     * try {
     *     isLock = redLock.tryLock(500, 30000, TimeUnit.MILLISECONDS);
     *     System.out.println("isLock = "+isLock);
     *     if (isLock) {
     *         //TODO if get lock success, do something;
     *         Thread.sleep(30000);
     *     }
     * } catch (Exception e) {
     * } finally {
     *     // 无论如何, 最后都要解锁
     *     System.out.println("");
     *     redLock.unlock();
     * }
     */
}
