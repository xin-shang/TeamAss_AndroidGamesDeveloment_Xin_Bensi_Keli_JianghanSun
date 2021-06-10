package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Camera;

/**
 * updatable sprite adapter
 * * @author Xin Shang
 */
public interface SpriteUpdateAdapter {
    /**
     * called by stage, update sprite position
     * @param delta time delta
     */
    void update(float delta);
}
