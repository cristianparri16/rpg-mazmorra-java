package com.nuestrojuego.hectorycristian;

public class ArmaADistancia extends Arma {
    private TipoMunicion tipoMunicion;

    public ArmaADistancia(int id, String nombre, String descripcion, int peso,int dañoArma,TipoMunicion municion) {
        super(id,nombre,descripcion,peso,dañoArma);
        this.tipoMunicion = municion;
    }

    public TipoMunicion getTipoMunicion() {return tipoMunicion;}


}
