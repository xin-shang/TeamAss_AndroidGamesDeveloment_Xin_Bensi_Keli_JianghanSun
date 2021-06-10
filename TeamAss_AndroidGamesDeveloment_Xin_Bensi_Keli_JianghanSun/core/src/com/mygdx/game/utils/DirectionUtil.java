package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.sprites.CommonState;

public class DirectionUtil {
    /**
     * player state enums
     * @author Ke Li
     */

    public static void rotateToDirection(Sprite sprite, CommonState.Direction direction) {
        switch (direction) {
            case Upward:
                sprite.setRotation(0);
                break;
            case Downward:
                sprite.setRotation(180);
                break;
            case Leftward:
                sprite.setRotation(90);
                break;
            case Rightward:
                sprite.setRotation(270);
        }
    }

}
