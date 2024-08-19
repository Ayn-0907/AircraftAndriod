package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.ActivityManager;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.music.MyMediaPlayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    public static Socket socket;
    String tag = "MainActivity";

    public static BufferedReader in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        setContentView(R.layout.activity_main);
        RadioButton radio_off = findViewById(R.id.radio_off);
        radio_off.setChecked(true);
        Button button = (Button) findViewById(R.id.start);
        button.setOnClickListener(this);
        Button button1 = (Button) findViewById(R.id.connectButton);
        button1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start) {
            GameActivity.ifConnect=false;
            RadioButton radio_on = findViewById(R.id.radio_on);
            Intent intent = new Intent(MainActivity.this, OfflineActivity.class);
            intent.putExtra("music", radio_on.isChecked());
            startActivity(intent);
        }
        else if(v.getId() == R.id.connectButton){
            GameActivity.ifConnect=true;
            //构建AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityManager.getActivityManager().currentActivity());
            builder.setMessage("匹配中，请等待……");
            AlertDialog dialog = builder.create();
            dialog.show();
            // 在主线程中创建Handler实例
            mHandler = new Handler(getMainLooper()){
                //当数据处理子线程更新数据后发送消息给UI线程，UI线程更新UI
                @Override
                public void handleMessage(Message msg){
                    if(msg.what == 1){//成功连接
                        RadioButton radio_on = findViewById(R.id.radio_on);
                        if(radio_on.isChecked()){
                            BaseGame.bgm = new MyMediaPlayer(ActivityManager.getActivityManager().currentActivity(), R.raw.bgm);
                            BaseGame.bgm_boss= new MyMediaPlayer(ActivityManager.getActivityManager().currentActivity(), R.raw.bgm_boss);
                        }
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra("gameType",2);
                        startActivity(intent);
                    }
                }
            };
            new Thread(new NetConn(mHandler)).start();
        }
    }

    protected class NetConn extends Thread{
        private Handler toClientHandler;

        public NetConn(Handler myHandler){
            this.toClientHandler = myHandler;
        }
        @Override
        public void run(){
            try{
                socket = new Socket();
                socket.connect(new InetSocketAddress
                        ("10.0.2.2",9999),5000);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                //接收服务器返回的数据
                Thread receiveServerMsg =  new Thread(){
                    @Override
                    public void run(){
                        while(true)
                        {
                            try {
                                String content=in.readLine();
                                if (content.equals("2")){
                                    //发送消息给UI线程
                                    Log.i(tag,"send2");
                                    Message msg = new Message();
                                    msg.what = 1;
                                    toClientHandler.sendMessage(msg);
                                    break;
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                };
                receiveServerMsg.start();
            }catch(UnknownHostException ex){
                ex.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
