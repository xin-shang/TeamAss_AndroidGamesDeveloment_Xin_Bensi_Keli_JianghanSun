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
 * win stage
 * click to return to title
 * @author Bensi You
 */
public class Win extends Stage {
    private final MyGdxGame game;
    private Image imgWin, btnBack;

    public Win(final MyGdxGame game) {
        super(new FitViewport(MyGdxGame.VIEWPORT_WIDTH, MyGdxGame.VIEWPORT_HEIGHT));
        this.game = game;

        imgWin= new Image(new Texture(Gdx.files.internal("win.png")));
        btnBack = new Image(new Texture(Gdx.files.internal("back.png")));

        imgWin.setPosition(0, 0);
        btnBack.setPosition(400 - 64, 190);
        imgWin.setSize(800,480);
        btnBack.setSize(128,64);

        btnBack.setBounds(btnBack.getX(), btnBack.getY(), btnBack.getWidth(), btnBack.getHeight());




        btnBack.addListener(( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound();

                game.title();
            }
        }));

        addActor(imgWin);
        addActor(btnBack);
    }
}
