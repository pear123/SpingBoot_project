package com.example.demo.service;

import com.example.demo.entity.Information;

import java.util.List;

public interface InformationService {
    boolean addInformation(Information information);
    boolean deleteInformation(Integer id);
    boolean updateInformation(Information information);
    List<Information> queryInformationList();
    List<Information> queryInformationByUid(Integer id);
    Information queryInformationById(Integer id);
}
