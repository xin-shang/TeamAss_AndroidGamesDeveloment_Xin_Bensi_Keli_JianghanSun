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
 * pause stage
 * @author Bensi You
 */
public class Pause extends Stage {
    private final MyGdxGame game;
    private Image pauseBkg, btnPause, btnExit;

    public Pause(final MyGdxGame game) {
        super(new FitViewport(MyGdxGame.VIEWPORT_WIDTH, MyGdxGame.VIEWPORT_HEIGHT));
        this.game = game;

        pauseBkg = new Image(new Texture(Gdx.files.internal("pause_bkg.png")));
        btnPause = new Image(new Texture(Gdx.files.internal("pause.png")));
        btnExit = new Image(new Texture(Gdx.files.internal("btn_quit.png")));

        pauseBkg.setPosition(0, 0);
        btnPause.setPosition(650, 400);
        btnExit.setPosition(320, 100);

        pauseBkg.setSize(800,480);
        btnPause.setSize(128,64);
        pauseBkg.setVisible(false);
        btnExit.setVisible(false);

        btnPause.setBounds(btnPause.getX(), btnPause.getY(), btnPause.getWidth(), btnPause.getHeight());

        btnPause.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound();
                game.pause();
                pauseBkg.setVisible(true);
                btnExit.setVisible(true);

                btnPause.setVisible(false);
            }
        }));

        // exit button
        btnExit.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        }));

        // background click - resume
        pauseBkg.addListener((new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound();
                game.resume();
                pauseBkg.setVisible(false);
                btnExit.setVisible(false);

                btnPause.setVisible(true);
            }
        }));

        addActor(pauseBkg);
        addActor(btnPause);
        addActor(btnExit);

    }

    @Override
    public void act() {
        super.act();
    }
}
