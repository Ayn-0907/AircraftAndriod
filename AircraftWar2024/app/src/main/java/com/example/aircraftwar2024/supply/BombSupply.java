package com.example.aircraftwar2024.supply;


import static com.example.aircraftwar2024.game.BaseGame.isMusic;
import static com.example.aircraftwar2024.game.BaseGame.mySoundPool;
import static com.example.aircraftwar2024.game.BaseGame.soundPoolMap;

import java.util.LinkedList;
import java.util.List;

/**
 * 炸弹道具，自动触发
 * <p>
 * 使用效果：清除界面上除BOSS机外的所有敌机（包括子弹）
 * <p>
 * 【观察者模式】
 *
 * @author hitsz
 */
public class BombSupply extends AbstractFlyingSupply {


    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate() {
        if(isMusic){
            mySoundPool.mysp.play(soundPoolMap.get(1),1,1,0,0,1.0f);
        }
        System.out.println("BombSupply active");
    }

}
