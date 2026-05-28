package com.nuestrojuego.hectorycristian;

/**
 * Representa un arma de ataque a distancia que requiere municion (flechas) para funcionar.
 */
public class ArmaADistancia extends Arma {
    private Flechas flechas;

    /**
     * Inicializa un arma a distancia descargada por defecto.
     *
     * @param id Identificador del objeto.
     * @param nombre Nombre del arma.
     * @param descripcion Detalle del arma.
     * @param peso Carga que ocupa en el inventario.
     * @param dañoArma Potencia ofensiva del arma.
     * @param rareza Clasificacion de rareza.
     */
    public ArmaADistancia(int id, String nombre, String descripcion, int peso, int dañoArma, Rareza rareza) {
        super(id, nombre, descripcion, peso, dañoArma, rareza);
        this.flechas = null;
    }

    /**
     * Obtiene la municion actual cargada en el arma.
     *
     * @return El objeto Flechas equipado, o null si esta descargada.
     */
    public Flechas getFlechas() {
        return flechas;
    }

    /**
     * Carga o retira municion del arma.
     *
     * @param flechas Las flechas a equipar.
     */
    public void setFlechas(Flechas flechas) {
        this.flechas = flechas;
    }

    /**
     * Comprueba si el arma tiene flechas cargadas listas para usar.
     *
     * @return true si el arma tiene flechas, false en caso contrario.
     */
    public boolean tieneFlechas() {
        return flechas != null;
    }
}