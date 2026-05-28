package com.nuestrojuego.hectorycristian;

import java.util.ArrayList;

/**
 * Representa a un monstruo o rival contra el que te vas a pelear en la mazmorra.
 */
public class Enemigo extends Entidad {
    private int dañoTotal;
    private int defensaTotal;
    private ArrayList<Objeto> drops;

    /**
     * Crea un bicho nuevo listo para pelear con sus estadisticas ya preparadas.
     * * @param dañoTotal El daño que te hace cuando te pega.
     * @param defensaTotal Lo duro que es para hacerle daño.
     * @param nombre Como se llama el monstruo.
     * @param vidaMaxima La salud total que tiene antes de morir.
     * @param defensaBase La defensa de fabrica que tiene.
     */
    public Enemigo (int dañoTotal, int defensaTotal, String nombre, int vidaMaxima, int defensaBase) {
        super(nombre, vidaMaxima, defensaBase);
        this.dañoTotal=dañoTotal;
        this.defensaTotal=defensaTotal;
        this.drops=new ArrayList<>();
    }

    /**
     * Te dice la defensa total del bicho.
     * * @return Los puntos de defensa.
     */
    public int getDefensaTotal() {
        return defensaTotal;
    }

    /**
     * Te dice lo fuerte que pega el bicho.
     * * @return Los puntos de daño.
     */
    public int getDañoTotal() {
        return dañoTotal;
    }
}