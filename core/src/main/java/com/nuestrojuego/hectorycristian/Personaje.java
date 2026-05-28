package com.nuestrojuego.hectorycristian;

/**
 * El motor de tu héroe. Guarda toda su vida, magia, estadísticas, dinero
 * y se encarga de calcular cuánto daño haces dependiendo de lo que lleves puesto.
 */
public class Personaje extends Entidad {
    private Arma armaEquipada;
    private Armadura armaduraEquipada;
    private int defensaArmadura;
    private int vidaBase;
    private int fuerza;
    private int destreza;
    private int inteligencia;
    private int constitucion;
    private int sabiduria;
    private int suerte;
    private int defensaTotal;
    private int dañoFisico;
    private int dañoMagico;
    private int mana;
    private double evasion;
    private int defensaExtra;
    private int dañoDistancia;
    private int nivel;
    private int experiencia;
    private int puntosAtributo;
    private static Personaje instancia;
    private int monedas;
    private int manaMaximo;

    /**
     * Crea a tu héroe desde cero con las estadísticas iniciales de su clase.
     * @param nombre Como se llama.
     * @param vidaBase La salud inicial.
     * @param fuerza Lo fuerte que pega.
     * @param destreza Su puntería y agilidad.
     * @param inteligencia Su capacidad mágica.
     * @param constitucion Lo duro que es.
     * @param sabiduria Su reserva extra de maná.
     * @param suerte Para sacar críticos y buen botín.
     * @param defensaBase Su armadura natural.
     */
    private Personaje(String nombre, int vidaBase, int fuerza, int destreza, int inteligencia, int constitucion, int sabiduria, int suerte, int defensaBase){
        super(nombre, vidaBase + (constitucion * 5), defensaBase);
        this.vidaBase = vidaBase;
        this.fuerza = fuerza;
        this.destreza = destreza;
        this.inteligencia = inteligencia;
        this.constitucion = constitucion;
        this.sabiduria = sabiduria;
        this.suerte = suerte;
        this.nivel = 1;
        this.experiencia = 0;
        this.puntosAtributo = 0;
        this.monedas = 20;
        this.armaEquipada = null;
        recalcularStats();
        this.mana = this.manaMaximo;
    }

    /**
     * Te pone un arma nueva en las manos y recalcula lo fuerte que pegas.
     * @param arma El arma que te vas a equipar.
     */
    public void equiparArma(Arma arma) {
        this.armaEquipada = arma;
        recalcularStats();
    }

    /**
     * Crea a tu héroe por primera vez si no existía de antes.
     * @param nombre Nombre del héroe.
     * @param vidaBase Salud inicial.
     * @param fuerza Fuerza inicial.
     * @param destreza Destreza inicial.
     * @param inteligencia Inteligencia inicial.
     * @param constitucion Constitución inicial.
     * @param sabiduria Sabiduría inicial.
     * @param suerte Suerte inicial.
     * @param defensaBase Defensa inicial.
     * @return Tu héroe recién creado.
     */
    public static Personaje getInstancia(String nombre, int vidaBase, int fuerza, int destreza, int inteligencia, int constitucion, int sabiduria, int suerte, int defensaBase){
        if (instancia == null) {
            instancia = new Personaje(nombre, vidaBase, fuerza, destreza, inteligencia, constitucion, sabiduria, suerte, defensaBase);
        }
        return instancia;
    }

    /**
     * Llama al héroe que ya está jugando. Si no hay ninguno, da un error.
     * @return Tu héroe actual.
     */
    public static Personaje getInstancia() {
        if (instancia == null) {
            throw new IllegalStateException("El personaje no ha sido creado todavía");
        }
        return instancia;
    }

    /**
     * Resta puntos de magia cuando lanzas un hechizo, evitando que baje de cero.
     * @param cantidad El maná que cuesta el hechizo.
     */
    public void gastarMana(int cantidad) {
        this.mana = Math.max(0, this.mana - cantidad);
    }

