package com.nuestrojuego.hectorycristian;

public class Arma extends Objeto{
    private int dañoArma;
    private TipoArma tipoArma;

    public Arma (int id, String nombre, String descripcion, int peso,int dañoArma,TipoArma tipoArma,Rareza rareza){
        super(id,nombre,descripcion,peso,rareza);
        this.dañoArma = dañoArma;
        this.tipoArma=tipoArma;
    }

    public int getDañoArma() {
        return dañoArma;
    }

    public TipoArma getTipoArma() {
        return tipoArma;
    }
}
