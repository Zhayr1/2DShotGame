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
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TileSet;
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
    private int score;
    private Image img,aimImg;
    //Variables de movimiento
    private boolean up,down,left,right;
    
    public Player(float x, float y, int width, int height, int ID) throws SlickException {
        super(x, y, width, height, ID);
        up = down = left = right = false;
        img = new Image("Assets/Player.png");
        aimImg = new Image("Assets/PlayerAim.png");
        int auxw,auxh;
        auxw = aimImg.getWidth();
        auxh = aimImg.getHeight();
        aimImg.setCenterOfRotation(auxw/2, auxh/2);
        VEL = 3;
        health = 10;
        score = 0;
        currentGun = 0;
        gunList = new ArrayList();
        gunList.add(new Gun("Assets/TestAssets/9mmFire.ogg",150,1,3));
    }
    public void updateMovement(int mx,int my){
        if(up)    super.setY(y - VEL);
        if(down)  super.setY(y + VEL);
        if(left)  super.setX(x - VEL);
        if(right) super.setX(x + VEL);
    }
    public void render(Graphics g){
        g.setColor(Color.white);
        //g.fill(this);
        img.draw(super.getX(),super.getY());
        aimImg.draw(super.getX(),super.getY());
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
