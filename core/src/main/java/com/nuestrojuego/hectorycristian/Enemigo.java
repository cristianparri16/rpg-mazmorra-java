package com.nuestrojuego.hectorycristian;


public class Enemigo extends Entidad {
    private int dañoTotal;
    private int defensaTotal;
    //private ArrayList<Objeto> drops;
    public Enemigo (int dañoTotal, int defensaTotal, String nombre, int vidaMaxima, int defensaBase) {
        super(nombre, vidaMaxima, defensaBase);
    }
}
