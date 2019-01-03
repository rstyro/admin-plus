package com.lrs.core;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String,Object> objectRedisTemplate;

    @Test
    public void testSetRedis(){
        objectRedisTemplate.opsForValue().set("name","rstyro");
        List<String> testList = new ArrayList<>();
        testList.add("test1");
        testList.add("test2");
        testList.add("test3");
        objectRedisTemplate.opsForValue().set("list",testList);
    }
    @Test
    public void testGetRedis(){
       String name = (String) objectRedisTemplate.opsForValue().get("name");
        System.out.println("name="+name);
        List<String> testList = (List<String>) objectRedisTemplate.opsForValue().get("list");
        System.out.println("testList="+ JSON.toJSONString(testList));
    }
}