    /**
     * La calculadora del juego. Mezcla tus atributos con los bonus de tu armadura
     * para saber exactamente cuánta vida, magia y daño total tienes en este momento.
     */
    private void recalcularStats(){
        super.vidaMaxima = this.vidaBase + (this.constitucion * 5);
        this.manaMaximo = (this.inteligencia * 10) + (this.sabiduria * 5);
        this.dañoFisico = this.fuerza * 2;
        this.dañoMagico = this.inteligencia * 2;
        this.evasion = this.destreza * 0.01;
        this.defensaExtra = (int) Math.floor(this.constitucion / 5);
        this.dañoDistancia = this.destreza * 2;

        if (this.armaduraEquipada != null) {
            this.defensaArmadura = armaduraEquipada.getArmadura();
        } else {
            this.defensaArmadura = 0;
        }

        this.defensaTotal = this.defensaBase + this.defensaExtra + this.defensaArmadura;

        if (this.armaduraEquipada != null) {
            String desc = this.armaduraEquipada.getOriginalDescripcion();
            if (desc.startsWith("BONUS:")) {
                String[] partes = desc.split(":");
                String tipoBonus = partes[1].toUpperCase();
                int valor = Integer.parseInt(partes[2]);

                switch (tipoBonus) {
                    case "VIDA": super.vidaMaxima += (valor * 10); break;
                    case "MANA": this.manaMaximo += (valor * 15); break;
                    case "FUERZA": this.dañoFisico += (valor * 2); break;
                    case "DESTREZA": this.dañoDistancia += (valor * 2); this.evasion += (valor * 0.01f); break;
                    case "INTELIGENCIA": this.dañoMagico += (valor * 2); break;
                }
            }
        }

        if (super.vidaActual > super.vidaMaxima) {
            super.vidaActual = super.vidaMaxima;
        }
        if (this.mana > this.manaMaximo) {
            this.mana = this.manaMaximo;
        }
    }

    /**
     * Te dice cuánto dinero llevas encima.
     * @return Tus monedas.
     */
    public int getMonedas() { return monedas; }

    /**
     * Truca la cantidad de dinero que tienes.
     * @param monedas La cantidad de dinero nueva.
     */
    public void setMonedas(int monedas) { this.monedas = monedas; }

    /**
     * Te mete dinero fresco en el bolsillo.
     * @param cantidad Las monedas que has encontrado.
     */
    public void ganarMonedas(int cantidad) { this.monedas += cantidad; }

    /**
     * Intenta comprar algo. Si tienes dinero te lo resta y te da permiso.
     * @param cantidad Lo que cuesta el objeto.
     * @return Verdadero si has podido pagar, falso si eres pobre.
     */
    public boolean gastarMonedas(int cantidad) {
        if (this.monedas >= cantidad) {
            this.monedas -= cantidad;
            return true;
        }
        return false;
    }

    /**
     * Calcula cuántos puntos te cuesta subir una estadística. Cuanto más alta, más cara.
     * @param valorActual Tu nivel actual en esa estadística.
     * @return Lo que te cuesta subirla un punto más.
     */
    public int calcularCosteAtributo(int valorActual) {
        if (valorActual < 15) return 1;
        if (valorActual < 25) return 2;
        if (valorActual < 35) return 3;
        return 4;
    }

    /**
     * Te chiva qué atributo tienes que subirte para pegar más fuerte con el arma que llevas.
     * @return El nombre del atributo (Fuerza, Destreza o Inteligencia).
     */
    public String getAtributoEscaladoArma() {
        if (armaEquipada == null) return "FUERZA";
        if (armaEquipada instanceof ArmaADistancia) return "DESTREZA";
        if (armaEquipada instanceof Baston) return "INTELIGENCIA";
        return "FUERZA";
    }

    /**
     * Intenta subirte la Fuerza.
     * @return Verdadero si tenías puntos suficientes, falso si no.
     */
    public boolean subirFuerza() {
        int coste = calcularCosteAtributo(this.fuerza);
        if (this.puntosAtributo >= coste) {
            this.puntosAtributo -= coste; this.fuerza++; recalcularStats(); return true;
        }
        return false;
    }

    /**
     * Intenta subirte la Destreza.
     * @return Verdadero si tenías puntos suficientes, falso si no.
     */
    public boolean subirDestreza() {
        int coste = calcularCosteAtributo(this.destreza);
        if (this.puntosAtributo >= coste) {
            this.puntosAtributo -= coste; this.destreza++; recalcularStats(); return true;
        }
        return false;
    }

    /**
     * Intenta subirte la Inteligencia.
     * @return Verdadero si tenías puntos suficientes, falso si no.
     */
    public boolean subirInteligencia() {
        int coste = calcularCosteAtributo(this.inteligencia);
        if (this.puntosAtributo >= coste) {
            this.puntosAtributo -= coste; this.inteligencia++; recalcularStats(); return true;
        }
        return false;
    }

    /**
     * Intenta subirte la Constitución.
     * @return Verdadero si tenías puntos suficientes, falso si no.
     */
    public boolean subirConstitucion() {
        int coste = calcularCosteAtributo(this.constitucion);
        if (this.puntosAtributo >= coste) {
            this.puntosAtributo -= coste; this.constitucion++; recalcularStats(); return true;
        }
        return false;
    }

