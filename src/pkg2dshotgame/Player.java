/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Jesus
 */
public class Player extends Entity{
    //Variables Finales
    public final byte UP = 0;
    public final byte DOWN = 1;
    public final byte LEFT = 2;
    public final byte RIGHT = 3;
    public final byte VEL;
    //
    private int health;
    private int score;
    private List<Bullet> bullets;
    //Variables de movimiento
    private boolean up,down,left,right;
    
    public Player(float x, float y, int width, int height, int ID) {
        super(x, y, width, height, ID);
        up = down = left = right = false;
        VEL = 5;
        health = 10;
        score = 0;
        bullets = new ArrayList();
        for (int i = 0; i < 10; i++) {
            bullets.add(new Bullet(-500, 0,5,5,1f,0));
        }
    }
    
    public void updateMovement(){
        if(up)    super.setY(y - VEL);
        if(down)  super.setY(y + VEL);
        if(left)  super.setX(x - VEL);
        if(right) super.setX(x + VEL);
    }
    public void render(Graphics g){
        g.setColor(Color.white);
        g.fill(this);
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
    //Getters
    public List<Bullet> getBullets(){
        return bullets;
    }
    public int getHealth(){
        return health;
    }
    public int getScore(){
        return score;
    }
}
