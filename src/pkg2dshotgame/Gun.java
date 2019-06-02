/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Jesus
 */
public class Gun {
    
    private final int ID;
    private final int chargerSize;
    private final float shotSpeedConstant;
    private final ArrayList<Bullet> arrB;
    private final int reloadConstant = 1000;
    private int totalBullets;
    private int currentBullets;
    private boolean reloading;
    private float reloadDelay;
    private final Sound shotSound;
    private final Sound reloadSound;
    private final Sound emptySound;
    
    public Gun(String ShotSoundRef,int chargerSize,float shotSpeed,int ID) throws SlickException{
        this.ID = ID;
        shotSound = new Sound(ShotSoundRef);
        reloadSound = new Sound("Assets/TestAssets/9mmLoad.ogg");
        emptySound = new Sound("Assets/TestAssets/9mmEmpty.ogg");
        shotSpeedConstant = shotSpeed;
        this.chargerSize = chargerSize;
        reloading = false;
        reloadDelay = reloadConstant;
        this.init();
        arrB = new ArrayList<>();
        for (int i = 0; i < chargerSize; i++) {
            arrB.add(new Bullet(-500, 0, 2, 2, shotSpeed, ID));
        }
    }
    
    private void init(){
        totalBullets = chargerSize;
        currentBullets = chargerSize;
    }
    public boolean reload(){
        if(totalBullets > 0 && currentBullets != chargerSize){
            reloading = true;
            reloadSound.play(1,0.9f);        
            while(currentBullets < chargerSize){
                if(totalBullets > 0){
                    currentBullets++;
                    totalBullets--;
                }else{
                    return false;
                }
            }
        }
        return true;
    }
    public void reloadDelay(int delta){
        if(reloading){
            reloadDelay -= delta;
            if(reloadDelay <= 0){
                reloading = false;
                reloadDelay = reloadConstant;
            }
        }
    }
    public boolean fire(int xp,int yp,int x2,int y2){
        if(currentBullets > 0 && !reloading){
            arrB.get(currentBullets - 1).shot(xp, yp, x2, y2);
            shotSound.play(1,0.2f);
            currentBullets--;
        }else if(currentBullets == 0 && totalBullets > 0 && !reloading){
            this.reload();
        }else if(currentBullets == 0 && !reloading && totalBullets == 0){
            emptySound.play(1,0.9f);
        }else{
            return false;
        }
        return true;
    }
    //Setters
    public void addBullets(int bn){
        totalBullets+=bn;
    }
    public void emptyGun(){
        currentBullets = totalBullets = 0;
    }
    //Getters
    public int getCurrentBullets(){
        return currentBullets;
    }
    public int getTotalBullets(){
        return totalBullets;
    }
    public int getChargerSize(){
        return chargerSize;
    }
    public float getShotSpeed(){
        return shotSpeedConstant;
    }
    public ArrayList<Bullet> getBullets(){
        return arrB;
    }    
    public int getID(){
        return ID;
    }
}
