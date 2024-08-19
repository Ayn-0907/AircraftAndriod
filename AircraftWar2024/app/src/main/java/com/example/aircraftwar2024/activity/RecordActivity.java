package com.example.aircraftwar2024.activity;

import static com.example.aircraftwar2024.activity.GameActivity.FILE_PATH_EASY;
import static com.example.aircraftwar2024.activity.GameActivity.FILE_PATH_HARD;
import static com.example.aircraftwar2024.activity.GameActivity.FILE_PATH_MEDIUM;
import static com.example.aircraftwar2024.game.BaseGame.allScores;
import static com.example.aircraftwar2024.score.ScoreDAOImpl.FILE_PATH;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.ActivityManager;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.score.Score;
import com.example.aircraftwar2024.score.ScoreDAOImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener{
    ScoreDAOImpl scoreDAO = new ScoreDAOImpl(RecordActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        setContentView(R.layout.activity_record);

        Button button = (Button) findViewById(R.id.returnButton);
        button.setOnClickListener(this);

        scoreDAO.saveScore(new Score("test", BaseGame.getScore(), new Date()));
        allScores = scoreDAO.getScores();

        //获得Layout里面的ListView
        ListView list = (ListView) findViewById(R.id.RankList);
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(
                this,
                getData(),
                R.layout.listitem,
                new String[]{"rank","name","score","time"},
                new int[]{R.id.rank,R.id.name,R.id.score,R.id.time});

        //添加并且显示
        list.setAdapter(listItemAdapter);

        //添加单击监听
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Map<String, Object> clkmap = (Map<String, Object>) arg0.getItemAtPosition(arg2);
                //构建AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityManager.getActivityManager().currentActivity());
                builder.setMessage("确定删除第"+clkmap.get("rank").toString()+"条数据吗").setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        scoreDAO.deleteScore(clkmap.get("name").toString(),Integer.parseInt(clkmap.get("score").toString()),clkmap.get("time").toString());// 参数
                        //生成适配器的Item和动态数组对应的元素
                        SimpleAdapter listItemAdapter = new SimpleAdapter(
                                ActivityManager.getActivityManager().currentActivity(),
                                getData(),
                                R.layout.listitem,
                                new String[]{"rank","name","score","time"},
                                new int[]{R.id.rank,R.id.name,R.id.score,R.id.time});
                        //添加并且显示
                        list.setAdapter(listItemAdapter);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        TextView Difficulty = findViewById(R.id.Difficulty);
        if (FILE_PATH.equals(FILE_PATH_EASY)) {
            Difficulty.setText("难度：简单");
        } else if (FILE_PATH.equals(FILE_PATH_MEDIUM)) {
            Difficulty.setText("难度：中等");
        } else if (FILE_PATH.equals(FILE_PATH_HARD)) {
            Difficulty.setText("难度：困难");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.returnButton) {
            ActivityManager.getActivityManager().finishActivity(RecordActivity.class);
            ActivityManager.getActivityManager().finishActivity(GameActivity.class);
            ActivityManager.getActivityManager().finishActivity(OfflineActivity.class);
        }
    }

    private List<Map<String, Object>> getData() {

        // 从文件中读取得分数据并排序
        allScores.sort(Comparator.comparingInt(Score::getScore).reversed());

        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rank", "排名");
        map.put("name", "用户");
        map.put("score", "得分");
        map.put("time", "时间");
        listitem.add(map);
        for (int i = 0; i < allScores.size(); i++) {
            map = new HashMap<String, Object>();
            Score scorelist = allScores.get(i);
            int rank = i + 1;
            String name = scorelist.getName();
            int score = scorelist.getScore();
            String time = scorelist.getTime();
            map.put("rank", rank);
            map.put("name", name);
            map.put("score", score);
            map.put("time", time);
            listitem.add(map);
        }

        return listitem;
    }
}