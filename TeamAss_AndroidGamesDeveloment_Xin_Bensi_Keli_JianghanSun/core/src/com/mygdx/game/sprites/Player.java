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
 * @author Xin Shang
 */
public class Player extends Sprite implements SpriteUpdateAdapter {
    private static final String TEX_PATH = "player.png";
    private static final Texture texture;
    // sounds
    private static final Sound actSound;
    private static final Sound deadSound;

    static {

        texture = new Texture(TEX_PATH);
    }

    // init of sounds
    static {
        actSound = Gdx.audio.newSound(Gdx.files.internal("act.mp3"));
        deadSound = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));


    }

    Direction direction = Direction.Rightward;
    // current state
    Survival state = Survival.Survived;
    // time of current state last for
    float stateTime;
    float fireTimer;
    float driveTimer;
    private TankStatus status;

    public void setStatus(TankStatus status) {
        this.status = status;
    }

    /**
     * create player and put on ground
     */
    public Player(float pixelX, float pixelY) {
        super(texture);
        reset(pixelX, pixelY);
        setTexture(texture);
    }


    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
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


    public TankStatus getStatus() {
        return status;
    }

    /**
     * reset player position and state
     */
    public void reset(float pixelX, float pixelY) {
        state = Survival.Survived;
        status = new TankStatus("Player", 1, 2, 2, false);
        driveTimer = 0;
        setY(pixelY);
        setX(pixelX);

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

    /**
     * get player state
     *
     * @return
     */
    public Survival getState() {
        return state;
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


}
