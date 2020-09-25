package com.aki.redis.controller;

import com.aki.redis.po.UserRedis;
import com.aki.redis.service.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * Created by 170725e on 2018/10/9.
 */
@Controller
public class Controller1 {

    @Autowired
    RedisUtil redisUtil;

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
            redisUtil.set(key, user);
            Object o = redisUtil.get(key);
            return o.toString();
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
            redisUtil.del(key);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
