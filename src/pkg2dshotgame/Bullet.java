/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

/**
 *
 * @author Jesus
 * 
 */
public class Bullet extends Entity{
    
    public final int LIFE_TIME_CONSTANT = 3000; // In MilliSeconds
    private final float BULLET_SPEED_CONSTANT;
    private int dmg;
    private float rp,xp,yp,theta,lifeTime;
    private boolean active,ready;
    
    public Bullet(float x, float y, int width, int height,float bulletSpeed, int ID) {
        super(x, y, width, height, ID);
        rp = xp = yp = theta = 0;
        dmg = 1;
        active = false;
        ready = true;
        lifeTime = LIFE_TIME_CONSTANT;
        this.BULLET_SPEED_CONSTANT = bulletSpeed;
    }
    public void render(Graphics g){
        if(active){
            g.setColor(Color.white);
            g.fill(this);
        }
    }
    public void shot(int xp,int yp,int x2,int y2){
        //Version en cordenadas polares
        yp += 10;
        xp += 10;
        rp = 1;
        float atan = (float) Math.atan2(yp-y2, xp-x2);
        theta = (float) (Math.PI/2 - atan);
        this.xp = xp;
        this.yp = yp;
        active = true;
        ready = false;
    }
    public void updatePosition(int delta){
        if(active){
            //Polares
            rp += delta * BULLET_SPEED_CONSTANT;
            super.setX((float) (this.xp - rp * Math.sin(theta) ));
            super.setY((float) (this.yp - rp * Math.cos(theta) )); 
            lifeTime -= delta;
            if(lifeTime <= 0){
                lifeTime = LIFE_TIME_CONSTANT;
                active = false;
                ready = true;
            }
        }
    }
    public boolean isActive(){
        return active;
    }
    public boolean isReady(){
        return ready;
    }
    public int getDmg(){
        return dmg;
    }
    //Setters
    public void setReady(){
        lifeTime = LIFE_TIME_CONSTANT;
        active = false;
        ready = true;
        super.setX(-500);
        super.setY(0);
    }
}
