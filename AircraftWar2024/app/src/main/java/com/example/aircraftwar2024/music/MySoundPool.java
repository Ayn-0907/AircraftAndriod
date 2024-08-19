package com.example.aircraftwar2024.music;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import java.util.HashMap;

public class MySoundPool {
    public SoundPool mysp;
    public MySoundPool() {

        AudioAttributes audioAttributes = null;
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mysp = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();
    }
}
