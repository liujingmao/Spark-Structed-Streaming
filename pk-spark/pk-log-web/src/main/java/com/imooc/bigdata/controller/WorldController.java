package com.imooc.bigdata.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorldController {

    @GetMapping("/world01")
    public String world01() {
        return "world01";
    }
}
