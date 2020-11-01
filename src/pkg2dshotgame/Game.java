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
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author Jesus
 */
public class Game extends BasicGameState{

    private static final int ID = 1;

    //Mapa
    private final int WORLD_SIZE_X = 3200;
    private final int WORLD_SIZE_Y = 3200;
    TiledGameMap map;
    TileMapAStar tmap;
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
    private boolean auxMbLeft,auxMbRight;
    private int shotDelay;
    //Player
    private Player p;
    private Image[] gunsHolders;
    private Image infoPanel;
    private Image scoreBanner;
    //Enemys
    private ArrayList<Enemy> enemyList;
    //AStar
    //Bloques de colision
    private ArrayList<Rectangle> walls;
    private ArrayList<Rectangle> roof;
    private int auxRoof;
    private boolean inRoof;
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
        enemyList = new ArrayList<>();
        gameC = gc;
        this.initCam();
        for (int i = 0; i < 5; i++) {
            enemyList.add(new Enemy( 2*32 * (i+1),25*32,32,32,3));
        }
        input = gc.getInput();
        globalTime = globalScore = globalWave = 0;
        auxTime = auxMb1 = auxMb2 = 0;
        auxDebug = false;
        auxMbLeft = true;
        auxMbRight = true;
        ap1 = new AmmoPack("Assets/AmmoPack.png",400,500,20,20,900,5);
        walls = new ArrayList();
        walls.add(new Rectangle(0,0,WORLD_SIZE_X,32));
        walls.add(new Rectangle(0,0,32,WORLD_SIZE_Y));
        walls.add(new Rectangle(0,WORLD_SIZE_X - 32,WORLD_SIZE_X,32));
        walls.add(new Rectangle(WORLD_SIZE_Y - 32,0,32,WORLD_SIZE_Y));
        walls.add(new Rectangle(16*32,32,32,8*32));
        walls.add(new Rectangle(23*32,32,32,8*32));
        walls.add(new Rectangle(16*32,8*32,32 * 3,32));
        walls.add(new Rectangle(21*32,8*32,32 * 3,32));
        walls.add(new Rectangle(0,13*32,8*32,32));
        walls.add(new Rectangle(0,21*32,8*32,32));
        walls.add(new Rectangle(7*32,13*32,32,3*32));
        walls.add(new Rectangle(7*32,19*32,32,3*32));
        roof = new ArrayList();
        roof.add(new Rectangle(17*32,32,6*32,7*32));
        roof.add(new Rectangle(32,14*32,6*32,7*32));
        gunsHolders = new Image[3];
        gunsHolders[0] = new Image("Assets/GunHolder_9mm.png");
        gunsHolders[0] = gunsHolders[0].getScaledCopy(0.35f);
        gunsHolders[1] = new Image("Assets/GunHolder_Uzi.png");
        gunsHolders[1] = gunsHolders[1].getScaledCopy(0.35f);
        gunsHolders[2] = new Image("Assets/GunHolder_Awp.png");
        gunsHolders[2] = gunsHolders[2].getScaledCopy(0.35f);
        infoPanel = new Image("Assets/GunHolder.png");
        infoPanel = infoPanel.getScaledCopy(0.35f);
        scoreBanner = new Image("Assets/scoreBanner.png");
        
