package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.utils.DirectionUtil;

import static com.mygdx.game.sprites.CommonState.Direction;
import static com.mygdx.game.sprites.CommonState.Survival;

/**
 * player sprite
 * @author Jianghan Sun
 */
public class Enemy extends Sprite implements SpriteUpdateAdapter {
    private static final String TEX1_PATH = "enemy.png";
    private static final String TEX2_PATH = "cannon.png";

    private static Texture texture1;
    private static Texture texture2;

    // sounds
    private static Sound deadSound;

    // init of texture
    static {

        texture1 = new Texture(TEX1_PATH);
        texture2 = new Texture(TEX2_PATH);

    }

    // init of sounds
    static {
        deadSound = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
    }

    private final Type type;

    Direction direction = Direction.Upward;
    // current state
    Survival state = Survival.Survived;
    float stateTime;
    float fireTimer;
    float driveTimer;
    private TankStatus status;

    /**
     * create player and put on ground
     */
    public Enemy(float pixelX, float pixelY, Type type) {
        super(type == Type.Chaser ? texture1 : texture2);
        this.type = type;
        reset(pixelX, pixelY, type);
    }

    public float getFireTimer() {
        return fireTimer;
    }

    public void setFireTimer(float fireTimer) {
        this.fireTimer = fireTimer;
    }

    public float getDriveTimer() {
        return driveTimer;
    }

    public void setDriveTimer(float driveTimer) {
        this.driveTimer = driveTimer;
    }

    /**
     * reset player position and state
     */
    public void reset(float pixelX, float pixelY, Type type) {
        state = Survival.Survived;
        stateTime = 0;
        setY(pixelY);
        setX(pixelX);
        switch (type) {
            case Chaser:
                status = new TankStatus("Chaser", 1, 1, 1, true);
                break;
            case Cannon:
                status = new TankStatus("Cannon", 3, 0, 0.25f, true);
        }

    }

    @Override
    public void update(float delta) {
        driveTimer += delta;
        fireTimer += delta;
        stateTime += delta;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        if (direction != this.direction) {
            DirectionUtil.rotateToDirection(this, direction);
        }
        this.direction = direction;
    }

    public TankStatus getStatus() {
        return status;
    }

    /**
     * get enemy state
     *
     * @return
     */
    public Survival getState() {
        return state;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    /**
     * dead
     */
    public void destroy() {
        if (this.state == Survival.Dead) return;
        this.stateTime = 0;
        this.state = Survival.Dead;
//        deadSound.play();
    }

    public Rectangle getCollisionRect() {
        return getBoundingRectangle();
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        Chaser,
        Cannon
    }


}
