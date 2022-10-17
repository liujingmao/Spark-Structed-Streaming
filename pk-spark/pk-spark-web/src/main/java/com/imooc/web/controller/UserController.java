package com.imooc.web.controller;

import com.imooc.web.domain.User;
import com.imooc.web.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    UserService userService;


    @GetMapping("/query")
    public List<User> query() {
        return userService.query();
    }

}
