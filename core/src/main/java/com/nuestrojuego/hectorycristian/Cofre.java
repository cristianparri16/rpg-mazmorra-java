package com.nuestrojuego.hectorycristian;


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
        if (tirada < Math.min(5 + bonusSuerte / 4, 25)) {
            return Rareza.LEGENDARIO;
        }
        if (tirada < Math.min(15 + bonusSuerte / 3, 40)) {
            return Rareza.EPICO;
        }
        if (tirada < Math.min(35 + bonusSuerte / 2, 60)) {
            return Rareza.RARO;
        }
        if (tirada < 65 + bonusSuerte) {
            return Rareza.POCO_COMUN;
        }
        return Rareza.COMUN;
    }

    private Objeto generarObjetoPorRareza(int nivel, Rareza rareza, Random azar) {
        double multiplicador = 0;
        switch (rareza) {
            case COMUN:
                multiplicador = 1.0;
                break;
            case POCO_COMUN:
                multiplicador = 1.3;
                break;
            case RARO:
                multiplicador = 1.6;
                break;
            case EPICO:
                multiplicador = 2.0;
                break;
            case LEGENDARIO:
                multiplicador = 2.5;
                break;
        }

        int id = contadorIds++;
        int tipoObjeto = azar.nextInt(4); // 0=armadura, 1=arma, 2=pocion, 3=flechas

        switch (tipoObjeto) {
            case 0:
                int defensa = (int) ((3 + nivel * 2) * multiplicador);
                int fuerzaMin = Math.max(0, nivel - 2);
                return new Armadura(id, nombreArmadura(rareza), "Armadura " + rareza, 8, defensa, fuerzaMin, rareza);
            case 1:
                int daño = (int) ((5 + nivel * 3) * multiplicador);
                return new Arma(id, nombreArma(rareza), "Arma " + rareza, 3, daño, rareza);
            case 2:
                int curacion = 20 + (nivel * 5);
                return new Pocion(id, "Poción de vida", "Restaura " + curacion + " de vida", 1, curacion);
            case 3:
                int dañoFlecha = 3 + nivel;
                return new Flechas(id, "Flechas", "Flechas del piso " + nivel, 1, dañoFlecha);
        }

        return null;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    private String nombreArma(Rareza rareza) {
        switch (rareza) {
            case COMUN:
                return "Espada oxidada";
            case POCO_COMUN:
                return "Espada de acero";
            case RARO:
                return "Espada élfica";
            case EPICO:
                return "Espada del abismo";
            case LEGENDARIO:
                return "Espada del destino";
        }
        return null;
    }

    private String nombreArmadura(Rareza rareza) {
        switch (rareza) {
            case COMUN:
                return "Cota de cuero";
            case POCO_COMUN:
                return "Cota de malla";
            case RARO:
                return "Armadura de placas";
            case EPICO:
                return "Armadura sombría";
            case LEGENDARIO:
                return "Armadura celestial";
        }
        return null;
    }
}
