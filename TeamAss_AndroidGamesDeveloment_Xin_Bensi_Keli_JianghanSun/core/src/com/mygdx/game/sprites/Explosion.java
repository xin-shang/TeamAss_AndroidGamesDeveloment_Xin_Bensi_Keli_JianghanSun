package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Sprite, hold the animatable texture
 * @author Jianghan sun
 */
public class Explosion extends Sprite implements SpriteUpdateAdapter{

    private static final Texture textureSheet;
    private static final Animation<TextureRegion> animation;
    private static final int REG_COL = 3;
    private static final int REG_ROW = 3;
    private static final String TEX_PATH = "explosion.png";
    private static final TextureRegion[] textureRegions;

    static {
        int totalFrame = 7;
        textureSheet = new Texture(TEX_PATH);
        TextureRegion[][] split = TextureRegion.split(textureSheet, textureSheet.getWidth() / REG_COL, textureSheet.getHeight() / REG_ROW);
        textureRegions = new TextureRegion[REG_ROW * REG_COL];
        int regionIndex = 0;
        for (int i = 0; i < REG_ROW; i++) {
            for (int j = 0; j < REG_COL; j++) {
                textureRegions[regionIndex++] = split[i][j];
                if(totalFrame == regionIndex) break;
            }
            if(totalFrame == regionIndex) break;
        }
        animation = new Animation<TextureRegion>(0.1f, textureRegions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
    }



    private float currentTime = 0;


    public boolean isOver() {
        return currentTime > 0.7f;
    }
    /**
     * update animation by delta
     * @param delta time delta
     */
    @Override
    public void update(float delta) {
        currentTime += delta;
        if(currentTime > 0.7f) return;
        // update animation
        setRegion(animation.getKeyFrame(currentTime, true));
    }

    /**
     * create a sprite at pixelX,pixelY
     * @param pixelX position pixelX
     * @param pixelY position pixelY
     */
    public Explosion(float pixelX, float pixelY) {
        super(animation.getKeyFrame(0));
        setRegionWidth(16);
        setRegionHeight(16);
        setPosition(pixelX,pixelY);
    }

    /**
     * get collision rectangle by default bounding rect
     * @return rect of collision
     */
    public Rectangle getCollisionRect() {
        return getBoundingRectangle();
    }
}
