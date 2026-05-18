package com.nuestrojuego.hectorycristian;

public class Baston extends Arma {
    private int manaBonus;

    public Baston(int id, String nombre, String descripcion, int peso, int dañoMagico, int manaBonus, Rareza rareza) {
        super(id, nombre, descripcion, peso, dañoMagico, rareza);
        this.manaBonus = manaBonus;
    }

    public int getManaBonus() { return manaBonus; }
}