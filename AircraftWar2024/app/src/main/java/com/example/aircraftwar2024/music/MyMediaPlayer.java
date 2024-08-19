package com.example.aircraftwar2024.music;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.aircraftwar2024.activity.GameActivity;

public class MyMediaPlayer {
    private MediaPlayer music;
    public MyMediaPlayer(Context context, int resid){
        music = MediaPlayer.create(context,resid);
    }

    public void startMusic(){
        music.start();
        music.setLooping(true);
    }

    public void stopMusic(){
        music.pause();
    }

    public void startMusicAgain(){
        music.seekTo(music.getCurrentPosition());
        music.start();
    }

    public void destroyMusic(){
        music.stop();
        music.release();
        music = null;
    }
}
