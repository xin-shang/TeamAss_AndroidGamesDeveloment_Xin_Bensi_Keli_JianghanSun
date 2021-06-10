package com.mygdx.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.*;
import com.mygdx.game.utils.AStar;
import com.mygdx.game.utils.DirectionUtil;
import com.mygdx.game.utils.LocationUtil;
import com.mygdx.game.utils.LoggerUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Gaming
 * @author Bensi You
 * */
public class GameScreen extends Stage implements InputProcessor {
    static TextureRegionDrawable drawableBkg;
    static TextureRegionDrawable drawableKnob;
    static TextureRegionDrawable drawableFire;
    static Texture heartTexture;

    static Texture bkgTex;
    static Texture hdTex;
    static Texture levelClearTex;

    static {
        drawableBkg = new TextureRegionDrawable(new Texture(Gdx.files.internal("touchpad_bkg.png")));
        drawableKnob = new TextureRegionDrawable(new Texture(Gdx.files.internal("touchpad_knob.png")));
        drawableFire = new TextureRegionDrawable(new Texture(Gdx.files.internal("btn_fire.png")));
        heartTexture = (new Texture(Gdx.files.internal("heart.png")));

        bkgTex = new Texture(Gdx.files.internal("bkg_game.jpg"));
        hdTex = new Texture(Gdx.files.internal("hd.png"));
        levelClearTex = new Texture(Gdx.files.internal("level_clear.png"));

    }

    private final MyGdxGame game;
    boolean showHd = false;
    boolean showLevelClear = false;
    double showHdOver = 0;
    // sprites
    Player spritePlayer;
    List<Enemy> spriteEnemies = new LinkedList<>();
    List<Bullet> spriteBullets = new LinkedList<>();
    List<Explosion> spriteExplosions = new LinkedList<>();

    List<Wall> spriteWalls = new LinkedList<>();
    List<Item> spriteItems = new LinkedList<>();
    TerritoryObject[][] map = new TerritoryObject[30][30];
    private final AStar aStar = new AStar(map, spriteEnemies);
    Touchpad touchpad;

    String[] levels = {"level1.txt", "level2.txt"};
    LevelType[] levelTypes = {LevelType.Escape, LevelType.Boss};

    int currentLevel = 0;
    // total time passed after game start
    double totalTime = 0;
    CommonState.Direction pressedDirection = null;
    boolean playerFiring = false;
    Image btnFire;
    Image bkg;
    private int keysLeft = 0;
    private int enemiesLeft = 0;

