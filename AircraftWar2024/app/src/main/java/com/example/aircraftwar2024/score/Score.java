package com.example.aircraftwar2024.score;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Score {
    private int score;
//    private int rank;
    private String name;
    private String time;
    public Score(int score, Date time) {
        this.score = score;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = dateFormat.format(time);
    }
    public Score(String name, int score, Date time) {
        this(score, time);
        this.name = name;
    }

    public Score(String name, int score, String date) {
        this.name = name;
        this.score = score;
        this.time = date;
    }

    public int getScore() {
        return score;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
