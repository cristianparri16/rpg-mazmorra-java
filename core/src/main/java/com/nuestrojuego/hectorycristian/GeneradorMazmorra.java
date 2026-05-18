package com.nuestrojuego.hectorycristian;

import java.util.ArrayList;
import java.util.Random;

public class GeneradorMazmorra {
    private Random azar;
    private ArrayList<Habitacion> entradasPisos;
    private final int TOTAL_PISOS = 31;

    public GeneradorMazmorra(long semilla) {
        this.azar = new Random(semilla);
        this.entradasPisos = new ArrayList<>();
        generarTodoElMundo();
    }

    private void generarTodoElMundo(){
        for(int i = 0; i < TOTAL_PISOS; i++){
            entradasPisos.add(generarPisoRamificado(i));
        }
    }

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

    private void conectarAleatoriamente(ArrayList<Habitacion> existentes, Habitacion nueva) {
        boolean conectada = false;
        while (!conectada) {
            Habitacion anfitriona = existentes.get(azar.nextInt(existentes.size()));
            int puerta = azar.nextInt(4);

            if (puerta == 0 && anfitriona.norte == null) {
                anfitriona.norte = nueva; nueva.sur = anfitriona; conectada = true;
            } else if (puerta == 1 && anfitriona.sur == null) {
                anfitriona.sur = nueva; nueva.norte = anfitriona; conectada = true;
            } else if (puerta == 2 && anfitriona.este == null) {
                anfitriona.este = nueva; nueva.oeste = anfitriona; conectada = true;
            } else if (puerta == 3 && anfitriona.oeste == null) {
                anfitriona.oeste = nueva; nueva.este = anfitriona; conectada = true;
            }
        }
    }

    private Habitacion crearHabitacionAleatoria(int nivel){
        int tiradaBoss = azar.nextInt(100);
        if (tiradaBoss < 5) {
            return new Habitacion(TipoHabitacion.BOSS, generarBoss(nivel));
        }

        int tirada = azar.nextInt(100);
        if(tirada < 50) return new Habitacion(TipoHabitacion.COMBATE, generarEnemigo(nivel));
        return new Habitacion(TipoHabitacion.COFRE, generarCofre(nivel));
    }

    private Enemigo generarEnemigo(int nivel){
        int vida = 40 +(nivel*10);
        int daño = 5 +(nivel*2);
        return new Enemigo(daño, nivel, "Enemigo Prueba", vida, nivel);
    }

    private Enemigo generarBoss(int nivel) {
        int vidaBoss = 150 + (nivel * 20);
        int dañoBoss = 15 + (nivel * 3);
        return new Enemigo(dañoBoss, nivel * 2, "Boss Prueba", vidaBoss, nivel);
    }

    private Cofre generarCofre(int nivel) {
        int suerte = Personaje.getInstancia().getSuerte();
        return new Cofre(nivel, suerte, azar);
    }

    public ArrayList<Habitacion> getEntradasPisos() {
        return entradasPisos;
    }
}