package com.example.demo.dao;

import com.example.demo.entity.Score;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreDao {
    void  addScore(Score score);
    void updateScore(Score score);
    Score querryScoreByUid(Integer u_id);
    Score querryScoreById(Integer id);
}
