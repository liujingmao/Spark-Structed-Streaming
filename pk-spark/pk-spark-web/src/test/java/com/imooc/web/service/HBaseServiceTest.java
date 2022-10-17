package com.imooc.web.service;

import com.imooc.web.domain.AccessUserHour;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HBaseServiceTest {

    @Resource
    HBaseService hBaseService;

    @Test
    public void testQuery(){
        List<AccessUserHour> accessUserHours = hBaseService
                .queryAccessUserHours("2020082414", "2020082415");

        for(AccessUserHour domain : accessUserHours) {
            System.out.println(domain);
        }
    }
}
