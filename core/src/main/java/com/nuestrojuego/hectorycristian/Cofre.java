package com.nuestrojuego.hectorycristian;

import java.util.ArrayList;
import java.util.Random;

/**
 * Representa un cofre en la mazmorra.
 * Se encarga de sortear y crear un objeto sorpresa dependiendo de la suerte que tengas y del piso en el que te encuentres.
 */
public class Cofre {
    private Objeto objeto;
    private static int contadorIds = 0;

    /**
     * Crea un cofre nuevo listo para abrirse con una recompensa dentro.
     *
     * @param nivel El piso de la mazmorra donde esta el cofre.
     * @param suerte Los puntos de suerte de tu heroe.
     * @param azar El motor de dados para que todo sea aleatorio.
     */
    public Cofre(int nivel, int suerte, Random azar) {
        Rareza rareza = determinarRareza(suerte, azar);
        this.objeto = generarObjetoPorRareza(nivel, rareza, azar);
    }

    /**
     * Decide la calidad del premio usando tu suerte y un poco de azar.
     *
     * @param suerte Tu suerte actual.
     * @param azar Motor de numeros aleatorios.
     * @return El color o rareza que tendra el premio final.
     */
    private Rareza determinarRareza(int suerte, Random azar) {
        int bonusSuerte = suerte * 2;
        int tirada = azar.nextInt(100);
        if (tirada < Math.min(5 + bonusSuerte / 4, 25)) return Rareza.LEGENDARIO;
        if (tirada < Math.min(15 + bonusSuerte / 3, 40)) return Rareza.EPICO;
        if (tirada < Math.min(35 + bonusSuerte / 2, 60)) return Rareza.RARO;
        if (tirada < 65 + bonusSuerte) return Rareza.POCO_COMUN;
        return Rareza.COMUN;
    }

    /**
     * Fabrica el objeto que va dentro del cofre basandose en lo raro que haya tocado.
     *
     * @param nivel Piso actual.
     * @param rareza Calidad del objeto.
     * @param azar Motor aleatorio.
     * @return El objeto recien creado.
     */
    private Objeto generarObjetoPorRareza(int nivel, Rareza rareza, Random azar) {
        DatosJuego datos = DatosJuego.getInstancia();
        int id = contadorIds++;
        int tipoObjeto = azar.nextInt(10);

        switch (tipoObjeto) {
            case 0:
            case 1:
            case 2:
                return generarEspada(id, nivel, rareza, datos, azar);
            case 3:
            case 4:
                return generarArco(id, nivel, rareza, datos, azar);
            case 5:
            case 6:
                return generarBaston(id, nivel, rareza, datos, azar);
            case 7:
            case 8:
                return generarArmadura(id, nivel, rareza, datos, azar);
            case 9:
                if (azar.nextBoolean()) {
                    return new Flechas(id, "Carcaj de Flechas (20x)", "Un fardo con 20 flechas del piso " + nivel, 2, 3 + nivel);
                } else {
                    return new Pocion(id, "Poción de salud", "Restaura vitalidad", 1, 30, TipoPocion.SALUD);
                }
        }
        return null;
    }

