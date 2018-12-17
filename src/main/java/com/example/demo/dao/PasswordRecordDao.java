package com.example.demo.dao;


import com.example.demo.entity.PasswordRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecordDao {
    void addRecord(PasswordRecord password_record);
    void updateRecord(PasswordRecord password_record);
    PasswordRecord queryRecordByPhone(String u_phone);
    void deleteRecord(Integer p_id);
    PasswordRecord queryRecordById(Integer p_id);
}
