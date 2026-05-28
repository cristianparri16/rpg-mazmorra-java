package com.nuestrojuego.hectorycristian;

/**
 * Un frasco mágico que te bebes para recuperar salud o magia en medio del combate.
 */
public class Pocion extends Objeto{
    private int curacion;
    private TipoPocion tipopocion;

    /**
     * Prepara un mejunje nuevo listo para tragar.
     * @param id El número que lo identifica.
     * @param nombre Cómo se llama el potingue.
     * @param descripcion Lo que pone en la etiqueta para saber qué hace.
     * @param peso Los kilos que pesa llevarlo en la mochila.
     * @param curacion Los puntos que te cura de golpe al beberla.
     * @param tipopocion Para saber si es una bebida para la vida o para el maná.
     */
    public Pocion(int id, String nombre, String descripcion, int peso, int curacion, TipoPocion tipopocion) {
        super(id, nombre, descripcion, peso, Rareza.NINGUNA);
        this.curacion = curacion;
        this.tipopocion = tipopocion;
    }

    /**
     * Te chiva cuántos puntos vas a recuperar al usar este objeto.
     * @return Los puntos de curación que da.
     */
    public int getCuracion() {
        return curacion;
    }
}