    /**
     * Intenta subirte la Suerte.
     * @return Verdadero si tenías puntos suficientes, falso si no.
     */
    public boolean subirSuerte() {
        int coste = calcularCosteAtributo(this.suerte);
        if (this.puntosAtributo >= coste) {
            this.puntosAtributo -= coste; this.suerte++; recalcularStats(); return true;
        }
        return false;
    }

    /** Fuerza bruscamente el atributo de fuerza. @param fuerza Nueva fuerza. */
    public void setFuerza(int fuerza) { this.fuerza = fuerza; recalcularStats(); }
    /** Fuerza bruscamente el atributo de destreza. @param destreza Nueva destreza. */
    public void setDestreza(int destreza) { this.destreza = destreza; recalcularStats(); }
    /** Fuerza bruscamente el atributo de inteligencia. @param inteligencia Nueva inteligencia. */
    public void setInteligencia(int inteligencia) { this.inteligencia = inteligencia; recalcularStats(); }
    /** Fuerza bruscamente el atributo de constitucion. @param constitucion Nueva constitucion. */
    public void setConstitucion(int constitucion) { this.constitucion = constitucion; recalcularStats(); }
    /** Fuerza bruscamente el atributo de sabiduria. @param sabiduria Nueva sabiduria. */
    public void setSabiduria(int sabiduria) { this.sabiduria = sabiduria; recalcularStats(); }
    /** Fuerza bruscamente el atributo de suerte. @param suerte Nueva suerte. */
    public void setSuerte(int suerte) { this.suerte = suerte; recalcularStats(); }
    /** Modifica la vida sin armadura. @param vidaBase Nueva vida base. */
    public void setVidaBase(int vidaBase) { this.vidaBase = vidaBase; recalcularStats(); }
    /** Modifica la defensa de fabrica. @param defensaBase Nueva defensa base. */
    public void setDefensaBase(int defensaBase) { this.defensaBase = defensaBase; recalcularStats(); }

    /**
     * Te da experiencia por matar cosas. Si llegas al tope, subes de nivel automáticamente.
     * @param cantidad La experiencia ganada.
     */
    public void ganarExperiencia(int cantidad){
        this.experiencia += cantidad;
        int xpNecesaria = calcularExperienciaNecesaria();
        while (this.experiencia >= xpNecesaria){
            subirDeNivel();
            this.experiencia -= xpNecesaria;
            xpNecesaria = calcularExperienciaNecesaria();
        }
    }

    /**
     * Calcula cuánta experiencia te hace falta sudar para llegar al siguiente nivel.
     * @return Los puntos necesarios.
     */
    public int calcularExperienciaNecesaria(){
        return (int) (this.nivel * 100 * 1.5);
    }

    /**
     * Te sube el nivel y te regala 3 puntos para que los gastes en atributos.
     */
    private void subirDeNivel(){
        this.nivel++;
        this.puntosAtributo += 3;
    }

