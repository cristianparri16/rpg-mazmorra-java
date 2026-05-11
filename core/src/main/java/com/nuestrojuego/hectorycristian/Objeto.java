package com.nuestrojuego.hectorycristian;

public abstract class Objeto {
        private int idObjeto;
        private String nombre;
        private String descripcion;
        private int peso;
        private Rareza rareza;

    public Objeto(int id, String nombre, String descripcion, int peso, Rareza rareza) {
        this.idObjeto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.peso = peso;
        this.rareza = rareza;
    }

    public Objeto(int id, String nombre, String descripcion, int peso) {
        this.idObjeto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.peso = peso;
        this.rareza = Rareza.NINGUNA;
    }



    public int getIdObjeto() {
        return idObjeto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPeso() {return peso;}
}

