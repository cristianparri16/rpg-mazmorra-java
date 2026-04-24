package com.nuestrojuego.hectorycristian;

import java.util.HashMap;
import java.util.Map;

public class Inventario {
   private HashMap<Objeto,Integer> objetos;
   private int capacidad;
   private int pesoActual;
   private static Inventario instancia;


   private Inventario(Personaje p) {
       this.pesoActual = 0;
       this.objetos =new HashMap<>();
       this.capacidad = p.getFuerza() * 10;
   }

   public static Inventario getInstancia(Personaje p) {
       if (instancia==null) {
           instancia=new Inventario(p);
       }
       return instancia;
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

        if (this.pesoActual + pesoAñadir <= capacidad) {
            int cantidadActual = objetos.getOrDefault(objeto, 0);
            objetos.put(objeto, cantidadActual + cantidad);

            this.pesoActual += pesoAñadir;
            return true;
        } else {
            System.out.println("¡Pesas demasiado para llevar esto!");
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
        System.out.println("--- INVENTARIO (" + pesoActual + "/" + capacidad + " kg) ---");
        for (Map.Entry<Objeto, Integer> entrada : objetos.entrySet()) {
            Objeto obj = entrada.getKey();
            int cant = entrada.getValue();
            System.out.println("- " + obj.getNombre() + " x" + cant + " (" + (obj.getPeso() * cant) + " kg)");
        }
    }
    public HashMap<Objeto, Integer> getObjetos() {
        return objetos;
    }

    public void setObjetos(HashMap<Objeto, Integer> objetos) {
        this.objetos = objetos;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getPesoActual() {
        return pesoActual;
    }

    public void setPesoActual(int pesoActual) {
        this.pesoActual = pesoActual;
    }

}
