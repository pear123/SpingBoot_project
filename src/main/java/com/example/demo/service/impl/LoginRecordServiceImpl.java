package com.example.demo.service.impl;

import com.example.demo.dao.LoginRecordDao;
import com.example.demo.entity.LoginRecord;
import com.example.demo.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {
    @Autowired
    private LoginRecordDao loginRecordDao;

    /**
    * @Description: 登录记录的增加
    * @Param: [login_record]
    * @return: boolean
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @Override
    public boolean addRecord(LoginRecord login_record) {
        if(login_record!=null){
            if(login_record.getU_phone()!=null){
                boolean b=login_record.getU_phone().matches("[0-9]{1,}");//是否都为数字
                if(login_record.getU_phone().length()==11&&b){
                    loginRecordDao.addRecord(login_record);
                    return true;
                }
            }
        }
        return false;

    }
    /**
    * @Description: 登录记录的更新
    * @Param: [login_record]
    * @return: boolean
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @Override
    public boolean updateRecord(LoginRecord login_record) {
        LoginRecord login_record2=loginRecordDao.querryRecordByPhone(login_record.getU_phone());
        if(login_record2!=null){
            loginRecordDao.updateRecord(login_record);
            return true;
        }
        return false;

    }

    /**
    * @Description: 通过电话号码查找记录
    * @Param: [phone]
    * @return: com.example.demo.entity.LoginRecord
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */

    @Override
    public LoginRecord queryRecordByPhone(String phone) {
        LoginRecord record=loginRecordDao.querryRecordByPhone(phone);
        return record;
    }

    /**
     * @Description: 删除记录
     * @Param: [login_record_id]
     * @return: boolean
     * @Author: Lili Chen
     * @Date: 2018/12/13
     */
    @Override
    public boolean deleteRecord(Integer login_record_id) {
        LoginRecord login_record=loginRecordDao.querryRecordById(login_record_id);
        if(login_record!=null){
            loginRecordDao.deleteRecord(login_record_id);
            return true;
        }
        return false;
    }


}
