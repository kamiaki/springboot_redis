package com.aki.redis.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
    private RedissonClient redisson;

    /**
     * 普通锁  测试这个方法的时候不能用 同一个浏览器 因为同一个地址是串行的会阻塞 用 谷歌和ie一起测试
     *
     * @return
     */
    @RequestMapping("run")
    @ResponseBody
    public String run() {
        String lockKey = "UDPGetThunderDataSaveToDB";
        String clientId = UUID.randomUUID().toString();
        try {
            Boolean lockFlag = redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 5, TimeUnit.SECONDS);
            if (!lockFlag) {
                return "锁住了";
            }
            if (lockFlag) {
                // 模拟执行了两秒
                Thread.sleep(5000);
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

    /**
     * redisson锁  测试这个方法的时候不能用 同一个浏览器 因为同一个地址是串行的会阻塞 用 谷歌和ie一起测试
     */
    @RequestMapping("run2")
    @ResponseBody
    public String run2() {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        // 设置锁定资源名称
        RLock disLock = redisson.getLock(method);
        boolean isLock;
        try {
            // 拿到锁执行 没拿到锁就一直等待          ※※ 锁会一直续命 ※※
//            disLock.lock();
            // 拿到锁执行 参数1 设置锁过期时间，没拿到锁一直等待
//            disLock.lock(10000, TimeUnit.MILLISECONDS);
            // 拿到锁执行 参数1 没拿到锁等待时间，没拿到返回false        ※※ 锁会一直续命 ※※
            isLock = disLock.tryLock(1000, TimeUnit.MILLISECONDS);
            // 拿到锁执行 参数1 没拿到锁等待时间，参数2 所的过期时间，没拿到返回false
//            isLock = disLock.tryLock(1000,10000, TimeUnit.MILLISECONDS);
            if (isLock) {
                // 模拟执行了两秒
                Thread.sleep(10000);
            } else {
                return "没拿到锁";
            }
        } catch (Exception e) {
            return "没拿到锁";
        } finally {
            // 无论如何, 最后都要解锁
            //两个判断条件是非常必要的 一个是是否有锁，一个是是否是当前线程的锁
            if (disLock.isLocked()) {
                if (disLock.isHeldByCurrentThread()) {
                    disLock.unlock();
                }
            }
        }
        return "拿到锁，执行完了";
    }


}
