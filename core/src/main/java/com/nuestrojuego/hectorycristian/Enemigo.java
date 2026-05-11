package com.nuestrojuego.hectorycristian;


import java.util.ArrayList;

public class Enemigo extends Entidad {
    private int dañoTotal;
    private int defensaTotal;
    private ArrayList<Objeto> drops;
    public Enemigo (int dañoTotal, int defensaTotal, String nombre, int vidaMaxima, int defensaBase) {
        super(nombre, vidaMaxima, defensaBase);
        this.dañoTotal=dañoTotal;
        this.defensaTotal=defensaTotal;
        this.drops=new ArrayList<>();
    }

    public int getDefensaTotal() {
        return defensaTotal;
    }

    public int getDañoTotal() {
        return dañoTotal;
    }
}
