package com.nuestrojuego.hectorycristian;

/**
 * La plantilla base para cualquier cosa que puedas recoger por el suelo y meter en tu mochila.
 * Sirve de molde igual para espadas, armaduras, pociones o lo que sea.
 */
public abstract class Objeto {
    private int idObjeto;
    private String nombre;
    private String descripcion;
    private int peso;
    private Rareza rareza;

    /**
     * Fabrica una cosa nueva con todos sus detalles, incluyendo lo difícil que es de encontrar.
     * * @param id El número que lo identifica de los demás.
     * @param nombre Cómo se llama el trasto.
     * @param descripcion Un texto que cuenta para qué sirve o cómo es.
     * @param peso Los kilos que pesa al llevarlo encima.
     * @param rareza Su color o categoría (común, épico, etc).
     */
    public Objeto(int id, String nombre, String descripcion, int peso, Rareza rareza) {
        this.idObjeto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.peso = peso;
        this.rareza = rareza;
    }

    /**
     * Fabrica una cosa normal y corriente que no tiene ninguna rareza especial.
     * * @param id El número que lo identifica.
     * @param nombre Cómo se llama.
     * @param descripcion Un texto que explica cómo es.
     * @param peso Los kilos que pesa.
     */
    public Objeto(int id, String nombre, String descripcion, int peso) {
        this.idObjeto = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.peso = peso;
        this.rareza = Rareza.NINGUNA;
    }

    /**
     * Te chiva el carné de identidad de este trasto.
     * * @return El número identificador.
     */
    public int getIdObjeto() {
        return idObjeto;
    }

    /**
     * Te dice cómo se llama la cosa.
     * * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Te lee la etiqueta para saber más sobre este objeto.
     * * @return El texto de descripción.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Te avisa de cuánto pesa para que controles la carga.
     * * @return Los kilos de peso.
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Te chiva de qué color o calidad es el objeto.
     * * @return La rareza.
     */
    public Rareza getRareza() {
        return rareza;
    }

    /**
     * Sirve para comparar si dos cosas que tienes son exactamente la misma mirando su matrícula o ID.
     * * @param o La otra cosa con la que lo quieres comparar.
     * @return Verdadero si son lo mismo, falso si son cosas distintas.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Objeto)) return false;
        return this.idObjeto == ((Objeto) o).idObjeto;
    }

    /**
     * Saca un número matemático para organizar el objeto por dentro del juego.
     * * @return El código matemático.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(idObjeto);
    }

    /**
     * Te da el texto de descripción tal cual venía de fábrica.
     * * @return El texto original.
     */
    public String getOriginalDescripcion() {
        return descripcion;
    }
}