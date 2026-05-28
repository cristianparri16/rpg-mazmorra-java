package com.nuestrojuego.hectorycristian;

/**
 * Representa un arma de tipo baston que basa su daño en magia y otorga mana adicional.
 */
public class Baston extends Arma {
    private int manaBonus;

    /**
     * Inicializa un nuevo baston magico.
     *
     * @param id Identificador del objeto.
     * @param nombre Nombre del baston.
     * @param descripcion Detalle del baston.
     * @param peso Carga que ocupa en el inventario.
     * @param dañoMagico Potencia magica ofensiva del baston.
     * @param manaBonus Puntos de mana maximo extra que proporciona.
     * @param rareza Clasificacion de rareza.
     */
    public Baston(int id, String nombre, String descripcion, int peso, int dañoMagico, int manaBonus, Rareza rareza) {
        super(id, nombre, descripcion, peso, dañoMagico, rareza);
        this.manaBonus = manaBonus;
    }

    /**
     * Obtiene los puntos de mana extra que proporciona el arma.
     *
     * @return Bonus de mana.
     */
    public int getManaBonus() {
        return manaBonus;
    }

    /**
     * Obtiene el daño magico del baston, que hereda de la estadistica base de daño.
     *
     * @return Puntos de daño magico.
     */
    public int getDañoMagico() {
        return getDañoArma();
    }
}