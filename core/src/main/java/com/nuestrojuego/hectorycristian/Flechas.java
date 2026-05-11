package com.nuestrojuego.hectorycristian;

public class Flechas extends Objeto {
    private int dañoFlecha;

    public Flechas(int id, String nombre, String descripcion, int peso, int dañoFlecha) {
        super(id, nombre, descripcion, peso,Rareza.NINGUNA);
        this.dañoFlecha = dañoFlecha;
    }

    public int getDañoFlecha() { return dañoFlecha; }
}