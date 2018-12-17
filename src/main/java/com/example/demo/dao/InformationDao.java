package com.example.demo.dao;

import com.example.demo.entity.Information;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InformationDao {
    List<Information> querryInforList();
    void  addInformation(Information information);
    void deleteInformation(Integer id);
    void updateInformation(Information information);
    List<Information> querryInformationByU_id(Integer u_id);
    Information querryInformationById(Integer id);
}
