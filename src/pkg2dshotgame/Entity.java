/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author Jesus
 */
public class Entity extends Rectangle{
    
    private final int ID;
    
    public Entity(float x,float y, int ID){
        super(x,y,5,5);
        this.ID = ID;
    }
    public Entity(float x,float y,int width, int height, int ID){
        super(x,y,width,height);
        this.ID = ID;
    }
    //Setters
    
    //Getters
    public int getID(){
        return ID;
    }
}
