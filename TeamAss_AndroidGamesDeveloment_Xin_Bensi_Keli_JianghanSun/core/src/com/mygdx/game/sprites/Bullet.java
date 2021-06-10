package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Sprite, hold the animatable texture
 * @author Jianghan Sun
 */
public class Bullet extends Sprite implements SpriteUpdateAdapter {

    private static final Texture textureSheet;
    private static final Animation<TextureRegion> animation;
    private static final int REG_COL = 1;
    private static final int REG_ROW = 3;
    private static final String TEX_PATH = "bullet.png";
    private static final TextureRegion[] textureRegions;

    static {
        int totalFrame = 3;
        textureSheet = new Texture(TEX_PATH);
        TextureRegion[][] split = TextureRegion.split(textureSheet, textureSheet.getWidth() / REG_COL, textureSheet.getHeight() / REG_ROW);
        textureRegions = new TextureRegion[REG_ROW * REG_COL];
        int regionIndex = 0;
        for (int i = 0; i < REG_ROW; i++) {
            for (int j = 0; j < REG_COL; j++) {
                textureRegions[regionIndex++] = split[i][j];
                if (totalFrame == regionIndex) break;
            }
            if (totalFrame == regionIndex) break;
        }
        animation = new Animation<TextureRegion>(1, textureRegions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    private final CommonState.Direction direction;


    public float getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(float currentTime) {
        this.currentTime = currentTime;
    }

    private float currentTime = 0;
    private boolean fromPlayer = false;

    public boolean isHit() {
        return hit;
    }

    private boolean hit = false;

    public void fireHit(){
        hit = true;
        currentTime = 0;
    }
    /**
     * create a sprite at pixelX,pixelY
     *
     * @param pixelX position pixelX
     * @param pixelY position pixelY
     */
    public Bullet(float pixelX, float pixelY, CommonState.Direction direction, boolean fromPlayer) {
        super(animation.getKeyFrame(0));
        this.fromPlayer = fromPlayer;
        this.direction = direction;
        setPosition(pixelX, pixelY);

    }

    /**
     * update animation by delta
     *
     * @param delta time delta
     */
    @Override
    public void update(float delta) {
        currentTime += delta;

        if (hit) {
            setRegion(animation.getKeyFrame(2, false));
        } else if (currentTime > 0.1) {
            setRegion(animation.getKeyFrame(1, false));
        }

    }

    /**
     * get collision rectangle by default bounding rect
     *
     * @return rect of collision
     */
    public Rectangle getCollisionRect() {
        return getBoundingRectangle();
    }

    public boolean isFromPlayer() {
        return fromPlayer;
    }

    public void setFromPlayer(boolean fromPlayer) {
        this.fromPlayer = fromPlayer;
    }

    public CommonState.Direction getDirection() {
        return direction;
    }
}
