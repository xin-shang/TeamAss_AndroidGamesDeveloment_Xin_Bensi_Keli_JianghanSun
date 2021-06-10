package com.mygdx.game.utils;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.sprites.CommonState;
import com.mygdx.game.sprites.Enemy;
import com.mygdx.game.stages.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The game logic
 * @author Ke Li
 * */
public class AStar {
    static Map<LocationUtil.Point<Integer>, MapNode> open = new HashMap<>();
    static Map<LocationUtil.Point<Integer>, MapNode> close = new HashMap<>();
    GameScreen.TerritoryObject[][] map;
    private List<Enemy> spriteEnemies;


    public AStar(GameScreen.TerritoryObject[][] map, List<Enemy> spriteEnemies) {
        this.map = map;
        this.spriteEnemies = spriteEnemies;
    }


    private int distance(int srcX, int srcY, int destX, int destY) {
        return Math.abs(srcX - destX) + Math.abs(srcY - destY);
    }

    public CommonState.Direction find(int srcX, int srcY, int destX, int destY) {
        open.clear();
        close.clear();
        MapNode path = null;
        // add start node
        LocationUtil.Point<Integer> srcPt = new LocationUtil.Point<>(srcX, srcY);
        LocationUtil.Point<Integer> destPt = new LocationUtil.Point<>(destX, destY);
        open.put(srcPt, new MapNode(srcX, srcY, 0, distance(srcX, srcY, destX, destY), null));
        while (!open.isEmpty()) {
            path = open.get(destPt);
            //FOUND
            if (path != null) break;

            // put into close
            MapNode first = getFirst(open);
            LocationUtil.Point<Integer> firstPt = new LocationUtil.Point<>(first.getX(), first.getY());
            close.put(firstPt, first);
            open.remove(firstPt);

            List<MapNode> nextSteps = new ArrayList<>(4);

            // right
            if (first.getX() + 1 < map.length && map[first.getX() + 1][first.getY()] != GameScreen.TerritoryObject.Wall) {
                nextSteps.add(new MapNode(first.getX() + 1, first.getY(), first.getG() + 1,
                        distance(destX, destY, first.getX() + 1, first.getY()), first));
            }

            // left
            if (first.getX() - 1 >= 0 && map[first.getX() - 1][first.getY()] != GameScreen.TerritoryObject.Wall) {
                nextSteps.add(new MapNode(first.getX() - 1, first.getY(), first.getG() + 1,
                        distance(destX, destY, first.getX() - 1, first.getY()), first));
            }

            // up
            if (first.getY() + 1 < map.length && map[first.getX()][first.getY() + 1] != GameScreen.TerritoryObject.Wall) {
                nextSteps.add(new MapNode(first.getX(), first.getY() + 1, first.getG() + 1,
                        distance(destX, destY, first.getX(), first.getY() + 1), first));
            }

            // down
            if (first.getY() - 1 >= 0 && map[first.getX()][first.getY() - 1] != GameScreen.TerritoryObject.Wall) {
                nextSteps.add(new MapNode(first.getX(), first.getY() - 1, first.getG() + 1,
                        distance(destX, destY, first.getX(), first.getY() - 1), first));
            }

            for (MapNode nextStep : nextSteps) {
                boolean collision = false;
                LocationUtil.Point<Float> floatPoint = LocationUtil.toScreen(nextStep.x, nextStep.y);
                for (Enemy spriteEnemy : spriteEnemies) {
                    if (spriteEnemy.getState() == CommonState.Survival.Survived && spriteEnemy.getCollisionRect().overlaps(new Rectangle(floatPoint.getX(), floatPoint.getY(),
                            1, 1))) {
                        collision = true;
                        break;
                    }
                }

                if(collision) continue;
                // in close? continue
                if (close.containsKey(new LocationUtil.Point<Integer>(nextStep.getX(), nextStep.getY()))) {
                    continue;
                }
                boolean replace = true;

                LocationUtil.Point<Integer> p = new LocationUtil.Point<>(nextStep.getX(), nextStep.getY());
                if (open.containsKey(p)) {
                    replace = nextStep.getF() < open.get(p).getF();
                }

                if (replace) {
                    open.put(p, nextStep);
                }
            }

        }

        if (path != null) {
            while (path.parent != null && path.parent.parent != null) {
                path = path.parent;
            }
            if (path.x > srcX) {
                return CommonState.Direction.Rightward;
            }
            if (path.x < srcX) {
                return CommonState.Direction.Leftward;
            }
            if (path.y > srcY) {
                return CommonState.Direction.Upward;
            }
            if (path.y < srcY) {
                return CommonState.Direction.Downward;
            }
        }

        return null;
    }

    private MapNode getFirst(Map<LocationUtil.Point<Integer>, MapNode> open) {
        int smallestF = Integer.MAX_VALUE;
        MapNode first = null;
        for (Map.Entry<LocationUtil.Point<Integer>, MapNode> entry : open.entrySet()) {
            if (entry.getValue().getF() < smallestF) {
                smallestF = entry.getValue().getF();
                first = entry.getValue();
            }
        }
        return first;
    }

    private static class MapNode implements Comparable<Object> {
        static MapNode staticMapNode = new MapNode(0, 0, 0, 0, null);
        int G;
        int H;
        MapNode parent;
        private int x;
        private int y;

        public MapNode(int x, int y, int g, int h, MapNode parent) {
            this.x = x;
            this.y = y;
            G = g;
            H = h;
            this.parent = parent;
        }

        public static MapNode of(int x, int y) {
            staticMapNode.x = x;
            staticMapNode.y = y;
            return staticMapNode;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MapNode mapNode = (MapNode) o;

            if (x != mapNode.x) return false;
            return y == mapNode.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        public int getG() {
            return G;
        }

        public void setG(int g) {
            G = g;
        }

        public int getH() {
            return H;
        }

        public void setH(int h) {
            H = h;
        }

        public MapNode getParent() {
            return parent;
        }

        public void setParent(MapNode parent) {
            this.parent = parent;
        }

        int getF() {
            return G + H;
        }


        @Override
        public int compareTo(Object o) {
            MapNode node = (MapNode) o;
            return 0;
        }
    }
}
