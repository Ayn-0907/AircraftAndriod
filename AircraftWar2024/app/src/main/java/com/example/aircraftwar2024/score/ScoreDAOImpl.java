package com.example.aircraftwar2024.score;


import static com.example.aircraftwar2024.game.BaseGame.allScores;

import android.content.Context;

import com.example.aircraftwar2024.activity.RecordActivity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAOImpl implements ScoreDAO{
    private List<Score> scores = new ArrayList<>();
    public static String FILE_PATH;
    public Context context;
    public ScoreDAOImpl(Context context) {
        this.context = context;
    }
    @Override
    public void saveScore(Score score) {
//        File f = new File(FILE_PATH);

        try {
            FileOutputStream writer = context.openFileOutput(FILE_PATH,Context.MODE_APPEND);
            writer.write((score.getName()+","+score.getScore()+","+score.getTime()+"\r\n").getBytes());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<Score> getScores() {
        try (InputStream inputStream = context.openFileInput(FILE_PATH);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String playerName = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    String date = parts[2];
                    scores.add(new Score(playerName, score, date));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scores;
    }


    @Override
    public void deleteScore(String name, int scorenum, String time) {
        try {
            FileOutputStream writer;
            writer = context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE);
            writer.write("\n".getBytes());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i=0;i< allScores.size();i++){
            if (allScores.get(i).getName() == name &&
                allScores.get(i).getScore() == scorenum &&
                allScores.get(i).getTime() == time) {
                Score delete = allScores.get(i);
                allScores.remove(delete);
            }
        }
        for (int i=0; i<allScores.size(); i++){
            Score score= allScores.get(i);
            this.saveScore(score);
        }
    }

}