    /**
     * Saca una espada de la base de datos que cuadre con la rareza que te ha tocado.
     *
     * @param id Identificador unico.
     * @param nivel Nivel del piso para ajustar el daño.
     * @param rareza Calidad elegida.
     * @param datos La lista de todas las espadas del juego.
     * @param azar Motor aleatorio.
     * @return La espada montada y lista.
     */
    private Espada generarEspada(int id, int nivel, Rareza rareza, DatosJuego datos, Random azar) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < datos.espadas.size(); i++) {
            if (datos.nombresEspadas.get(i)[1].equals(rareza.name())) indices.add(i);
        }
        if (indices.isEmpty()) return new Espada(id, "Espada básica", "Sin datos", 3, 5 + nivel, Rareza.COMUN);

        int idx = indices.get(azar.nextInt(indices.size()));
        int[] s = datos.espadas.get(idx);
        return new Espada(id, datos.nombresEspadas.get(idx)[0], "Espada " + rareza, s[2], s[0] + (s[1] * nivel), rareza);
    }

    /**
     * Saca un arco de la base de datos que cuadre con la rareza que te ha tocado.
     *
     * @param id Identificador unico.
     * @param nivel Nivel del piso para ajustar el daño.
     * @param rareza Calidad elegida.
     * @param datos La lista de arcos del juego.
     * @param azar Motor aleatorio.
     * @return El arco montado y listo.
     */
    private ArmaADistancia generarArco(int id, int nivel, Rareza rareza, DatosJuego datos, Random azar) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < datos.arcos.size(); i++) {
            if (datos.nombresArcos.get(i)[1].equals(rareza.name())) indices.add(i);
        }
        if (indices.isEmpty()) return new ArmaADistancia(id, "Arco básico", "Sin datos", 2, 5 + nivel, Rareza.COMUN);

        int idx = indices.get(azar.nextInt(indices.size()));
        int[] s = datos.arcos.get(idx);
        return new ArmaADistancia(id, datos.nombresArcos.get(idx)[0], "Arco " + rareza, s[2], s[0] + (s[1] * nivel), rareza);
    }

    /**
     * Saca un baston magico de la base de datos que cuadre con la rareza.
     *
     * @param id Identificador unico.
     * @param nivel Nivel del piso para ajustar el daño.
     * @param rareza Calidad elegida.
     * @param datos La lista de bastones del juego.
     * @param azar Motor aleatorio.
     * @return El baston montado y listo.
     */
    private Baston generarBaston(int id, int nivel, Rareza rareza, DatosJuego datos, Random azar) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < datos.bastones.size(); i++) {
            if (datos.nombresBastones.get(i)[1].equals(rareza.name())) indices.add(i);
        }
        if (indices.isEmpty()) return new Baston(id, "Bastón básico", "Sin datos", 2, 5 + nivel, 10, Rareza.COMUN);

        int idx = indices.get(azar.nextInt(indices.size()));
        int[] s = datos.bastones.get(idx);
        return new Baston(id, datos.nombresBastones.get(idx)[0], "Bastón " + rareza, 2, s[0] + (s[1] * nivel), s[2], rareza);
    }

    /**
     * Saca una armadura de la base de datos basandose en lo profundo que estes en la mazmorra.
     *
     * @param id Identificador unico.
     * @param nivel Nivel del piso actual.
     * @param rareza Calidad elegida.
     * @param datos La lista de armaduras del juego.
     * @param azar Motor aleatorio.
     * @return La armadura lista para equipar.
     */
    private Armadura generarArmadura(int id, int nivel, Rareza rareza, DatosJuego datos, Random azar) {
        if (datos.armaduras.isEmpty()) return new Armadura(id, "Armadura acolchada básica", "Sin descripción", 3, 4 + nivel, 5, Rareza.COMUN);

        ArrayList<Integer> indicesValidos = new ArrayList<>();
        for (int i = 0; i < datos.armaduras.size(); i++) {
            int n = datos.armaduras.get(i)[0];
            if (Math.abs(n - nivel) <= 4 || (nivel >= 26 && n > 25)) indicesValidos.add(i);
        }

        if (indicesValidos.isEmpty()) indicesValidos.add(azar.nextInt(datos.armaduras.size()));

        int idx = indicesValidos.get(azar.nextInt(indicesValidos.size()));
        int[] s = datos.armaduras.get(idx);
        String[] info = datos.nombresArmaduras.get(idx);

        return new Armadura(id, info[0], info[2], s[4], s[1] + (s[2] * nivel), s[3], rareza);
    }

    /**
     * Recupera la recompensa guardada dentro de este cofre.
     *
     * @return Lo que te ha tocado.
     */
    public Objeto getObjeto() {
        return objeto;
    }

    /**
     * Cambia el premio del cofre a mano.
     *
     * @param objeto El nuevo premio a guardar.
     */
    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }
}