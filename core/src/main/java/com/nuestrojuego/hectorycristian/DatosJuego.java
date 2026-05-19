package com.nuestrojuego.hectorycristian;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

public class DatosJuego {
    private static DatosJuego instancia;

    public ArrayList<int[]> enemigos = new ArrayList<>();
    public ArrayList<String[]> nombresEnemigos = new ArrayList<>();
    public ArrayList<int[]> espadas = new ArrayList<>();
    public ArrayList<String[]> nombresEspadas = new ArrayList<>();
    public ArrayList<int[]> arcos = new ArrayList<>();
    public ArrayList<String[]> nombresArcos = new ArrayList<>();
    public ArrayList<int[]> bastones = new ArrayList<>();
    public ArrayList<String[]> nombresBastones = new ArrayList<>();

    private DatosJuego() {}

    public static DatosJuego getInstancia() {
        if (instancia == null) instancia = new DatosJuego();
        return instancia;
    }

    public void cargar() {
        String json = Gdx.files.internal("DungeonAssets/Datos.JSON").readString();
        cargarEnemigos(json);
        cargarEspadas(json);
        cargarArcos(json);
        cargarBastones(json);
    }

    private void cargarEnemigos(String json) {
        String bloque = extraerBloque(json, "enemigos");
        String[] partes = bloque.split("\\{");
        for (String b : partes) {
            if (!b.contains("nombre")) continue;
            int[] stats = {
                    getInt(b, "vidaBase"),
                    getInt(b, "dañoBase"),
                    getInt(b, "defensaBase"),
                    getInt(b, "vidaPorNivel"),
                    getInt(b, "dañoPorNivel"),
                    getInt(b, "defensaPorNivel")
            };
            String[] info = { getString(b, "nombre"), getString(b, "tipo") };
            enemigos.add(stats);
            nombresEnemigos.add(info);
        }
    }

    private void cargarEspadas(String json) {
        String bloque = extraerBloque(json, "espadas");
        String[] partes = bloque.split("\\{");
        for (String b : partes) {
            if (!b.contains("nombre")) continue;
            int[] stats = { getInt(b, "dañoBase"), getInt(b, "dañoPorNivel"), getInt(b, "peso") };
            String[] info = { getString(b, "nombre"), getString(b, "rareza") };
            espadas.add(stats);
            nombresEspadas.add(info);
        }
    }

    private void cargarArcos(String json) {
        String bloque = extraerBloque(json, "arcos");
        String[] partes = bloque.split("\\{");
        for (String b : partes) {
            if (!b.contains("nombre")) continue;
            int[] stats = { getInt(b, "dañoBase"), getInt(b, "dañoPorNivel"), getInt(b, "peso") };
            String[] info = { getString(b, "nombre"), getString(b, "rareza") };
            arcos.add(stats);
            nombresArcos.add(info);
        }
    }

    private void cargarBastones(String json) {
        String bloque = extraerBloque(json, "bastones");
        String[] partes = bloque.split("\\{");
        for (String b : partes) {
            if (!b.contains("nombre")) continue;
            int[] stats = { getInt(b, "dañoBase"), getInt(b, "dañoPorNivel"), getInt(b, "manaBonus") };
            String[] info = { getString(b, "nombre"), getString(b, "rareza") };
            bastones.add(stats);
            nombresBastones.add(info);
        }
    }

    private String extraerBloque(String json, String clave) {
        int inicio = json.indexOf("\"" + clave + "\"");
        int corchete = json.indexOf("[", inicio);
        int fin = json.indexOf("]", corchete);
        return json.substring(corchete, fin);
    }

    private int getInt(String bloque, String clave) {
        String buscar = "\"" + clave + "\":";
        int inicio = bloque.indexOf(buscar);
        if (inicio == -1) return 0;

        inicio += buscar.length();
        int fin = inicio;

        while (fin < bloque.length() && (Character.isWhitespace(bloque.charAt(fin)) || bloque.charAt(fin) == ',')) {
            inicio++;
            fin++;
        }

        while (fin < bloque.length() && (Character.isDigit(bloque.charAt(fin)) || bloque.charAt(fin) == '-')) {
            fin++;
        }

        String valor = bloque.substring(inicio, fin).trim();
        if (valor.isEmpty()) return 0;

        return Integer.parseInt(valor);
    }

    private String getString(String bloque, String clave) {
        String buscar = "\"" + clave + "\":";
        int inicio = bloque.indexOf(buscar);
        if (inicio == -1) return "Desconocido";

        inicio += buscar.length();

        int comillaInicio = bloque.indexOf("\"", inicio);
        if (comillaInicio == -1) return "Desconocido";

        int comillaFin = bloque.indexOf("\"", comillaInicio + 1);
        if (comillaFin == -1) return "Desconocido";

        return bloque.substring(comillaInicio + 1, comillaFin);
    }
}.