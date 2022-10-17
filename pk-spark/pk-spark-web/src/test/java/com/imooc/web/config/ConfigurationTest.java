package com.imooc.web.config;


import com.imooc.web.domain.User;
import com.imooc.web.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationTest {

    @Resource
    HBaseConfiguration hBaseConfiguration;

    @Test
    public void testSave(){
        System.out.println(hBaseConfiguration);
    }

}
