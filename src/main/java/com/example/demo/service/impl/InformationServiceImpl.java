package com.example.demo.service.impl;

import com.example.demo.dao.InformationDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.Information;
import com.example.demo.entity.User;
import com.example.demo.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class InformationServiceImpl implements InformationService {
    @Autowired
    private InformationDao informationDao;
    @Autowired
    private UserDao userDao;
   /**
   * @Description: 增加消息
   * @Param: [information]
   * @return: boolean
   * @Author: Lili Chen
   * @Date: 2018/12/13
   */
    @Override
    public boolean addInformation(Information information) {
        informationDao.addInformation(information);
        return true;
    }
    /**
    * @Description: 删除消息
    * @Param: [id]
    * @return: boolean
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @Override
    public boolean deleteInformation(Integer id) {
        Information information=informationDao.querryInformationById(id);
        if(information!=null){
            informationDao.deleteInformation(id);
            return true;
        }
        return false;
    }

    /**
    * @Description: 更新消息
    * @Param: [information]
    * @return: boolean
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @Override
    public boolean updateInformation(Information information) {
        Information information1=informationDao.querryInformationById(information.getInfor_id());
        if(information1!=null){
           informationDao.updateInformation(information);
            return true;
        }
        return false;
    }

    @Override
    public List<Information> queryInformationList() {
        return informationDao.querryInforList();
    }
    /**
    * @Description: 通过用户id查找消息
    * @Param: [id]
    * @return: java.util.List<com.example.demo.entity.Information>
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @Override
    public List<Information> queryInformationByUid(Integer id) {
        User user=userDao.queryUserById(id);
        if(user!=null){
            return informationDao.querryInformationByU_id(id);
        }
        return null;
    }
    /**
    * @Description: 通过id查找消息
    * @Param: [id]
    * @return: com.example.demo.entity.Information
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @Override
    public Information queryInformationById(Integer id) {
        Information information=null;
        information=informationDao.querryInformationById(id);
        return information;
    }


    /**
    * @Description:  某用户消息条数
    * @Param: [u_id]
    * @return: int
    * @Author: Lili Chen
    * @Date: 2018/12/17
    */
    @Override
    public int getCount(Integer u_id) {
        List<Information> informationList=informationDao.querryInformationByU_id(u_id);
        int count=informationList.size();
        return count;
    }


    /**
    * @Description: 获取某用户从beginIndex开始，size条数的消息
    * @Param: [beginIndex, size, u_id]
    * @return: java.util.List<com.example.demo.entity.Information>
    * @Author: Lili Chen
    * @Date: 2018/12/17
    */
    @Override
    public List<Information> findInformationListIndex(int beginIndex, int size,Integer u_id) {
        Map map=new HashMap<>();
        map.put("beginIndex",beginIndex);
        map.put("size",size);
        map.put("u_id",u_id);
        List<Information> informationList= informationDao.findInformationListIndex(map);
        return informationList;
    }
}
