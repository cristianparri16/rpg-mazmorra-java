package com.nuestrojuego.hectorycristian;

/**
 * Representa la base para cualquier tipo de arma equipable en el juego.
 */
public abstract class Arma extends Objeto {
    private int dañoArma;

    /**
     * Instancia un arma nueva con su daño especifico y atributos de objeto.
     *
     * @param id Identificador del objeto.
     * @param nombre Nombre del arma.
     * @param descripcion Detalle del arma.
     * @param peso Carga que ocupa en el inventario.
     * @param dañoArma Potencia ofensiva del arma.
     * @param rareza Clasificacion de rareza.
     */
    public Arma(int id, String nombre, String descripcion, int peso, int dañoArma, Rareza rareza) {
        super(id, nombre, descripcion, peso, rareza);
        this.dañoArma = dañoArma;
    }

    /**
     * Recupera el daño que causa el arma.
     *
     * @return Puntos de daño.
     */
    public int getDañoArma() {
        return dañoArma;
    }

    /**
     * Actualiza el daño del arma.
     *
     * @param dañoArma Nuevo valor de daño.
     */
    public void setDañoArma(int dañoArma) {
        this.dañoArma = dañoArma;
    }
}