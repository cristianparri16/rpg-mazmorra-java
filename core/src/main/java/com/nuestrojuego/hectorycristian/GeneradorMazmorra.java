package com.nuestrojuego.hectorycristian;

import java.util.ArrayList;
import java.util.Random;

public class GeneradorMazmorra {
    private Random azar;
    private ArrayList<ArrayList<Habitacion>> pisos;
    private final int TOTAL_PISOS = 31;


    public GeneradorMazmorra(long semilla) {
        this.azar = new Random(semilla);
        this.pisos = new ArrayList<>();
        generarTodoElMundo();
    }

    private void generarTodoElMundo(){
        for(int i = 0; i < TOTAL_PISOS; i++){
            pisos.add(generarPiso(i));
        }
    }
    private ArrayList<Habitacion> generarPiso(int nuemroDePiso){
        ArrayList<Habitacion> habitaciones = new ArrayList<>();
        int cantidadHabitaciones = (nuemroDePiso >= 26) ? 1: azar.nextInt(3) +1;
        for(int i = 0; i < cantidadHabitaciones; i++){
            habitaciones.add(crearHabitacionAleatoria(nuemroDePiso));
        }
        return habitaciones;
    }

    private Habitacion crearHabitacionAleatoria(int nivel){
        if(nivel == 0) return  new Habitacion(TipoHabitacion.SANTUARIO, null);
        if(nivel >= 26) return new Habitacion(TipoHabitacion.BOSS, generarBoss(nivel));
        int tirada = azar.nextInt(100);
        if(tirada<40) {
            return new Habitacion(TipoHabitacion.COMBATE,generarEnemigo(nivel));
        } else if (tirada<80) {
            return new Habitacion(TipoHabitacion.COFRE,generarCofre(nivel));
        } else if (tirada<95) {
            return new Habitacion(TipoHabitacion.SANTUARIO, null);
        }else {
            return new Habitacion(TipoHabitacion.BOSS, generarBoss(nivel));
        }
    }

    private Enemigo generarEnemigo(int nivel){
        int vida = 40 +(nivel*10);
        int daño = 5 +(nivel*2);
        int defensa = nivel;
        return new Enemigo(daño,defensa,"Enemigo Prueba", vida,defensa);
    }

    private Enemigo generarBoss(int nivel) {
        int vidaBoss = 150 + (nivel * 20);
        int dañoBoss = 15 + (nivel * 3);
        return new Enemigo(dañoBoss, nivel * 2, "Boss Prueba " , vidaBoss,nivel);
    }

    private Objeto generarCofre(int nivel){
        return null;
    }

    public ArrayList<ArrayList<Habitacion>> getPisos() {
        return pisos;
    }



}
