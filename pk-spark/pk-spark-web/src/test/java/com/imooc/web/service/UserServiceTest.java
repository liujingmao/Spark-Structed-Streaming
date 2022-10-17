package com.imooc.web.service;


import com.imooc.web.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    public void testSave(){
        for(int i=0; i<10; i++) {
            User user = new User("pk" + i, "pk" + i + "@gmail.com");
            userService.save(user);
        }
    }

    @Test
    public void testQuery(){
        List<User> users = userService.query();
        for(User user : users) {
            System.out.println(user);
        }
    }
}
