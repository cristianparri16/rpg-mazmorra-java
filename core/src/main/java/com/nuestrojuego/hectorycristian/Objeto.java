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

    public int getIdObjeto() { return idObjeto; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getPeso() { return peso; }
    public Rareza getRareza() { return rareza; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Objeto)) return false;
        return this.idObjeto == ((Objeto) o).idObjeto;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idObjeto);
    }
}