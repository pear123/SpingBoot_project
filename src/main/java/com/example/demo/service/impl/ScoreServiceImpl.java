package com.example.demo.service.impl;

import com.example.demo.dao.ScoreDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.Score;
import com.example.demo.entity.User;
import com.example.demo.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ScoreDao scoreDao;


    /**
    * @Description: 查看个人积分
    * @Param: [u_id]
    * @return: com.example.demo.entity.Score
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @Override
    public Score queryScore(Integer u_id) {
        User user=userDao.queryUserById(u_id);
        if(user!=null){
            return scoreDao.querryScoreByUid(u_id);
        }
        return null;
    }

    /**
    * @Description: 添加积分
    * @Param: [score]
    * @return: boolean
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @Override
    public boolean addScore(Score score) {
        Score score1=scoreDao.querryScoreByUid(score.getU_id());
        if(score1==null){
            scoreDao.addScore(score);
            return true;
        }
        return false;
    }

    /**
    * @Description: 修改积分
    * @Param: [score]
    * @return: boolean
    * @Author: Lili Chen
    * @Date: 2018/12/13
    */
    @Override
    public boolean updateScore(Score score) {
        Score score1=scoreDao.querryScoreByUid(score.getU_id());
        if(score1!=null){
            scoreDao.updateScore(score);
            return true;

        }
        return false;
    }

}
