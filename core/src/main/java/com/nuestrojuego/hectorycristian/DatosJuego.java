package com.nuestrojuego.hectorycristian;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

/**
 * El almacen de datos del juego. Lee el archivo externo y guarda todas
 * las listas de cosas listas para cuando hagan falta en la partida.
 */
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
    public ArrayList<int[]> armaduras = new ArrayList<>();
    public ArrayList<String[]> nombresArmaduras = new ArrayList<>();

    private DatosJuego() {}

    /**
     * Consigue la caja central de datos. Si no se ha abierto nunca, la crea en el momento.
     * * @return El almacen de datos general.
     */
    public static DatosJuego getInstancia() {
        if (instancia == null) instancia = new DatosJuego();
        return instancia;
    }

    /**
     * Abre el archivo de texto y rellena todas las listas.
     * Tambien se encarga de crear 30 armaduras mezclando palabras y numeros.
     */
    public void cargar() {
        String json = Gdx.files.internal("DungeonAssets/Datos.JSON").readString();
        cargarEnemigos(json);
        cargarEspadas(json);
        cargarArcos(json);
        cargarBastones(json);

        String[] prefijos = {"Harapos", "Armadura", "Jubón", "Cota", "Peto", "Coraza", "Placas", "Manto", "Pavés", "Arnés"};
        String[] materiales = {"de Cuero", "de Hierro", "de Bronce", "de Acero", "Mithril", "Élfica", "Rúnica", "Maldita", "del Abismo", "Celestial"};
        String[] bonusTipos = {"VIDA", "FUERZA", "DESTREZA", "MANA"};

        for (int i = 1; i <= 30; i++) {
            String prefijo = prefijos[(i - 1) % prefijos.length];
            String material = materiales[(i - 1) % materiales.length];
            String nombre = prefijo + " " + material;

            String rareza = "COMUN";
            if (i > 6) rareza = "POCO_COMUN";
            if (i > 13) rareza = "RARO";
            if (i > 20) rareza = "EPICO";
            if (i > 26) rareza = "LEGENDARIO";

            int armaduraBase = 4 + (i * 2);
            int armaduraPorNivel = 1 + (i / 7);
            int fuerzaMinima = 4 + (i * 2) / 3;
            int peso = 2 + (i / 3);

            String bonusEfecto = bonusTipos[(i - 1) % bonusTipos.length];
            int valorBonus = 2 + (i / 4);
            String descripcion = "BONUS:" + bonusEfecto + ":" + valorBonus;

            int[] stats = {i, armaduraBase, armaduraPorNivel, fuerzaMinima, peso};
            String[] info = {nombre, rareza, descripcion};

            armaduras.add(stats);
            nombresArmaduras.add(info);
        }
    }

    /**
     * Busca los monstruos en el texto y guarda sus atributos y su nombre.
     * * @param json El texto completo del archivo.
     */
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

    /**
     * Busca las espadas en el texto y las deja listas en su lista.
     * * @param json El texto completo del archivo.
     */
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

    /**
     * Busca los arcos en el texto y guarda su daño y peso.
     * * @param json El texto completo del archivo.
     */
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

    /**
     * Busca los bastones magicos en el texto y anota su bonus de mana.
     * * @param json El texto completo del archivo.
     */
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

    /**
     * Corta un cacho del archivo de texto para que sea mas facil de leer luego.
     * @param json El texto completo del archivo.
     * @param clave La palabra por la que queremos empezar a cortar.
     * @return El trozo de texto ya recortado.
     */
    private String extraerBloque(String json, String clave) {
        int inicio = json.indexOf("\"" + clave + "\"");
        int corchete = json.indexOf("[", inicio);
        int fin = json.indexOf("]", corchete);
        return json.substring(corchete, fin);
    }

    /**
     * Rastrea un cacho de texto intentando pescar un numero exacto.
     * * @param bloque El texto donde vamos a buscar.
     * @param clave El nombre del numero que queremos encontrar.
     * @return El numero que ha pillado, o un cero si no lo encuentra.
     */
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

    /**
     * Rastrea un cacho de texto para atrapar una palabra entre comillas.
     * * @param bloque El texto donde vamos a buscar.
     * @param clave El nombre del texto que buscamos.
     * @return La palabra que haya encontrado, o un texto de error.
     */
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
}