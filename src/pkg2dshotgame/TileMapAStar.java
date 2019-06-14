/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dshotgame;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 *
 * @author Jesus
 */
public class TileMapAStar{
    
    private final ArrayList<Rectangle> boundaries;
    private AStarPathFinder starPath;
    private Path apath;
    private TiledGameMap map;

    public TileMapAStar(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement,ArrayList<Rectangle> bound) {
        boundaries = bound;
    }
    
    
}
