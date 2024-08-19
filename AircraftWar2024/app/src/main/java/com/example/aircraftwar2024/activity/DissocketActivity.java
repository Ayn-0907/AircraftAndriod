package com.example.aircraftwar2024.activity;

import static com.example.aircraftwar2024.game.BaseGame.enemyScore;
import static com.example.aircraftwar2024.game.BaseGame.getScore;
import static com.example.aircraftwar2024.game.BaseGame.opscore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DissocketActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
//    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            MainActivity.socket.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        Log.i(BaseGame.TAG,"enter");
        ActivityManager.getActivityManager().addActivity(this);
        setContentView(R.layout.activity_dissocket);

        Button button = (Button) findViewById(R.id.returnButton);
        button.setOnClickListener(this);


        TextView myscore = (TextView) findViewById(R.id.myScore);
        myscore.setText("你的分数："+getScore());
        TextView opscore = (TextView) findViewById(R.id.opScore);
        opscore.setText("对方分数："+enemyScore);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.returnButton) {
            ActivityManager.getActivityManager().finishActivity(DissocketActivity.class);
            ActivityManager.getActivityManager().finishActivity(GameActivity.class);
            ActivityManager.getActivityManager().finishActivity(OfflineActivity.class);
        }
    }

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        super.onPointerCaptureChanged(hasCapture);
//    }
}
