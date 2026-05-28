package com.nuestrojuego.hectorycristian;

/**
 * Se encarga de fabricar al heroe al principio de la partida.
 * Como ya no usamos la consola, ahora recibe los datos directamente del menu.
 */
public class CreadorPersonaje {

    /**
     * Prepara el creador de personajes.
     */
    public CreadorPersonaje() {
    }

    /**
     * Crea y guarda a tu personaje con las estadisticas base de la profesion que hayas elegido.
     *
     * @param nombre El nombre que le quieres poner a tu heroe.
     * @param clase El numero de la clase (1=Guerrero, 2=Mago, 3=Nigromante, 4=Asesino).
     */
    public void crearPersonaje(String nombre, int clase) {
        switch (clase) {
            case 1:
                Personaje.getInstancia(nombre, 100, 15, 10, 5, 15, 7, 8, 8);
                break;
            case 2:
                Personaje.getInstancia(nombre, 50, 5, 8, 16, 7, 14, 10, 0);
                break;
            case 3:
                Personaje.getInstancia(nombre, 70, 7, 7, 14, 11, 12, 9, 4);
                break;
            case 4:
                Personaje.getInstancia(nombre, 80, 9, 16, 7, 9, 7, 12, 2);
                break;
        }
    }
}