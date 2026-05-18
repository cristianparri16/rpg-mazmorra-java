package com.nuestrojuego.hectorycristian;

import java.util.ArrayList;
import java.util.Random;

public class Cofre {
    private Objeto objeto;
    private static int contadorIds = 0;

    public Cofre(int nivel, int suerte, Random azar) {
        Rareza rareza = determinarRareza(suerte, azar);
        this.objeto = generarObjetoPorRareza(nivel, rareza, azar);
    }

    private Rareza determinarRareza(int suerte, Random azar) {
        int bonusSuerte = suerte * 2;
        int tirada = azar.nextInt(100);
        if (tirada < Math.min(5 + bonusSuerte / 4, 25)) return Rareza.LEGENDARIO;
        if (tirada < Math.min(15 + bonusSuerte / 3, 40)) return Rareza.EPICO;
        if (tirada < Math.min(35 + bonusSuerte / 2, 60)) return Rareza.RARO;
        if (tirada < 65 + bonusSuerte) return Rareza.POCO_COMUN;
        return Rareza.COMUN;
    }

    private Objeto generarObjetoPorRareza(int nivel, Rareza rareza, Random azar) {
        DatosJuego datos = DatosJuego.getInstancia();
        int id = contadorIds++;
        int tipoObjeto = azar.nextInt(5); // 0=espada, 1=arco, 2=baston, 3=pocion, 4=flechas

        switch (tipoObjeto) {
            case 0:
                return generarEspada(id, nivel, rareza, datos, azar);
            case 1:
                return generarArco(id, nivel, rareza, datos, azar);
            case 2:
                return generarBaston(id, nivel, rareza, datos, azar);
            case 3:
                int curacion = 20 + (nivel * 5);
                int tipoPocion = azar.nextInt(2);
                if (tipoPocion == 0) {
                    return new Pocion(id, "Poción de vida", "Restaura " + curacion + " de vida", 1, curacion, TipoPocion.SALUD);
                } else {
                    return new Pocion(id, "Poción de mana", "Restaura " + curacion + " de mana", 1, curacion, TipoPocion.MANA);
                }
            case 4:
                int dañoFlecha = 3 + nivel;
                return new Flechas(id, "Flechas", "Flechas del piso " + nivel, 1, dañoFlecha);
        }
        return null;
    }

    private Espada generarEspada(int id, int nivel, Rareza rareza, DatosJuego datos, Random azar) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < datos.espadas.size(); i++) {
            if (datos.nombresEspadas.get(i)[1].equals(rareza.name())) {
                indices.add(i);
            }
        }
        if (indices.isEmpty()) {
            return new Espada(id, "Espada básica", "Sin datos", 3, 5 + nivel, Rareza.COMUN);
        }
        int idx = indices.get(azar.nextInt(indices.size()));
        int[] stats = datos.espadas.get(idx);
        String nombre = datos.nombresEspadas.get(idx)[0];
        int daño = stats[0] + (stats[1] * nivel);
        return new Espada(id, nombre, "Espada " + rareza, stats[2], daño, rareza);
    }

    private ArmaADistancia generarArco(int id, int nivel, Rareza rareza, DatosJuego datos, Random azar) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < datos.arcos.size(); i++) {
            if (datos.nombresArcos.get(i)[1].equals(rareza.name())) {
                indices.add(i);
            }
        }
        if (indices.isEmpty()) {
            return new ArmaADistancia(id, "Arco básico", "Sin datos", 2, 5 + nivel, Rareza.COMUN);
        }
        int idx = indices.get(azar.nextInt(indices.size()));
        int[] stats = datos.arcos.get(idx);
        String nombre = datos.nombresArcos.get(idx)[0];
        int daño = stats[0] + (stats[1] * nivel);
        return new ArmaADistancia(id, nombre, "Arco " + rareza, stats[2], daño, rareza);
    }

    private Baston generarBaston(int id, int nivel, Rareza rareza, DatosJuego datos, Random azar) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < datos.bastones.size(); i++) {
            if (datos.nombresBastones.get(i)[1].equals(rareza.name())) {
                indices.add(i);
            }
        }
        if (indices.isEmpty()) {
            return new Baston(id, "Bastón básico", "Sin datos", 2, 5 + nivel, 10, Rareza.COMUN);
        }
        int idx = indices.get(azar.nextInt(indices.size()));
        int[] stats = datos.bastones.get(idx);
        String nombre = datos.nombresBastones.get(idx)[0];
        int daño = stats[0] + (stats[1] * nivel);
        int manaBonus = stats[2];
        return new Baston(id, nombre, "Bastón " + rareza, 2, daño, manaBonus, rareza);
    }

    public Objeto getObjeto() { return objeto; }
    public void setObjeto(Objeto objeto) { this.objeto = objeto; }
}