package com.nuestrojuego.hectorycristian;

/**
 * Representa una pieza de armadura equipable que proporciona defensa al personaje.
 */
public class Armadura extends Objeto {
    private int armadura;
    private int fuerzaMinima;
    private Rareza rareza;

    /**
     * Inicializa una nueva armadura con sus atributos defensivos y requisitos.
     *
     * @param id Identificador del objeto.
     * @param nombre Nombre de la armadura.
     * @param descripcion Detalle de la armadura.
     * @param peso Carga que ocupa en el inventario.
     * @param armadura Puntos de defensa que otorga.
     * @param fuerzaMinima Nivel de fuerza requerido para equiparla.
     * @param rareza Clasificacion de rareza.
     */
    public Armadura(int id, String nombre, String descripcion, int peso, int armadura, int fuerzaMinima, Rareza rareza) {
        super(id, nombre, descripcion, peso, rareza);
        this.armadura = armadura;
        this.fuerzaMinima = fuerzaMinima;
    }

    /**
     * Obtiene los puntos de defensa de la armadura.
     *
     * @return Puntos de armadura.
     */
    public int getArmadura() {
        return armadura;
    }

    /**
     * Obtiene la fuerza minima necesaria para equipar la armadura.
     *
     * @return Requisito de fuerza.
     */
    public int getFuerzaMinima() {
        return fuerzaMinima;
    }
}