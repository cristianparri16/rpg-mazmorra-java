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

/**
 * El motor principal de tu aventura. Controla la pantalla, las teclas que pulsas,
 * el movimiento del enano y los guantazos en combate.
 */
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
    private boolean mostrandoTienda = false;
    private int tipoClase = 1;

    private Sprite cofre, spriteEscalera, spriteEnano, spriteEnemigo;
    private Texture texDerecha, texArriba, texAbajo, texturaBg;
    private float xEnemigo, yEnemigo, xEscalera, yEscalera, xEnano, yEnano;
    private boolean enemigoVivo = false;

    private GeneradorMazmorra dungeon;
    private int pisoActual = 0;
    private Habitacion salaActual;
    private final Random random = new Random();

    private FontGlyphLayout layoutTexto;
    private int indiceSeleccionado = 0;
    private Objeto objetoEnFoco = null;

    private boolean mostrandoCombate = false;
    private Enemigo enemigoActual = null;
    private String logAccionJugador = "";
    private String logAccionEnemigo = "";
    private boolean turnoDelJugador = true;
    private float temporizadorTurnoEnemigo = 0f;

    private String logTienda = "Selecciona un recurso para fabricar.";
    private int pocionesVidaCompradasEstePiso = 0;
    private int pocionesManaCompradasEstePiso = 0;

    /**
     * Prepara todas las fotos, los monigotes y el fondo la primera vez que abres la ventana.
     */
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

    /**
     * Le da a tu heroe la profesion que hayas elegido en el menu inicial.
     * @param clase El numero de la profesion (1 para guerrero, 2 para mago, etc).
     */
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

    /**
     * Prepara todo el nivel cuando entras por primera vez o cuando bajas por unas escaleras.
     */
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

        pocionesVidaCompradasEstePiso = 0;
        pocionesManaCompradasEstePiso = 0;

        colocarEnemigo();
    }

    /**
     * Tira los dados para decidir en que esquina de la sala aparece el monstruo.
     */
    private void colocarEnemigo() {
        if (salaActual.getTipoHabitacion() == TipoHabitacion.SANTUARIO) {
            enemigoVivo = false;
            return;
        }
        float m = 100f;
        xEnemigo = m + random.nextFloat() * (ANCHO_VIRTUAL - 64f - m * 2f);
        yEnemigo = m + random.nextFloat() * (ALTO_VIRTUAL - 64f - m * 2f);
        enemigoVivo = true;
    }

    /**
     * El cerebro del juego. Lee los botones que tocas todo el rato y decide que menu abrir o hacia donde andas.
     * @param delta El tiempo que pasa entre un fotograma y otro.
     */
    @Override
    public void update(float delta) {
        if (seleccionandoPersonaje) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) seleccionarClase(1);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) seleccionarClase(2);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) seleccionarClase(3);
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) seleccionarClase(4);
            return;
        }

        if (mostrandoCombate) {
            procesarTurnosCombate();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            mostrandoTienda = !mostrandoTienda;
            mostrandoInventario = false;
            mostrandoStats = false;
            logTienda = "Selecciona un recurso para fabricar.";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            mostrandoInventario = !mostrandoInventario;
            mostrandoStats = false;
            mostrandoTienda = false;
            indiceSeleccionado = 0;
            objetoEnFoco = null;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            mostrandoStats = !mostrandoStats;
            mostrandoInventario = false;
            mostrandoTienda = false;
        }

        if (mostrandoTienda) {
            Personaje p = Personaje.getInstancia();
            Inventario inv = Inventario.getInstancia(p);

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                if (pocionesVidaCompradasEstePiso >= 2) {
                    logTienda = "¡Límite alcanzado! Solo 2 pociones de salud por piso.";
                } else {
                    if (p.gastarMonedas(15)) {
                        inv.añadirObjeto(new Pocion(990, "Poción de salud", "Restaura vitalidad", 1, 30, TipoPocion.SALUD), 1);
                        pocionesVidaCompradasEstePiso++;
                        logTienda = "¡Poción de Salud fabricada! (" + pocionesVidaCompradasEstePiso + "/2)";
                    } else {
                        logTienda = "Faltan monedas. Se requieren 15 orecos.";
                    }
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                if (pocionesManaCompradasEstePiso >= 2) {
                    logTienda = "¡Límite alcanzado! Solo 2 pociones de maná por piso.";
                } else {
                    if (p.gastarMonedas(15)) {
                        inv.añadirObjeto(new Pocion(991, "Poción de mana", "Restaura reservas mágicas", 1, 30, TipoPocion.MANA), 1);
                        pocionesManaCompradasEstePiso++;
                        logTienda = "¡Poción de Maná fabricada! (" + pocionesManaCompradasEstePiso + "/2)";
                    } else {
                        logTienda = "Faltan monedas. Se requieren 15 orecos.";
                    }
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                if (p.gastarMonedas(10)) {
                    inv.añadirObjeto(new Flechas(992, "Carcaj de Flechas (20x)", "Munición ligera de ataque", 2, 5), 20);
                    logTienda = "¡Carcaj de 20 flechas añadido al inventario!";
                } else {
                    logTienda = "Faltan monedas. Se requieren 10 orecos.";
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                if (p.gastarMonedas(25)) {
                    inv.añadirObjeto(new Espada(880, "Espada de hierro vieja", "Arma de acero común de la forja", 3, 8, Rareza.COMUN), 1);
                    logTienda = "¡Espada básica forjada con éxito!";
                } else {
                    logTienda = "Faltan monedas. Se requieren 25 orecos.";
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
                if (p.gastarMonedas(25)) {
                    inv.añadirObjeto(new ArmaADistancia(881, "Arco corto de tejo", "Un arco ligero tallado a mano", 2, 6, Rareza.COMUN), 1);
                    logTienda = "¡Arco básico tensado con éxito!";
                } else {
                    logTienda = "Faltan monedas. Se requieren 25 orecos.";
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
                if (p.gastarMonedas(25)) {
                    inv.añadirObjeto(new Baston(882, "Báculo de aprendiz", "Madera vieja con una pizca de magia", 2, 5, 10, Rareza.COMUN), 1);
                    logTienda = "¡Bastón de mago crafteado con éxito!";
                } else {
                    logTienda = "Faltan monedas. Se requieren 25 orecos.";
                }
            }
            return;
        }

        if (mostrandoInventario) {
            Personaje p = Personaje.getInstancia();
            Inventario inv = Inventario.getInstancia(p);
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
                    if (objetoEnFoco instanceof Arma) {
                        if (p.getArmaEquipada() != null && p.getArmaEquipada().equals(objetoEnFoco)) {
                            p.setArmaEquipada(null);
                        } else {
                            p.equiparArma((Arma) objetoEnFoco);
                        }
                        if (enemigoActual != null && enemigoActual.estaVivo()) {
                            mostrandoInventario = false;
                            mostrandoCombate = true;
                            logAccionJugador = "Mochila: Modificado armamento básico";
                        }
                    } else if (objetoEnFoco instanceof Armadura) {
                        if (p.getArmaduraEquipada() != null && p.getArmaduraEquipada().equals(objetoEnFoco)) {
                            p.setArmaduraEquipada(null);
                        } else {
                            p.equiparArmadura((Armadura) objetoEnFoco);
                        }
                        if (enemigoActual != null && enemigoActual.estaVivo()) {
                            mostrandoInventario = false;
                            mostrandoCombate = true;
                            logAccionJugador = "Mochila: Modificada protección corporal";
                        }
                    } else if (objetoEnFoco instanceof Pocion) {
                        Pocion poc = (Pocion) objetoEnFoco;
                        p.curar(poc.getCuracion());
                        inv.eliminarObjeto(objetoEnFoco, 1);
                        indiceSeleccionado = 0;
                        objetoEnFoco = null;
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    if (objetoEnFoco != null) {
                        inv.eliminarObjeto(objetoEnFoco, 1);
                        items = new ArrayList<>(inv.getObjetos().keySet());
                        if (indiceSeleccionado >= items.size()) {
                            indiceSeleccionado = Math.max(0, items.size() - 1);
                        }
                        objetoEnFoco = items.isEmpty() ? null : items.get(indiceSeleccionado);
                    }
                }
            } else {
                objetoEnFoco = null;
            }
            return;
        }

        if (mostrandoStats) {
            Personaje p = Personaje.getInstancia();
            if (p.getPuntosAtributo() > 0) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) p.subirFuerza();
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) p.subirDestreza();
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) p.subirInteligencia();
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) p.subirConstitucion();
                if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) p.subirSuerte();
            }
            return;
        }

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

        if ((salaActual.getTipoHabitacion() == TipoHabitacion.COMBATE || salaActual.getTipoHabitacion() == TipoHabitacion.BOSS) && enemigoVivo) {
            float diffX = xEnano - xEnemigo;
            float diffY = yEnano - yEnemigo;
            if (Math.abs(diffX) > Math.abs(diffY)) {
                xEnemigo += Math.signum(diffX) * VEL_ENEMIGO * delta;
            } else {
                yEnemigo += Math.signum(diffY) * VEL_ENEMIGO * delta;
            }

            float hitboxBicho = (salaActual.getTipoHabitacion() == TipoHabitacion.BOSS) ? 70f : 64f;
            if (xEnano < xEnemigo + hitboxBicho && xEnano + sw > xEnemigo && yEnano < yEnemigo + hitboxBicho && yEnano + sh > yEnemigo) {
                if (salaActual.getContenido() instanceof Enemigo) {
                    enemigoActual = (Enemigo) salaActual.getContenido();
                } else {
                    DatosJuego datos = DatosJuego.getInstancia();
                    if (!datos.enemigos.isEmpty()) {
                        ArrayList<Integer> minionsNormales = new ArrayList<>();
                        for (int i = 0; i < datos.enemigos.size(); i++) {
                            String rarezaBicho = datos.nombresEnemigos.get(i)[1].toUpperCase();
                            if (!rarezaBicho.contains("BOSS") && !rarezaBicho.contains("JEFE")) {
                                minionsNormales.add(i);
                            }
                        }

                        int idx = minionsNormales.isEmpty() ? 0 : minionsNormales.get(random.nextInt(minionsNormales.size()));
                        int[] statsBase = datos.enemigos.get(idx);
                        String[] info = datos.nombresEnemigos.get(idx);
                        enemigoActual = new Enemigo(statsBase[1] + (statsBase[4] * pisoActual), statsBase[2] + (statsBase[5] * pisoActual), info[0], statsBase[0] + (statsBase[3] * pisoActual), statsBase[2]);
                    } else {
                        enemigoActual = new Enemigo(5 + (pisoActual * 2), pisoActual, "Esqueleto Errante", 40 + (pisoActual * 10), pisoActual);
                    }
                }
                mostrandoCombate = true;
                logAccionJugador = "¡Un salvaje " + enemigoActual.getNombre() + " corta tu avance!";
                logAccionEnemigo = "¿Qué vas a hacer?";
                turnoDelJugador = true;
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
                        Objeto item = mapaCofre.getObjeto();
                        int cantidadAñadir = (item instanceof Flechas) ? 20 : 1;
                        boolean exito = inv.añadirObjeto(item, cantidadAñadir);
                        if (exito) salaActual.setContenido(null);
                    }
                }
            }

            if (salaActual.tieneEscaleras && spriteEscalera != null) {
                float distanciaEscaleraX = Math.abs(xEnano - xEscalera);
                float distanciaEscaleraY = Math.abs(yEnano - yEscalera);
                if (distanciaEscaleraX < 60 && distanciaEscaleraY < 60) {
                    pisoActual++;
                    nuevaMazmorra();
                    return;
                }
            }

            if (salaActual.getTipoHabitacion() == TipoHabitacion.SANTUARIO && salaActual.altarDisponible) {
                float distanciaSantuarioX = Math.abs(xEnano - (ANCHO_VIRTUAL / 2f - 20f));
                float distanciaSantuarioY = Math.abs(yEnano - (ALTO_VIRTUAL / 2f - 20f));
                if (distanciaSantuarioX < 60 && distanciaSantuarioY < 60) {
                    Personaje p = Personaje.getInstancia();
                    p.curar(p.getVidaMaxima());
                    int maxManaSantuario = (p.getInteligencia() * 10) + (p.getSabiduria() * 5);
                    p.setMana(maxManaSantuario);
                    salaActual.altarDisponible = false;
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

    /**
     * Hace todas las matematicas de las peleas: restar vidas, gastar pociones o salir por patas.
     */
    private void procesarTurnosCombate() {
        Personaje p = Personaje.getInstancia();
        Inventario inv = Inventario.getInstancia(p);

        if (!turnoDelJugador) {
            temporizadorTurnoEnemigo += Gdx.graphics.getDeltaTime();
            if (temporizadorTurnoEnemigo >= 0.9f) {
                int dañoBaseEnemigo = enemigoActual.getDañoTotal();
                float modEnemigo = 0.85f + random.nextFloat() * 0.30f;
                int dañoEnemigoVariado = Math.max(1, Math.round(dañoBaseEnemigo * modEnemigo));

                int vidaAntes = p.getVidaActual();
                p.recibirDaño(dañoEnemigoVariado, p.getDefensaTotal());
                logAccionEnemigo = "¡El enemigo te causa " + (vidaAntes - p.getVidaActual()) + " de daño!";
                if (!p.estaVivo()) Gdx.app.exit();
                turnoDelJugador = true;
                temporizadorTurnoEnemigo = 0f;
            }
            return;
        }

        int clickX = Gdx.input.getX();
        int clickY = Gdx.input.getY();
        boolean click = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        if (click) {
            int opcionPulsada = -1;
            float btnX = Gdx.graphics.getWidth() / 2f + 40;
            float baseBtnY = 255 + 0;

            for (int i = 0; i < 5; i++) {
                float bY = baseBtnY + (i * 45) - 20;
                if (clickX >= btnX && clickX <= btnX + 270 && clickY >= bY && clickY <= bY + 32) {
                    opcionPulsada = i;
                    break;
                }
            }

            if (opcionPulsada == 0) {
                Arma arma = p.getArmaEquipada();
                int dmgBase = 0;
                boolean esCritico = false;

                if (arma == null) {
                    dmgBase = p.getDañoFisico();
                    logAccionJugador = "Golpeas con tus puños.";
                } else if (arma instanceof ArmaADistancia) {
                    Flechas f = null;
                    for (Objeto o : inv.getObjetos().keySet()) {
                        if (o instanceof Flechas) {
                            f = (Flechas) o;
                            break;
                        }
                    }
                    if (f == null) {
                        logAccionJugador = "¡No tienes flechas!";
                        return;
                    }
                    inv.eliminarObjeto(f, 1);
                    dmgBase = p.getDañoDistancia() + arma.getDañoArma() + f.getDañoFlecha();
                    logAccionJugador = "Disparas una flecha.";
                } else if (arma instanceof Baston) {
                    if (p.getMana() < 10) {
                        logAccionJugador = "¡Sin Maná!";
                        return;
                    }
                    p.gastarMana(10);
                    dmgBase = p.getDañoMagico() + ((Baston) arma).getDañoMagico();
                    logAccionJugador = "Lanzas un hechizo.";
                } else {
                    dmgBase = p.getDañoFisico() + arma.getDañoArma();
                    logAccionJugador = "Atacas con tu " + arma.getNombre() + ".";
                }

                float modificadorDmg = 0.85f + random.nextFloat() * 0.30f;
                int dmgFinal = Math.round(dmgBase * modificadorDmg);

                if (random.nextInt(100) < p.getSuerte()) {
                    dmgFinal = Math.round(dmgFinal * 1.5f);
                    esCritico = true;
                }

                int vAntes = enemigoActual.getVidaActual();
                enemigoActual.recibirDaño(dmgFinal, enemigoActual.getDefensaTotal());

                logAccionJugador += (esCritico ? " ¡GOLPE CRÍTICO!" : "") + " Infliges " + (vAntes - enemigoActual.getVidaActual()) + " de daño.";

                if (!enemigoActual.estaVivo()) {
                    int xpGanada = (enemigoActual.getVidaMaxima() / 3) + (enemigoActual.getDañoTotal() * 2);

                    if (salaActual.getTipoHabitacion() == TipoHabitacion.BOSS) {
                        xpGanada = xpGanada * 4;
                    }
                    p.ganarExperiencia(xpGanada);

                    int multiplicadorDificultad = Math.max(1, enemigoActual.getDefensaTotal());
                    int monedasGanadas = random.nextInt(5) + 5 + (pisoActual * 2) + (multiplicadorDificultad * 3);
                    if (salaActual.getTipoHabitacion() == TipoHabitacion.BOSS) {
                        monedasGanadas = monedasGanadas * 3;
                    }
                    p.ganarMonedas(monedasGanadas);

                    enemigoVivo = false;
                    mostrandoCombate = false;

                    if (salaActual.getTipoHabitacion() == TipoHabitacion.BOSS) {
                        salaActual.setTipoHabitacion(TipoHabitacion.SANTUARIO);
                        salaActual.altarDisponible = random.nextInt(100) < 40;
                    }
                    salaActual.setContenido(null);
                } else {
                    turnoDelJugador = false;
                    logAccionEnemigo = "El enemigo contraataca...";
                }
            } else if (opcionPulsada == 1) {
                mostrandoInventario = true;
                mostrandoCombate = false;
            } else if (opcionPulsada == 2) {
                Pocion pocHP = null;
                for (Objeto o : inv.getObjetos().keySet()) {
                    if (o instanceof Pocion && o.getNombre().toLowerCase().contains("salud")) {
                        pocHP = (Pocion) o;
                        break;
                    }
                }
                if (pocHP == null) {
                    logAccionJugador = "¡No tienes elixires de salud!";
                    return;
                }
                int vPrev = p.getVidaActual();
                p.curar(pocHP.getCuracion());
                inv.eliminarObjeto(pocHP, 1);
                logAccionJugador = "Sanas " + (p.getVidaActual() - vPrev) + " HP con el elixir.";
                turnoDelJugador = false;
                logAccionEnemigo = "¡Te golpean mientras bebes!";
            } else if (opcionPulsada == 3) {
                Pocion pocMP = null;
                for (Objeto o : inv.getObjetos().keySet()) {
                    if (o instanceof Pocion && o.getNombre().toLowerCase().contains("mana")) {
                        pocMP = (Pocion) o;
                        break;
                    }
                }
                if (pocMP == null) {
                    logAccionJugador = "¡No tienes elixires de maná!";
                    return;
                }
                int mMax = (p.getInteligencia() * 10) + (p.getSabiduria() * 5);
                int mAntes = p.getMana();
                p.setMana(Math.min(mMax, p.getMana() + pocMP.getCuracion()));
                inv.eliminarObjeto(pocMP, 1);
                logAccionJugador = "Recuperas " + (p.getMana() - mAntes) + " MP con el elixir.";
                turnoDelJugador = false;
                logAccionEnemigo = "¡Te asaltan mientras recuperas magia!";
            } else if (opcionPulsada == 4) {
                if (salaActual.getTipoHabitacion() == TipoHabitacion.BOSS) {
                    logAccionJugador = "¡No puedes huir de un Jefe de zona!";
                    return;
                }
                int dexEnemigo = enemigoActual.getDefensaTotal();
                float pctTuVida = (float) p.getVidaActual() / p.getVidaMaxima();

                int chanceEscape = 40 + (p.getDestreza() - dexEnemigo) + (int)(pctTuVida * 20);

                if (chanceEscape < 15) chanceEscape = 15;
                if (chanceEscape > 85) chanceEscape = 85;

                if (random.nextInt(100) < chanceEscape) {
                    mostrandoCombate = false;
                    logAccionJugador = "¡Huyes despavorido del combate!";

                    if (xEnano < ANCHO_VIRTUAL / 2f) {
                        xEnemigo = ANCHO_VIRTUAL - 100f;
                    } else {
                        xEnemigo = 100f;
                    }
                    if (yEnano < ALTO_VIRTUAL / 2f) {
                        yEnemigo = ALTO_VIRTUAL - 100f;
                    } else {
                        yEnemigo = 100f;
                    }
                } else {
                    logAccionJugador = "¡Tropiezas al intentar escapar!";
                    turnoDelJugador = false;
                    logAccionEnemigo = "¡El enemigo aproveva tu distracción!";
                }
                return;
            }
        }
    }

    /**
     * Rellena los huecos de animacion si el juego da algun tiron. Aqui no lo usamos.
     * @param alpha Relleno entre fotogramas.
     */
    @Override
    public void interpolate(float alpha) {}

    /**
     * Pinta absolutamente todos los dibujos, textos, barras de vida y menus encima de la pantalla.
     * @param g La herramienta grafica que dibuja las cosas.
     */
    @Override
    public void render(Graphics g) {
        float nativoW = g.getWindowWidth();
        float nativoH = g.getWindowHeight();

        if (layoutTexto == null) layoutTexto = g.getFont().newGlyphLayout();

        g.scale(nativoW / ANCHO_VIRTUAL, nativoH / ALTO_VIRTUAL);

        if (!seleccionandoPersonaje && !mostrandoStats && !mostrandoInventario && !mostrandoCombate && !mostrandoTienda) {
            g.drawTexture(texturaBg, 0f, 0f, ANCHO_VIRTUAL, ALTO_VIRTUAL);
            g.setColor(Color.YELLOW);
            if (salaActual.norte != null) g.fillRect(ANCHO_VIRTUAL/2f - SEMIPUERTA, 0, SEMIPUERTA*2, GROSOR_PUERTA);
            if (salaActual.sur != null) g.fillRect(ANCHO_VIRTUAL/2f - SEMIPUERTA, ALTO_VIRTUAL - GROSOR_PUERTA, SEMIPUERTA*2, GROSOR_PUERTA);
            if (salaActual.este != null) g.fillRect(ANCHO_VIRTUAL - GROSOR_PUERTA, ALTO_VIRTUAL/2f - SEMIPUERTA, GROSOR_PUERTA, SEMIPUERTA*2);
            if (salaActual.oeste != null) g.fillRect(0, ALTO_VIRTUAL/2f - SEMIPUERTA, GROSOR_PUERTA, SEMIPUERTA*2);

            if (salaActual.getTipoHabitacion() == TipoHabitacion.COMBATE && enemigoVivo) g.drawSprite(spriteEnemigo, xEnemigo, yEnemigo);
            if (salaActual.getTipoHabitacion() == TipoHabitacion.COFRE && salaActual.getContenido() != null) g.drawSprite(cofre, ANCHO_VIRTUAL / 2f - 22f, ALTO_VIRTUAL / 2f - 22f);

            if (salaActual.getTipoHabitacion() == TipoHabitacion.BOSS && enemigoVivo) {
                g.setColor(Color.RED);
                g.fillRect(xEnemigo, yEnemigo, 70, 70);
            }

            if (salaActual.getTipoHabitacion() == TipoHabitacion.SANTUARIO && salaActual.altarDisponible) {
                g.setColor(Color.GREEN);
                g.fillRect(ANCHO_VIRTUAL / 2f - 20f, ALTO_VIRTUAL / 2f - 20f, 40, 40);
            }

            if (salaActual.tieneEscaleras && spriteEscalera != null) g.drawSprite(spriteEscalera, xEscalera, yEscalera);
            g.drawSprite(spriteEnano, xEnano, yEnano);
        }

        g.scale(ANCHO_VIRTUAL / nativoW, ALTO_VIRTUAL / nativoH);

        float ratioX = nativoW / ANCHO_VIRTUAL;
        float ratioY = nativoH / ALTO_VIRTUAL;

        if (!seleccionandoPersonaje && !mostrandoStats && !mostrandoInventario && !mostrandoCombate && !mostrandoTienda) {
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

            if (salaActual.getTipoHabitacion() == TipoHabitacion.SANTUARIO && salaActual.altarDisponible) {
                float distSantuario = (float) Math.sqrt(Math.pow(xEnano - (ANCHO_VIRTUAL / 2f - 20f), 2) + Math.pow(yEnano - (ALTO_VIRTUAL / 2f - 20f), 2));
                if (distSantuario < 60) {
                    layoutTexto.setText("[E] REZAR");
                    float fX = (ANCHO_VIRTUAL / 2f) * ratioX;
                    float fY = (ALTO_VIRTUAL / 2f - 35f) * ratioY;
                    g.setColor(new Color(0f, 0f, 0f, 0.8f));
                    g.fillRect(fX - (layoutTexto.getWidth() / 2f) - 6, fY - 2, layoutTexto.getWidth() + 12, 18);
                    g.setColor(Color.GREEN); g.drawRect(fX - (layoutTexto.getWidth() / 2f) - 6, fY - 2, layoutTexto.getWidth() + 12, 18);
                    g.drawString("[E] REZAR", fX - (layoutTexto.getWidth() / 2f), fY);
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

        if (mostrandoTienda) {
            g.drawTexture(texturaBg, 0f, 0f, nativoW, nativoH);
            g.setColor(new Color(0.05f, 0.05f, 0.08f, 0.97f));
            g.fillRect(40, 40, nativoW - 80, nativoH - 80);
            g.setColor(Color.ORANGE);
            g.drawRect(40, 40, nativoW - 80, nativoH - 80);

            Personaje p = Personaje.getInstancia();
            g.setColor(Color.GOLD); g.drawString("ESTACIÓN DE FORJA Y ALQUIMIA", 65, 75);
            g.setColor(Color.WHITE); g.drawString("Monedas Actuales: " + p.getMonedas() + " Orecos", 65, 105);
            g.drawString("Controles: [Pulsa Número] Fabricar recurso | [T] Cerrar Tienda", nativoW - 480, 75);
            g.setColor(Color.DARK_GRAY); g.drawLineSegment(65, 130, nativoW - 65, 130);

            float itemX = 80;
            float itemY = 170;
            float lineH = 55;

            g.setColor(Color.CYAN); g.drawString("[1] POCIÓN DE SALUD (" + pocionesVidaCompradasEstePiso + "/2)", itemX, itemY);
            g.setColor(Color.LIGHT_GRAY); g.drawString("Restaura +30 HP instantáneos.", itemX + 240, itemY);
            g.setColor(Color.GOLD); g.drawString("Coste: 15 Monedas", nativoW - 250, itemY);

            g.setColor(Color.CYAN); g.drawString("[2] POCIÓN DE MANÁ (" + pocionesManaCompradasEstePiso + "/2)", itemX, itemY + lineH);
            g.setColor(Color.LIGHT_GRAY); g.drawString("Restaura +30 MP instantáneos.", itemX + 240, itemY + lineH);
            g.setColor(Color.GOLD); g.drawString("Coste: 15 Monedas", nativoW - 250, itemY + lineH);

            g.setColor(Color.CYAN); g.drawString("[3] CARCAJ DE FLECHAS", itemX, itemY + (lineH * 2));
            g.setColor(Color.LIGHT_GRAY); g.drawString("Paquete con 20 flechas del piso.", itemX + 240, itemY + (lineH * 2));
            g.setColor(Color.GOLD); g.drawString("Coste: 10 Monedas", nativoW - 250, itemY + (lineH * 2));

            g.setColor(Color.ORANGE); g.drawString("[4] ESPADA BÁSICA", itemX, itemY + (lineH * 3));
            g.setColor(Color.LIGHT_GRAY); g.drawString("+8 ATQ Físico para Guerreros y Asesinos.", itemX + 240, itemY + (lineH * 3));
            g.setColor(Color.GOLD); g.drawString("Coste: 25 Monedas", nativoW - 250, itemY + (lineH * 3));

            g.setColor(Color.ORANGE); g.drawString("[5] ARCO BÁSICO", itemX, itemY + (lineH * 4));
            g.setColor(Color.LIGHT_GRAY); g.drawString("+6 ATQ Lejano. Requiere flechas para disparar.", itemX + 240, itemY + (lineH * 4));
            g.setColor(Color.GOLD); g.drawString("Coste: 25 Monedas", nativoW - 250, itemY + (lineH * 4));

            g.setColor(Color.ORANGE); g.drawString("[6] BASTÓN ARCANO", itemX, itemY + (lineH * 5));
            g.setColor(Color.LIGHT_GRAY); g.drawString("+5 ATQ Mágico y +10 de Maná extra.", itemX + 240, itemY + (lineH * 5));
            g.setColor(Color.GOLD); g.drawString("Coste: 25 Monedas", nativoW - 250, itemY + (lineH * 5));

            g.setColor(Color.DARK_GRAY); g.drawRect(65, nativoH - 110, nativoW - 130, 45);
            g.setColor(Color.GREEN); g.drawString(logTienda, 85, nativoH - 83);
            return;
        }

        if (mostrandoStats) {
            g.drawTexture(texturaBg, 0f, 0f, nativoW, nativoH);
            g.setColor(new Color(0.03f, 0.04f, 0.06f, 0.96f));
            g.fillRect(25, 25, nativoW - 50, nativoH - 50);
            g.setColor(Color.CYAN);
            g.drawRect(25, 25, nativoW - 50, nativoH - 50);

            Personaje p = Personaje.getInstancia();
            String nombreClase = (tipoClase == 1) ? "GUERRERO" : (tipoClase == 2) ? "MAGO" : (tipoClase == 3) ? "NIGROMANTE" : "ASESINO";

            g.setColor(Color.GOLD); g.drawString("ENTRENAMIENTO Y HOJA DE PERSONAJE - " + nombreClase, 50, 60);
            g.setColor(Color.WHITE); g.drawString("Puntos Disponibles: " + p.getPuntosAtributo() + " Pts", 50, 85);
            g.drawString("Controles: [1-5] Subir Atributo | [O] Cerrar menú", nativoW - 380, 60);
            g.setColor(Color.GRAY); g.drawLineSegment(45, 105, nativoW - 45, 105);

            float divisorX = nativoW * 0.52f;
            g.drawLineSegment(divisorX, 105, divisorX, nativoH - 45);

            float col1X = 55;
            float col2X = divisorX + 20;
            float filaY = 140;
            float saltoFila = 50;

            String armaEscala = p.getAtributoEscaladoArma();

            g.setColor(Color.CYAN); g.drawString("=== ASIGNAR ATRIBUTOS ===", col1X, filaY);

            g.setColor(armaEscala.equals("FUERZA") ? Color.GOLD : Color.WHITE);
            int costSTR = p.calcularCosteAtributo(p.getFuerza());
            g.drawString("[1] FUERZA (STR):      " + p.getFuerza() + "  (Coste: " + costSTR + " Pts)" + (armaEscala.equals("FUERZA") ? " ★" : ""), col1X, filaY + saltoFila);

            g.setColor(armaEscala.equals("DESTREZA") ? Color.GOLD : Color.WHITE);
            int costDEX = p.calcularCosteAtributo(p.getDestreza());
            g.drawString("[2] DESTREZA (DEX):    " + p.getDestreza() + "  (Coste: " + costDEX + " Pts)" + (armaEscala.equals("DESTREZA") ? " ★" : ""), col1X, filaY + (saltoFila * 2));

            g.setColor(armaEscala.equals("INTELIGENCIA") ? Color.GOLD : Color.WHITE);
            int costINT = p.calcularCosteAtributo(p.getInteligencia());
            g.drawString("[3] INTELIGENCIA (INT): " + p.getInteligencia() + "  (Coste: " + costINT + " Pts)" + (armaEscala.equals("INTELIGENCIA") ? " ★" : ""), col1X, filaY + (saltoFila * 3));

            g.setColor(Color.WHITE);
            int costCON = p.calcularCosteAtributo(p.getConstitucion());
            g.drawString("[4] CONSTITUCION (CON): " + p.getConstitucion() + "  (Coste: " + costCON + " Pts)", col1X, filaY + (saltoFila * 4));

            g.setColor(Color.WHITE);
            int costLUK = p.calcularCosteAtributo(p.getSuerte());
            g.drawString("[5] SUERTE (LUK):       " + p.getSuerte() + "  (Coste: " + costLUK + " Pts)", col1X, filaY + (saltoFila * 5));

            g.setColor(Color.CYAN); g.drawString("=== GUÍA DE APRENDIZAJE ===", col2X, filaY);
            g.setColor(Color.WHITE);
            g.drawString("• STR: Eleva el daño físico base e incrementa la", col2X, filaY + 30);
            g.drawString("       capacidad de carga de tu mochila RPG.", col2X, filaY + 50);

            g.drawString("• DEX: Multiplica la precisión balística lejana y", col2X, filaY + 85);
            g.drawString("       mejora el porcentaje de éxito al huir.", col2X, filaY + 105);

            g.drawString("• INT: Desata la potencia de los bastones mágicos y", col2X, filaY + 140);
            g.drawString("       amplía las reservas de maná de combate.", col2X, filaY + 160);

            g.drawString("• CON: Robustece el cuerpo otorgando +5 puntos de", col2X, filaY + 195);
            g.drawString("       salud máxima permanente por cada nivel.", col2X, filaY + 215);

            g.drawString("• LUK: Dispara la tasa de impacto crítico en combate", col2X, filaY + 250);
            g.drawString("       y optimiza el botín que sacas de los cofres.", col2X, filaY + 270);

            g.setColor(Color.DARK_GRAY); g.fillRect(col2X, nativoH - 105, nativoW - col2X - 45, 45);
            g.setColor(Color.GOLD); g.drawRect(col2X, nativoH - 105, nativoW - col2X - 45, 45);
            g.setColor(Color.WHITE);
            String nombreArma = (p.getArmaEquipada() != null) ? p.getArmaEquipada().getNombre().toUpperCase() : "PUÑOS DESNUDOS";
            g.drawString("ARMA: " + nombreArma + " ESCALA CON [ " + armaEscala + " ]", col2X + 15, nativoH - 78);
            return;
        }

        if (mostrandoInventario) {
            g.drawTexture(texturaBg, 0f, 0f, nativoW, nativoH);
            g.setColor(new Color(0.04f, 0.04f, 0.06f, 0.94f));
            g.fillRect(25, 25, nativoW - 50, nativoH - 50);
            g.setColor(Color.GOLD);
            g.drawRect(25, 25, nativoW - 50, nativoH - 50);

            Personaje p = Personaje.getInstancia();
            Inventario inv = Inventario.getInstancia(p);
            g.setColor(Color.CYAN); g.drawString("INVENTARIO DE TU HEROE", 45, 60);
            g.setColor(Color.WHITE); g.drawString("Carga Actual: " + inv.getPesoActual() + " / " + inv.getCapacidad() + " kg", 45, 85);
            g.drawString("Menu: [W/S] Navegar | [ENTER] Interactuar/Equipar | [I] Salir", nativoW - 440, 60);
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

                    String estadoEquipado = "";
                    boolean estaPuesto = false;
                    if (obj instanceof Arma && p.getArmaEquipada() != null && p.getArmaEquipada().equals(obj)) {
                        estadoEquipado = " [E]";
                        estaPuesto = true;
                    } else if (obj instanceof Armadura && p.getArmaduraEquipada() != null && p.getArmaduraEquipada().equals(obj)) {
                        estadoEquipado = " [E]";
                        estaPuesto = true;
                    }

                    String nombreRender = obj.getNombre().toUpperCase() + estadoEquipado;
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
                    if (estaPuesto) {
                        colorItem = Color.GREEN;
                    } else {
                        String nm = obj.getNombre().toLowerCase();
                        if (nm.contains("destino") || nm.contains("celestial")) colorItem = Color.ORANGE;
                        else if (nm.contains("abismo") || nm.contains("sombr") || nm.contains("alma")) colorItem = Color.PURPLE;
                        else if (nm.contains("élfic") || nm.contains("rúnic") || nm.contains("plata")) colorItem = Color.CYAN;
                        else if (nm.contains("acero") || nm.contains("malla")) colorItem = Color.GREEN;
                        else colorItem = Color.LIGHT_GRAY;
                    }

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

                boolean focoEquipado = false;
                if (objetoEnFoco instanceof Espada) {
                    g.drawString("TIPO: Espada Corto Alcance", panelX, statsY);
                    g.drawString("ATQ: +" + ((Espada) objetoEnFoco).getDañoArma() + " Fisico", panelX, statsY + 25);
                    if (p.getArmaEquipada() != null && p.getArmaEquipada().equals(objetoEnFoco)) focoEquipado = true;
                } else if (objetoEnFoco instanceof ArmaADistancia) {
                    g.drawString("TIPO: Arma Distancia", panelX, statsY);
                    g.drawString("ATQ: +" + ((ArmaADistancia) objetoEnFoco).getDañoArma() + " Lejano", panelX, statsY + 25);
                    if (p.getArmaEquipada() != null && p.getArmaEquipada().equals(objetoEnFoco)) focoEquipado = true;
                } else if (objetoEnFoco instanceof Baston)  {
                    g.drawString("TIPO: Baston Magico", panelX, statsY);
                    g.drawString("PODER MAGICO: +" + ((Baston) objetoEnFoco).getDañoArma(), panelX, statsY + 25);
                    g.drawString("MANA BONUS: +" + ((Baston) objetoEnFoco).getManaBonus(), panelX, statsY + 50);
                    if (p.getArmaEquipada() != null && p.getArmaEquipada().equals(objetoEnFoco)) focoEquipado = true;
                } else if (objetoEnFoco instanceof Armadura) {
                    g.drawString("TIPO: Armadura Corporal", panelX, statsY);
                    g.drawString("DEFENSA: +" + ((Armadura) objetoEnFoco).getArmadura(), panelX, statsY + 25);
                    g.drawString("REQ. FUERZA: " + ((Armadura) objetoEnFoco).getFuerzaMinima(), panelX, statsY + 50);

                    String descRaw = objetoEnFoco.getOriginalDescripcion();
                    if (descRaw.startsWith("BONUS:")) {
                        String[] pParts = descRaw.split(":");
                        g.setColor(Color.GOLD);
                        g.drawString("EFECTO: +" + pParts[2] + " " + pParts[1], panelX, statsY + 75);
                        g.setColor(Color.WHITE);
                    }
                } else if (objetoEnFoco instanceof Pocion) {
                    g.drawString("TIPO: Consumible", panelX, statsY);
                    g.drawString("EFECTO: Restaura Recursos", panelX, statsY + 25);
                }

                g.setColor(Color.GOLD); g.drawRect(panelX, nativoH - 85, nativoW - panelX - 45, 45);
                g.setColor(Color.WHITE);

                String accionText = "[ENTER] Equipar Objeto";
                if (objetoEnFoco instanceof Pocion) {
                    accionText = "[ENTER] Consumir Pocion";
                } else if (focoEquipado) {
                    accionText = "[ENTER] Desequipar Objeto";
                }
                g.drawString(accionText, panelX + 20, nativoH - 60);
            }
            return;
        }

        if (mostrandoCombate && enemigoActual != null) {
            Personaje p = Personaje.getInstancia();
            int mX = Gdx.input.getX();
            int mY = Gdx.input.getY();

            g.drawTexture(texturaBg, 0f, 0f, nativoW, nativoH);
            g.setColor(new Color(0.04f, 0.04f, 0.07f, 0.96f));
            g.fillRect(35, 35, nativoW - 70, nativoH - 70);
            g.setColor(Color.CYAN);
            g.drawRect(35, 35, nativoW - 70, nativoH - 70);

            g.setColor(Color.RED);
            g.drawString("ENEMIGO: " + enemigoActual.getNombre().toUpperCase(), 65, 70);

            g.setColor(Color.BLACK);
            g.fillRect(65, 95, nativoW - 130, 22);
            g.setColor(new Color(0.8f, 0.1f, 0.1f, 1f));
            float pctVidaEnemigo = (float) enemigoActual.getVidaActual() / enemigoActual.getVidaMaxima();
            g.fillRect(65, 95, (nativoW - 130) * Math.max(0, pctVidaEnemigo), 22);
            g.setColor(Color.RED);
            g.drawRect(65, 95, nativoW - 130, 22);
            g.setColor(Color.WHITE);
            String textoVidaEnemigo = enemigoActual.getVidaActual() + " / " + enemigoActual.getVidaMaxima() + " HP";
            g.drawString(textoVidaEnemigo, 75, 98);

            g.setColor(new Color(0.08f, 0.09f, 0.15f, 0.9f));
            g.fillRect(65, 140, nativoW - 130, 90);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(65, 140, nativoW - 130, 90);
            g.setColor(Color.GOLD);
            g.drawString(logAccionJugador, 85, 160);
            g.setColor(Color.PINK);
            g.drawString(logAccionEnemigo, 85, 195);

            float baseControlY = 255;
            g.setColor(Color.GREEN);
            g.drawString("TU ESTADO:", 65, baseControlY);
            g.setColor(Color.WHITE);
            g.drawString("Héroe: " + p.getNombre(), 65, baseControlY + 30);
            g.drawString("Arma: " + (p.getArmaEquipada() != null ? p.getArmaEquipada().getNombre() : "Puños"), 65, baseControlY + 55);
            g.drawString("Escudo: " + p.getDefensaTotal() + " Pts", 65, baseControlY + 80);

            g.drawString("VITALIDAD:", 65, baseControlY + 112);
            g.setColor(Color.BLACK); g.fillRect(165, baseControlY + 98, 160, 16);
            g.setColor(Color.RED);
            float pctPlayerHP = (float) p.getVidaActual() / p.getVidaMaxima();
            g.fillRect(165, baseControlY + 98, 160 * Math.max(0, pctPlayerHP), 16);
            g.setColor(Color.WHITE); g.drawRect(165, baseControlY + 98, 160, 16);
            g.setColor(Color.WHITE);
            String textoVidaJugador = p.getVidaActual() + " / " + p.getVidaMaxima() + " HP";
            g.drawString(textoVidaJugador, 175, baseControlY + 100);

            g.drawString("ENERGÍA:", 65, baseControlY + 140);
            g.setColor(Color.BLACK); g.fillRect(165, baseControlY + 126, 160, 16);
            g.setColor(Color.BLUE);
            float maxManaCombate = (p.getInteligencia() * 10) + (p.getSabiduria() * 5);
            float pctPlayerMP = maxManaCombate > 0 ? (float) p.getMana() / maxManaCombate : 0f;
            g.fillRect(165, baseControlY + 126, 160 * Math.max(0, pctPlayerMP), 16);
            g.setColor(Color.WHITE); g.drawRect(165, baseControlY + 126, 160, 16);

            float btnX = nativoW / 2f + 40;
            float baseBtnY = baseControlY + 0;
            String[] labels = {"[1] LANZAR ATAQUE", "[2] ABRIR MOCHILA", "[3] ELIXIR DE SALUD", "[4] ELIXIR DE MANÁ", "[5] ESCAPAR CORRIENDO"};

            for (int i = 0; i < 5; i++){
                float bY = baseBtnY + (i * 45);
                boolean hover = (mX >= btnX && mX <= btnX + 270 && mY >= bY - 20 && mY <= bY + 12);

                if (hover) {
                    g.setColor(new Color(0.1f, 0.4f, 0.4f, 0.8f));
                    g.fillRect(btnX, bY - 20, 270, 32);
                    g.setColor(Color.CYAN);
                } else {
                    g.setColor(new Color(0.1f, 0.12f, 0.2f, 0.9f));
                    g.fillRect(btnX, bY - 20, 270, 32);
                    g.setColor(Color.DARK_GRAY);
                }
                g.drawRect(btnX, bY - 20, 270, 32);
                g.setColor(Color.WHITE);
                g.drawString(labels[i], btnX + 20, bY + 2);
            }
            return;
        }

        if (!seleccionandoPersonaje) {
            Personaje p = Personaje.getInstancia();
            layoutTexto.setText("PISO " + (pisoActual + 1) + " - " + salaActual.getTipoHabitacion());
            float boxW = Math.max(250, layoutTexto.getWidth() + 24);
            float boxH = 142;

            g.setColor(new Color(0.1f, 0.1f, 0.1f, 0.85f)); g.fillRect(12, 12, boxW, boxH);
            g.setColor(Color.GOLD); g.drawRect(12, 12, boxW, boxH);

            g.setColor(Color.CYAN); g.drawString("LVL " + p.getNivel(), 24, 23);
            g.setColor(Color.WHITE); g.drawString("PISO " + (pisoActual + 1) + " - " + salaActual.getTipoHabitacion(), 85, 23);

            g.setColor(Color.BLACK); g.fillRect(22, 46, boxW - 35, 12);
            g.setColor(Color.RED);
            float ratioVida = (float) p.getVidaActual() / p.getVidaMaxima();
            g.fillRect(22, 46, (boxW - 35) * ratioVida, 12);
            g.setColor(Color.WHITE); g.drawString("HP: " + p.getVidaActual() + "/" + p.getVidaMaxima(), 26, 47);

            g.setColor(Color.BLACK); g.fillRect(22, 68, boxW - 35, 12);
            g.setColor(Color.BLUE);
            float maxMana = (p.getInteligencia() * 10) + (p.getSabiduria() * 5);
            float ratioMana = maxMana > 0 ? (float) p.getMana() / maxMana : 0f;
            g.fillRect(22, 68, (boxW - 35) * ratioMana, 12);
            g.setColor(Color.WHITE); g.drawString("MP: " + p.getMana() + "/" + (int)maxMana, 26, 69);

            int xpActual = p.getExperiencia();
            int xpNecesaria = p.calcularExperienciaNecesaria();
            float ratioXp = xpNecesaria > 0 ? (float) xpActual / xpNecesaria : 0f;
            g.setColor(Color.BLACK); g.fillRect(22, 90, boxW - 35, 12);
            g.setColor(Color.ORANGE);
            g.fillRect(22, 90, (boxW - 35) * ratioXp, 12);
            g.setColor(Color.WHITE); g.drawString("XP: " + xpActual + "/" + xpNecesaria, 26, 91);

            g.setColor(Color.GOLD);
            g.drawString("MONEDAS: " + p.getMonedas() + " $$", 24, 134);
        }
    }
}