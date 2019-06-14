/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 *
 * @author Jesus
 */
public class ColitionsManager {
    
    public static final int NOCOL = 0;
    public static final int UP    = 1;
    public static final int DOWN  = 2;
    public static final int LEFT  = 3;
    public static final int RIGHT = 4;
    
    public ColitionsManager(){
        
    }
    
    public boolean checkBasicColition(Shape a,Shape b){
        return a.intersects(b);
    }
    public boolean checkFutureCol(float xa, float ya,float wa,float ha,float xb, float yb,float wb,float hb){
        Rectangle rectA,rectB;
        rectA = new Rectangle(xa,ya,wa,ha);
        rectB = new Rectangle(xb,yb,wb,hb);
        return rectA.intersects(rectB);
    }
    public boolean checkFutureCol(float xp,float yp,float wp,float hp,Rectangle rb){
        Rectangle rectA,rectB;
        rectA = new Rectangle(xp,yp,wp,hp);
        rectB = rb;
        return rectB.intersects(rectA);
    }
    public int checkCol(Shape a,Shape b){
        float ax1,ax2,ay1,ay2,bx1,bx2,by1,by2;
        //A - player
        ax1 = a.getX();
        ax2 = a.getX() + a.getWidth();
        ay1 = a.getY();
        ay2 = a.getY() + a.getHeight();
        //B
        bx1 = b.getX();
        bx2 = b.getX() + b.getWidth();
        by1 = b.getY();
        by2 = b.getY() + b.getHeight();
        if( (ax2 > bx1) && (ax1 < bx2) &&( ay2 == by1 || (ay2 <= by1 && ay1 >= by1) ) ){
            a.setY(ay1 - (ay2 - by1));
            return UP;
        }else if( (ax2 > bx1 +5 && ax1 < bx2 -5) && ( ay1 == by2 || (ay1 <= by2 && ay2 >= by2) ) ){
            a.setY(by2);
            return DOWN;
        }else if( ( (ax2 <= bx1 + 5 ) && (ax2 > bx1 ) ) && ay2 >= by1 && ay1 <= by2){
            a.setX(bx1 - a.getWidth() - 1);
            return LEFT;
        }else if( ( (ax1 >= bx2 - 5 ) && (ax1 < bx2 ) ) && ay2 >= by1 && ay1 <= by2 ){
            a.setX(bx2);
            return RIGHT;
        }
        return NOCOL;
    }
}
