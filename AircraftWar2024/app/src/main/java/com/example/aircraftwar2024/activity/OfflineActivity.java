package com.example.aircraftwar2024.activity;

import static com.example.aircraftwar2024.game.BaseGame.isMusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.ActivityManager;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.music.MyMediaPlayer;

public class OfflineActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        setContentView(R.layout.activity_offline);

        isMusic = getIntent().getBooleanExtra("music",false);
        if(isMusic){
            BaseGame.bgm = new MyMediaPlayer(ActivityManager.getActivityManager().currentActivity(), R.raw.bgm);
            BaseGame.bgm_boss= new MyMediaPlayer(ActivityManager.getActivityManager().currentActivity(), R.raw.bgm_boss);
        }

        Button button1=(Button)findViewById(R.id.easy);
        button1.setOnClickListener(this);
        Button button2=(Button)findViewById(R.id.normal);
        button2.setOnClickListener(this);
        Button button3=(Button)findViewById(R.id.hard);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent=new Intent(OfflineActivity.this, GameActivity.class);
        if(v.getId()==R.id.easy){
            intent.putExtra("gameType",1);
            startActivity(intent);
        }
        else if(v.getId()==R.id.normal){
            intent.putExtra("gameType",2);
            startActivity(intent);
        }
        else if(v.getId()==R.id.hard){
            intent.putExtra("gameType",3);
            startActivity(intent);
        }
    }
}