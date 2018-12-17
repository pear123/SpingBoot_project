package com.example.demo.dao;

import com.example.demo.entity.LoginRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRecordDao {
     void addRecord(LoginRecord login_record);

     void updateRecord(LoginRecord login_record);

     LoginRecord querryRecordByPhone(String u_phone);

     void deleteRecord(Integer login_record_id);

     LoginRecord querryRecordById(Integer login_record_id);
}
