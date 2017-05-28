package com.furion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;


/**
 * Created by Furion on 14.05.2017.
 */

public class MainMenuScreen implements Screen {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;

    final physMath game;
    OrthographicCamera camera;
    Texture menu;
    Music music;

    public MainMenuScreen(final physMath game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);
        menu = new Texture("menu.jpg");
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(menu,0,0,WIDTH,HEIGHT);
        music.play();
        game.font.draw(game.batch, "Touch to start",WIDTH/2,HEIGHT/4);
        game.batch.end();

        if(Gdx.input.justTouched()){
            game.setScreen(new GameScreen(game));
            music.pause();
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.dispose();
        menu.dispose();
        music.dispose();
    }
}
