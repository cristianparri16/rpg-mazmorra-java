package com.nuestrojuego.hectorycristian;

public class Arma extends Objeto {
    private int dañoArma;

    public Arma(int id, String nombre, String descripcion, int peso, int dañoArma, Rareza rareza) {
        super(id, nombre, descripcion, peso, rareza);
        this.dañoArma = dañoArma;
    }

    public int getDañoArma() {
        return dañoArma;
    }
}





