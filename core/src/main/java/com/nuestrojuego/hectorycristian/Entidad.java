package com.nuestrojuego.hectorycristian;

/**
 * La base para cualquier ser vivo del juego, ya sea tu heroe o los monstruos.
 * Guarda lo mas basico: nombre, vida y defensa.
 */
public abstract class Entidad {
    protected String nombre;
    protected int vidaActual;
    protected int vidaMaxima;
    protected int defensaBase;

    /**
     * Prepara a un ser vivo con sus datos iniciales.
     * * @param nombre Como se llama.
     * @param vidaMaxima La salud a tope que tiene.
     * @param defensaBase Lo duro que es de base.
     */
    public Entidad(String nombre, int vidaMaxima, int defensaBase) {
        this.nombre = nombre;
        this.vidaActual = vidaMaxima;
        this.vidaMaxima = vidaMaxima;
        this.defensaBase = defensaBase;
    }

    /**
     * Calcula cuanta pupa te hacen de verdad despues de que tu armadura o piel pare el golpe.
     * * @param dañoEntrante El tortazo crudo que te intentan dar.
     * @param defensaTotal Toda la defensa que tienes puesta para parar el golpe.
     */
    public void recibirDaño(int dañoEntrante, int defensaTotal) {
        if (defensaTotal <= 0) {
            if (dañoEntrante > 0) {
                this.vidaActual -= dañoEntrante;
            }
            return;
        }

        double multiplicador = 100.0 / (100.0 + defensaTotal);
        int dañoFinal = (int) Math.round(dañoEntrante * multiplicador);

        if (dañoFinal < 1) {
            if (dañoEntrante > 0) {
                dañoFinal = 1;
            }
        }

        if (dañoFinal > 0) {
            this.vidaActual -= dañoFinal;
        }
    }

    /**
     * Comprueba si a este ser vivo le quedan puntos de vida.
     * * @return Verdadero si sigue vivo, falso si ha palmado.
     */
    public boolean estaVivo() {
        return this.vidaActual > 0;
    }

    /**
     * Te dice el nombre.
     * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre.
     * @param nombre El nombre nuevo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Te dice cuanta vida le queda ahora mismo.
     * @return La vida actual.
     */
    public int getVidaActual() {
        return vidaActual;
    }

    /**
     * Le cambia la vida que tiene ahora mismo.
     * @param vidaActual La nueva vida.
     */
    public void setVidaActual(int vidaActual) {
        this.vidaActual = vidaActual;
    }

    /**
     * Te dice cual es su tope de vida.
     * @return La vida maxima.
     */
    public int getVidaMaxima() {
        return vidaMaxima;
    }

    /**
     * Cambia su tope de vida.
     * @param vidaMaxima El nuevo tope de vida.
     */
    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    /**
     * Te dice su defensa de fabrica.
     * @return La defensa base.
     */
    public int getDefensaBase() {
        return defensaBase;
    }

    /**
     * Cambia su defensa de fabrica.
     * @param defensaBase La nueva defensa base.
     */
    public void setDefensaBase(int defensaBase) {
        this.defensaBase = defensaBase;
    }
}