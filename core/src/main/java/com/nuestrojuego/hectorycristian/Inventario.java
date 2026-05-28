package com.nuestrojuego.hectorycristian;

import java.util.HashMap;

/**
 * La mochila de tu personaje. Guarda todas las cosas que recoges por la mazmorra
 * y controla que no te pases de peso para que no te hernies al andar.
 */
public class Inventario {
    private HashMap<Objeto, Integer> objetos;
    private int pesoActual;
    private static Inventario instancia;
    private Personaje personaje;

    /**
     * Prepara la mochila vacia al empezar la aventura.
     *
     * @param p El heroe que va a cargar con ella.
     */
    private Inventario(Personaje p) {
        this.pesoActual = 0;
        this.objetos = new HashMap<>();
        this.personaje = p;
    }

    /**
     * Consigue la mochila de tu jugador. Si no tiene una, le da una nueva.
     *
     * @param jugador El heroe que la lleva puesta.
     * @return La mochila lista para usarse.
     */
    public static Inventario getInstancia(Personaje jugador) {
        if (instancia == null) {
            instancia = new Inventario(Personaje.getInstancia());
        }
        return instancia;
    }

    /**
     * Calcula cuantos kilos puede llevar el personaje dependiendo de lo fuerte que este.
     *
     * @return Los kilos maximos que aguanta la mochila.
     */
    public int getCapacidad() {
        return personaje.getFuerza() * 10;
    }

    /**
     * Busca una cosa concreta dentro de la mochila usando su numero de identidad.
     *
     * @param id El numero del objeto que estas buscando.
     * @return El objeto si lo encuentra, o nada si no lo tienes.
     */
    public Objeto buscarObjeto(int id) {
        for (Objeto objeto : objetos.keySet()) {
            if (objeto.getIdObjeto() == id) {
                return objeto;
            }
        }
        return null;
    }

    /**
     * Intenta meter cosas nuevas en la mochila. Si pesas demasiado, no te dejara guardarlo.
     *
     * @param objeto La cosa que quieres meter.
     * @param cantidad Cuantos de estos quieres guardar de golpe.
     * @return Verdadero si ha cabido, falso si pesa demasiado.
     */
    public boolean añadirObjeto(Objeto objeto, int cantidad) {
        int pesoAñadir = objeto.getPeso() * cantidad;

        if (this.pesoActual + pesoAñadir <= getCapacidad()) {
            int cantidadActual = objetos.getOrDefault(objeto, 0);
            objetos.put(objeto, cantidadActual + cantidad);
            this.pesoActual += pesoAñadir;
            return true;
        }
        return false;
    }

    /**
     * Tira cosas de la mochila para hacer espacio o porque las has gastado.
     *
     * @param objeto La cosa que quieres tirar.
     * @param cantidad Cuantos quieres tirar.
     * @return Verdadero si lo ha tirado bien, falso si intentas tirar mas de los que tienes.
     */
    public boolean eliminarObjeto(Objeto objeto, int cantidad) {
        if (objetos.containsKey(objeto)) {
            int cantidadActual = objetos.get(objeto);

            if (cantidadActual >= cantidad) {
                objetos.put(objeto, cantidadActual - cantidad);
                this.pesoActual -= (objeto.getPeso() * cantidad);

                if (objetos.get(objeto) <= 0) {
                    objetos.remove(objeto);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Te enseña la lista de todas las cosas que llevas guardadas.
     *
     * @return La lista de objetos con sus cantidades.
     */
    public HashMap<Objeto, Integer> getObjetos() {
        return objetos;
    }

    /**
     * Cambia de golpe todo lo que tienes en la mochila por otra lista de cosas.
     *
     * @param objetos La nueva lista de trastos.
     */
    public void setObjetos(HashMap<Objeto, Integer> objetos) {
        this.objetos = objetos;
    }

    /**
     * Te dice cuantos kilos en total llevas a la espalda ahora mismo.
     *
     * @return El peso total de tu mochila.
     */
    public int getPesoActual() {
        return pesoActual;
    }

    /**
     * Truca el peso que crees llevar puesto.
     *
     * @param pesoActual El nuevo peso forzado.
     */
    public void setPesoActual(int pesoActual) {
        this.pesoActual = pesoActual;
    }
}