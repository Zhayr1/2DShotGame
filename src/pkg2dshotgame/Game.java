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
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 *
 * @author Jesus
 */
public class Game extends BasicGameState{

    private static int ID = 1;

    //Mapa
    private final int WORLD_SIZE_X = 3200;
    private final int WORLD_SIZE_Y = 3200;
    TiledGameMap map;
    //Camara
    private int offsetMaxX,offsetMaxY,offsetMinX,offsetMinY;
    private final int VIEWPORT_SIZE_X = Main.SCREEN_X;
    private final int VIEWPORT_SIZE_Y = Main.SCREEN_Y;
    private float camX,camY;
    //    
    private int globalTime;
    private int globalScore;
    private int globalWave;
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
    private int auxGunSize;
    private Gun auxGun;
    //Enemys
    private ArrayList<Enemy> enemyList;
    //AStar
    //Bloques de colision
    private ArrayList<Rectangle> paredes;
    public ColitionsManager cmP,cmE;
    private GameContainer gameC;
    private AmmoPack ap1,ap2,ap3;
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        p = new Player(500,400,32,32,1);
        auxGun = p.getGun();
        auxGunSize = auxGun.getChargerSize();
        enemyList = new ArrayList<>();
        gameC = gc;
        this.initCam();
        for (int i = 0; i < 10; i++) {
            enemyList.add(new Enemy(100 * (i+1),100 * (i+1),32,32,3));
        }
        input = gc.getInput();
        globalTime = globalScore = globalWave = 0;
        auxTime = auxMb1 = auxMb2 = 0;
        auxDebug = false;
        ap1 = new AmmoPack("Assets/AmmoPack.png",400,500,20,20,900,5);
        paredes = new ArrayList();
        //paredes.add(new Rectangle(0,0,WORLD_SIZE_X,32));
        //paredes.add(new Rectangle(0,0,32,WORLD_SIZE_Y));
        paredes.add(new Rectangle(0,WORLD_SIZE_X - 32,WORLD_SIZE_X,32));
        //paredes.add(new Rectangle(WORLD_SIZE_Y - 32,0,32,WORLD_SIZE_Y));
        //paredes.add(new Rectangle(128,256,320,32));
        shotDelay = 70; // In MilliSeconds
        gc.setMouseCursor("Assets/Cursor.png", 1, 1 );
        cmP = new ColitionsManager();
        cmE = new ColitionsManager();
        map = new TiledGameMap(new TiledMap("Assets/TestMap.tmx"),paredes);
        
    }
    private void initCam(){
        offsetMaxX = WORLD_SIZE_X - VIEWPORT_SIZE_X;
        offsetMaxY = WORLD_SIZE_Y - VIEWPORT_SIZE_Y;
        offsetMinX = 0;
        offsetMinY = 0;       
    }    
    private void updateCameraOffSets(){
        camX = p.getX() - VIEWPORT_SIZE_X / 2;
        camY = p.getY() - VIEWPORT_SIZE_Y / 2;    
        
        if (camX > offsetMaxX){
            camX = offsetMaxX;
        }else if (camX < offsetMinX){
            camX = offsetMinX;
        }
        if (camY > offsetMaxY){
            camY = offsetMaxY;
        }else if (camY < offsetMinY){
            camY = offsetMinY;
        }
    }
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.translate(-camX, -camY);
        this.renderMap(g);
        this.renderBullets(g);
        if(ap1.isActive()) ap1.render();
        this.renderEnemies(g);
        g.setColor(Color.white);
        p.render(g);
        if(auxDebug){
            this.renderDebug(gc,g);
        }else{
            this.renderHUD(g);
        }        
        if(Mouse.isInsideWindow()) g.fillOval(camX+mx - 5, camY+my - 5, 10, 10); // Mouse Point
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        this.updateEnviromentVars(i);
        this.updateMovements(i);
        this.updateEntityColitions();
        this.updateCameraOffSets();
    }
    private void renderDebug(GameContainer gc,Graphics g){
        g.setColor(Color.yellow);
        //Strings
        g.drawString("Time: "+globalTime, camX +10, camY +50);
        g.drawString("Mx: "+Mouse.getX(),camX +10,camY +80);
        g.drawString("My: "+(Math.abs( Mouse.getY() - Main.SCREEN_Y ) ),camX +10,camY +100);
        g.drawString("Px: "+p.getX()+"\nPy: "+p.getY(), camX +10, camY +120);
        //Figuras Geometricas y Lineas
    }
    private void renderEnemies(Graphics g){
        for (int i = 0; i < enemyList.size(); i++) {
            if(!enemyList.get(i).isActive()) {
                enemyList.get(i).render(g);
            }else{
                enemyList.get(i).render(g);
            }    
        }
    }
    private void renderHUD(Graphics g){
        g.setColor(Color.white);
        String auxS = "Hp: "+p.getHealth()+"\nBalas: "+auxGun.getCurrentBullets()+"/"+auxGun.getTotalBullets()+
                      "\nScore: "+p.getScore()+"\n";
        auxS += "Wave: "+globalWave;
        g.drawString(auxS, camX + 10, camY + 10);
    }
    private void renderMap(Graphics g){
        g.setBackground(Color.black);
        map.getTiledMap().render(0, 0);
        g.setColor(Color.darkGray);
        for (int i = 0; i < paredes.size(); i++) {
            g.fill((Shape) paredes.get(i));
        }
    }
    private void renderBullets(Graphics g){
        int auxBalas = auxGunSize;
        for (int i = 0; i < auxBalas; i++) {
            if(auxGun.getBullets().get(i).isActive()){
                auxGun.getBullets().get(i).render(g);
            }
        }
    }
    private void updateMovements(int delta){
        //PlayerMovement
        p.updateMovement((int)(mx+camX),(int)(my+camY));
        //Enemies Movement
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).updatePosition(delta);    
        }
        //BulletsMovement
        int auxBalas = auxGunSize;
        for (int i = 0; i < auxBalas; i++) {
            if(auxGun.getBullets().get(i).isActive()){
                auxGun.getBullets().get(i).updatePosition(delta);
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
        auxGun.reloadDelay(delta);
    }
    private void updateEntityColitions(){
        for (int i = 0; i < auxGunSize; i++) {
            Bullet auxB = auxGun.getBullets().get(i);
            if(auxB.isActive()){
                for (int j = 0; j < enemyList.size(); j++) {
                    if(enemyList.get(j).isActive()){
                        if(cmE.checkBasicColition(auxB,enemyList.get(j))){
                            enemyList.get(j).hit(auxB.getDmg());
                            auxB.setReady();
                        }
                    }
                }
            } 
        }
        if(p.intersects(ap1) && ap1.isActive()){
            p.getGun().addBullets(ap1.getAmmo());
            ap1.disable();
        }
        //Player Colitions
        //this.rigidBodyColitions(p);
        //EnemyColitions
        for (int i = 0; i < enemyList.size(); i++) {
            if(enemyList.get(i).isActive())this.rigidBodyColitions(enemyList.get(i));
        }
    }
    private void rigidBodyColitions(Enemy e){
        for (int i = 0; i < paredes.size(); i++) {
            if(e.intersects(paredes.get(i))){
                switch(cmE.checkCol(e, paredes.get(i))){
                    case ColitionsManager.UP:
                        e.setDir(false, p.DOWN);
                        break;
                    case ColitionsManager.DOWN:
                        e.setDir(false, p.UP);
                        break;
                    case ColitionsManager.LEFT:
                        e.setDir(false, p.RIGHT);
                        break;
                    case ColitionsManager.RIGHT:
                        e.setDir(false, p.LEFT);
                        break;
                }
            }
        }
        for (int i = 0; i < enemyList.size(); i++) {
            if(e.intersects(enemyList.get(i)) && e != enemyList.get(i) && enemyList.get(i).isActive()){
                switch(cmE.checkCol(e, enemyList.get(i))){
                    case ColitionsManager.UP:
                        e.setDir(false, p.DOWN);
                        break;
                    case ColitionsManager.DOWN:
                        e.setDir(false, p.UP);
                        break;
                    case ColitionsManager.LEFT:
                        e.setDir(false, p.RIGHT);
                        break;
                    case ColitionsManager.RIGHT:
                        e.setDir(false, p.LEFT);
                        break;
                }
            }
        }
    }
    private void checkFutureMovementCol(boolean Vertical,boolean Horizontal){
        int auxC = 0;
        Rectangle auxR;
        if(!Vertical && !Horizontal){
            //UP
            for (int i = 0; i < paredes.size(); i++) {
                auxR = paredes.get(i);
                if(cmP.checkFutureCol(p.getX(),p.getY() - p.getVel(),p.getWidth(),p.getHeight(),auxR)){
                   auxC++; 
                    //p.setY(p.getY() - p.getVel());
                }
            }
            if(auxC == 0) p.setY(p.getY() - p.getVel());
        }
        auxC = 0;
        if(Vertical && !Horizontal){
            //DOWN
            for (int i = 0; i < paredes.size(); i++) {
                auxR = paredes.get(i);
                if(cmP.checkFutureCol(p.getX(),p.getY() + p.getVel(),p.getWidth(),p.getHeight(),auxR)){
                    auxC++;
                }
            }
            if(auxC == 0) p.setY(p.getY() + p.getVel());
        }
        auxC = 0;
        if(!Vertical && Horizontal){
            //LEFT
            for (int i = 0; i < paredes.size(); i++) {
                auxR = paredes.get(i);
                if(cmP.checkFutureCol(p.getX() - p.getVel(),p.getY(),p.getWidth(),p.getHeight(),auxR)){
                    auxC++;
                }
            }
            if(auxC == 0) p.setX(p.getX() - p.getVel());
        }
        auxC = 0;
        if(Vertical && Horizontal){
            //RIGHT
            for (int i = 0; i < paredes.size(); i++) {
                auxR = paredes.get(i);
                if(cmP.checkFutureCol(p.getX() + p.getVel(),p.getY(),p.getWidth(),p.getHeight(),auxR)){
                    auxC++;
                }
            }
            if(auxC == 0) p.setX(p.getX() + p.getVel());
        }
    }
    private void InputManager(int delta){
        //KeyBoard Inputs
        if(input.isKeyDown(Input.KEY_W)){
            this.checkFutureMovementCol(false, false);
        }
        if(input.isKeyDown(Input.KEY_S)){
            this.checkFutureMovementCol(true, false);
        }
        if(input.isKeyDown(Input.KEY_A)){
            this.checkFutureMovementCol(false, true);
        }
        if(input.isKeyDown(Input.KEY_D)){
            this.checkFutureMovementCol(true, true);
        }
        //F's
        
        if(input.isKeyDown(Input.KEY_F1)){
            for (int i = 0; i < enemyList.size(); i++) {
                enemyList.get(i).follow(p);
            }
        }
        if(input.isKeyPressed(Input.KEY_F5)){
            auxDebug = !auxDebug;
            gameC.setShowFPS(auxDebug);
        }
        if(input.isKeyPressed(Input.KEY_R)){
            p.getGun().reload();
        }
        //
        //Mouse Inputs
        if(Mouse.isButtonDown(0) && auxMb1 == 0){
            //CLICK IZQUIERDO
            for (int i = 0; i < auxGunSize; i++) {
                if(auxGun.getBullets().get(i).isReady()){
                    p.shot((int)p.getX(),(int)p.getY(),(int)(mx+camX),(int)(my+camY));
                    break;
                }
            }
            auxMb1 = shotDelay;
        }
        if(Mouse.isButtonDown(1) && auxMb2 == 0){
            //CLICK DERECHO
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
