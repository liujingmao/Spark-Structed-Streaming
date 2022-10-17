package com.imooc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EchartsController {


    @GetMapping("/echarts")
    public String echarts() {

        return "demo";
    }

}
