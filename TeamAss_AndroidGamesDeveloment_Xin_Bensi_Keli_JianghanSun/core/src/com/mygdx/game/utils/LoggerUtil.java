package com.mygdx.game.utils;

import com.badlogic.gdx.utils.Logger;

/**
 * @author Ke Li
 * */
public class LoggerUtil {
    public static Logger logger = new Logger("DEBUG", Logger.DEBUG);

    public static void print(String msg){
        logger.debug(msg);
    }
}
