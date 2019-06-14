/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Jesus
 */
public class Enemy extends Entity{
    
    //TEST
    public final byte UP = 0;
    public final byte DOWN = 1;
    public final byte LEFT = 2;
    public final byte RIGHT = 3;
    public final byte VEL;
    private boolean up,down,left,right;
    private int refreshRate; //In Milliseconds
    private int refreshConstant = 150;
    private Sound enemyHit;
    private Image img,deathimg,rotateImg,rotateAuxImg,rotateAuxImg2;
    //\TEST
    
    private int health;
    private float yp,xp,xe,ye,rp,theta;
    private boolean active;
    private Player targetPlayer;
    public Enemy(float x, float y, int width, int height, int ID) throws SlickException {
        super(x, y, width, height, ID);
        health = 10;
        active = false;
        enemyHit = new Sound("Assets/TestAssets/zombieHit.ogg");
        //TEST
        refreshRate = refreshConstant;
        up = down = left = right = false;    
        VEL = 1;
        img = new Image("Assets/enemy.png");
        rotateImg  = new Image("Assets/enemy.png");
        rotateAuxImg = new Image("Assets/enemy.png");
        rotateAuxImg2 = new Image("Assets/enemy.png");
        deathimg = new Image("Assets/deathEnemy.png");
        rotateAuxImg.rotate(90);
        rotateAuxImg2.rotate(180);
        //\TEST
    }
    //\TEST
    private void updateMovement(){
        if(active){
            if(up)    super.setY(y - VEL);
            if(down)  super.setY(y + VEL);
            if(left)  super.setX(x - VEL);
            if(right) super.setX(x + VEL);
        }
    }    
    //\TEST
    public void follow(Player p){
        if(health > 0){
        //Version en cordenadas polares
            if(targetPlayer != p) targetPlayer = p;
            xp = p.getX();
            yp = p.getY();
            xe = super.getX();
            ye = super.getY();
            //rp = 1;
            //float atan = (float) Math.atan2(ye-yp, xe-xp);
            //theta = (float) (Math.PI/2 - atan);
            active = true;
            if(up){
                rotateImg = img.getFlippedCopy(false, true);
            }
            if(down){
                rotateImg = img;
            }
            if(left){
                rotateImg = rotateAuxImg;
            }
            if(right){
                rotateImg = rotateAuxImg2;
            }
        }
        //TEST
        
        //\TEST
    }
    public void hit(int dmg){
        if(active){
            health -= dmg;
            enemyHit.play(1,0.1f);
            if(health <= 0) targetPlayer.addScore(1);
        }
    }
    public void updatePosition(int delta){
        refreshRate -= delta;
        this.updateMovement();
        if(active && health > 0 && refreshRate <= 0){
            //Polares
            //rp += delta * 0.15;
            //super.setX((float) (this.xe - rp * Math.sin(theta) ));
            //super.setY((float) (this.ye - rp * Math.cos(theta) )); 
            //TEST
            refreshRate = refreshConstant;
            if(xp >= xe){
                setDir(true,RIGHT);
                setDir(false,LEFT);
            }
            if(xp < xe){
                setDir(true,LEFT);
                setDir(false,RIGHT);
            }
            if(yp < ye){
                setDir(true,UP);
                setDir(false,DOWN);
            }
            if(yp >= ye){
                setDir(true,DOWN);
                setDir(false,UP);
            }
            //\TEST
            this.follow(targetPlayer);
        }else if(health <= 0){
            active = false;
        }
    }
    //TEST
    public void setDir(boolean set,int key){
        switch(key){
            case UP:
                up = set;
                break;
            case DOWN:
                down = set;
                break;
            case LEFT:
                left = set;
                break;
            case RIGHT:
                right = set;
                break;
            default:
                //Nada
                System.out.println("Tecla invalida");
                break;
        }
    }    
    //\TEST
    public void render(Graphics g){
        if(active){
            //g.fill(this);
            rotateImg.draw(super.getX(),super.getY());
        }else{
            //g.fill(this);
            deathimg.draw(super.getX(),super.getY());
        }
    }    
    public boolean isActive(){
        return active;
    }
}
