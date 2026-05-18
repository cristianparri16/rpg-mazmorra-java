package com.nuestrojuego.hectorycristian;

public class Pocion extends Objeto{
    private int curacion;
    private TipoPocion tipopocion;

    public Pocion(int id, String nombre, String descripcion, int peso,int curacion,TipoPocion tipopocion) {
        super(id, nombre, descripcion, peso,Rareza.NINGUNA);
        this.curacion = curacion;
        this.tipopocion = tipopocion;
    }

    public int getCuracion() {
        return curacion;
    }
}
