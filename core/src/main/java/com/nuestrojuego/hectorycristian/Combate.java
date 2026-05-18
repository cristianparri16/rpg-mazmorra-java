package com.nuestrojuego.hectorycristian;

import java.util.Scanner;

public class Combate {
    private Personaje jugador;
    private Enemigo enemigo;
    private Inventario inventario;
    private Scanner teclado;
    private boolean combateActivo;

    public Combate(Enemigo enemigo) {
        this.jugador = Personaje.getInstancia();
        this.enemigo = enemigo;
        this.inventario = Inventario.getInstancia(jugador);
        this.teclado = new Scanner(System.in);
        this.combateActivo = true;
    }

    public void iniciarCombate() {
        System.out.println("=== COMBATE INICIADO ===");
        System.out.println("Te enfrentas a: " + enemigo.getNombre());

        while (combateActivo) {
            mostrarEstado();
            turnoJugador();

            if (!enemigo.estaVivo()) {
                victoriaJugador();
                break;
            }

            turnoEnemigo();

            if (!jugador.estaVivo()) {
                derrotaJugador();
                break;
            }
        }
    }



    private void turnoJugador() {
        int opcion = 0;
        do {
            System.out.println("1 - Atacar");
            System.out.println("2 - Cambiar arma");
            System.out.println("3 - Usar poción");
            opcion = teclado.nextInt();

            switch (opcion) {
                case 1:
                    atacarEnemigo();
                    break;
                case 2:
                    cambiarArma();
                    break;
                case 3:
                    usarPocion();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (opcion < 1 || opcion > 3);
    }

    private void atacarEnemigo() {
        Arma arma = jugador.getArmaEquipada();
        int daño = 0;

        if (arma == null) {
            daño = jugador.getDañoFisico();
            enemigo.recibirDaño(daño, enemigo.getDefensaBase());
            System.out.println("Atacas con los puños y haces " + daño + " de daño");
            return;
        }

        if (arma instanceof ArmaADistancia) {
            ArmaADistancia arco = (ArmaADistancia) arma;
            if (!arco.tieneFlechas()) {
                System.out.println("¡No tienes flechas!");
                turnoJugador();
                return;
            }
            daño = jugador.getDañoDistancia() + arco.getDañoArma();
            arco.setFlechas(null);
            System.out.println("Disparas con " + arco.getNombre() + " y haces " + daño + " de daño");
        } else if (arma instanceof Baston) {
            Baston baston = (Baston) arma;
            if (jugador.getMana() < 10) {
                System.out.println("¡No tienes mana suficiente!");
                turnoJugador();
                return;
            }
            daño = jugador.getDañoMagico() + baston.getDañoMagico();
            jugador.gastarMana(10);
            System.out.println("Lanzas un hechizo con " + baston.getNombre() + " y haces " + daño + " de daño");
        } else {
            daño = jugador.getDañoFisico() + arma.getDañoArma();
            System.out.println("Atacas con " + arma.getNombre() + " y haces " + daño + " de daño");
        }

        enemigo.recibirDaño(daño, enemigo.getDefensaBase());
    }

    private void cambiarArma() {
        System.out.println("\n--- ARMAS DISPONIBLES ---");
        boolean hayArmas = false;

        for (Objeto obj : inventario.getObjetos().keySet()) {
            if (obj instanceof Arma || obj instanceof Baston) {
                String tipo = "";
                int dañoMostrar = 0;
                if (obj instanceof ArmaADistancia) { tipo = "[Arco]"; dañoMostrar = ((Arma) obj).getDañoArma(); }
                else if (obj instanceof Baston) { tipo = "[Bastón]"; dañoMostrar = ((Baston) obj).getDañoMagico(); }
                else { tipo = "[Espada]"; dañoMostrar = ((Arma) obj).getDañoArma(); }

                System.out.println("ID: " + obj.getIdObjeto() + " - " + obj.getNombre()
                        + " | Daño: " + dañoMostrar + " | Tipo: " + tipo);
                hayArmas = true;
            }
        }

        if (!hayArmas) {
            System.out.println("No tienes armas en el inventario");
            turnoJugador();
            return;
        }

        System.out.println("Introduce el ID:");
        int idArma = teclado.nextInt();
        Objeto objeto = inventario.buscarObjeto(idArma);

        if (objeto instanceof Arma) {
            jugador.equiparArma((Arma) objeto);
            System.out.println("Equipas: " + objeto.getNombre());
        } else {
            System.out.println("ID no válido");
            turnoJugador();
        }
    }

    private void usarPocion() {
        Pocion pocion = buscarPocion();

        if (pocion == null) {
            System.out.println("No tienes pociones");
            turnoJugador();
            return;
        }

        int vidaAntes = jugador.getVidaActual();
        int vidaNueva = Math.min(vidaAntes + pocion.getCuracion(), jugador.getVidaMaxima());
        jugador.setVidaActual(vidaNueva);
        inventario.eliminarObjeto(pocion, 1);
        System.out.println("Usas " + pocion.getNombre() + " y recuperas " + (vidaNueva - vidaAntes) + " de vida");
    }



    private void turnoEnemigo() {
        System.out.println("\n--- Turno de " + enemigo.getNombre() + " ---");
        int defensaJugador = jugador.getDefensaTotal();
        jugador.recibirDaño(enemigo.getDañoTotal(), defensaJugador);
        System.out.println(enemigo.getNombre() + " te ataca");
    }



    private void victoriaJugador() {
        combateActivo = false;
        System.out.println("\n=== VICTORIA ===");
        System.out.println("Has derrotado a " + enemigo.getNombre());
        int xp = calcularXP();
        jugador.ganarExperiencia(xp);
        System.out.println("Ganas " + xp + " de experiencia");
    }

    private void derrotaJugador() {
        combateActivo = false;
        System.out.println("\n=== DERROTA ===");
        System.out.println("Has sido derrotado por " + enemigo.getNombre());
    }

    private int calcularXP() {
        return enemigo.getVidaMaxima() / 2;
    }



    private void mostrarEstado() {
        System.out.println("\n HP: " + jugador.getVidaActual() + "/" + jugador.getVidaMaxima()
                + " | Arma: " + (jugador.getArmaEquipada() != null ? jugador.getArmaEquipada().getNombre() : "Ninguna")
                + " | Enemigo HP: " + enemigo.getVidaActual() + "/" + enemigo.getVidaMaxima());
    }

    private Pocion buscarPocion() {
        for (Objeto obj : inventario.getObjetos().keySet()) {
            if (obj instanceof Pocion) {
                return (Pocion) obj;
            }
        }
        return null;
    }

    private Flechas buscarFlechas() {
        for (Objeto obj : inventario.getObjetos().keySet()) {
            if (obj instanceof Flechas) {
                return (Flechas) obj;
            }
        }
        return null;
    }
}