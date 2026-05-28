package com.nuestrojuego.hectorycristian;

/**
 * Una espada normal y corriente para pelear cuerpo a cuerpo.
 */
public class Espada extends Arma {

    /**
     * Prepara una espada nueva lista para dar tajos.
     * * @param id El numero que la identifica.
     * @param nombre Como se llama la espada.
     * @param descripcion Un texto que explica como es.
     * @param peso Lo que te pesa al llevarla en la mochila.
     * @param dañoArma La pupa que hace al golpear con ella.
     * @param rareza Lo dificil que es encontrar una asi.
     */
    public Espada(int id, String nombre, String descripcion, int peso, int dañoArma, Rareza rareza) {
        super(id, nombre, descripcion, peso, dañoArma, rareza);
    }
}