package com.nuestrojuego.hectorycristian;

/**
 * Munición necesaria para poder disparar con los arcos.
 */
public class Flechas extends Objeto {
    private int dañoFlecha;

    /**
     * Crea un montón de flechas listas para usar.
     * * @param id El numero que las identifica.
     * @param nombre Como se llaman estas flechas.
     * @param descripcion Un texto que explica como son.
     * @param peso Lo que pesan al llevarlas en la mochila.
     * @param dañoFlecha La pupa extra que hacen al clavarse.
     */
    public Flechas(int id, String nombre, String descripcion, int peso, int dañoFlecha) {
        super(id, nombre, descripcion, peso, Rareza.NINGUNA);
        this.dañoFlecha = dañoFlecha;
    }

    /**
     * Te dice el daño extra que suma esta flecha al dispararla.
     * @return Los puntos de daño de la flecha.
     */
    public int getDañoFlecha() {
        return dañoFlecha;
    }
}