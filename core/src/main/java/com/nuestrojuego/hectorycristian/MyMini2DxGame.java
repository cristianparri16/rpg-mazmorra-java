package com.nuestrojuego.hectorycristian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import java.util.Random;

public class MyMini2DxGame extends BasicGame {

    public static final String GAME_IDENTIFIER = "com.nuestrojuego.hectorycristian";

    private static final float VELOCIDAD     = 180f;
    private static final float VEL_ENEMIGO   = 100f;
    private static final float SEMIPUERTA    = 40f;
    private static final float GROSOR_PUERTA = 10f;

    private Texture texDerecha, texArriba, texAbajo;
    private Sprite spriteEnano;

    private Texture texturaBg;
    private Texture texturaEnemigo;
    private Sprite spriteEnemigo;
    private float xEnemigo, yEnemigo;
    private boolean enemigoVivo = false;

    private GeneradorMazmorra dungeon;
    private int pisoActual = 0;
    private Habitacion salaActual;
    private Habitacion salaEscalera;
    private float xEscalera, yEscalera;
    private boolean escaleraColocada = false;

    private float xEnano, yEnano;
    private final Random random = new Random();

    @Override
    public void initialise() {
        texDerecha = new Texture(Gdx.files.internal("DungeonAssets/Players/derecha.png"));
        texArriba  = new Texture(Gdx.files.internal("DungeonAssets/Players/arriba.png"));
        texAbajo   = new Texture(Gdx.files.internal("DungeonAssets/Players/abajo.png"));

        spriteEnano = new Sprite(texAbajo);


        texturaBg = new Texture(Gdx.files.internal("DungeonAssets/BackgroundTexture.jpg"));
        texturaBg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        texturaEnemigo = new Texture(Gdx.files.internal("DungeonAssets/Enemies/skeleton.png"));
        spriteEnemigo  = new Sprite(texturaEnemigo);
        spriteEnemigo.setSize(64, 64);
        CreadorPersonaje creador = new CreadorPersonaje();
        creador.crearPersonaje();
        nuevaMazmorra();
    }

    private void nuevaMazmorra() {
        dungeon = new GeneradorMazmorra(System.currentTimeMillis());
        salaActual = dungeon.getEntradasPisos().get(0);
        salaEscalera = null;
        escaleraColocada = false;

        xEnano = Gdx.graphics.getWidth() / 2f;
        yEnano = Gdx.graphics.getHeight() / 2f;
        colocarEnemigo();
    }

    private void colocarEnemigo() {
        float ancho = Gdx.graphics.getWidth();
        float alto  = Gdx.graphics.getHeight();
        float m = 100f;
        xEnemigo = m + random.nextFloat() * (ancho - 64f - m * 2f);
        yEnemigo = m + random.nextFloat() * (alto  - 64f - m * 2f);
        enemigoVivo = true;
    }

