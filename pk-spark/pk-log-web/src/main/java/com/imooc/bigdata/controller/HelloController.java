package com.imooc.bigdata.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {


    @ResponseBody
    @RequestMapping(value = "/hello")
    public String sayHello() {
        return "Hello: PK...";
    }


    @ResponseBody
    @RequestMapping("/world")
    public String sayWorld() {
        return "World: PK...";
    }
}
