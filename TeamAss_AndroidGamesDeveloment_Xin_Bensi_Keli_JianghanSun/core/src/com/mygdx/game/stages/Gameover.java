package com.mygdx.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;

/**
 * game over stage
 * @author Bensi You
 */
public class Gameover extends Stage {
    private final MyGdxGame game;
    private Image imgFailed, btnBack;

    public Gameover(final MyGdxGame game) {
        super(new FitViewport(MyGdxGame.VIEWPORT_WIDTH, MyGdxGame.VIEWPORT_HEIGHT));
        this.game = game;

        // button and image
        imgFailed= new Image(new Texture(Gdx.files.internal("failed.png")));
        btnBack = new Image(new Texture(Gdx.files.internal("back.png")));

        imgFailed.setPosition(200, 290);
        btnBack.setPosition(400 - 64, 190);

        btnBack.setBounds(btnBack.getX(), btnBack.getY(), btnBack.getWidth(), btnBack.getHeight());



        // click listener
        btnBack.addListener(( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound();

                game.title();
            }
        }));

        addActor(imgFailed);
        addActor(btnBack);
    }
}
