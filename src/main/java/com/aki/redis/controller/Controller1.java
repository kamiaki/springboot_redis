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
            System.out.println("key:" + key);
            System.out.println("user:" + user.toString());
            Object o = redisUtil.get(key);
            System.out.println("o:" + o.toString());

            Object ss = redisUtil.get("ss");
            System.out.println("ss:" + ss.toString());

            Object dd = redisUtil.get("dd");
            System.out.println("dd:" + dd.toString());
            return ss.toString() + dd.toString();
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
