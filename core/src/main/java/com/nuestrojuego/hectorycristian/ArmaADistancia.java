package com.nuestrojuego.hectorycristian;
public class ArmaADistancia extends Arma {
    private Flechas flechas;

    public ArmaADistancia(int id, String nombre, String descripcion, int peso, int dañoArma, Rareza rareza) {
        super(id, nombre, descripcion, peso, dañoArma, rareza);
        this.flechas = null;
    }

    public Flechas getFlechas() { return flechas; }
    public void setFlechas(Flechas flechas) { this.flechas = flechas; }
    public boolean tieneFlechas() { return flechas != null; }
}



