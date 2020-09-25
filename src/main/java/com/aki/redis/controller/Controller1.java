package com.aki.redis.controller;

import com.aki.redis.po.UserRedis;
import com.aki.redis.service.IRedisGsonService;
import com.aki.redis.service.serviceimpl.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 170725e on 2018/10/9.
 */
@Controller
public class Controller1 {

    @Autowired
    RedisService redisService;
    @Autowired
    IRedisGsonService iRedisGsonService;

    //gson的
    @ResponseBody
    @RequestMapping("/hello2")
    public String index2() {
        UserRedis userRedis = new UserRedis();
        userRedis.setId("1");
        userRedis.setName("aaa111");
        List<UserRedis> userRedislist = new ArrayList<>();
        userRedislist.add(userRedis);
        iRedisGsonService.add("g1", userRedis, 10L);
        iRedisGsonService.addList("g1_list", userRedislist, 10L);
        UserRedis userRedis1 = iRedisGsonService.get("g1");
        List<UserRedis> g1_list = iRedisGsonService.getUserList("g1_list");
        System.out.println(userRedis1);
        System.out.println(g1_list.toString());
        return "Hello World";
    }


    /**
     * 设置Str缓存
     *
     * @param key
     * @param val
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "setStr")
    public String setStr(String key, String val) {
        try {
            redisService.setStr(key, val);
            return redisService.getStr(key);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    /**
     * 根据key产出Str缓存
     *
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
     *
     * @param key
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "setObj")
    public String setObj(String key, UserRedis user) {
        try {
            redisService.setObj(key, user);
            UserRedis userRedis = (UserRedis) redisService.getObj(key);
//        return redisService.getObj(key);
            return userRedis.getId() + userRedis.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 删除obj缓存
     *
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
