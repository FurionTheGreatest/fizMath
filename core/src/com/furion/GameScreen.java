package com.furion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Created by Furion on 14.05.2017.
 */

public class GameScreen implements Screen {

    private final physMath game;

    SpriteBatch batch;
    OrthographicCamera camera;
    Music music;
    Sound catchSound;
    Vector3 touchPos;
    Texture numberMap;
    Texture background;
    Texture loose;
    TextureRegion nul0,nul1,nul2,nul3,nul4,nul5,nul6,nul7,nul8,nul9;
    boolean leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
    Array<Rectangle> numbers;
    long lastSpawnTime;
    int numbersGatchered;
    int numbersLost;
    int SPEED=150;
    Rectangle upperBorder = new Rectangle(0,479,800,1);
    Rectangle lowerBorder = new Rectangle(MainMenuScreen.WIDTH/2,2,MainMenuScreen.WIDTH,1);
    Array<TextureRegion> numbArr = new Array<TextureRegion>();
    TextureRegion temp;
    int i = (int) (Math.random()*10);

    public GameScreen (final physMath game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);

        batch = new SpriteBatch();
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        catchSound = Gdx.audio.newSound(Gdx.files.internal("0.wav"));

        touchPos = new Vector3();
        background = new Texture("background.jpg");
        numberMap = new Texture("numberMap.png");
        loose = new Texture("loose.gif");

        nul1 = new TextureRegion(numberMap,0,0,40,64);   numbArr.add(nul1);
        nul2 = new TextureRegion(numberMap,40,0,60,64);  numbArr.add(nul2);
        nul3 = new TextureRegion(numberMap,100,0,60,64); numbArr.add(nul3);
        nul4 = new TextureRegion(numberMap,170,0,60,64); numbArr.add(nul4);
        nul5 = new TextureRegion(numberMap,230,0,60,64); numbArr.add(nul5);
        nul6 = new TextureRegion(numberMap,300,0,60,64); numbArr.add(nul6);
        nul7 = new TextureRegion(numberMap,360,0,60,64); numbArr.add(nul7);
        nul8 = new TextureRegion(numberMap,420,0,60,64); numbArr.add(nul8);
        nul9 = new TextureRegion(numberMap,480,0,60,64); numbArr.add(nul9);
        nul0 = new TextureRegion(numberMap,550,0,60,64); numbArr.add(nul0);

        numbers = new Array<Rectangle>();
    }

    public void spawnNumbers(){
        Rectangle number = new Rectangle();
        number.x = 0;
        number.y = MathUtils.random(20,400);
        number.width = 80;
        number.height = 80;
        numbers.add(number);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);
        game.font.draw(game.batch, "" + numbersGatchered, 25, 450);
        music.play();
        levelEasy();

        for (Rectangle number : numbers) {
            temp=numbArr.get(i);
            game.batch.draw(numbArr.get(i), number.x, number.y);
        }

        if(numbersGatchered>=10) levelMedium();
        if(numbersGatchered>=25) levelHard();
        if(numbersGatchered>=50) levelHardcore();
        if(numbersLost>=3){
            game.batch.draw(loose, 200, 100);
            numbers.clear();
            lastSpawnTime*=0;
            numbersGatchered=0;
            if(Gdx.input.justTouched()){
                game.setScreen(new GameScreen(game));
                music.pause();
                dispose();
            }
        }
        game.batch.end();
    }
    public void control(Rectangle number,Iterator iter){

        if (number.x > 780) {
            iter.remove();
            numbersLost++;
        }
        else if(Gdx.input.isTouched()){
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            if(number.contains(x,MainMenuScreen.HEIGHT - y)){
                catchSound.play();
                iter.remove();
                numbersGatchered++;
            }
        }
        else if (leftPressed){
            int X = Gdx.input.getX();
            int Y = Gdx.input.getY();
            if(number.contains(X,MainMenuScreen.HEIGHT - Y)){
                catchSound.play();
                iter.remove();
                numbersGatchered++;
            }
        }
    }

    public void levelEasy(){
        if(TimeUtils.nanoTime() - lastSpawnTime > 600000000) spawnNumbers();

        Iterator<Rectangle> iter = numbers.iterator();

        while(iter.hasNext()) {
            Rectangle number = iter.next();
            number.x += SPEED * Gdx.graphics.getDeltaTime();
            control(number,iter);
        }
    }

    public void levelMedium(){
        if(TimeUtils.nanoTime() - lastSpawnTime > 700000000) spawnNumbers();
        Iterator<Rectangle> iter = numbers.iterator();
        while(iter.hasNext()) {
            Rectangle number = iter.next();

            number.x +=0.5*Math.abs(SPEED)* Gdx.graphics.getDeltaTime();
            number.y += 2*MathUtils.cosDeg(number.x*700)+2*MathUtils.sinDeg(number.x*700);

            if(upperBorder.overlaps(number)) number.y -= 10*SPEED * Gdx.graphics.getDeltaTime();
            if(lowerBorder.overlaps(number)) number.y += 10*SPEED * Gdx.graphics.getDeltaTime();

            control(number,iter);
        }
    }

    public void levelHard() {
        if(TimeUtils.nanoTime() - lastSpawnTime > 800000000) spawnNumbers();
        Iterator<Rectangle> iter = numbers.iterator();

        while(iter.hasNext()) {
            Rectangle number = iter.next();
            number.x +=0.4*Math.abs(SPEED)* Gdx.graphics.getDeltaTime();
            number.y += (MathUtils.cosDeg(number.x*1000)+MathUtils.sinDeg(number.x*1000))*MathUtils.random()*10;

            if(upperBorder.overlaps(number)) number.y -= 30*SPEED * Gdx.graphics.getDeltaTime();
            if(lowerBorder.overlaps(number)) number.y += 30*SPEED * Gdx.graphics.getDeltaTime();

            control(number,iter);
        }
    }
    

    public void levelHardcore(){
        if(TimeUtils.nanoTime() - lastSpawnTime > 800000000) spawnNumbers();
        Iterator<Rectangle> iter = numbers.iterator();

        while(iter.hasNext()) {
            Rectangle number = iter.next();
            number.x +=0.3* Math.abs(SPEED)* Gdx.graphics.getDeltaTime();
            number.y += MathUtils.cosDeg(number.x * 1000) * 12;

            if (upperBorder.overlaps(number)) number.y -= 30 * SPEED * Gdx.graphics.getDeltaTime();
            if (lowerBorder.overlaps(number)) number.y += 30 * SPEED * Gdx.graphics.getDeltaTime();

            control(number,iter);
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
    public void dispose () {
        music.dispose();
        batch.dispose();
        numberMap.dispose();
        background.dispose();
        loose.dispose();
        catchSound.dispose();
    }

    @Override
    public void show() {

    }
}
