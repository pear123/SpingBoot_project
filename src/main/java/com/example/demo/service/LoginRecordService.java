package com.example.demo.service;

import com.example.demo.entity.LoginRecord;

public interface LoginRecordService {
    boolean addRecord(LoginRecord login_record);

    boolean updateRecord(LoginRecord login_record);

    LoginRecord queryRecordByPhone(String u_phone);

    boolean deleteRecord(Integer login_record_id);

}
