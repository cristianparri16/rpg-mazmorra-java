package com.nuestrojuego.hectorycristian;

import java.util.Scanner;
public class CreadorPersonaje {
    private Scanner teclado;

    public CreadorPersonaje() {
        teclado = new Scanner(System.in);
    }

    public void crearPersonaje() {
        System.out.println("Nombre del personaje: ");
        String nombre = teclado.nextLine();
        int clase=0;
        do {
            System.out.println("Clase del personaje: \n" +
                    "1 - Guerrero \n" +
                    "2 - Mago \n" +
                    "3 - Nigromante\n" +
                    "4 - Asesino");
            clase = teclado.nextInt();
            int vida,defensaBase,fuerza, inteligencia, destreza, constitucion, sabiduria, suerte;
            switch (clase) {
                case 1:
                    vida=100;
                    defensaBase=8;
                    fuerza = 15;
                    destreza = 10;
                    inteligencia = 5;
                    constitucion = 15;
                    sabiduria = 7;
                    suerte = 8;
                    Personaje.getInstancia(nombre, vida, fuerza, destreza, inteligencia, constitucion, sabiduria, suerte, defensaBase);
                    break;
                case 2:
                    vida=50;
                    defensaBase=0;
                    fuerza = 5;
                    destreza = 8;
                    inteligencia = 16;
                    constitucion = 7;
                    sabiduria = 14;
                    suerte = 10;
                    Personaje.getInstancia(nombre, vida, fuerza, destreza, inteligencia, constitucion, sabiduria, suerte, defensaBase);
                    break;
                case 3:
                    vida=70;
                    defensaBase=4;
                    fuerza = 7;
                    destreza = 7;
                    inteligencia = 14;
                    constitucion = 11;
                    sabiduria = 12;
                    suerte = 9;
                    Personaje.getInstancia(nombre, vida, fuerza, destreza, inteligencia, constitucion, sabiduria, suerte, defensaBase);
                    break;
                case 4:
                    vida=80;
                    defensaBase=2;
                    fuerza = 9;
                    destreza = 16;
                    inteligencia = 7;
                    constitucion = 9;
                    sabiduria = 7;
                    suerte = 12;
                    Personaje.getInstancia(nombre, vida, fuerza, destreza, inteligencia, constitucion, sabiduria, suerte, defensaBase);
                    break;
                default:
                    System.out.println("No has seleccionado una clase");
            }

        } while(clase>4 || clase<1);
    }
    }
