package com.nuestrojuego.hectorycristian;

public class Habitacion {
    public TipoHabitacion tipoHabitacion;
    private Object contenido;
    private boolean salacompletada;
    public Habitacion norte, sur, este, oeste;
    public boolean tieneEscaleras = false;

    public Habitacion(TipoHabitacion tipoHabitacion, Object contenido) {
        this.tipoHabitacion = tipoHabitacion;
        this.contenido = contenido;
        this.salacompletada = false;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public Object getContenido() {
        return contenido;
    }

    public void setContenido(Object contenido) {
        this.contenido = contenido;
    }
}