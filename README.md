Proyecto desarrollado para segundo de DAM (Desarrollo de Aplicaciones Multiplataforma). Es un juego RPG de mazmorras en 2D con mapas generados de forma aleatoria, sistema de combate por turnos y gestión de inventario.


## Características del juego

* **Generación procedural:** Los pisos y las salas cambian de forma aleatoria en cada partida mediante una semilla.
* **Sistema de clases:** 4 clases para elegir (Guerrero, Mago, Nigromante y Asesino), cada una con sus propios atributos y escalado de daño.
* **Inventario con peso:** Mochila basada en un `HashMap` donde el límite de carga depende de la fuerza del personaje.
* **Combate por turnos:** Sistema de lucha contra enemigos y jefes con cálculo de daño, críticos, esquiva y uso de pociones.
* **Tienda y forja:** Mecánica para gastar monedas conseguidas en combate para craftear equipo o pociones.

## Arquitectura y diseño

* **Patrón Singleton:** Usado en gestores principales como `Personaje`, `Inventario` y `DatosJuego` para tener una única instancia y evitar problemas en memoria.
* **POO (Herencia y Polimorfismo):** 
  * Clase abstracta `Entidad` de la que heredan `Personaje` y `Enemigo`.
  * Jerarquía en los objetos (`Objeto` -> `Arma`, `Armadura`, `Pocion`, `Baston`, etc.).
* **Carga de datos:** Lectura de archivos JSON para cargar las estadísticas de enemigos y objetos.

## Controles

* **Movimiento:** Teclas `W`, `A`, `S`, `D`.
* **Interactuar (cofres, escaleras, altares):** Tecla `E`.
* **Inventario:** Tecla `I` (navegar con `W`/`S`, equipar con `ENTER`, tirar con `P`).
* **Atributos / Stats:** Tecla `O`.
* **Tienda / Forja:** Tecla `T`.

## Tecnologías

* Java 17
* libGDX & mini2Dx
* Gradle

## Autores

* Cristian
* Héctor
