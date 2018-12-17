package com.example.demo.service.impl;


import com.example.demo.DemoApplication;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVo;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class UserServiceImplTest {
    @Autowired private UserService userService;

    @Test
    public void register() {
        User user=new User();
        user.setPhone("18397926354");
        user.setPassword("1234");
        userService.register(user);
    }

    @Test
    public void updatePassword() {
        UserVo userVo=new UserVo();
        userVo.setRe_password("1234");
        User user=new User()               ;
        user.setPhone("18397926325");
        user.setPassword("1234");
        userVo.setUser(user);
        userService.updatePassword(userVo);
    }
}