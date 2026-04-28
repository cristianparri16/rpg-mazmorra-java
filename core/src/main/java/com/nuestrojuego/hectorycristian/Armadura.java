package com.nuestrojuego.hectorycristian;

public class Armadura extends Objeto {
    private int armadura;
    private int fuerzaMinima;
    private Rareza rareza;
    public Armadura(int id,String nombre,String descripcion, int peso,int armadura, int fuerzaMinima,Rareza rareza) {
        super(id,nombre,descripcion,peso);
        this.rareza = rareza;
    }

    public int getArmadura() {return armadura;}

    public int getFuerzaMinima() {return fuerzaMinima;}
}
