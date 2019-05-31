/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author Jesus
 */
public class Game extends BasicGameState{

    private static int ID = 1;
    private int globalTime;
    private int auxTime;
    //Input
    private Input input;
    private int mx,my;
    private boolean auxDebug;
    //
    private int auxMb1,auxMb2;
    private int shotDelay;
    //Player
    private Player p;
    //Enemys
    private ArrayList<Enemy> enemyList;
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        p = new Player(500,400,20,20,1);
        enemyList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            enemyList.add(new Enemy(100,100,20,20,3));
        }
        input = gc.getInput();
        globalTime = 0;
        auxTime = auxMb1 = auxMb2 = 0;
        auxDebug = false;
        shotDelay = 300; // In MilliSeconds
        gc.setMouseCursor("Assets/Cursor.png", 1, 1 );
        gc.setVSync(true);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        this.renderMap(g);
        if(auxDebug) this.renderDebug(gc,g);
        this.renderBullets(g);
        p.render(g);
        enemyList.get(0).render(g);
        g.setColor(Color.red);
        if(Mouse.isInsideWindow()) g.fillOval(mx - 5, my - 5, 10, 10); // Mouse Point
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        this.updateEnviromentVars(i);
        this.updateMovements(i);
        this.updateEntityColitions();
    }
    private void renderDebug(GameContainer gc,Graphics g){
        g.setColor(Color.yellow);
        //Strings
        g.drawString("Time: "+globalTime, 10, 50);
        g.drawString("Mx: "+Mouse.getX(),10,80);
        g.drawString("My: "+(Math.abs( Mouse.getY() - Main.SCREEN_Y ) ),10,100);
        g.drawString("Px: "+p.getX()+"\nPy: "+p.getY(), 10, 120);
        //Figuras Geometricas y Lineas
    }
    private void renderMap(Graphics g){
        g.setBackground(Color.black);
    }
    private void renderBullets(Graphics g){
        int auxBalas = p.getBullets().size();
        for (int i = 0; i < auxBalas; i++) {
            if(p.getBullets().get(i).isActive()){
                p.getBullets().get(i).render(g);
            }
        }
    }
    private void updateMovements(int delta){
        //PlayerMovement
        p.updateMovement();
        //Enemies Movement
        enemyList.get(0).updatePosition(delta);
        //BulletsMovement
        int auxBalas = p.getBullets().size();
        for (int i = 0; i < auxBalas; i++) {
            if(p.getBullets().get(i).isActive()){
                p.getBullets().get(i).updatePosition(delta);
            }
        }
        //Input Manager
        this.InputManager(delta);
        
    }
    private void updateEnviromentVars(int delta){
        auxTime+= delta;
        if(auxTime >= 1000){
            auxTime = 0;
            globalTime++;
        }
        mx = Mouse.getX();
        my = Math.abs( Mouse.getY() - Main.SCREEN_Y );   
    }
    private void updateEntityColitions(){
        for (int i = 0; i < p.getBullets().size(); i++) {
            Bullet auxB = p.getBullets().get(i);
            if(auxB.isActive() && ColitionsManager.checkBasicColition(auxB,enemyList.get(0))){
                enemyList.get(0).hit(auxB.getDmg());
            } 
        }
    }
    private void InputManager(int delta){
        //KeyBoard Inputs
        if(input.isKeyDown(Input.KEY_UP)){
            p.setDir(true, p.UP);
        }else{
            p.setDir(false, p.UP);
        }
        if(input.isKeyDown(Input.KEY_DOWN)){
            p.setDir(true, p.DOWN);
        }else{
            p.setDir(false, p.DOWN);
        }
        if(input.isKeyDown(Input.KEY_LEFT)){
            p.setDir(true, p.LEFT);
        }else{
            p.setDir(false, p.LEFT);
        }
        if(input.isKeyDown(Input.KEY_RIGHT)){
            p.setDir(true, p.RIGHT);
        }else{
            p.setDir(false, p.RIGHT);
        }
        if(input.isKeyDown(Input.KEY_F1)) enemyList.get(0).follow(p);
        if(input.isKeyPressed(Input.KEY_F5)) auxDebug = !auxDebug;
        //Mouse Inputs
        if(Mouse.isButtonDown(0) && auxMb1 == 0){
            System.out.println("Click izq");
            for (int i = 0; i < p.getBullets().size(); i++) {
                if(p.getBullets().get(i).isReady()){
                    p.getBullets().get(i).shot((int)p.getX(),(int)p.getY(),mx, my);
                    System.out.println("Shot: "+(i+1));
                    break;
                }
            }
            auxMb1 = shotDelay;
        }
        if(Mouse.isButtonDown(1) && auxMb2 == 0){
            System.out.println("Click der");
            auxMb2 = shotDelay;
        }
        if(auxMb1 > 0){
            auxMb1 -= delta;
            if(auxMb1 < 0) auxMb1 = 0;
        }
        if(auxMb2 > 0){
            auxMb2 -= delta;
            if(auxMb2 < 0) auxMb2 = 0;
        }
    }
}
