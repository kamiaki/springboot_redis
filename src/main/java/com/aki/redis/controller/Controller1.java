package com.aki.redis.controller;

import com.aki.redis.po.UserRedis;
import com.aki.redis.service.serviceimpl.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by 170725e on 2018/10/9.
 */
@Controller
public class Controller1 {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisService redisService;

    @ResponseBody
    @RequestMapping("/hello")
    public String index() {
        stringRedisTemplate.opsForValue().set("aaa", "bbb");
        String getV = stringRedisTemplate.opsForValue().get("aaa");
        System.out.println(getV);
        return "Hello World";
    }

    /**
     * 设置Str缓存
     * @param key
     * @param val
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "setStr")
    public String setStr(String key, String val) {
        try {
            redisService.setStr(key, val);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 根据key查询Str缓存
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getStr")
    public String getStr(String key) {
        return redisService.getStr(key);
    }

    /**
     * 根据key产出Str缓存
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "delStr")
    public String delStr(String key) {
        try {
            redisService.del(key);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * 设置obj缓存
     * @param key
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "setObj")
    public String setObj(String key, UserRedis user) {
        try {
            redisService.setObj(key, user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 获取obj缓存
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getObj")
    public Object getObj(String key) {
        UserRedis userRedis = (UserRedis) redisService.getObj(key);
//        return redisService.getObj(key);
        return userRedis.getId() + userRedis.getName();
    }

    /**
     * 删除obj缓存
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "delObj")
    public Object delObj(String key) {
        try {
            redisService.delObj(key);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
