package com.example.aircraftwar2024.score;
//import edu.hitsz.score.Score;

import java.util.List;

public interface ScoreDAO {
    List<Score> getScores();
    void saveScore(Score score);
    void deleteScore(String name,int scorenum,String time);
}
