package com.imooc.web.service;

import com.imooc.web.domain.ProvinceCntDay;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RedisService {

    @Resource
    RedisTemplate redisTemplate;

    public List<ProvinceCntDay> query(String day){

        List<ProvinceCntDay> results = new ArrayList<>();

        Map<String, String> map = hgetall("day-province-cnts-" + day);

        for(Map.Entry<String,String> entry : map.entrySet()) {
            ProvinceCntDay bean = new ProvinceCntDay();
            bean.setDay(day);
            bean.setProvince(entry.getKey());
            bean.setCnt(Long.parseLong(entry.getValue()));
            results.add(bean);
        }

        return results;
    }

    private Map<String,String> hgetall(String key) {
        return (Map<String,String>)redisTemplate.execute((RedisCallback<Map<String,String>>) con -> {

            Map<byte[], byte[]> result = con.hGetAll(key.getBytes());

            Map<String,String> map = new HashMap<>(result.size());

            for(Map.Entry<byte[], byte[]> entry : result.entrySet()) {
                map.put(new String(entry.getKey()), new String(entry.getValue()));
            }

            return map;
        });
    }
}
