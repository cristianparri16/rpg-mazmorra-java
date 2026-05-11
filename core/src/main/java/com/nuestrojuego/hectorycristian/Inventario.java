package com.nuestrojuego.hectorycristian;

import java.util.HashMap;
import java.util.Map;

public class Inventario {
    private HashMap<Objeto, Integer> objetos;
    private int pesoActual;
    private static Inventario instancia;
    private Personaje personaje;

    private Inventario(Personaje p) {
        this.pesoActual = 0;
        this.objetos = new HashMap<>();
        this.personaje = p;
    }

    public static Inventario getInstancia(Personaje jugador) {
        if (instancia == null) {
            instancia = new Inventario(Personaje.getInstancia());
        }
        return instancia;
    }

    // La capacidad siempre se calcula desde la fuerza actual del personaje
    public int getCapacidad() {
        return personaje.getFuerza() * 10;
    }

    public Objeto buscarObjeto(int id) {
        for (Objeto objeto : objetos.keySet()) {
            if (objeto.getIdObjeto() == id) {
                return objeto;
            }
        }
        return null;
    }

    public boolean añadirObjeto(Objeto objeto, int cantidad) {
        int pesoAñadir = objeto.getPeso() * cantidad;

        if (this.pesoActual + pesoAñadir <= getCapacidad()) {
            int cantidadActual = objetos.getOrDefault(objeto, 0);
            objetos.put(objeto, cantidadActual + cantidad);
            this.pesoActual += pesoAñadir;
            return true;
        } else {
            System.out.println("¡No puedes llevar más peso! (" + pesoActual + "/" + getCapacidad() + " kg)");
            return false;
        }
    }

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

    public void mostrarInventario() {
        System.out.println("--- INVENTARIO (" + pesoActual + "/" + getCapacidad() + " kg) ---");
        if (objetos.isEmpty()) {
            System.out.println("El inventario está vacío");
            return;
        }
        for (Map.Entry<Objeto, Integer> entrada : objetos.entrySet()) {
            Objeto obj = entrada.getKey();
            int cant = entrada.getValue();
            String tipo = "";
            if (obj instanceof ArmaADistancia) tipo = "[Arco]";
            else if (obj instanceof Arma) tipo = "[Espada]";
            else if (obj instanceof Armadura) tipo = "[Armadura]";
            else if (obj instanceof Pocion) tipo = "[Poción]";
            else if (obj instanceof Flechas) tipo = "[Flechas]";

            System.out.println("- " + tipo + " " + obj.getNombre()
                    + " x" + cant
                    + " | Peso: " + (obj.getPeso() * cant) + " kg"
                    + " | ID: " + obj.getIdObjeto());
        }
    }

    public HashMap<Objeto, Integer> getObjetos() { return objetos; }
    public void setObjetos(HashMap<Objeto, Integer> objetos) { this.objetos = objetos; }
    public int getPesoActual() { return pesoActual; }
    public void setPesoActual(int pesoActual) { this.pesoActual = pesoActual; }
}