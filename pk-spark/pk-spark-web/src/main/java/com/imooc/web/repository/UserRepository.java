package com.imooc.web.repository;

import com.imooc.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer>{
}
