package com.example.demo.service.impl;

import com.example.demo.dao.PasswordRecordDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.PasswordRecord;
import com.example.demo.entity.User;
import com.example.demo.service.PasswordRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordRecordServiceImpl implements PasswordRecordService {
    @Autowired
    private PasswordRecordDao passwordRecordDao;
    @Autowired
    private UserDao userDao;
    
    
    /** 
    * @Description: 添加更改密码记录 
    * @Param: [password_record] 
    * @return: boolean 
    * @Author: Lili Chen 
    * @Date: 2018/12/17 
    */
    @Override
    public boolean addPasswordRecord(PasswordRecord password_record) {
        User user=userDao.queryUserByPhone(password_record.getU_phone());
        PasswordRecord password_record1=passwordRecordDao.queryRecordByPhone(password_record.getU_phone());
        if(user!=null){
            if(password_record1==null){
                System.out.println("密码记录为空");
                passwordRecordDao.addRecord(password_record);
                return true;
            }else{
                System.out.println("密码记录不为空");
                passwordRecordDao.deleteRecord(password_record1.getP_id());//先删除原来的找回记录
                passwordRecordDao.addRecord(password_record);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deletePasswordRecord(Integer p_id) {
        PasswordRecord password_record=passwordRecordDao.queryRecordById(p_id);
        if(password_record!=null){
           passwordRecordDao.deleteRecord(p_id);
            return true;
        }
        return false;
    }

    /**
    * @Description: 更改密码时进行验证码核对 （找回密码）
    * @Param: [phone, validate_number, date]
    * @return: java.lang.String
    * @Author: Lili Chen
    * @Date: 2018/12/17
    */
    @Override
    public String getPassword(String phone, Integer validate_number, Date date) {
        User user=userDao.queryUserByPhone(phone);
        if(user!=null){
            PasswordRecord password_record=passwordRecordDao.queryRecordByPhone(phone);
            if(password_record!=null){
                int c=(int)((date.getTime()-password_record.getP_createtime().getTime())/1000);
                if(c<=60){
                    if(password_record.getU_phone().equals(phone)&&password_record.getValidate_number().equals(validate_number)){
                       passwordRecordDao.deleteRecord(password_record.getP_id());
                        return user.getPassword();
                    }
                }
            }
        }
        return "";
    }
}
