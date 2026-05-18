package com.nuestrojuego.hectorycristian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
    private boolean seleccionandoPersonaje = true;

    private Sprite cofre;
    private Sprite spriteEscalera;
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
    private float xEscalera, yEscalera;

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

        Texture texturaCofre = new Texture(Gdx.files.internal("cofre.png"));
        texturaCofre.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        cofre = new Sprite(texturaCofre);
        cofre.setSize(44, 44);

        Texture texturaEscalera = new Texture(Gdx.files.internal("escalera.png"));
        texturaEscalera.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        spriteEscalera = new Sprite(texturaEscalera);
        spriteEscalera.setSize(56, 56);
    }

    private void seleccionarClase(int clase) {
        String n = "Heroe";
        switch (clase) {
            case 1: Personaje.getInstancia(n, 100, 15, 10, 5, 15, 7, 8, 8); break;
            case 2: Personaje.getInstancia(n, 50, 5, 8, 16, 7, 14, 10, 0); break;
            case 3: Personaje.getInstancia(n, 70, 7, 7, 14, 11, 12, 9, 4); break;
            case 4: Personaje.getInstancia(n, 80, 9, 16, 7, 9, 7, 12, 2); break;
        }
        seleccionandoPersonaje = false;
        nuevaMazmorra();
    }

    private void nuevaMazmorra() {
        if (dungeon == null) {
            dungeon = new GeneradorMazmorra(System.currentTimeMillis());
        }

        if (pisoActual >= 31) {
            Gdx.app.exit();
            return;
        }

        salaActual = dungeon.getEntradasPisos().get(pisoActual);
        xEnano = Gdx.graphics.getWidth() / 2f;
        yEnano = Gdx.graphics.getHeight() / 2f;

        xEscalera = 120f + random.nextFloat() * (Gdx.graphics.getWidth() - 64f - 240f);
        yEscalera = 120f + random.nextFloat() * (Gdx.graphics.getHeight() - 64f - 240f);

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
        if (seleccionandoPersonaje) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) seleccionarClase(1);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) seleccionarClase(2);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) seleccionarClase(3);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) seleccionarClase(4);
            return;
        }

        float ancho = Gdx.graphics.getWidth();
        float alto  = Gdx.graphics.getHeight();
        float sw = spriteEnano.getWidth();
        float sh = spriteEnano.getHeight();

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

        float puertaMinX = ancho / 2f - SEMIPUERTA;
        float puertaMaxX = ancho / 2f + SEMIPUERTA;
        float puertaMinY = alto / 2f - SEMIPUERTA;
        float puertaMaxY = alto / 2f + SEMIPUERTA;

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

        if (salaActual.tieneEscaleras) {
            if (xEnano < xEscalera + 56 && xEnano + sw > xEscalera && yEnano < yEscalera + 56 && yEnano + sh > yEscalera) {
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
        if (seleccionandoPersonaje) {
            g.setBackgroundColor(Color.BLACK);
            g.setColor(Color.WHITE);
            g.drawString("SELECCIONA TU CLASE (Pulsa el numero):", 100, 100);
            g.drawString("1 - Guerrero (Vida: 100, Fuerza: 15)", 100, 150);
            g.drawString("2 - Mago (Vida: 50, Inteligencia: 16)", 100, 180);
            g.drawString("3 - Nigromante (Vida: 70, Inteligencia: 14)", 100, 210);
            g.drawString("4 - Asesino (Vida: 80, Destreza: 16)", 100, 240);
            return;
        }

        float ancho = g.getWindowWidth();
        float alto  = g.getWindowHeight();
        g.drawTexture(texturaBg, 0f, 0f, ancho, alto);

        g.setColor(Color.WHITE);
        g.drawString("PISO: " + (pisoActual + 1) + " | SALA: " + salaActual.getTipoHabitacion(), 20, 20);

        g.setColor(Color.YELLOW);
        if (salaActual.norte != null) g.fillRect(ancho/2f - SEMIPUERTA, 0, SEMIPUERTA*2, GROSOR_PUERTA);
        if (salaActual.sur != null) g.fillRect(ancho/2f - SEMIPUERTA, alto - GROSOR_PUERTA, SEMIPUERTA*2, GROSOR_PUERTA);
        if (salaActual.este != null) g.fillRect(ancho - GROSOR_PUERTA, alto/2f - SEMIPUERTA, GROSOR_PUERTA, SEMIPUERTA*2);
        if (salaActual.oeste != null) g.fillRect(0, alto/2f - SEMIPUERTA, GROSOR_PUERTA, SEMIPUERTA*2);

        if (salaActual.getTipoHabitacion() == TipoHabitacion.COMBATE && enemigoVivo) {
            g.drawSprite(spriteEnemigo, xEnemigo, yEnemigo);
        }

        if (salaActual.getTipoHabitacion() == TipoHabitacion.COFRE && cofre != null) {
            g.drawSprite(cofre, ancho / 2f - 22f, alto / 2f - 22f);
        }

        if (salaActual.tieneEscaleras && spriteEscalera != null) {
            g.drawSprite(spriteEscalera, xEscalera, yEscalera);
        }

        g.drawSprite(spriteEnano, xEnano, yEnano);
    }
}