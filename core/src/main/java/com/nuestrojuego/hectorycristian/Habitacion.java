package com.nuestrojuego.hectorycristian;

/**
 * Representa un cuarto o sala dentro del mapa.
 * Sabe qué puertas tiene abiertas, con qué otras salas conecta y qué sorpresa esconde dentro.
 */
public class Habitacion {
    public TipoHabitacion tipoHabitacion;
    private Object contenido;
    private boolean salacompletada;
    public Habitacion norte, sur, este, oeste;
    public boolean tieneEscaleras = false;
    public boolean altarDisponible = Math.random() < 0.40;

    /**
     * Prepara una sala nueva diciendo de qué tipo es y qué hay en su interior.
     * * @param tipoHabitacion La clase de cuarto que es (de pelea, del jefe, de cofre...).
     * @param contenido Lo que te vas a encontrar dentro (un monstruo, un cofre...).
     */
    public Habitacion(TipoHabitacion tipoHabitacion, Object contenido) {
        this.tipoHabitacion = tipoHabitacion;
        this.contenido = contenido;
        this.salacompletada = false;
    }

    /**
     * Te dice qué clase de cuarto es este.
     * * @return El tipo de la sala.
     */
    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    /**
     * Cambia el tipo de cuarto por otro nuevo.
     * * @param tipoHabitacion El nuevo tipo que quieres que sea.
     */
    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    /**
     * Te enseña lo que hay escondido en la sala.
     * * @return El monstruo o el cofre que esté guardado.
     */
    public Object getContenido() {
        return contenido;
    }

    /**
     * Cambia lo que hay guardado en la sala por otra cosa.
     * * @param contenido La nueva sorpresa que quieres meter dentro.
     */
    public void setContenido(Object contenido) {
        this.contenido = contenido;
    }
}