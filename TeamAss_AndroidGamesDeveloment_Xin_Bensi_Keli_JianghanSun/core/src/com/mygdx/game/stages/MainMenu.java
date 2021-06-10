package com.mygdx.game.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;

public class MainMenu extends Stage {
    private final MyGdxGame game;
    private Image btnStart, btnExit, bkg;

    /**
     * main menu
     * @author Bensi You
     * @param game
     */
    public MainMenu(final MyGdxGame game) {
        super(new FitViewport(MyGdxGame.VIEWPORT_WIDTH, MyGdxGame.VIEWPORT_HEIGHT));
        this.game = game;

        bkg = new Image(new Texture(Gdx.files.internal("bkg_menu.png")));
        btnStart = new Image(new Texture(Gdx.files.internal("btn_start.png")));
        btnExit = new Image(new Texture(Gdx.files.internal("btn_quit.png")));
        bkg.setWidth(MyGdxGame.VIEWPORT_WIDTH);
        bkg.setHeight(MyGdxGame.VIEWPORT_HEIGHT);
        btnStart.setPosition(400 - 100, 190);
        btnExit.setPosition(400 - 100, 90);

        btnExit.setBounds(btnExit.getX(), btnExit.getY(), btnExit.getWidth(), btnExit.getHeight());
        btnStart.setBounds(btnStart.getX(), btnStart.getY(), btnStart.getWidth(), btnStart.getHeight());


        // listeners

        btnStart.addListener(( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound();
                game.startGame();
            }
        }));

        btnExit.addListener(( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound();
                System.exit(0);
            }
        }));

        addActor(bkg);
        addActor(btnStart);
        addActor(btnExit);

    }
}
