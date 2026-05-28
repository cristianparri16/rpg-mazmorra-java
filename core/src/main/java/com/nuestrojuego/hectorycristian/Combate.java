package com.nuestrojuego.hectorycristian;

import java.util.Scanner;

/**
 * Gestiona la logica de combate por turnos entre el jugador y un enemigo.
 */
public class Combate {
    private Personaje jugador;
    private Enemigo enemigo;
    private Inventario inventario;
    private Scanner teclado;
    private boolean combateActivo;

    /**
     * Inicializa un nuevo combate contra un enemigo especifico.
     *
     * @param enemigo El enemigo al que se enfrentara el jugador.
     */
    public Combate(Enemigo enemigo) {
        this.jugador = Personaje.getInstancia();
        this.enemigo = enemigo;
        this.inventario = Inventario.getInstancia(jugador);
        this.teclado = new Scanner(System.in);
        this.combateActivo = true;
    }

    /**
     * Inicia el bucle de combate alternando turnos hasta que uno de los dos muera.
     */
    public void iniciarCombate() {
        while (combateActivo) {
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

    /**
     * Pide una accion al jugador y ejecuta la opcion seleccionada.
     */
    private void turnoJugador() {
        int opcion = 0;
        do {
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
            }
        } while (opcion < 1 || opcion > 3);
    }

    /**
     * Calcula y aplica el daño al enemigo dependiendo del arma equipada.
     */
    private void atacarEnemigo() {
        Arma arma = jugador.getArmaEquipada();
        int daño = 0;

        if (arma == null) {
            daño = jugador.getDañoFisico();
            enemigo.recibirDaño(daño, enemigo.getDefensaBase());
            return;
        }

        if (arma instanceof ArmaADistancia) {
            ArmaADistancia arco = (ArmaADistancia) arma;
            if (!arco.tieneFlechas()) {
                turnoJugador();
                return;
            }
            daño = jugador.getDañoDistancia() + arco.getDañoArma();
            arco.setFlechas(null);
        } else if (arma instanceof Baston) {
            Baston baston = (Baston) arma;
            if (jugador.getMana() < 10) {
                turnoJugador();
                return;
            }
            daño = jugador.getDañoMagico() + baston.getDañoMagico();
            jugador.gastarMana(10);
        } else {
            daño = jugador.getDañoFisico() + arma.getDañoArma();
        }

        enemigo.recibirDaño(daño, enemigo.getDefensaBase());
    }

    /**
     * Permite al jugador equipar un arma diferente desde su inventario.
     */
    private void cambiarArma() {
        boolean hayArmas = false;

        for (Objeto obj : inventario.getObjetos().keySet()) {
            if (obj instanceof Arma || obj instanceof Baston) {
                hayArmas = true;
            }
        }

        if (!hayArmas) {
            turnoJugador();
            return;
        }

        int idArma = teclado.nextInt();
        Objeto objeto = inventario.buscarObjeto(idArma);

        if (objeto instanceof Arma) {
            jugador.equiparArma((Arma) objeto);
        } else {
            turnoJugador();
        }
    }

    /**
     * Consume la primera pocion disponible en el inventario para recuperar salud.
     */
    private void usarPocion() {
        Pocion pocion = buscarPocion();

        if (pocion == null) {
            turnoJugador();
            return;
        }

        int vidaAntes = jugador.getVidaActual();
        int vidaNueva = Math.min(vidaAntes + pocion.getCuracion(), jugador.getVidaMaxima());
        jugador.setVidaActual(vidaNueva);
        inventario.eliminarObjeto(pocion, 1);
    }

    /**
     * Aplica el daño correspondiente del enemigo al jugador.
     */
    private void turnoEnemigo() {
        jugador.recibirDaño(enemigo.getDañoTotal(), jugador.getDefensaTotal());
    }

    /**
     * Cierra el combate con exito y otorga experiencia al jugador.
     */
    private void victoriaJugador() {
        combateActivo = false;
        jugador.ganarExperiencia(enemigo.getVidaMaxima() / 2);
    }

    /**
     * Cierra el combate tras la muerte del jugador.
     */
    private void derrotaJugador() {
        combateActivo = false;
    }

    /**
     * Busca una pocion en el inventario del jugador.
     *
     * @return El objeto Pocion encontrado, o null si no hay pociones.
     */
    private Pocion buscarPocion() {
        for (Objeto obj : inventario.getObjetos().keySet()) {
            if (obj instanceof Pocion) {
                return (Pocion) obj;
            }
        }
        return null;
    }

    /**
     * Busca municion de flechas en el inventario del jugador.
     *
     * @return El objeto Flechas encontrado, o null si no hay flechas.
     */
    private Flechas buscarFlechas() {
        for (Objeto obj : inventario.getObjetos().keySet()) {
            if (obj instanceof Flechas) {
                return (Flechas) obj;
            }
        }
        return null;
    }
}