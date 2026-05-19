package com.nuestrojuego.hectorycristian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.font.FontGlyphLayout;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MyMini2DxGame extends BasicGame {

    public static final String GAME_IDENTIFIER = "com.nuestrojuego.hectorycristian";

    private static final float ANCHO_VIRTUAL = 800f;
    private static final float ALTO_VIRTUAL  = 600f;

    private static final float VELOCIDAD     = 180f;
    private static final float VEL_ENEMIGO   = 100f;
    private static final float SEMIPUERTA    = 40f;
    private static final float GROSOR_PUERTA = 10f;

    private boolean seleccionandoPersonaje = true;
    private boolean mostrandoInventario = false;
    private boolean mostrandoStats = false;
    private int tipoClase = 1;

    private Sprite cofre;
    private Sprite spriteEscalera;
    private Texture texDerecha, texArriba, texAbajo;
    private Sprite spriteEnano;
    private Texture texturaBg;
    private Sprite spriteEnemigo;
    private float xEnemigo, yEnemigo;
    private boolean enemigoVivo = false;

    private GeneradorMazmorra dungeon;
    private int pisoActual = 0;
    private Habitacion salaActual;
    private float xEscalera, yEscalera;

    private float xEnano, yEnano;
    private final Random random = new Random();

    private FontGlyphLayout layoutTexto;
    private int indiceSeleccionado = 0;
    private Objeto objetoEnFoco = null;

    @Override
    public void initialise() {
        DatosJuego.getInstancia().cargar();

        texDerecha = new Texture(Gdx.files.internal("DungeonAssets/Players/derecha.png"));
        texDerecha.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        texArriba  = new Texture(Gdx.files.internal("DungeonAssets/Players/arriba.png"));
        texArriba.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        texAbajo   = new Texture(Gdx.files.internal("DungeonAssets/Players/abajo.png"));
        texAbajo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        spriteEnano = new Sprite(texAbajo);

        texturaBg = new Texture(Gdx.files.internal("DungeonAssets/BackgroundTexture.jpg"));
        texturaBg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texturaBg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        Texture texturaEnemigo = new Texture(Gdx.files.internal("DungeonAssets/Enemies/skeleton.png"));
        texturaEnemigo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteEnemigo  = new Sprite(texturaEnemigo);
        spriteEnemigo.setSize(64, 64);

        Texture texturaCofre = new Texture(Gdx.files.internal("cofre.png"));
        texturaCofre.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        cofre = new Sprite(texturaCofre);
        cofre.setSize(44, 44);

        Texture texturaEscalera = new Texture(Gdx.files.internal("escalera.png"));
        texturaEscalera.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteEscalera = new Sprite(texturaEscalera);
        spriteEscalera.setSize(56, 56);
    }

    private void seleccionarClase(int clase) {
        String n = "Heroe";
        tipoClase = clase;
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
        xEnano = ANCHO_VIRTUAL / 2f;
        yEnano = ALTO_VIRTUAL / 2f;

        xEscalera = 120f + random.nextFloat() * (ANCHO_VIRTUAL - 64f - 240f);
        yEscalera = 120f + random.nextFloat() * (ALTO_VIRTUAL - 64f - 240f);

        colocarEnemigo();
    }

    private void colocarEnemigo() {
        float m = 100f;
        xEnemigo = m + random.nextFloat() * (ANCHO_VIRTUAL - 64f - m * 2f);
        yEnemigo = m + random.nextFloat() * (ALTO_VIRTUAL - 64f - m * 2f);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            mostrandoInventario = !mostrandoInventario;
            mostrandoStats = false;
            indiceSeleccionado = 0;
            objetoEnFoco = null;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            mostrandoStats = !mostrandoStats;
            mostrandoInventario = false;
        }

        if (mostrandoInventario) {
            Inventario inv = Inventario.getInstancia(Personaje.getInstancia());
            ArrayList<Objeto> items = new ArrayList<>(inv.getObjetos().keySet());

            if (!items.isEmpty()) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    indiceSeleccionado = (indiceSeleccionado - 1 + items.size()) % items.size();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    indiceSeleccionado = (indiceSeleccionado + 1) % items.size();
                }
                objetoEnFoco = items.get(indiceSeleccionado);

                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    if (objetoEnFoco instanceof Arma || objetoEnFoco instanceof Armadura) {
                        System.out.println("¡Te has equipado: " + objetoEnFoco.getNombre() + "!");
                    } else if (objetoEnFoco instanceof Pocion) {
                        System.out.println("¡Has consumido la pocion!");
                        inv.eliminarObjeto(objetoEnFoco, 1);
                        indiceSeleccionado = 0;
                        objetoEnFoco = null;
                    }
                }
            } else {
                objetoEnFoco = null;
            }
            return;
        }

        if (mostrandoStats) return;

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

        float puertaMinX = ANCHO_VIRTUAL / 2f - SEMIPUERTA;
        float puertaMaxX = ANCHO_VIRTUAL / 2f + SEMIPUERTA;
        float puertaMinY = ALTO_VIRTUAL / 2f - SEMIPUERTA;
        float puertaMaxY = ALTO_VIRTUAL / 2f + SEMIPUERTA;

        boolean cabePuertaX = (xEnano >= puertaMinX) && (xEnano + sw <= puertaMaxX);
        boolean cabePuertaY = (yEnano >= puertaMinY) && (yEnano + sh <= puertaMaxY);

        if (yEnano < 0 && (salaActual.norte == null || !cabePuertaX)) yEnano = 0;
        if (yEnano + sh > ALTO_VIRTUAL && (salaActual.sur == null || !cabePuertaX)) yEnano = ALTO_VIRTUAL - sh;
        if (xEnano < 0 && (salaActual.oeste == null || !cabePuertaY)) xEnano = 0;
        if (xEnano + sw > ANCHO_VIRTUAL && (salaActual.este == null || !cabePuertaY)) xEnano = ANCHO_VIRTUAL - sw;

        if (salaActual.getTipoHabitacion() == TipoHabitacion.COMBATE && enemigoVivo) {
            float diffX = xEnano - xEnemigo;
            float diffY = yEnano - yEnemigo;
            if (Math.abs(diffX) > Math.abs(diffY)) xEnemigo += Math.signum(diffX) * VEL_ENEMIGO * delta;
            else yEnemigo += Math.signum(diffY) * VEL_ENEMIGO * delta;

            if (xEnano < xEnemigo + 64 && xEnano + sw > xEnemigo && yEnano < yEnemigo + 64 && yEnano + sh > yEnemigo) {
                enemigoVivo = false;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (salaActual.getTipoHabitacion() == TipoHabitacion.COFRE && salaActual.getContenido() != null) {
                float distanciaCofreX = Math.abs(xEnano - (ANCHO_VIRTUAL / 2f - 22f));
                float distanciaCofreY = Math.abs(yEnano - (ALTO_VIRTUAL / 2f - 22f));
                if (distanciaCofreX < 60 && distanciaCofreY < 60) {
                    Cofre mapaCofre = (Cofre) salaActual.getContenido();
                    if (mapaCofre.getObjeto() != null) {
                        Inventario inv = Inventario.getInstancia(Personaje.getInstancia());
                        boolean exito = inv.añadirObjeto(mapaCofre.getObjeto(), 1);
                        if (exito) {
                            salaActual.setContenido(null);
                        }
                    }
                }
            }

            if (salaActual.tieneEscaleras && spriteEscalera != null) {
                float distanciaEscaleraX = Math.abs(xEnano - xEscalera);
                float distanciaEscaleraY = Math.abs(yEnano - yEscalera);
                if (distanciaEscaleraX < 60 && distanciaEscaleraY < 60) {
                    pisoActual++;
                    nuevaMazmorra();
                }
            }
        }

        Habitacion sig = null;
        if (xEnano + sw < -2) { sig = salaActual.oeste; xEnano = ANCHO_VIRTUAL - sw - 8; }
        else if (xEnano > ANCHO_VIRTUAL + 2) { sig = salaActual.este; xEnano = 8; }
        else if (yEnano + sh < -2) { sig = salaActual.norte; yEnano = ALTO_VIRTUAL - sh - 8; }
        else if (yEnano > ALTO_VIRTUAL + 2) { sig = salaActual.sur; yEnano = 8; }

        if (sig != null) {
            salaActual = sig;
            colocarEnemigo();
        }
    }

    @Override
    public void interpolate(float alpha) {}

    @Override
    public void render(Graphics g) {
        float nativoW = g.getWindowWidth();
        float nativoH = g.getWindowHeight();

        if (layoutTexto == null) {
            layoutTexto = g.getFont().newGlyphLayout();
        }

        // === ESCALADO INTERNO 1: RENDER DE LA MAZMORRA (MUNDO DE JUEGO) ===
        g.scale(nativoW / ANCHO_VIRTUAL, nativoH / ALTO_VIRTUAL);

        if (!seleccionandoPersonaje && !mostrandoStats && !mostrandoInventario) {
            g.drawTexture(texturaBg, 0f, 0f, ANCHO_VIRTUAL, ALTO_VIRTUAL);

            g.setColor(Color.YELLOW);
            if (salaActual.norte != null) g.fillRect(ANCHO_VIRTUAL/2f - SEMIPUERTA, 0, SEMIPUERTA*2, GROSOR_PUERTA);
            if (salaActual.sur != null) g.fillRect(ANCHO_VIRTUAL/2f - SEMIPUERTA, ALTO_VIRTUAL - GROSOR_PUERTA, SEMIPUERTA*2, GROSOR_PUERTA);
            if (salaActual.este != null) g.fillRect(ANCHO_VIRTUAL - GROSOR_PUERTA, ALTO_VIRTUAL/2f - SEMIPUERTA, GROSOR_PUERTA, SEMIPUERTA*2);
            if (salaActual.oeste != null) g.fillRect(0, ALTO_VIRTUAL/2f - SEMIPUERTA, GROSOR_PUERTA, SEMIPUERTA*2);

            if (salaActual.getTipoHabitacion() == TipoHabitacion.COMBATE && enemigoVivo) {
                g.drawSprite(spriteEnemigo, xEnemigo, yEnemigo);
            }
            if (salaActual.getTipoHabitacion() == TipoHabitacion.COFRE && salaActual.getContenido() != null) {
                g.drawSprite(cofre, ANCHO_VIRTUAL / 2f - 22f, ALTO_VIRTUAL / 2f - 22f);
            }

            // --- ARREGLADO: El sprite de la escalera se dibuja aquí en coordenadas virtuales ---
            if (salaActual.tieneEscaleras && spriteEscalera != null) {
                g.drawSprite(spriteEscalera, xEscalera, yEscalera);
            }
            g.drawSprite(spriteEnano, xEnano, yEnano);
        }

        // === ESCALADO INTERNO 2: RESTAURAMOS RESOLUCIÓN NATIVA PARA MENÚS 1:1 SÚPER NÍTIDOS ===
        g.scale(ANCHO_VIRTUAL / nativoW, ALTO_VIRTUAL / nativoH);

        float ratioX = nativoW / ANCHO_VIRTUAL;
        float ratioY = nativoH / ALTO_VIRTUAL;

        if (!seleccionandoPersonaje && !mostrandoStats && !mostrandoInventario) {
            if (salaActual.getTipoHabitacion() == TipoHabitacion.COFRE && salaActual.getContenido() != null) {
                float distCofre = (float) Math.sqrt(Math.pow(xEnano - (ANCHO_VIRTUAL / 2f - 22f), 2) + Math.pow(yEnano - (ALTO_VIRTUAL / 2f - 22f), 2));
                if (distCofre < 60) {
                    layoutTexto.setText("[E] INTERACTUAR");
                    float fX = (ANCHO_VIRTUAL / 2f) * ratioX;
                    float fY = (ALTO_VIRTUAL / 2f - 35f) * ratioY;
                    g.setColor(new Color(0f, 0f, 0f, 0.8f));
                    g.fillRect(fX - (layoutTexto.getWidth() / 2f) - 6, fY - 2, layoutTexto.getWidth() + 12, 18);
                    g.setColor(Color.CYAN); g.drawRect(fX - (layoutTexto.getWidth() / 2f) - 6, fY - 2, layoutTexto.getWidth() + 12, 18);
                    g.drawString("[E] INTERACTUAR", fX - (layoutTexto.getWidth() / 2f), fY);
                }
            }

            if (salaActual.tieneEscaleras && spriteEscalera != null) {
                float distEsc = (float) Math.sqrt(Math.pow(xEnano - xEscalera, 2) + Math.pow(yEnano - yEscalera, 2));
                if (distEsc < 60) {
                    layoutTexto.setText("[E] SUBIR PISO");
                    float fX = (xEscalera + 28f) * ratioX;
                    float fY = (yEscalera - 15f) * ratioY;
                    g.setColor(new Color(0f, 0f, 0f, 0.8f));
                    g.fillRect(fX - (layoutTexto.getWidth() / 2f) - 6, fY - 2, layoutTexto.getWidth() + 12, 18);
                    g.setColor(Color.GREEN); g.drawRect(fX - (layoutTexto.getWidth() / 2f) - 6, fY - 2, layoutTexto.getWidth() + 12, 18);
                    g.drawString("[E] SUBIR PISO", fX - (layoutTexto.getWidth() / 2f), fY);
                }
            }
        }

        // --- MENU SELECCION DE PERSONAJE NATIVO PROPORCIONAL ---
        if (seleccionandoPersonaje) {
            g.drawTexture(texturaBg, 0f, 0f, nativoW, nativoH);
            g.setColor(new Color(0f, 0f, 0f, 0.75f));
            g.fillRect(0f, 0f, nativoW, nativoH);

            g.setColor(Color.DARK_GRAY);
            g.fillRect(50, 40, nativoW - 100, 60);
            g.setColor(Color.GOLD);
            g.drawRect(50, 40, nativoW - 100, 60);
            g.drawString("SELECCIONA TU CLASE (Pulsa el numero)", 75, 75);

            float cardW = (nativoW - 160) / 4;
            float cardH = nativoH - 200;

            for (int i = 0; i < 4; i++) {
                float cardX = 50 + i * (cardW + 20);
                float cardY = 130;

                g.setColor(new Color(0.1f, 0.1f, 0.15f, 0.9f));
                g.fillRect(cardX, cardY, cardW, cardH);
                g.setColor(Color.GRAY);
                g.drawRect(cardX, cardY, cardW, cardH);

                g.setColor(Color.CYAN);
                g.drawString("[" + (i + 1) + "]", cardX + 15, cardY + 30);

                g.setColor(Color.WHITE);
                if (i == 0) {
                    g.drawString("GUERRERO", cardX + 15, cardY + 70);
                    g.setColor(Color.RED); g.drawString("Vida: 100", cardX + 15, cardY + 120);
                    g.setColor(Color.ORANGE); g.drawString("Fuerza: 15", cardX + 15, cardY + 150);
                } else if (i == 1) {
                    g.drawString("MAGO", cardX + 15, cardY + 70);
                    g.setColor(Color.RED); g.drawString("Vida: 50", cardX + 15, cardY + 120);
                    g.setColor(Color.ORANGE); g.drawString("Inteli: 16", cardX + 15, cardY + 150);
                } else if (i == 2) {
                    g.drawString("NIGROMANTE", cardX + 15, cardY + 70);
                    g.setColor(Color.RED); g.drawString("Vida: 70", cardX + 15, cardY + 120);
                    g.setColor(Color.ORANGE); g.drawString("Inteli: 14", cardX + 15, cardY + 150);
                } else {
                    g.drawString("ASESINO", cardX + 15, cardY + 70);
                    g.setColor(Color.RED); g.drawString("Vida: 80", cardX + 15, cardY + 120);
                    g.setColor(Color.ORANGE); g.drawString("Destreza: 16", cardX + 15, cardY + 150);
                }
            }
            return;
        }

        // --- HOJA DE PERSONAJE NATIVA (TECLA O) ---
        if (mostrandoStats) {
            g.drawTexture(texturaBg, 0f, 0f, nativoW, nativoH);
            g.setColor(new Color(0.03f, 0.04f, 0.06f, 0.96f));
            g.fillRect(25, 25, nativoW - 50, nativoH - 50);
            g.setColor(Color.CYAN);
            g.drawRect(25, 25, nativoW - 50, nativoH - 50);

            Personaje p = Personaje.getInstancia();
            String nombreClase = (tipoClase == 1) ? "GUERRERO" : (tipoClase == 2) ? "MAGO" : (tipoClase == 3) ? "NIGROMANTE" : "ASESINO";

            g.setColor(Color.GOLD); g.drawString("HOJA DE PERSONAJE - " + nombreClase, 50, 60);
            g.setColor(Color.WHITE); g.drawString("Presiona [O] para cerrar menu", nativoW - 280, 60);
            g.setColor(Color.GRAY); g.drawLineSegment(45, 95, nativoW - 45, 95);

            float col1X = 70;
            float col2X = Math.max(col1X + 320, nativoW * 0.52f);
            float filaY = 140;
            float saltoFila = 45;

            g.setColor(Color.CYAN); g.drawString("=== ATRIBUTOS PRINCIPALES ===", col1X, filaY);
            g.setColor(Color.WHITE);
            g.drawString("FUERZA (STR):         " + p.getFuerza() + "   (Carga de Inventario)", col1X, filaY + saltoFila);
            g.drawString("DESTREZA (DEX):       " + p.getDestreza() + "   (Velocidad y Evasion)", col1X, filaY + (saltoFila * 2));
            g.drawString("INTELIGENCIA (INT):   " + p.getInteligencia() + "   (Potencia de Hechizos)", col1X, filaY + (saltoFila * 3));
            g.drawString("CONSTITUCION (CON):   " + p.getConstitucion() + "   (Bonificacion de HP)", col1X, filaY + (saltoFila * 4));
            g.drawString("SUERTE (LUK):         " + p.getSuerte() + "   (Calidad del Botin)", col1X, filaY + (saltoFila * 5));

            g.setColor(Color.GOLD); g.drawString("=== ESTADO DE COMBATE ===", col2X, filaY);
            g.setColor(Color.WHITE);
            g.drawString("DEFENSA TOTAL BASE:   " + p.getDefensaBase() + " Pts", col2X, filaY + saltoFila);
            g.drawString("SABIDURIA:            " + p.getSabiduria() + " Pts", col2X, filaY + (saltoFila * 2));
            g.setColor(Color.RED); g.drawString("SALUD ACTUAL:         " + p.getVidaActual() + " / " + p.getVidaMaxima() + " HP", col2X, filaY + (saltoFila * 4));
            g.setColor(Color.BLUE); g.drawString("MANA MÁXIMO:          " + p.getMana() + " / " + ((p.getInteligencia() * 10) + (p.getSabiduria() * 5)) + " MP", col2X, filaY + (saltoFila * 5));
            return;
        }

        if (mostrandoInventario) {
            g.drawTexture(texturaBg, 0f, 0f, nativoW, nativoH);
            g.setColor(new Color(0.04f, 0.04f, 0.06f, 0.94f));
            g.fillRect(25, 25, nativoW - 50, nativoH - 50);
            g.setColor(Color.GOLD);
            g.drawRect(25, 25, nativoW - 50, nativoH - 50);

            Inventario inv = Inventario.getInstancia(Personaje.getInstancia());
            g.setColor(Color.CYAN); g.drawString("INVENTARIO DE TU HEROE", 45, 60);
            g.setColor(Color.WHITE); g.drawString("Carga Actual: " + inv.getPesoActual() + " / " + inv.getCapacidad() + " kg", 45, 85);
            g.drawString("Menu: [W/S] Navegar | [ENTER] Interactuar | [I] Salir", nativoW - 440, 60);
            g.setColor(Color.GRAY); g.drawLineSegment(45, 105, nativoW - 45, 105);

            float divisorX = nativoW * 0.53f;
            g.drawLineSegment(divisorX, 105, divisorX, nativoH - 45);

            int itemY = 130;
            ArrayList<Objeto> items = new ArrayList<>(inv.getObjetos().keySet());

            if (items.isEmpty()) {
                g.setColor(Color.GRAY); g.drawString("El inventario esta completamente vacio.", 55, itemY);
            } else {
                for (int i = 0; i < items.size(); i++) {
                    Objeto obj = items.get(i);
                    int cant = inv.getObjetos().get(obj);

                    String nombreRender = obj.getNombre().toUpperCase();
                    layoutTexto.setText(nombreRender);

                    float cajaW = layoutTexto.getWidth() + 30;
                    float cajaH = layoutTexto.getHeight() + 14;

                    if (i == indiceSeleccionado) {
                        g.setColor(new Color(0.15f, 0.25f, 0.4f, 0.7f));
                        g.fillRect(45, itemY - 6, cajaW, cajaH);
                        g.setColor(Color.CYAN);
                        g.drawRect(45, itemY - 6, cajaW, cajaH);
                    }

                    Color colorItem = Color.WHITE;
                    String nombreMinuscula = obj.getNombre().toLowerCase();
                    if (nombreMinuscula.contains("destino") || nombreMinuscula.contains("celestial")) colorItem = Color.ORANGE;
                    else if (nombreMinuscula.contains("abismo") || nombreMinuscula.contains("sombr") || nombreMinuscula.contains("alma")) colorItem = Color.PURPLE;
                    else if (nombreMinuscula.contains("élfic") || nombreMinuscula.contains("rúnic") || nombreMinuscula.contains("plata") || nombreMinuscula.contains("cristal")) colorItem = Color.CYAN;
                    else if (nombreMinuscula.contains("acero") || nombreMinuscula.contains("malla") || nombreMinuscula.contains("placas") || nombreMinuscula.contains("tejo") || nombreMinuscula.contains("ceniza")) colorItem = Color.GREEN;
                    else colorItem = Color.LIGHT_GRAY;

                    g.setColor(colorItem);
                    g.drawString(nombreRender, 60, itemY + (cajaH - g.getFont().getLineHeight()) / 2f);
                    g.setColor(Color.GOLD);
                    g.drawString("x" + cant, divisorX - 60, itemY + (cajaH - g.getFont().getLineHeight()) / 2f);

                    itemY += (cajaH + 12);
                    if (itemY > nativoH - 65) break;
                }
            }

            if (objetoEnFoco != null) {
                float panelX = divisorX + 20;
                float panelY = 130;

                g.setColor(Color.CYAN); g.drawString("=== INSPECTOR ===", panelX, panelY);
                g.setColor(Color.WHITE);
                g.drawString("Nombre: " + objetoEnFoco.getNombre(), panelX, panelY + 30);
                g.drawString("Descripcion: " + objetoEnFoco.getOriginalDescripcion(), panelX, panelY + 55);
                g.drawString("Peso Unitario: " + objetoEnFoco.getPeso() + " kg", panelX, panelY + 80);

                g.setColor(Color.GOLD);
                float statsY = panelY + 115;
                if (objetoEnFoco instanceof Espada) {
                    g.drawString("TIPO: Espada Corto Alcance", panelX, statsY);
                    g.drawString("ATQ: +" + ((Espada) objetoEnFoco).getDañoArma() + " Fisico", panelX, statsY + 25);
                } else if (objetoEnFoco instanceof ArmaADistancia) {
                    g.drawString("TIPO: Arma Distancia", panelX, statsY);
                    g.drawString("ATQ: +" + ((ArmaADistancia) objetoEnFoco).getDañoArma() + " Lejano", panelX, statsY + 25);
                } else if (objetoEnFoco instanceof Baston) {
                    g.drawString("TIPO: Baston Magico", panelX, statsY);
                    g.drawString("PODER MAGICO: +" + ((Baston) objetoEnFoco).getDañoArma(), panelX, statsY + 25);
                    g.drawString("MANA BONUS: +" + ((Baston) objetoEnFoco).getManaBonus(), panelX, statsY + 50);
                } else if (objetoEnFoco instanceof Armadura) {
                    g.drawString("TIPO: Armadura Corporal", panelX, statsY);
                    g.drawString("DEFENSA: +" + ((Armadura) objetoEnFoco).getArmadura(), panelX, statsY + 25);
                    g.drawString("REQ. FUERZA: " + ((Armadura) objetoEnFoco).getFuerzaMinima(), panelX, statsY + 50);
                } else if (objetoEnFoco instanceof Pocion) {
                    g.drawString("TIPO: Consumible", panelX, statsY);
                    g.drawString("EFECTO: Restaura Recursos", panelX, statsY + 25);
                }

                g.setColor(Color.GOLD); g.drawRect(panelX, nativoH - 85, nativoW - panelX - 45, 45);
                g.setColor(Color.WHITE);
                String accionText = (objetoEnFoco instanceof Pocion) ? "[ENTER] Consumir Pocion" : "[ENTER] Equipar Objeto";
                g.drawString(accionText, panelX + 20, nativoH - 60);
            }
            return;
        }

        if (!seleccionandoPersonaje) {
            Personaje p = Personaje.getInstancia();
            layoutTexto.setText("PISO " + (pisoActual + 1) + " - " + salaActual.getTipoHabitacion());
            float boxW = Math.max(220, layoutTexto.getWidth() + 24);
            float boxH = 85;

            g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.85f)); g.fillRect(12, 12, boxW, boxH);
            g.setColor(Color.GOLD); g.drawRect(12, 12, boxW, boxH);

            g.setColor(Color.WHITE); g.drawString("PISO " + (pisoActual + 1) + " - " + salaActual.getTipoHabitacion(), 24, 23);

            g.setColor(Color.BLACK); g.fillRect(22, 44, boxW - 20, 12);
            g.setColor(Color.RED);
            float ratioVida = (float) p.getVidaActual() / p.getVidaMaxima();
            g.fillRect(22, 44, (boxW - 20) * ratioVida, 12);
            g.setColor(Color.WHITE); g.drawString("HP: " + p.getVidaActual() + "/" + p.getVidaMaxima(), 26, 45);

            g.setColor(Color.BLACK); g.fillRect(22, 64, boxW - 20, 12);
            g.setColor(Color.BLUE); g.fillRect(22, 64, boxW - 20, 12);
            g.setColor(Color.WHITE); g.drawString("MP: " + p.getMana() + "/" + ((p.getInteligencia() * 10) + (p.getSabiduria() * 5)) + " MP", 26, 65);
        }
    }
}.