package com.nuestrojuego.hectorycristian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.graphics.TextureRegion;

public class MyMini2DxGame extends BasicGame {
    private Texture hojaSprites;
    private Sprite spriteEnano;
    private GeneradorMazmorra dungeon;
    private int pisoActual = 0;
    private Habitacion salaActual;
    private Sprite bgSala;
    private Sprite spriteEnemigo;
    private float xEnano, yEnano;
    private float velocidad = 250f;
    private TextureRegion frameLado, frameArriba, frameAbajo;

    public static final String GAME_IDENTIFIER = "com.nuestrojuego.hectorycristian";

    @Override
    public void initialise() {
        Texture texturaEnano = new Texture(Gdx.files.internal("DungeonAssets/Players/dwarfx4.png"));

        int anchoEnano = texturaEnano.getWidth() / 5;
        int altoEnano = texturaEnano.getHeight() / 9;


        frameLado = new TextureRegion(texturaEnano, 0, 0, anchoEnano, altoEnano);

        frameArriba = new TextureRegion(texturaEnano, anchoEnano, 0, anchoEnano, altoEnano);

        frameAbajo = new TextureRegion(texturaEnano, anchoEnano * 2, 0, anchoEnano, altoEnano);


        spriteEnano = new Sprite(frameAbajo);

        xEnano = Gdx.graphics.getWidth() / 2f;
        yEnano = Gdx.graphics.getHeight() / 2f;
        dungeon = new GeneradorMazmorra(System.currentTimeMillis());
        salaActual = dungeon.getEntradasPisos().get(pisoActual);

        xEnano = Gdx.graphics.getWidth() / 2f;
        yEnano = Gdx.graphics.getHeight() / 2f;

        Texture texturaPiedra = new Texture(Gdx.files.internal("DungeonAssets/BackgroundTexture.jpg"));
        texturaPiedra.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgSala = new Sprite(texturaPiedra);

        spriteEnemigo = new Sprite(new Texture(Gdx.files.internal("DungeonAssets/Enemies/skeleton.png")));
    }

    @Override
    public void update(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            yEnano -= velocidad * delta;
            // Cambiar al frame de espaldas y asegurar que NO esté invertido
            spriteEnano.setRegion(frameArriba);
            spriteEnano.setFlip(false, false);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            yEnano += velocidad * delta;
            // Cambiar al frame de frente y asegurar que NO esté invertido
            spriteEnano.setRegion(frameAbajo);
            spriteEnano.setFlip(false, false);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            xEnano -= velocidad * delta;
            // Usar frame de lado e INVERTIR horizontalmente (true)
            spriteEnano.setRegion(frameLado);
            spriteEnano.setFlip(true, false);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            xEnano += velocidad * delta;
            // Usar frame de lado y asegurar que NO esté invertido (false)
            spriteEnano.setRegion(frameLado);
            spriteEnano.setFlip(false, false);
        }

        float ancho = Gdx.graphics.getWidth();
        float alto = Gdx.graphics.getHeight();
        float offsetSprite = spriteEnano.getHeight() * spriteEnano.getScaleY();

        if (salaActual.sur == null) {
            yEnano = Math.min(yEnano, alto - offsetSprite);
        }
        if (salaActual.norte == null) {
            yEnano = Math.max(yEnano, 0);
        }
        if (salaActual.este == null) {
            xEnano = Math.min(xEnano, ancho - spriteEnano.getWidth() * spriteEnano.getScaleX());
        }
        if (salaActual.oeste == null) {
            xEnano = Math.max(xEnano, 0);
        }

        if (yEnano > alto) {
            salaActual = salaActual.sur;
            yEnano = 10;
        } else if (yEnano < -offsetSprite) {
            salaActual = salaActual.norte;
            yEnano = alto - 10;
        } else if (xEnano > ancho) {
            salaActual = salaActual.este;
            xEnano = 10;
        } else if (xEnano < -spriteEnano.getWidth()) {
            salaActual = salaActual.oeste;
            xEnano = ancho - 10;
        }
    }

    @Override
    public void interpolate(float alpha) {}

    @Override
    public void render(Graphics g) {
        if (bgSala != null) {
            g.drawTexture(bgSala.getTexture(), 0f, 0f, g.getWindowWidth(), g.getWindowHeight());
        }

        g.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        g.drawString("PISO: " + (pisoActual + 1) + " | SALA: " + salaActual.getTipoHabitacion(), 20, 20);

        g.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        if (salaActual.norte != null) g.fillRect(g.getWindowWidth()/2f - 32, 0, 64, 10);
        if (salaActual.sur != null)   g.fillRect(g.getWindowWidth()/2f - 32, g.getWindowHeight() - 10, 64, 10);
        if (salaActual.este != null)  g.fillRect(g.getWindowWidth() - 10, g.getWindowHeight()/2f - 32, 10, 64);
        if (salaActual.oeste != null) g.fillRect(0, g.getWindowHeight()/2f - 32, 10, 64);

        if (salaActual.getTipoHabitacion() == TipoHabitacion.COMBATE) {
            g.drawSprite(spriteEnemigo, g.getWindowWidth()/2f - 32, g.getWindowHeight()/2f - 100);
        }

        if (spriteEnano != null) {
            g.drawSprite(spriteEnano, xEnano, yEnano);
        }
    }
}