        auxRoof = 0;
        inRoof = false;
        shotDelay = 30; // In MilliSeconds
        gc.setMouseCursor("Assets/Cursor.png", 1, 1 );
        cmP = new ColitionsManager();
        cmE = new ColitionsManager();
        map = new TiledGameMap(new TiledMap("Assets/TestMap.tmx"),walls);
        tmap = new TileMapAStar(map,100,false,walls);
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
        g.setColor(Color.yellow);
        this.renderBullets(g);
        if(ap1.isActive()) ap1.render();
        this.renderEnemies(g);
        p.render(g);
        if(auxDebug){
            this.renderDebug(gc,g);
        }else{
            if(camX + mx >= camX + this.VIEWPORT_SIZE_X - gunsHolders[p.getCurrentGun()].getWidth()  - infoPanel.getWidth() && 
               camX + mx <= camX + this.VIEWPORT_SIZE_X ){
                if(camY + my >= camY + this.VIEWPORT_SIZE_Y - gunsHolders[p.getCurrentGun()].getHeight() && 
                   camY + my <= camY + this.VIEWPORT_SIZE_Y ){
                    this.renderHUD(g,0.3f);
                }else{
                    this.renderHUD(g,1f);
                }
            }else{
                this.renderHUD(g,1f);
            }
        }        
        if(Mouse.isInsideWindow()) g.fillOval(camX+mx - 5, camY+my - 5, 10, 10); // Mouse Point
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        this.updateEnviromentVars(i);
        this.updateMovements(i);
        this.updateEntityColitions();
        this.updateCameraOffSets();
        tmap.updateMap(enemyList);
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
        for(Enemy e: enemyList){
            if(!e.isActive()){
                e.render(g);
            }
        }
        for(Enemy e: enemyList){
            if(e.isActive()){
                e.render(g);
            }
        }
    }
    private void renderHUD(Graphics g,float alpha){
        g.setColor(Color.white);
        String auxS = "Health: "+p.getHealth()+"\nBullets: "+p.getGun().getCurrentBullets()+"/"+p.getGun().getTotalBullets();
        auxS += "\nScore: "+p.getScore();
        gunsHolders[p.getCurrentGun()].setAlpha(alpha);
        gunsHolders[p.getCurrentGun()].draw(camX + this.VIEWPORT_SIZE_X - gunsHolders[p.getCurrentGun()].getWidth(),camY + this.VIEWPORT_SIZE_Y - gunsHolders[p.getCurrentGun()].getHeight());
        infoPanel.setAlpha(alpha);
        infoPanel.draw(camX + this.VIEWPORT_SIZE_X - gunsHolders[p.getCurrentGun()].getWidth() - infoPanel.getWidth(),
                       camY + this.VIEWPORT_SIZE_Y - gunsHolders[p.getCurrentGun()].getHeight());
        g.drawString(auxS, camX + this.VIEWPORT_SIZE_X - gunsHolders[p.getCurrentGun()].getWidth() - infoPanel.getWidth() +10,
                     camY + this.VIEWPORT_SIZE_Y - gunsHolders[p.getCurrentGun()].getHeight() + 10);
        //scoreBanner.draw(camX + this.VIEWPORT_SIZE_X/2 - scoreBanner.getWidth()/3,camY + 10,400,40);
    }
    private void renderMap(Graphics g){
        map.getTiledMap().render(0, 0, 0);
        if(inRoof){
            g.setColor(Color.black);
            g.fill(new Rectangle(camX,camY,this.VIEWPORT_SIZE_X,this.VIEWPORT_SIZE_Y));
        }
        map.getTiledMap().render(0, 0, 1);
        if(!inRoof){
            map.getTiledMap().render(0, 0, 2);    
        }
    }
    private void renderBullets(Graphics g){
        for (int i = 0; i < p.getGun().getBullets().size(); i++) {
            if(p.getGun().getBullets().get(i).isActive()){
                p.getGun().getBullets().get(i).render(g);
            }
        }
    }
    private void updateMovements(int delta){
        //PlayerMovement
        p.updateMovement((int)(mx+camX),(int)(my+camY),delta);
        //Enemies Movement
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).updatePosition(delta,tmap,p,enemyList);    
        }
        //BulletsMovement
        for (int i = 0; i < p.getGun().getBullets().size(); i++) {
            if(p.getGun().getBullets().get(i).isActive()){
                p.getGun().getBullets().get(i).updatePosition(delta);
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
        p.getGun().reloadDelay(delta);
        for(Rectangle rect: roof){
            if(!rect.intersects(p)){
                auxRoof++;
            }
        }
        if(auxRoof == roof.size()){
            inRoof = false;
            auxRoof = 0;
        }else{
            inRoof = true;
            auxRoof = 0;
        }
    }
    private void updateEntityColitions(){
        for (int i = 0; i < p.getGun().getBullets().size(); i++) {
            Bullet auxB = p.getGun().getBullets().get(i);
            if(auxB.isActive()){
                for (int j = 0; j < enemyList.size(); j++) {
                    if(enemyList.get(j).isActive()){
                        if(cmE.checkBasicColition(auxB,enemyList.get(j))){
                            enemyList.get(j).hit(auxB.getDmg());
                            auxB.setReady();
                        }
                    }
                }
                for(Rectangle rect: walls){
                    if(auxB.intersects(rect)) auxB.setReady();
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
    }

    private void checkFutureMovementCol(boolean Vertical,boolean Horizontal){
        int auxC = 0;
        int auxE = 0;
        int padd = 5;
        Rectangle auxR;
        if(!Vertical && !Horizontal){
            //UP
            for (int i = 0; i < walls.size(); i++) {
                auxR = walls.get(i);
                if(cmP.checkFutureCol(p.getX() + padd,p.getY() - p.getVel(),p.getWidth() - padd,p.getVel(),auxR)){
                   auxC++; 
                }
            }
            for (int i = 0; i < enemyList.size(); i++) {
                if(enemyList.get(i).isActive()){
                    if(cmE.checkFutureCol(p.getX() + padd,p.getY() - p.getVel(),p.getWidth() - padd,p.getVel(),enemyList.get(i))){
                        auxE++;
                    }
                }
            }
            if(auxC == 0 && auxE == 0) p.setY(p.getY() - p.getVel());
        }
        auxC = 0;
        auxE = 0;
        if(Vertical && !Horizontal){
            //DOWN
            for (int i = 0; i < walls.size(); i++) {
                auxR = walls.get(i);
                if(cmP.checkFutureCol(p.getX() + padd,p.getY() + p.getHeight(),p.getWidth() - padd,p.getVel(),auxR)){
                    auxC++;
                }
            }
            for (int i = 0; i < enemyList.size(); i++) {
                if(enemyList.get(i).isActive()){
                    if(cmE.checkFutureCol(p.getX() + padd,p.getY() + p.getHeight(),p.getWidth() - padd,p.getVel(),enemyList.get(i))){
                        auxE++;
                    }
                }
            }
            if(auxC == 0 && auxE == 0) p.setY(p.getY() + p.getVel());
        }
        auxC = 0;
        auxE = 0;
        if(!Vertical && Horizontal){
            //LEFT
            for (int i = 0; i < walls.size(); i++) {
                auxR = walls.get(i);
                if(cmP.checkFutureCol(p.getX() - p.getVel(),p.getY() + padd,p.getVel(),p.getHeight() - padd,auxR)){
                    auxC++;
                }
            }
            for (int i = 0; i < enemyList.size(); i++) {
                if(enemyList.get(i).isActive()){
                    if(cmE.checkFutureCol(p.getX() - p.getVel(),p.getY() + padd,p.getVel(),p.getHeight() - padd,enemyList.get(i))){
                        auxE++;
                    }
                }
            }
            if(auxC == 0 && auxE == 0) p.setX(p.getX() - p.getVel());
        }
        auxC = 0;
        auxE = 0;
        if(Vertical && Horizontal){
            //RIGHT
            for (int i = 0; i < walls.size(); i++) {
                auxR = walls.get(i);
                if(cmP.checkFutureCol(p.getX() + p.getWidth(),p.getY() + padd,p.getVel(),p.getHeight() - padd,auxR)){
                    auxC++;
                }
            }
            for (int i = 0; i < enemyList.size(); i++) {
                if(enemyList.get(i).isActive()){
                    if(cmE.checkFutureCol(p.getX() + p.getWidth(),p.getY() + padd,p.getVel(),p.getHeight() - padd,enemyList.get(i))){
                        auxE++;
                    }
                }
            }
            if(auxC == 0 && auxE == 0) p.setX(p.getX() + p.getVel());
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
        if(input.isKeyPressed(Input.KEY_Q)){
            p.previousGun();
        }
        if(input.isKeyPressed(Input.KEY_E)){
            p.nextGun();
        }
        //F's
        
        if(input.isKeyPressed(Input.KEY_F1)){
            for (int i = 0; i < enemyList.size(); i++) {
                Enemy e = enemyList.get(i);
                e.follow(p,tmap.getPathFinder().findPath(p,(int)(e.getX()/32),(int)(e.getY()/32),
                         (int)(p.getX()/32), (int)(p.getY()/32)),enemyList);
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
        this.gunManager(delta);
        //Mouse Wheel Inputs
    }
    private void gunManager(int delta){
        Gun playerGun = p.getGun();
        int GunSize = playerGun.getChargerSize();
        if(playerGun.isAutomatic()){
            if(Mouse.isButtonDown(0) && auxMb1 == 0){
                //CLICK IZQUIERDO
                for (int i = 0; i < playerGun.getChargerSize(); i++) {
                    if(playerGun.getBullets().get(i).isReady()){
                        p.shot((int)p.getX(),(int)p.getY(),(int)(mx+camX),(int)(my+camY));
                        break;
                    }
                }
                auxMb1 = (int) playerGun.getShotSpeed();
            }
            if(Mouse.isButtonDown(1) && auxMb2 == 0){
                //CLICK DERECHO
                auxMb2 = (int) playerGun.getShotSpeed();
            }
            if(auxMb1 > 0){
                auxMb1 -= delta;
                if(auxMb1 < 0) auxMb1 = 0;
            }
            if(auxMb2 > 0){
                auxMb2 -= delta;
                if(auxMb2 < 0) auxMb2 = 0;
            }
        }else{
            if(Mouse.isButtonDown(0) && auxMb1 == 0 && auxMbLeft){
                //CLICK IZQUIERDO
                auxMbLeft = false;
                for (int i = 0; i < GunSize; i++) {
                    if(playerGun.getBullets().get(i).isReady()){
                        p.shot((int)p.getX(),(int)p.getY(),(int)(mx+camX),(int)(my+camY));
                        break;
                    }
                }
                auxMb1 = (int) playerGun.getShotSpeed();
            }
            if(Mouse.isButtonDown(1) && auxMb2 == 0 && auxMbRight){
                //CLICK DERECHO
                auxMbRight = false;
                auxMb2 = (int) playerGun.getShotSpeed();
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
    @Override
    public void mouseReleased(int button, int x, int y) {
        if(!p.getGun().isAutomatic()){
            if(button == 0){
                auxMbLeft = true;
            }else if(button == 1){
                auxMbRight = true;
            }
        }
    }
}
