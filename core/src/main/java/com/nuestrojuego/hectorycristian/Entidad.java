package com.nuestrojuego.hectorycristian;

public abstract class Entidad {
    protected String nombre;
    protected int vidaActual;
    protected int vidaMaxima;
    protected int defensaBase;

    public Entidad(String nombre, int vidaMaxima,int defensaBase) {
        this.nombre = nombre;
        this.vidaActual = vidaMaxima;
        this.vidaMaxima = vidaMaxima;
        this.defensaBase = defensaBase;
    }

    public void recibirDaño(int dañoEntrante, int defensaTotal){
        int dañoFinal = dañoEntrante - defensaTotal;
        if (dañoFinal < 1) dañoFinal = 0;
        if (dañoFinal>0){
            this.vidaActual -= dañoFinal;
        }
    }

    public boolean estaVivo(){
        return this.vidaActual > 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    public int getDefensaBase() {
        return defensaBase;
    }

    public void setDefensaBase(int defensaBase) {
        this.defensaBase = defensaBase;
    }
}
