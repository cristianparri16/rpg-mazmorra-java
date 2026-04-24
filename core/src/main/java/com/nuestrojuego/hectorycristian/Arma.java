package com.nuestrojuego.hectorycristian;

public class Arma extends Objeto{
    private int dañoArma;

    public Arma (int id, String nombre, String descripcion, int peso,int dañoArma){
        super(id,nombre,descripcion,peso);
        this.dañoArma = dañoArma;
    }

    public int getDañoArma() {
        return dañoArma;
    }
}
