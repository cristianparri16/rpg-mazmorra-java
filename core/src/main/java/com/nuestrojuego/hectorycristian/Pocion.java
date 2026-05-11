package com.nuestrojuego.hectorycristian;

public class Pocion extends Objeto{
    private int curacion;

    public Pocion(int id, String nombre, String descripcion, int peso,int curacion) {
        super(id, nombre, descripcion, peso,Rareza.NINGUNA);
        this.curacion = curacion;
    }

    public int getCuracion() {
        return curacion;
    }
}
