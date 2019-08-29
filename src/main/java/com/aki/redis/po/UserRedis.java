package com.aki.redis.po;

import java.io.Serializable;

/**
 * Created by 170725e on 2018/10/11.
 */
public class UserRedis implements Serializable {
    private static final long serialVersionUId = 1L;

    private String id;
    private String name;

    public UserRedis(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
