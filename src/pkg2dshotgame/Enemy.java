/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

/**
 *
 * @author Jesus
 */
public class Enemy extends Entity implements Mover{
    
    //TEST
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public final int VEL;
    private int refreshRate; //In Milliseconds
    private final int refreshConstant = 1500;
    private int currentStep,auxEndPath;
    private boolean nullPath;
    private Path path;
    private boolean pathEnded;
    private final Sound enemyHit;
    private final Image deathimg;
    private final Image rotateImg;
    private final Image rotateAuxImg;
    private final Image rotateAuxImg2;
    private final ColitionsManager cm;
    
    private int health;
    private boolean active;
    private Player targetPlayer;
    private Point oldP_Pos;
    public Enemy(float x, float y, int width, int height, int ID) throws SlickException {
        super(x, y, width, height, ID);
        health = 10;
        active = false;
        currentStep = 0;
        auxEndPath = 0;
        path = new Path();
        pathEnded = false;
        nullPath = false;
        enemyHit = new Sound("Assets/TestAssets/zombieHit.ogg");
        //TEST
        refreshRate = refreshConstant;
        VEL = 2;
        rotateImg  = new Image("Assets/enemy.png");
        rotateAuxImg = new Image("Assets/enemy.png");
        rotateAuxImg2 = new Image("Assets/enemy.png");
        deathimg = new Image("Assets/deathEnemy.png");
        rotateAuxImg.rotate(90);
        rotateAuxImg2.rotate(180);
        cm = new ColitionsManager();
        //\TEST
    }
    //\TEST
    private void updateMovement(){
        if(targetPlayer != null){
            if(this.intersects(targetPlayer) && this.isActive()){
                targetPlayer.hit(1);
            }
        }
    }    
    //\TEST
    public void follow(Player p,Path path,ArrayList<Enemy> eList){
        for(Enemy e: eList){
            
        }
        if(health > 0 && !p.intersects(this)){
            active = true;
            targetPlayer = p;
            oldP_Pos = new Point(p.getX(),p.getY());
            this.path = path;
            currentStep = 1;
            pathEnded = false;
            nullPath = false;
            try{
                auxEndPath = path.getLength();
            }catch(NullPointerException e){
                nullPath = true;
                auxEndPath = 1;
            }
        }
    }
    public void hit(int dmg){
        if(active){
            health -= dmg;
            enemyHit.play(1,0.1f);
            if(health <= 0) targetPlayer.addScore(1);
        }
    }
    public void updatePosition(int delta,TileMapAStar tmap,Player p,ArrayList eList){
        refreshRate -= delta;
        this.updateMovement();
        if(active && health > 0 && !this.intersects(p) && !nullPath) move(super.getX()/32,super.getY()/32,path.getStep(currentStep).getX(),path.getStep(currentStep).getY(),p,eList);
        if(active && health > 0 && (refreshRate <= 0 || pathEnded) ){
            refreshRate = this.refreshConstant;
            if(pathEnded || ( oldP_Pos.getX() != p.getX() || oldP_Pos.getY() != p.getY()) ) {
                this.follow(targetPlayer,tmap.getPathFinder().findPath(targetPlayer,(int)(super.getX()/32),
                (int)(super.getY()/32),(int)(targetPlayer.getX()/32), (int)(targetPlayer.getY()/32)),eList);
            }
        }else if(health <= 0){
            active = false;
        }
    }
    public void move(float xi,float yi,float xf,float yf,Player p,ArrayList<Enemy> eList){
        int padd = 12;
        if(xi > xf){
            //LEFT
            if(cm.checkFutureCol(xf,yf,padd,Enemy.LEFT,p,this,eList)) super.setX(super.getX() - VEL);
        }
        if(xi < xf){
            //RIGHT
            if(cm.checkFutureCol(xf,yf,padd,Enemy.RIGHT,p,this,eList)) super.setX(super.getX() + VEL);
        }
        if(yi > yf){
            //UP
            if(cm.checkFutureCol(xf,yf,padd,Enemy.UP,p,this,eList)) super.setY(super.getY() - VEL);
        }
        if(yi < yf){
            //DOWN
            if(cm.checkFutureCol(xf,yf,padd,Enemy.DOWN,p,this,eList)) super.setY(super.getY() + VEL);
        }
        if(xi == xf && yi == yf){
            if((currentStep) == auxEndPath - 1){
                pathEnded = true;
            }
            if((currentStep) < auxEndPath - 1) currentStep++;
        }
    }
    public void render(Graphics g){
        if(active){
            rotateImg.draw(super.getX(),super.getY());
        }else{
            deathimg.draw(super.getX(),super.getY());
        }
    }    
    public boolean isActive(){
        return active;
    }
    public int getVel(){
        return VEL;
    }
}
