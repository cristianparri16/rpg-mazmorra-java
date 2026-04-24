package com.nuestrojuego.hectorycristian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.TextureRegion;

public class MyMini2DxGame extends BasicGame {
    private Texture hojaSprites;
    private Animation<Sprite> animacionIdle;
    public static final String GAME_IDENTIFIER = "com.nuestrojuego.hectorycristian";

    @Override
    public void initialise() {
        hojaSprites = new Texture(Gdx.files.internal("DungeonAssets/Players/dwarfx4.png"));
        hojaSprites.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        int anchoCelda = hojaSprites.getWidth() / 5;
        int altoCelda = hojaSprites.getHeight() / 9;

        animacionIdle = new Animation<Sprite>();


        TextureRegion frame1 = new TextureRegion(hojaSprites, 0, 0, anchoCelda, altoCelda);

        TextureRegion frame2 = new TextureRegion(hojaSprites, anchoCelda, 0, anchoCelda, altoCelda);

        TextureRegion frame3 = new TextureRegion(hojaSprites, anchoCelda * 2, 0, anchoCelda, altoCelda);


        animacionIdle.addFrame(new Sprite(frame1), 0.7f);
        animacionIdle.addFrame(new Sprite(frame2), 0.7f);
        animacionIdle.addFrame(new Sprite(frame3), 0.7f);
        animacionIdle.setLooping(true);
    }

    @Override
    public void update(float delta) {
        if (animacionIdle != null) {
            animacionIdle.update(delta);
        }
    }

    @Override
    public void interpolate(float alpha) {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(com.badlogic.gdx.graphics.Color.DARK_GRAY);
        g.fillRect(0, 0, 2000, 2000);

        if (animacionIdle != null) {
            Sprite spriteActual = animacionIdle.getCurrentFrame();

            float xCentro = (g.getWindowWidth() / 2f) - (spriteActual.getWidth() / 2f);
            float yCentro = (g.getWindowHeight() / 2f) - (spriteActual.getHeight() / 2f);

            animacionIdle.draw(g, xCentro, yCentro);
        }
    }
}