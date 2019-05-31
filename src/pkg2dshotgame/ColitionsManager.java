/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import org.newdawn.slick.geom.Shape;

/**
 *
 * @author Jesus
 */
public class ColitionsManager {
    
    public ColitionsManager(Shape a, Shape b){
        
    }
    
    public static boolean checkBasicColition(Shape a, Shape b){
        return a.intersects(b);
    }
    
}
