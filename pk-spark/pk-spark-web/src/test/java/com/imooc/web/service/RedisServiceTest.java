package com.imooc.web.service;

import com.imooc.web.domain.AccessUserHour;
import com.imooc.web.domain.ProvinceCntDay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {

    @Resource
    RedisService redisService;

    @Test
    public void testQuery(){
        List<ProvinceCntDay> values = redisService.query("20200830");

        for(ProvinceCntDay value : values) {
            System.out.println(value);
        }
    }
}
