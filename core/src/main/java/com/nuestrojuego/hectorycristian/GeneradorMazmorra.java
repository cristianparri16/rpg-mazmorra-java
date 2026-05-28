package com.nuestrojuego.hectorycristian;

import java.util.ArrayList;
import java.util.Random;

/**
 * Se encarga de construir todo el mapa del juego de golpe.
 * Crea los pisos, las habitaciones, los monstruos y conecta las puertas.
 */
public class GeneradorMazmorra {
    private Random azar;
    private ArrayList<Habitacion> entradasPisos;
    private final int TOTAL_PISOS = 31;

    /**
     * Prepara el motor del generador para empezar a construir el mapa.
     * * @param semilla El numero inicial para que el azar funcione.
     */
    public GeneradorMazmorra(long semilla) {
        this.azar = new Random(semilla);
        this.entradasPisos = new ArrayList<>();
        generarTodoElMundo();
    }

    /**
     * Construye los 31 pisos del juego seguidos y guarda la sala inicial de cada uno.
     */
    private void generarTodoElMundo() {
        for(int i = 0; i < TOTAL_PISOS; i++) {
            entradasPisos.add(generarPisoRamificado(i));
        }
    }

    /**
     * Crea un piso entero. Pone una sala segura al principio, añade varias salas sueltas,
     * las conecta entre si y pone las escaleras para bajar en la ultima.
     * * @param nivelPiso El numero del piso que estamos construyendo.
     * @return La primera sala de este piso por la que entra el jugador.
     */
    private Habitacion generarPisoRamificado(int nivelPiso) {
        Habitacion inicio = new Habitacion(TipoHabitacion.SANTUARIO, null);
        int cantidadExtras = azar.nextInt(4) + 3;

        ArrayList<Habitacion> todasLasDelPiso = new ArrayList<>();
        todasLasDelPiso.add(inicio);

        for(int i = 0; i < cantidadExtras; i++) {
            Habitacion nueva = crearHabitacionAleatoria(nivelPiso);
            conectarAleatoriamente(todasLasDelPiso, nueva);
            todasLasDelPiso.add(nueva);
        }

        Habitacion habitacionFinal = todasLasDelPiso.get(todasLasDelPiso.size() - 1);
        habitacionFinal.tieneEscaleras = true;

        if (nivelPiso >= 26) {
            habitacionFinal.tipoHabitacion = TipoHabitacion.BOSS;
            habitacionFinal.setContenido(generarBoss(nivelPiso));
        } else {
            habitacionFinal.tipoHabitacion = TipoHabitacion.COMBATE;
        }

        return inicio;
    }

    /**
     * Busca una puerta libre en las salas que ya estan puestas y pega la sala nueva ahi.
     * * @param existentes La lista de salas que ya estan colocadas en el mapa.
     * @param nueva La sala nueva que queremos enganchar.
     */
    private void conectarAleatoriamente(ArrayList<Habitacion> existentes, Habitacion nueva) {
        boolean conectada = false;
        while (!conectada) {
            Habitacion anfitriona = existentes.get(azar.nextInt(existentes.size()));
            int puerta = azar.nextInt(4);

            if (puerta == 0 && anfitriona.norte == null) {
                anfitriona.norte = nueva;
                nueva.sur = anfitriona;
                conectada = true;
            } else if (puerta == 1 && anfitriona.sur == null) {
                anfitriona.sur = nueva;
                nueva.norte = anfitriona;
                conectada = true;
            } else if (puerta == 2 && anfitriona.este == null) {
                anfitriona.este = nueva;
                nueva.oeste = anfitriona;
                conectada = true;
            } else if (puerta == 3 && anfitriona.oeste == null) {
                anfitriona.oeste = nueva;
                nueva.este = anfitriona;
                conectada = true;
            }
        }
    }

