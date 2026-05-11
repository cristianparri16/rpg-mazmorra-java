package com.nuestrojuego.hectorycristian;

public class ArmaADistancia extends Arma {
    private TipoMunicion tipoMunicion;

    public ArmaADistancia(int id, String nombre, String descripcion, int peso,int dañoArma,Rareza rareza,TipoMunicion municion) {
        super(id,nombre,descripcion,peso,dañoArma,rareza);
        this.tipoMunicion = municion;
    }

    public TipoMunicion getTipoMunicion() {return tipoMunicion;}


}
