/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Jesus
 */
public class AmmoPack extends Entity{
    
    private final int ammo;
    private Image img;
    private boolean active;
    private Sound ammoGrab;
    
    public AmmoPack(String ref,float x, float y, int width, int height,int AmmoAmount, int ID) throws SlickException {
        super(x, y, width, height, ID);
        ammo = AmmoAmount;
        img = new Image(ref);
        img = img.getScaledCopy(width, height);
        active = true;
        ammoGrab = new Sound("Assets/TestAssets/AmmoPackGrab.ogg");
    }
    
    public void render(){
        if(active){
            img.draw(x,y);
        }
    }
    public int getAmmo(){
        ammoGrab.play(1,0.9f);
        return ammo;
    }
    public boolean isActive(){
        return active;
    }
    public void disable(){
        active = false;
    }
    
}
