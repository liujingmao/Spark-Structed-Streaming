package com.imooc.bigdata.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DomainIntercepter implements Interceptor {


    List<Event> events;

    // 初始化
    @Override
    public void initialize() {
        events = new ArrayList<>();
    }

    // 单个事件
    @Override
    public Event intercept(Event event) {

        Map<String, String> headers = event.getHeaders();
        String body = new String(event.getBody());

        if(body.contains("imooc")) {
            headers.put("type", "imooc");
        } else {
            headers.put("type","other");
        }

        return event;
    }

    // 多个事件
    @Override
    public List<Event> intercept(List<Event> list) {

        events.clear();
        for(Event event : list) {
            events.add(intercept(event));
        }

        return events;
    }

    // 扫尾
    @Override
    public void close() {
        events = null;
    }


    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new DomainIntercepter();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
