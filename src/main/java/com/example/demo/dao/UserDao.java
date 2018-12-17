package com.example.demo.dao;

import com.example.demo.entity.User;
import com.example.demo.entity.UserVo;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserDao {
     List<User> queryUser();
     void addUser(User user);
     User queryUserByPhone(String phone);
     User queryUserById(Integer id);
     void updatePassword(UserVo userVo);


}
