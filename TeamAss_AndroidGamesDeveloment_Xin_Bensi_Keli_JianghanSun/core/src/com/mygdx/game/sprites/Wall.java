package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Sprite, hold the animatable texture
 * @author Xin Shang
 */
public class Wall extends Sprite implements SpriteUpdateAdapter{

    private static final Texture textureSheet;
    private static final Animation<TextureRegion> animation;
    private static final int REG_COL = 1;
    private static final int REG_ROW = 1;
    private static final String TEX_PATH = "wall.png";
    private static final TextureRegion[] textureRegions;

    static {
        int totalFrame = 1;
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
        animation = new Animation<TextureRegion>(0.01f, textureRegions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
    }



    private float currentTime = 0;


    /**
     * update animation by delta
     * @param delta time delta
     */
    @Override
    public void update(float delta) {
        currentTime += delta;
        // update animation
        setRegion(animation.getKeyFrame(currentTime, false));
    }

    /**
     * create a sprite at pixelX,pixelY
     * @param pixelX position pixelX
     * @param pixelY position pixelY
     */
    public Wall(float pixelX, float pixelY) {
        super(animation.getKeyFrame(0));
        setPosition(pixelX,pixelY);
        setRegionWidth(16);
        setRegionHeight(16);

    }

    /**
     * get collision rectangle by default bounding rect
     * @return rect of collision
     */
    public Rectangle getCollisionRect() {
        return getBoundingRectangle();
    }
}
