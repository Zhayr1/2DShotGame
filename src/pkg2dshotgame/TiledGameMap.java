/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import java.util.ArrayList;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 *
 * @author Jesus
 */
public class TiledGameMap implements TileBasedMap{
    
    private final ArrayList<Rectangle> boundaries;
    private final TiledMap map;
    private final int tileBasedMap[][];
    private final int mapWidth;
    private final int mapHeight;
    private final int mapTileWidth;
    private final int mapTileHeight;
    
    private ArrayList<Point> auxList;
    
    public TiledGameMap(TiledMap map,ArrayList<Rectangle> boundaries){
        this.map = map;
        this.boundaries = boundaries;
        mapWidth = map.getWidth();
        mapHeight = map.getHeight();
        mapTileWidth = map.getTileWidth();
        mapTileHeight = map.getTileHeight();
        tileBasedMap = new int[mapWidth][mapHeight];
        auxList = new ArrayList<>();
        for (Rectangle bound : boundaries) {
            float auxWidth = bound.getWidth();
            float auxHeight = bound.getHeight();
            int auxTileWidth = Math.floorDiv((int)auxWidth, mapTileWidth);
            int auxTileHeight = Math.floorDiv((int)auxHeight, mapTileHeight);
            int startX; 
            int startY;
            if(bound.getX()/auxTileWidth == 1){
                startX = (int) mapTileWidth;
            }else{
                startX = (int) bound.getX()/mapTileWidth;
            }
            if(bound.getY()/auxTileHeight == 1){
                startY = (int) mapTileHeight;
            }else{
                startY = (int) bound.getY()/mapTileHeight;
            }
            for (int i = 0; i < auxTileHeight; i++) {
                for (int j = 0; j < auxTileWidth; j++) {
                    auxList.add(new Point(j+startX,i+startY));
                }
            }
        }
        for (Point p : auxList) {
            int auxX = (int)p.getX();
            int auxY = (int)p.getY();
            tileBasedMap[auxX][auxY] = 1;
        }
    }
    public void printMatrixMap(){
        for(int i = 0; i < mapHeight; i++){
            for (int j = 0; j < mapWidth; j++) {
                System.out.print(tileBasedMap[j][i]);
            }
            System.out.print("\n");
        }
    }
    @Override
    public int getWidthInTiles() {
        return mapWidth;
    }

    @Override
    public int getHeightInTiles() {
        return mapHeight;
    }

    @Override
    public void pathFinderVisited(int i, int i1) {
    }

    @Override
    public boolean blocked(PathFindingContext pfc, int tx, int ty) {
        //return boundaries.stream().filter((boundarie) -> (boundarie.getX() >= tx && boundarie.getX()+boundarie.getWidth() <= tx+mapTileWidth)).anyMatch((boundarie) -> (boundarie.getY() >= ty && boundarie.getY()+boundarie.getHeight() <= tx+mapTileHeight));
        return tileBasedMap[(int)(tx)][(int)(ty)] == 1;
    }

    @Override
    public float getCost(PathFindingContext pfc, int i, int i1) {
        return 1;
    }
    public TiledMap getTiledMap(){
        return map;
    }
    
}
