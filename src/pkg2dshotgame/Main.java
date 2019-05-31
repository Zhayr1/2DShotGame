/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Jesus
 */
public class Main extends StateBasedGame{
    //ID
    public static final int MENU    = 0;
    public static final int GAME    = 1;
    public static final int OPTIONS = 2;
    
    //GAME CONSTANTS
    public static final int SCREEN_X = 800;
    public static final int SCREEN_Y = 600;
    public static final int FRAMERATE = 60;
    
    
    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Main("2D ShotGame"));
            app.setDisplayMode(SCREEN_X, SCREEN_Y, false);
            app.setTargetFrameRate(FRAMERATE);
            app.setShowFPS(false);
            app.start();
        } catch(SlickException e) {
            System.out.println("Ex: "+e);
        }
    }

    public Main(String name) {
        super(name);
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        //this.addState(new Menu());
        this.addState(new Game());
        //this.addState(new Options());
    }
    
}
