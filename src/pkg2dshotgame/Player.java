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
import org.newdawn.slick.util.pathfinding.Mover;

/**
 *
 * @author Jesus
 */
public class Player extends Entity implements Mover{
    //Variables Finales
    public final byte UP = 0;
    public final byte DOWN = 1;
    public final byte LEFT = 2;
    public final byte RIGHT = 3;
    public final byte VEL;
    //
    private ArrayList<Gun> gunList;
    private int currentGun;
    //
    private int health;
    private int invTime;
    private boolean isInv;
    private int score;
    private final Image img;
    //Variables de movimiento
    private boolean up,down,left,right;
    
    public Player(float x, float y, int width, int height, int ID) throws SlickException {
        super(x, y, width, height, ID);
        up = down = left = right = false;
        img = new Image("Assets/Player.png");
        int auxw,auxh;
        VEL = 3;
        health = 10;
        invTime = 500;
        isInv = false;
        score = 0;
        currentGun = 0;
        gunList = new ArrayList();
        gunList.add(new Gun("Assets/TestAssets/9mmFire.ogg","Assets/TestAssets/9mmLoad.ogg","Assets/TestAssets/9mmEmpty.ogg",15,45,250,1,2,500,false,Gun.NINEMM));
        gunList.add(new Gun("Assets/TestAssets/9mmFire.ogg","Assets/TestAssets/9mmLoad.ogg","Assets/TestAssets/9mmEmpty.ogg",150,300,100,1,1,750,true,Gun.UZI));
        gunList.add(new Gun("Assets/TestAssets/RifleFire.ogg","Assets/TestAssets/RifleLoad.ogg","Assets/TestAssets/RifleEmpty.ogg",7,21,500,2,5,1000,false,Gun.AWP));
    }
    public void updateMovement(int mx,int my,int delta){
        if(up)    super.setY(y - VEL);
        if(down)  super.setY(y + VEL);
        if(left)  super.setX(x - VEL);
        if(right) super.setX(x + VEL);
        this.updateTimers(delta);
    }
    public void render(Graphics g){
        g.setColor(Color.white);
        //g.fill(this);
        img.draw(super.getX(),super.getY());
    }
    public void updateTimers(int delta){
        if(isInv && invTime > 0){
            invTime -= delta;
        }else if(invTime <= 0){
            isInv = false;
            invTime = 500;
        }
    }
    public void hit(int dmg){
        if(!isInv){
            health -= dmg;
            isInv = true;
        }
    }
    //Setters
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
    public void addScore(int i){
        score += i;
    }
    public void shot(int xp,int yp,int x2,int y2){
        this.getGun().fire(xp,yp,x2,y2);
    }
    //Getters
    public Gun getGun(){
        return gunList.get(currentGun);
    }
    public void nextGun(){
        if(currentGun + 1 > gunList.size() - 1){
            currentGun = 0;
        }else{
            currentGun++;
        }
    }
    public void previousGun(){
        if(currentGun - 1 < 0){
            currentGun = gunList.size() - 1;
        }else{
            currentGun--;
        }
    }
    public int getCurrentGun(){
        return currentGun;
    }
    public int getHealth(){
        return health;
    }
    public int getScore(){
        return score;
    }
    public int getVel(){
        return VEL;
    }
}
