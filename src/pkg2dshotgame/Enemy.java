/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Jesus
 */
public class Enemy extends Entity{
    
    private int health;
    private float yp,xp,xe,ye,rp,theta;
    private boolean active;
    private Player targetPlayer;
    public Enemy(float x, float y, int width, int height, int ID) {
        super(x, y, width, height, ID);
        health = 10;
        active = false;
    }
    public void follow(Player p){
        //Version en cordenadas polares
        targetPlayer = p;
        xp = p.getX() + 10;
        yp = p.getY() + 10;
        xe = super.getX();
        ye = super.getY();
        rp = 1;
        float atan = (float) Math.atan2(ye-yp, xe-xp);
        theta = (float) (Math.PI/2 - atan);
        active = true;
    }
    public void hit(int dmg){
        health -= dmg;
    }
    public void updatePosition(int delta){
        if(active && health > 0){
            //Polares
            rp += delta * 0.15;
            super.setX((float) (this.xe - rp * Math.sin(theta) ));
            super.setY((float) (this.ye - rp * Math.cos(theta) )); 
            this.follow(targetPlayer);
        }else{
            active = false;
        }
    }
    public void render(Graphics g){
        if(active){
            g.setColor(Color.red);
            g.fill(this);
        }else{
            g.setColor(Color.gray);
            g.fill(this);
        }
    }    
}
