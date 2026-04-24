package com.nuestrojuego.hectorycristian;

public class Municion extends Objeto {
    private TipoMunicion tipoMunicion;
    public Municion(int id, String nombre, String descripcion, int peso, TipoMunicion tipoMunicion) {
        super(id, nombre, descripcion, peso);
        this.tipoMunicion = tipoMunicion;
    }

    public TipoMunicion getTipoMunicion() {return tipoMunicion;}
}