    /**
     * Tira los dados para ver que toca poner en una sala vacia.
     * * @param nivel El piso en el que estamos para ajustar la dificultad.
     * @return Una sala nueva con su sorpresa dentro.
     */
    private Habitacion crearHabitacionAleatoria(int nivel) {
        int tiradaBoss = azar.nextInt(100);
        if (tiradaBoss < 5) {
            return new Habitacion(TipoHabitacion.BOSS, generarBoss(nivel));
        }

        int tirada = azar.nextInt(100);
        if(tirada < 50) {
            return new Habitacion(TipoHabitacion.COMBATE, generarEnemigo(nivel));
        }
        return new Habitacion(TipoHabitacion.COFRE, generarCofre(nivel));
    }

    /**
     * Saca un monstruo normal de la caja de datos y lo hace mas fuerte segun el piso.
     * * @param nivel El piso actual.
     * @return El monstruo listo para pelear.
     */
    private Enemigo generarEnemigo(int nivel) {
        DatosJuego datos = DatosJuego.getInstancia();

        if (!datos.enemigos.isEmpty()) {
            ArrayList<Integer> indicesNormales = new ArrayList<>();
            for (int i = 0; i < datos.nombresEnemigos.size(); i++) {
                if (!datos.nombresEnemigos.get(i)[1].equals("BOSS")) {
                    indicesNormales.add(i);
                }
            }

            int indice = indicesNormales.isEmpty() ? azar.nextInt(datos.enemigos.size()) : indicesNormales.get(azar.nextInt(indicesNormales.size()));

            int[] statsBase = datos.enemigos.get(indice);
            String[] info = datos.nombresEnemigos.get(indice);

            String nombre = info[0];
            int vida = statsBase[0] + (statsBase[3] * nivel);
            int daño = statsBase[1] + (statsBase[4] * nivel);
            int defensa = statsBase[2] + (statsBase[5] * nivel);

            return new Enemigo(daño, defensa, nombre, vida, defensa);
        }

        return new Enemigo(8 + (nivel * 2), nivel, "Esqueleto Errante", 50 + (nivel * 10), nivel);
    }

    /**
     * Saca un jefe enorme de la caja de datos y le sube los atributos segun el piso.
     * * @param nivel El piso actual.
     * @return El jefe final listo para pelear.
     */
    private Enemigo generarBoss(int nivel) {
        DatosJuego datos = DatosJuego.getInstancia();

        if (!datos.enemigos.isEmpty()) {
            ArrayList<Integer> indicesBosses = new ArrayList<>();
            for (int i = 0; i < datos.nombresEnemigos.size(); i++) {
                if (datos.nombresEnemigos.get(i)[1].equals("BOSS")) {
                    indicesBosses.add(i);
                }
            }

            int indice = indicesBosses.isEmpty() ? azar.nextInt(datos.enemigos.size()) : indicesBosses.get(azar.nextInt(indicesBosses.size()));

            int[] statsBase = datos.enemigos.get(indice);
            String[] info = datos.nombresEnemigos.get(indice);

            String nombre = "BOSS: " + info[0];
            int vida = statsBase[0] + (statsBase[3] * nivel);
            int daño = statsBase[1] + (statsBase[4] * nivel);
            int defensa = statsBase[2] + (statsBase[5] * nivel);

            return new Enemigo(daño, defensa, nombre, vida, defensa);
        }

        return new Enemigo(20 + (nivel * 3), nivel * 2, "Guardián de la Mazmorra", 180 + (nivel * 20), nivel);
    }

    /**
     * Crea un cofre con botin y usa la suerte de tu personaje para darte mejores cosas.
     * * @param nivel El piso actual.
     * @return Un cofre lleno.
     */
    private Cofre generarCofre(int nivel) {
        int suerte = Personaje.getInstancia().getSuerte();
        return new Cofre(nivel, suerte, azar);
    }

    /**
     * Te da la lista con la primera habitacion de todos los pisos para poder entrar a jugar.
     * * @return La lista de salas iniciales.
     */
    public ArrayList<Habitacion> getEntradasPisos() {
        return entradasPisos;
    }
}