    /**
     * create game screen
     *
     * @param game game instance
     */
    public GameScreen(final MyGdxGame game) {
        super(new FitViewport(MyGdxGame.VIEWPORT_WIDTH, MyGdxGame.VIEWPORT_HEIGHT));
        this.game = game;

        touchpad = new Touchpad(20, new Touchpad.TouchpadStyle(drawableBkg, drawableKnob));
        btnFire = new Image(drawableFire);
        bkg = new Image(bkgTex);
        bkg.setBounds(0, 0, 800, 480);

        btnFire.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerFiring = true;
            }
        }));
        btnFire.setBounds(660, 15, 100, 100);
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float left = -touchpad.getKnobPercentX();
                float right = touchpad.getKnobPercentX();
                float down = -touchpad.getKnobPercentY();
                float up = touchpad.getKnobPercentY();

                if (!touchpad.isTouched()) {
                    if (pressedDirection == null) return;
                    pressedDirection = null;
                    LoggerUtil.print("Turn stop");

                    return;
                }
                if (left > right && left > up && left > down) {
                    if (pressedDirection == CommonState.Direction.Leftward) return;
                    pressedDirection = CommonState.Direction.Leftward;
                    LoggerUtil.print("Turn left");
                    return;
                }
                if (right > left && right > up && right > down) {
                    if (pressedDirection == CommonState.Direction.Rightward) return;
                    pressedDirection = CommonState.Direction.Rightward;
                    LoggerUtil.print("Turn right");
                    return;
                }
                if (up > right && up > left && up > down) {
                    if (pressedDirection == CommonState.Direction.Upward) return;
                    pressedDirection = CommonState.Direction.Upward;
                    LoggerUtil.print("Turn up");
                    return;
                }
                if (down > right && down > up && down > up) {
                    if (pressedDirection == CommonState.Direction.Downward) return;
                    pressedDirection = CommonState.Direction.Downward;
                    LoggerUtil.print("Turn down");

                }

            }
        });
        touchpad.setBounds(30, 30, 100, 100);
        addActor(touchpad);
        addActor(btnFire);
    }

    @Override
    public boolean keyDown(int keyCode) {
        return super.keyDown(keyCode);
    }

    @Override
    public boolean keyUp(int keyCode) {
        return super.keyDown(keyCode);
    }

    /**
     * draw routine
     */
    @Override
    public void draw() {
        getBatch().setProjectionMatrix(getViewport().getCamera().combined);
        getBatch().begin();
        bkg.draw(getBatch(), 1);
        if (spritePlayer.getStatus().isCanFire()) {
            btnFire.draw(getBatch(), 1);
        }
        touchpad.draw(getBatch(), 1);


        // draw wall
        for (Wall spriteWall : spriteWalls) {
            spriteWall.draw(getBatch());
        }


        // draw items
        for (Item spriteItem : spriteItems) {
            if (!spriteItem.isPicked())
                spriteItem.draw(getBatch());
        }

        // draw bullets
        for (Bullet spriteBullet : spriteBullets) {
            // bullet disappear after hit 0.1 second
            if (!(spriteBullet.isHit() && spriteBullet.getCurrentTime() > 0.1))
                spriteBullet.draw(getBatch());
        }


        // draw player
        if (spritePlayer.getState() == CommonState.Survival.Survived) {
            spritePlayer.draw(getBatch());
        }

        // draw enemies
        for (Enemy spriteEnemy : spriteEnemies) {
            if (spriteEnemy.getState() == CommonState.Survival.Survived)
                spriteEnemy.draw(getBatch());
        }

        // draw explosions
        for (Explosion spriteExplosion : spriteExplosions) {
            // if explosion is over, skip it
            if (!spriteExplosion.isOver())
                spriteExplosion.draw(getBatch());
        }

        if (spritePlayer != null)
            for (int i = 0; i < spritePlayer.getStatus().getHealth(); i++) {
                getBatch().draw(heartTexture, 15 + i * 64, 400);
            }
        if(showHd & totalTime < showHdOver) {
            getBatch().draw(hdTex,0,0);
        }

        if(showLevelClear) {
            getBatch().draw(levelClearTex,0,0);
        }
        getBatch().end();
    }

    // fire
    void fire(boolean fromPlayer, int x, int y, CommonState.Direction direction) {
        LocationUtil.Point<Float> floatPoint = LocationUtil.toScreen(x, y);
        Bullet bullet = new Bullet(floatPoint.getX(), floatPoint.getY(), direction, fromPlayer);
        switch (direction) {
            case Upward:
                bullet.setRotation(180);
                break;
            case Downward:
                bullet.setRotation(0);
                break;
            case Leftward:
                bullet.setRotation(90);
                break;
            case Rightward:
                bullet.setRotation(270);
        }
        spriteBullets.add(bullet);

    }

    /**
     * update player and check collision
     *
     * @param delta time delta
     */
    @Override
    public void act(float delta) {

        // if not dead, move
        if (spritePlayer.getState() != CommonState.Survival.Dead) {
            totalTime += delta;
        } else {
            // dead for 2 seconds, show game over screen
            if (spritePlayer.getStateTime() > 2.0f) {
                game.failed();
            }
        }

        if(showLevelClear) {
            if(totalTime > 2.0) showLevelClear = false;
            return;
        }
        // update state
        spritePlayer.update(delta);

        if (spritePlayer.getState() != CommonState.Survival.Dead) {
            TankStatus playerStatus = spritePlayer.getStatus();
            if (pressedDirection != null) {
                DirectionUtil.rotateToDirection(spritePlayer, pressedDirection);
                if (spritePlayer.getDirection() != pressedDirection) {
                    spritePlayer.setDriveTimer(0);
                }
                spritePlayer.setDirection(pressedDirection);

                // can move
                if (spritePlayer.getDriveTimer() >= 1.0f / playerStatus.getMoveFreq()) {
                    int destX = 0, destY = 0;
                    LocationUtil.Point<Integer> point = LocationUtil.toMap(spritePlayer.getX(), spritePlayer.getY());
                    if (pressedDirection == CommonState.Direction.Leftward) {
                        destX = point.getX() - 1;
                        destY = point.getY();
                    }

                    if (pressedDirection == CommonState.Direction.Rightward) {
                        destX = point.getX() + 1;
                        destY = point.getY();
                    }
                    if (pressedDirection == CommonState.Direction.Downward) {
                        destX = point.getX();
                        destY = point.getY() - 1;
                    }

                    if (pressedDirection == CommonState.Direction.Upward) {
                        destX = point.getX();
                        destY = point.getY() + 1;
                    }
                    boolean hitEnemy = false;
                    LocationUtil.Point<Float> floatPoint = LocationUtil.toScreen(destX, destY);
                    for (Enemy spriteEnemy : spriteEnemies) {
                        if (spriteEnemy.getState() == CommonState.Survival.Survived && spriteEnemy.getCollisionRect().overlaps(new Rectangle(floatPoint.getX(), floatPoint.getY(),
                                spritePlayer.getRegionWidth(), spritePlayer.getRegionHeight()))) {
                            hitEnemy = true;
                            break;
                        }
                    }

                    if (!hitEnemy && destX >= 0 && destX < 30 && destY >= 0 && destY < 30) {
                        if (map[destX][destY] != TerritoryObject.Wall) {

                            spritePlayer.setPosition(floatPoint.getX(), floatPoint.getY());
                            if (map[destX][destY] == TerritoryObject.Item) {
                                // pick up a item
                                for (Item item : spriteItems) {
                                    if (item.isPicked()) continue;
                                    LocationUtil.Point<Integer> itemMapPos = LocationUtil.toMap(item.getX(), item.getY());
                                    if (itemMapPos.getX() == destX && itemMapPos.getY() == destY) {
                                        pickupItem(item.getItemType());
                                        item.setPicked(true);
                                    }
                                }
                            }

                            if (levelTypes[currentLevel] == LevelType.Escape) {
                                if (map[destX][destY] == TerritoryObject.Exit) {
                                    if (keysLeft == 0) {
                                        currentLevel++;
                                        if (levels.length == currentLevel) {
                                            game.winGame();
                                        } else {
                                            showLevelClear = true;
                                            loadMap(levels[currentLevel]);
                                        }
                                    }
                                }
                            }

                            // clear state time
                            spritePlayer.setDriveTimer(0);
                        }
                    }


                }
            }

            if (playerFiring && spritePlayer.getStatus().isCanFire()) {
                LocationUtil.Point<Integer> point = LocationUtil.toMap(spritePlayer.getX(), spritePlayer.getY());
                if (spritePlayer.getFireTimer() >= 1.0f / playerStatus.getFireFreq()) {
                    spritePlayer.setFireTimer(0);
                    fire(true, point.getX(), point.getY(), spritePlayer.getDirection());
                }
            }

            playerFiring = false;
        }
        for (Enemy spriteEnemy : spriteEnemies) {
            if (spriteEnemy.getState() == CommonState.Survival.Dead)
                continue;
            spriteEnemy.update(delta);
            updateEnemy(spriteEnemy);
        }

        // collision check
        for (Explosion explosion : spriteExplosions) {
            if (!explosion.isOver()) {
                explosion.update(delta);
            }

        }

        // collision check
        for (Bullet bullet : spriteBullets) {
            bullet.update(delta);
            // if a hit bullet skip it
            if (bullet.isHit()) continue;

            // update bullet location
            float floatDestX = 0, floatDestY = 0;
            LocationUtil.Point<Integer> point;
            //TODO bullet speed
            float dist = bullet.getCurrentTime() * 16 * 3f;
            bullet.setCurrentTime(0);
            int xOffset = 0, yOffset = 0;
            if (bullet.getDirection() == CommonState.Direction.Leftward) {
                floatDestX = bullet.getX() - dist;
                floatDestY = bullet.getY();
                xOffset = 1;
            }

            if (bullet.getDirection() == CommonState.Direction.Rightward) {
                floatDestX = bullet.getX() + dist;
                floatDestY = bullet.getY();
            }
            if (bullet.getDirection() == CommonState.Direction.Downward) {
                floatDestX = bullet.getX();
                floatDestY = bullet.getY() - dist;
                yOffset = 1;
            }

            if (bullet.getDirection() == CommonState.Direction.Upward) {
                floatDestX = bullet.getX();
                floatDestY = bullet.getY() + dist;
            }

            point = LocationUtil.toMap(floatDestX, floatDestY);
            bullet.setPosition(floatDestX, floatDestY);

            // in map and not on wall
            int xOfMap = point.getY() + yOffset;
            int yOfMap = point.getX() + xOffset;
            if (xOfMap < 0 || xOfMap >= 30 || yOfMap < 0 || yOfMap >= 30 || map[yOfMap][xOfMap] == TerritoryObject.Wall) {
                // hit on wall or out of bound
                bullet.fireHit();
                continue;
            }

            if (bullet.isFromPlayer()) {
                for (Enemy enemy : spriteEnemies) {
                    // only hit survived enemy
                    if (enemy.getState() != CommonState.Survival.Survived) continue;

                    if (bullet.getCollisionRect().overlaps(enemy.getCollisionRect())) {
                        enemy.getStatus().setHealth(enemy.getStatus().getHealth() - 1);
                        if (enemy.getStatus().getHealth() <= 0) {
                            enemy.destroy();
                            enemiesLeft--;
                            makeExplosion(enemy.getX(), enemy.getY());
                            if(enemy.getType() == Enemy.Type.Cannon) game.explosionLoudSound();
                            if(enemy.getType() == Enemy.Type.Chaser) game.explosionSound();
                            // if a boss battle, kill all enemies to win
                            if (levelTypes[currentLevel] == LevelType.Boss) {
                                if (enemiesLeft == 0) {
                                    currentLevel++;
                                    if (levels.length == currentLevel) {
                                        game.winGame();
                                    } else {
                                        showLevelClear = true;
                                        loadMap(levels[currentLevel]);
                                    }
                                }
                            }
                        }
                        bullet.fireHit();
                        return;
                    }
                }
            } else {
                // bullet hit player
                if (bullet.getCollisionRect().overlaps(spritePlayer.getCollisionRect())) {
                    spritePlayer.getStatus().setHealth(spritePlayer.getStatus().getHealth() - 1);
                    if (spritePlayer.getStatus().getHealth() == 0) {
                        spritePlayer.destroy();
                        game.explosionSound();
                        makeExplosion(spritePlayer.getX(), spritePlayer.getY());

                    }
                    bullet.fireHit();
                    return;
                }
            }

        }
    }

    private void makeExplosion(float x, float y) {
        spriteExplosions.add(new Explosion(x, y));
    }

    private void updateEnemy(Enemy enemy) {
        TankStatus enemyStatus = enemy.getStatus();
        LocationUtil.Point<Integer> point = LocationUtil.toMap(enemy.getX(), enemy.getY());
        if (enemy.getDriveTimer() >= 1.0f / enemyStatus.getMoveFreq()) {
            enemy.setDriveTimer(0);
            CommonState.Direction nextDirection = wayToPlayer(point);
            // can not move
            if (nextDirection == null) return;

            enemy.setDirection(nextDirection);
            int destX = 0, destY = 0;
            if (nextDirection == CommonState.Direction.Leftward) {
                destX = point.getX() - 1;
                destY = point.getY();
            }

            if (nextDirection == CommonState.Direction.Rightward) {
                destX = point.getX() + 1;
                destY = point.getY();
            }
            if (nextDirection == CommonState.Direction.Downward) {
                destX = point.getX();
                destY = point.getY() - 1;
            }

            if (nextDirection == CommonState.Direction.Upward) {
                destX = point.getX();
                destY = point.getY() + 1;
            }
            LocationUtil.Point<Float> floatPoint = LocationUtil.toScreen(destX, destY);
            if (spritePlayer.getX() != floatPoint.getX() || spritePlayer.getY() != floatPoint.getY()) {
                enemy.setPosition(floatPoint.getX(), floatPoint.getY());
            }
        }
        if (enemy.getFireTimer() >= 1.0f / enemyStatus.getFireFreq()) {
            LocationUtil.Point<Integer> firePt = LocationUtil.toMap(enemy.getX(), enemy.getY());
            enemy.setFireTimer(0);
            if (enemy.getType() == Enemy.Type.Cannon) {
                fire(false, firePt.getX(), firePt.getY(), CommonState.Direction.Leftward);
                fire(false, firePt.getX(), firePt.getY(), CommonState.Direction.Downward);

                fire(false, firePt.getX() + 1, firePt.getY(), CommonState.Direction.Rightward);
                fire(false, firePt.getX() + 1, firePt.getY(), CommonState.Direction.Downward);

                fire(false, firePt.getX(), firePt.getY() + 1, CommonState.Direction.Leftward);
                fire(false, firePt.getX(), firePt.getY() + 1, CommonState.Direction.Upward);

                fire(false, firePt.getX() + 1, firePt.getY() + 1, CommonState.Direction.Rightward);
                fire(false, firePt.getX() + 1, firePt.getY() + 1, CommonState.Direction.Upward);

            } else {
                fire(false, firePt.getX(), firePt.getY(), enemy.getDirection());
            }
        }
    }

    private CommonState.Direction wayToPlayer(LocationUtil.Point<Integer> point) {
        LocationUtil.Point<Integer> playerPoint = LocationUtil.toMap(spritePlayer.getX(), spritePlayer.getY());
        return aStar.find(point.getX(), point.getY(), playerPoint.getX(), playerPoint.getY());
    }

    private void pickupItem(Item.Type itemType) {
        switch (itemType) {
            case Gun:
                spritePlayer.getStatus().setCanFire(true);
                break;
            case Key:
                keysLeft--;
                break;
            case Health:
                spritePlayer.getStatus().setHealth(spritePlayer.getStatus().getHealth() + 1);
                break;
            case Speed:
                spritePlayer.getStatus().setMoveFreq(spritePlayer.getStatus().getMoveFreq() + 1);
                break;
            case WeNeedHD:
                showHdOver = totalTime + 3.0;
                showHd = true;
                break;
        }
    }

    /**
     * load enemies, species split by line, location split by space
     *
     * @param map map file level1.tmx -> level1_enemy.txt
     */
    private void loadMap(String map) {
        spriteItems.clear();
        spriteWalls.clear();
        spriteEnemies.clear();
        spriteBullets.clear();
        spriteExplosions.clear();
        enemiesLeft = 0;
        String texts = Gdx.files.internal(map).readString();
        String[] lines = texts.split("\\n");
        keysLeft = 0;
        if (lines.length >= 1) {
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 30; j++) {
                    int y = 29 - i, x = j;
                    LocationUtil.Point<Float> point = LocationUtil.toScreen(x, y);
                    this.map[x][y] = TerritoryObject.Road;
                    char c = lines[i].charAt(j);
                    switch (c) {
                        case 'N': //nothing
                            break;
                        case 'W': //wall
                            this.map[x][y] = TerritoryObject.Wall;
                            spriteWalls.add(new Wall(point.getX(), point.getY()));
                            break;
                        case 'X': //exit
                            this.map[x][y] = TerritoryObject.Exit;
                            break;
                        case 'P': //player
                            if (spritePlayer != null) {
                                TankStatus prevStatus = spritePlayer.getStatus();
                                spritePlayer = new Player(point.getX(), point.getY());
                                spritePlayer.setStatus(prevStatus);
                            } else {
                                spritePlayer = new Player(point.getX(), point.getY());
                            }
                            break;
                        case 'E': //enemy tank
                            enemiesLeft++;
                            spriteEnemies.add(new Enemy(point.getX(), point.getY(), Enemy.Type.Chaser));
                            break;
                        case 'C': //cannon
                            enemiesLeft++;
                            spriteEnemies.add(new Enemy(point.getX(), point.getY(), Enemy.Type.Cannon));
                            break;
                        case 'G': //gun
                            this.map[x][y] = TerritoryObject.Item;
                            spriteItems.add(new Item(point.getX(), point.getY(), Item.Type.Gun));
                            break;
                        case 'H': //hp +1
                            this.map[x][y] = TerritoryObject.Item;
                            spriteItems.add(new Item(point.getX(), point.getY(), Item.Type.Health));
                            break;
                        case 'S': //speed +1
                            this.map[x][y] = TerritoryObject.Item;
                            spriteItems.add(new Item(point.getX(), point.getY(), Item.Type.Speed));
                            break;
                        case 'K': //key
                            keysLeft++;
                            this.map[x][y] = TerritoryObject.Item;
                            spriteItems.add(new Item(point.getX(), point.getY(), Item.Type.Key));
                            break;
                        case 'D': //we need HD
                            this.map[x][y] = TerritoryObject.Item;
                            spriteItems.add(new Item(point.getX(), point.getY(), Item.Type.WeNeedHD));
                    }
                }
            }
        }

    }

    /**
     * check if player is dead
     *
     * @return if dead
     */
    public boolean notDead() {
        return spritePlayer.getState() != CommonState.Survival.Dead;
    }

    /**
     * reload all enemies and reset player location, called by start
     */
    public void reload(int level) {
        totalTime = 0;
        currentLevel = 0;
        spritePlayer = null;
        keysLeft = 0;
        showHd = false;


        loadMap(levels[level]);
    }

    enum LevelType {
        Escape, Boss,
    }

    public enum TerritoryObject {
        Wall,
        Item,
        Road, Exit,
    }
}