    @Override
    public void update(float delta) {
        float ancho = Gdx.graphics.getWidth();
        float alto  = Gdx.graphics.getHeight();
        float sw = spriteEnano.getWidth();
        float sh = spriteEnano.getHeight();

        float puertaMinX = ancho / 2f - SEMIPUERTA;
        float puertaMaxX = ancho / 2f + SEMIPUERTA;
        float puertaMinY = alto / 2f - SEMIPUERTA;
        float puertaMaxY = alto / 2f + SEMIPUERTA;

        if (Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            yEnano -= VELOCIDAD * delta;
            spriteEnano.setTexture(texArriba);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
            yEnano += VELOCIDAD * delta;
            spriteEnano.setTexture(texAbajo);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
            xEnano -= VELOCIDAD * delta;
            spriteEnano.setTexture(texDerecha);
            spriteEnano.setFlip(true, false);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
            xEnano += VELOCIDAD * delta;
            spriteEnano.setTexture(texDerecha);
            spriteEnano.setFlip(false, false);
        }

        boolean cabePuertaX = (xEnano >= puertaMinX) && (xEnano + sw <= puertaMaxX);
        boolean cabePuertaY = (yEnano >= puertaMinY) && (yEnano + sh <= puertaMaxY);

        if (yEnano < 0 && (salaActual.norte == null || !cabePuertaX)) yEnano = 0;
        if (yEnano + sh > alto && (salaActual.sur == null || !cabePuertaX)) yEnano = alto - sh;
        if (xEnano < 0 && (salaActual.oeste == null || !cabePuertaY)) xEnano = 0;
        if (xEnano + sw > ancho && (salaActual.este == null || !cabePuertaY)) xEnano = ancho - sw;

        if (salaActual.getTipoHabitacion() == TipoHabitacion.COMBATE && enemigoVivo) {
            float diffX = xEnano - xEnemigo;
            float diffY = yEnano - yEnemigo;
            if (Math.abs(diffX) > Math.abs(diffY)) xEnemigo += Math.signum(diffX) * VEL_ENEMIGO * delta;
            else yEnemigo += Math.signum(diffY) * VEL_ENEMIGO * delta;

            if (xEnano < xEnemigo + 64 && xEnano + sw > xEnemigo && yEnano < yEnemigo + 64 && yEnano + sh > yEnemigo) {
                enemigoVivo = false;
            }
        }

        if (!escaleraColocada) {
            TipoHabitacion tipo = salaActual.getTipoHabitacion();
            if (tipo == TipoHabitacion.COMBATE || tipo == TipoHabitacion.COFRE || tipo == TipoHabitacion.BOSS) {
                int puertas = 0;
                if (salaActual.norte != null) puertas++;
                if (salaActual.sur != null) puertas++;
                if (salaActual.este != null) puertas++;
                if (salaActual.oeste != null) puertas++;

                if (puertas == 1) {
                    salaEscalera = salaActual;
                    escaleraColocada = true;
                    float m = 120f;
                    xEscalera = m + random.nextFloat() * (ancho - 64f - m * 2f);
                    yEscalera = m + random.nextFloat() * (alto - 64f - m * 2f);
                }
            }
        }

        if (salaActual == salaEscalera) {
            if (xEnano < xEscalera + 40 && xEnano + sw > xEscalera && yEnano < yEscalera + 40 && yEnano + sh > yEscalera) {
                pisoActual++;
                nuevaMazmorra();
            }
        }

        Habitacion sig = null;
        if (xEnano + sw < 0) { sig = salaActual.oeste; xEnano = ancho - sw - 5; }
        else if (xEnano > ancho) { sig = salaActual.este; xEnano = 5; }
        else if (yEnano + sh < 0) { sig = salaActual.norte; yEnano = alto - sh - 5; }
        else if (yEnano > alto) { sig = salaActual.sur; yEnano = 5; }

        if (sig != null) {
            salaActual = sig;
            colocarEnemigo();
        }
    }

    @Override
    public void interpolate(float alpha) {}

    @Override
    public void render(Graphics g) {
        float ancho = g.getWindowWidth();
        float alto  = g.getWindowHeight();
        g.drawTexture(texturaBg, 0f, 0f, ancho, alto);
        g.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        g.drawString("PISO: " + (pisoActual + 1) + " | SALA: " + salaActual.getTipoHabitacion(), 20, 20);
        g.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        if (salaActual.norte != null) g.fillRect(ancho/2f - SEMIPUERTA, 0, SEMIPUERTA*2, GROSOR_PUERTA);
        if (salaActual.sur != null) g.fillRect(ancho/2f - SEMIPUERTA, alto - GROSOR_PUERTA, SEMIPUERTA*2, GROSOR_PUERTA);
        if (salaActual.este != null) g.fillRect(ancho - GROSOR_PUERTA, alto/2f - SEMIPUERTA, GROSOR_PUERTA, SEMIPUERTA*2);
        if (salaActual.oeste != null) g.fillRect(0, alto/2f - SEMIPUERTA, GROSOR_PUERTA, SEMIPUERTA*2);

        if (salaActual.getTipoHabitacion() == TipoHabitacion.COMBATE && enemigoVivo) {
            g.drawSprite(spriteEnemigo, xEnemigo, yEnemigo);
        }

        if (salaActual == salaEscalera) {
            g.setColor(com.badlogic.gdx.graphics.Color.RED);
            g.fillRect(xEscalera, yEscalera, 40, 40);
        }

        g.drawSprite(spriteEnano, xEnano, yEnano);
    }
}