package com.nuestrojuego.hectorycristian;

public class Armadura extends Objeto {
    private int armadura;
    private int fuerzaMinima;
    public Armadura(int id,String nombre,String descripcion, int peso,int armadura, int fuerzaMinima) {
        super(id,nombre,descripcion,peso);
    }

    public int getArmadura() {return armadura;}

    public int getFuerzaMinima() {return fuerzaMinima;}
}
