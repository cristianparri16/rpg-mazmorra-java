package com.nuestrojuego.hectorycristian;

public class Arma extends Objeto{
    private int dañoArma;
    private Rareza rareza;

    public Arma (int id, String nombre, String descripcion, int peso,int dañoArma,Rareza rareza){
        super(id,nombre,descripcion,peso);
        this.dañoArma = dañoArma;
        this.rareza = rareza;
    }

    public int getDañoArma() {
        return dañoArma;
    }
}
