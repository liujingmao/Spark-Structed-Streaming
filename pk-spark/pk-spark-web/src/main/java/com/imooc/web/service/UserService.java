package com.imooc.web.service;

import com.imooc.web.domain.User;
import com.imooc.web.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> query(){
        return userRepository.findAll();
    }


}
