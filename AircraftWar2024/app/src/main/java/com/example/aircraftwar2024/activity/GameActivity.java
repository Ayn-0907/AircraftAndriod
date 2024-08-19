package com.example.aircraftwar2024.activity;

import static com.example.aircraftwar2024.activity.MainActivity.in;
import static com.example.aircraftwar2024.score.ScoreDAOImpl.FILE_PATH;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.aircraft.HeroAircraft;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.EasyGame;
import com.example.aircraftwar2024.game.HardGame;
import com.example.aircraftwar2024.game.MediumGame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import com.example.aircraftwar2024.ActivityManager;
import com.example.aircraftwar2024.music.MyMediaPlayer;
import com.example.aircraftwar2024.score.ScoreDAO;
import com.example.aircraftwar2024.score.ScoreDAOImpl;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    private int gameType=0;

    public static int screenWidth,screenHeight;

    public static Handler mHandler;

    public static final String FILE_PATH_EASY = "score_easy.txt";
    public static final String FILE_PATH_MEDIUM = "score_medium.txt";
    public static final String FILE_PATH_HARD = "score_hard.txt";
    public static boolean ifConnect;
    public static boolean enemyDied=false;
    String tag = "MainActivity";
    public static PrintWriter writer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);

        getScreenHW();

        // 步骤2：在主线程中创建Handler实例
        mHandler = new Mhandler();

        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
        }

        /*TODO:根据用户选择的难度加载相应的游戏界面*/
        BaseGame baseGameView = null;

        switch (gameType){
            case 1:
                baseGameView=new EasyGame(this);
                ScoreDAOImpl.FILE_PATH = FILE_PATH_EASY;
                break;
            case 2:
                baseGameView=new MediumGame(this);
                ScoreDAOImpl.FILE_PATH = FILE_PATH_MEDIUM;
                break;
            case 3:
                baseGameView=new HardGame(this);
                ScoreDAOImpl.FILE_PATH = FILE_PATH_HARD;
                break;
        }
        setContentView(baseGameView);
        if(ifConnect){
            new Thread(){
                @Override
                public void run(){
                    try {
                        String content= null;
                        while((content = in.readLine()) != null){
                            synchronized (this){
                                Log.i(tag,content);
                                if(content.equals("died")){
                                    Log.i(BaseGame.TAG,"enemydied");
                                    GameActivity.enemyDied=true;
                                }
                                else if(content!=null){
                                    BaseGame.enemyScore=Integer.parseInt(content);
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();

            new Thread(){
                @Override
                public void run(){
                    try {
                        writer = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(
                                        MainActivity.socket.getOutputStream(),"utf-8")),true);
                        while(true){
                            synchronized (this){
                                writer.println(BaseGame.score);
                                sleep(100);
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();
        }
        }

    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getDisplay().getRealMetrics(dm);

        //窗口的宽度
        screenWidth= dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + screenWidth + " screenHeight : " + screenHeight);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    // 步骤1：（自定义）新创建Handler子类(继承Handler类) & 复写handleMessage（）方法
    class Mhandler extends Handler {

        // 通过复写handlerMessage() 从而确定更新UI的操作
        @Override
        public void handleMessage(Message msg) {
            if(getIntent() != null){
                // 根据不同线程发送过来的消息，执行不同的UI操作
                // 根据 Message对象的what属性 标识不同的消息
                if(msg.what==1&&!ifConnect){
                    Intent intent = new Intent(GameActivity.this, RecordActivity.class);
                    startActivity(intent);
                }
                else if(msg.what==1&&GameActivity.enemyDied){
                    Log.i(BaseGame.TAG,"game over handleMessage");
                    Intent intent = new Intent(GameActivity.this, DissocketActivity.class);
                    Log.i(BaseGame.TAG,"tiaozhuan");
                    startActivity(intent);
                }
            }
        }
    }
}