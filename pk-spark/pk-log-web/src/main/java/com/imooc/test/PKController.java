package com.imooc.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PKController {

    @GetMapping("/world02")
    public String world02() {
        return "world02";
    }
}
