package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.stages.*;

/**
 * Main class of game
 * @author Xin Shang
 */
public class MyGdxGame extends ApplicationAdapter {


    public static final int VIEWPORT_WIDTH = 800;
    public static final int VIEWPORT_HEIGHT = 480;
    // current stage
    StageState stageState = StageState.MainMenu;
    // default viewport
    Viewport viewport;
    boolean isPaused;
    // stages
    GameScreen stageGame;
    MainMenu stageMainMenu;
    Win stageWin;
    Gameover stageGameOver;
    Pause stagePause;
    Sound soundWin;
    Music bgm;
    Sound soundClick;
    Music soundExplosion;
    Music soundExplosionLoud;

    /**
     * play click sound
     */
    public void clickSound() {
        soundClick.play();
    }

    /**
     * play explosion sound
     */
    public void explosionSound() {
        soundExplosion.play();
    }
    /**
     * play explosion sound
     */
    public void explosionLoudSound() {
        soundExplosionLoud.play();
    }

    /**
     * start game
     * active game input
     */
    public void startGame() {
        stageGame.reload(0);
        InputMultiplexer inputMultiplexer = new InputMultiplexer(stagePause, stageGame);
        Gdx.input.setInputProcessor(inputMultiplexer);
        stageState = StageState.Game;
    }

    /**
     * turn to title (Main menu)
     */
    public void title() {
        Gdx.input.setInputProcessor(stageMainMenu);
        stageState = StageState.MainMenu;
    }

    /**
     * turn to game failed
     */
    public void failed() {
        Gdx.input.setInputProcessor(stageGameOver);
        stageState = StageState.GameOver;
    }

    /**
     * pause game
     */
    @Override
    public void pause() {
        super.pause();
        isPaused = true;
    }

    /**
     * resume game
     */
    @Override
    public void resume() {
        isPaused = false;
        super.resume();
    }

    /**
     * win game
     */
    public void winGame() {
        soundWin.play();
        Gdx.input.setInputProcessor(stageWin);
        stageState = StageState.GameWin;
    }

    /**
     * init routine
     */
    @Override
    public void create() {
        // bgm
        bgm = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3"));
        bgm.setLooping(true);
        bgm.play();

        // sounds
        soundWin = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
        soundExplosion = Gdx.audio.newMusic(Gdx.files.internal("explosion.mp3"));
        soundExplosionLoud = Gdx.audio.newMusic(Gdx.files.internal("loud_explosion.mp3"));

        soundClick = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));


//        OrthographicCamera gameStageCamera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        //stages
        stagePause = new Pause(this);
        stageGame = new GameScreen(this);
        stageGameOver = new Gameover(this);
        stageWin = new Win(this);
        stageMainMenu = new MainMenu(this);

        Gdx.input.setInputProcessor(stageMainMenu);
    }

    /**
     * resize viewport
     *
     * @param width  w
     * @param height h
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * Render routine
     */
    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        float deltaTime = Gdx.graphics.getDeltaTime();

        // render main menu
        if (stageState == StageState.MainMenu) {
            stageMainMenu.act(deltaTime);
            stageMainMenu.draw();
        }

        // render game scene
        if (stageState == StageState.Game) {

            // only render pause scene when player is alive
            if (stageGame.notDead()) {
                stagePause.act(deltaTime);
            }

            if (!isPaused) {
                stageGame.act(deltaTime);
                stageGame.draw();
            }

            // only render pause scene when player is alive
            if (stageGame.notDead()) {
                stagePause.draw();
            }
        }


        if (stageState == StageState.GameOver) {
            stageGameOver.act(deltaTime);
            stageGameOver.draw();
        }


        if (stageState == StageState.GameWin) {
            stageWin.act(deltaTime);
            stageWin.draw();

        }


    }

    /**
     * dispose resource
     */
    @Override
    public void dispose() {
        stageMainMenu.dispose();
        stageGame.dispose();
        stageGameOver.dispose();
        stageWin.dispose();
    }


    // game stage state
    enum StageState {
        MainMenu, Game, GameOver, GameWin,
    }
}