    /**
     * Intenta ponerte una armadura. Si estás debilucho y no tienes fuerza, no te deja.
     * @param armadura La pieza de armadura a equipar.
     * @return Verdadero si te la has puesto, falso si no tienes fuerza suficiente.
     */
    public boolean equiparArmadura(Armadura armadura) {
        if (this.fuerza >= armadura.getFuerzaMinima()){
            this.armaduraEquipada = armadura;
            recalcularStats();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Te cura heridas con pociones o en el santuario, frenando al llegar a tu salud máxima.
     * @param cantidad Los puntos de vida que recuperas.
     */
    public void curar(int cantidad) {
        this.vidaActual = Math.min(this.vidaMaxima, this.vidaActual + cantidad);
    }

    /** Devuelve tu tope de mana. @return El mana maximo. */
    public int getManaMaximo() {return this.manaMaximo; }
    /** Cambia tu arma bruscamente sin recalcular. @param armaEquipada El arma. */
    public void setArmaEquipada(Arma armaEquipada) { this.armaEquipada = armaEquipada; }
    /** Te chiva que armadura llevas puesta. @return La armadura actual. */
    public Armadura getArmaduraEquipada() { return armaduraEquipada; }
    /** Cambia tu armadura sin chequear si puedes llevarla. @param armaduraEquipada La armadura. */
    public void setArmaduraEquipada(Armadura armaduraEquipada) { this.armaduraEquipada = armaduraEquipada; }
    /** Devuelve lo que te protege tu equipo. @return La armadura. */
    public int getDefensaArmadura() { return defensaArmadura; }
    /** Truca la defensa de tu equipo. @param defensaArmadura Defensa nueva. */
    public void setDefensaArmadura(int defensaArmadura) { this.defensaArmadura = defensaArmadura; }
    /** Devuelve tu vida inicial sin bonos. @return Vida base. */
    public int getVidaBase() { return vidaBase; }
    /** Truca tu escudo total. @param defensaTotal Escudo nuevo. */
    public void setDefensaTotal(int defensaTotal) { this.defensaTotal = defensaTotal; }
    /** Truca tu daño fisico bruto. @param dañoFisico Daño nuevo. */
    public void setDañoFisico(int dañoFisico) { this.dañoFisico = dañoFisico; }
    /** Truca tu magia bruta. @param dañoMagico Magia nueva. */
    public void setDañoMagico(int dañoMagico) { this.dañoMagico = dañoMagico; }
    /** Fuerza tus puntos de mana actuales asegurando que no pases el maximo. @param mana Nuevo mana. */
    public void setMana(int mana) { this.mana = Math.min(mana, this.manaMaximo); }
    /** Truca tu probabilidad de esquivar. @param evasion Nueva probabilidad. */
    public void setEvasion(double evasion) { this.evasion = evasion; }
    /** Devuelve tu aguante natural. @return Defensa extra. */
    public int getDefensaExtra() { return defensaExtra; }
    /** Truca tu aguante natural. @param defensaExtra Aguante nuevo. */
    public void setDefensaExtra(int defensaExtra) { this.defensaExtra = defensaExtra; }
    /** Truca tu punteria lejana. @param dañoDistancia Daño lejano nuevo. */
    public void setDañoDistancia(int dañoDistancia) { this.dañoDistancia = dañoDistancia; }
    /** Te chiva tu nivel actual. @return El nivel. */
    public int getNivel() { return nivel; }
    /** Truca tu nivel actual. @param nivel Nivel nuevo. */
    public void setNivel(int nivel) { this.nivel = nivel; }
    /** Te chiva la experiencia que tienes. @return Puntos de XP. */
    public int getExperiencia() { return experiencia; }
    /** Truca tus puntos de experiencia. @param experiencia Puntos nuevos. */
    public void setExperiencia(int experiencia) { this.experiencia = experiencia; }
    /** Devuelve los puntos libres para subir atributos. @return Puntos libres. */
    public int getPuntosAtributo() { return puntosAtributo; }
    /** Truca los puntos libres para subir cosas. @param puntosAtributo Puntos nuevos. */
    public void setPuntosAtributo(int puntosAtributo) { this.puntosAtributo = puntosAtributo; }
    /** Cambia el heroe entero del juego. @param instancia El heroe nuevo. */
    public static void setInstancia(Personaje instancia) { Personaje.instancia = instancia; }
    /** Cambia tu nombre. @param nombre El nombre nuevo. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** Devuelve tu nombre. @return Nombre. */
    public String getNombre() { return nombre; }
    /** Devuelve tu Fuerza bruta. @return Puntos de STR. */
    public int getFuerza() { return fuerza; }
    /** Devuelve tu Destreza y punteria. @return Puntos de DEX. */
    public int getDestreza() { return destreza; }
    /** Devuelve tu poder cerebral. @return Puntos de INT. */
    public int getInteligencia() { return inteligencia; }
    /** Devuelve tu aguante y salud. @return Puntos de CON. */
    public int getConstitucion() { return constitucion; }
    /** Devuelve tu pozo de mana extra. @return Puntos de WIS. */
    public int getSabiduria() { return sabiduria; }
    /** Devuelve tu capacidad de dar criticos. @return Puntos de LUK. */
    public int getSuerte() { return suerte; }
    /** Devuelve la salud que te queda viva. @return HP actuales. */
    public int getVidaActual() { return super.vidaActual; }
    /** Devuelve los puntos magicos que te quedan. @return MP actuales. */
    public int getMana() { return mana; }
    /** Devuelve la torta que das a mele. @return Daño de cerca. */
    public int getDañoFisico() { return dañoFisico; }
    /** Devuelve el golpe de tus hechizos. @return Daño magico. */
    public int getDañoMagico() { return dañoMagico; }
    /** Devuelve la posibilidad de esquivar golpes. @return Evasion. */
    public double getEvasion() { return evasion; }
    /** Devuelve el escudo total sumando todo. @return Defensa completa. */
    public int getDefensaTotal() { return defensaTotal; }
    /** Te dice el arma que empuñas ahora mismo. @return El arma actual. */
    public Arma getArmaEquipada() { return armaEquipada; }
    /** Devuelve el daño base cuando disparas de lejos. @return Daño lejano. */
    public int getDañoDistancia() { return dañoDistancia; }
}