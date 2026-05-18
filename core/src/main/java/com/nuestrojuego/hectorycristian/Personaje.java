package com.nuestrojuego.hectorycristian;

public class Personaje extends Entidad {
    private Arma armaEquipada;
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
    private  int dañoDistancia;
    private int nivel;
    private int experiencia;
    private int puntosAtributo;
    private static Personaje instancia;

    private Personaje(String nombre, int vidaBase, int fuerza, int destreza, int inteligencia, int constitucion, int sabiduria, int suerte,int defensaBase){
            super(nombre, vidaBase +(constitucion*5),defensaBase);
            this.vidaBase = vidaBase;
            this.fuerza = fuerza;
            this.destreza = destreza;
            this.inteligencia = inteligencia;
            this.constitucion = constitucion;
            this.sabiduria = sabiduria;
            this.suerte = suerte;
            this.nivel =1;
            this.experiencia = 0;
            this.puntosAtributo = 0;
            recalcularStats();
            this.armaEquipada=null;
    }
    public void equiparArma(Arma arma) {
        this.armaEquipada = arma;
        recalcularStats();
    }
    public static Personaje getInstancia(String nombre, int vidaBase, int fuerza, int destreza, int inteligencia, int constitucion, int sabiduria, int suerte,int defensaBase){
        if (instancia==null){
            instancia = new Personaje(nombre, vidaBase, fuerza, destreza, inteligencia, constitucion, sabiduria, suerte, defensaBase);
        }
        return instancia;
    }
    public static Personaje getInstancia() {
        if (instancia == null) throw new IllegalStateException("El personaje no ha sido creado todavía");
        return instancia;
    }
    public void gastarMana(int cantidad) {
        this.mana = Math.max(0, this.mana - cantidad);
    }


    private void recalcularStats(){
        super.vidaMaxima = this.vidaBase + (this.constitucion*5);
        this.mana = (this.inteligencia * 10) + (this.sabiduria * 5);
        this.dañoFisico = this.fuerza * 2;
        this.dañoMagico = this.inteligencia * 2;
        this.evasion = this.destreza * 0.01;
        this.defensaExtra = (int) Math.floor(this.constitucion/5);
        this.dañoDistancia = this.destreza *2;

    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
        recalcularStats();
    }

    public void setDestreza(int destreza) {
        this.destreza = destreza;
        recalcularStats();
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
        recalcularStats();
    }

    public void setConstitucion(int constitucion) {
        this.constitucion = constitucion;
        recalcularStats();
    }

    public void setSabiduria(int sabiduria) {
        this.sabiduria = sabiduria;
        recalcularStats();
    }

    public void setSuerte(int suerte) {
        this.suerte = suerte;
        recalcularStats();
    }

    public void setVidaBase(int vidaBase) {
        this.vidaBase = vidaBase;
        recalcularStats();
    }

    public void setDefensaBase(int defensaBase) {
        this.defensaBase = defensaBase;
        recalcularStats();
    }

    public void ganarExperiencia(int cantidad){
        this.experiencia += cantidad;
        int xpNecesaria = calcularExperienciaNecesaria();

        while (this.experiencia >= xpNecesaria){
            subirDeNivel();
            this.experiencia -= xpNecesaria;
            xpNecesaria = calcularExperienciaNecesaria();
        }
    }

    public int calcularExperienciaNecesaria(){
        return (int) (this.nivel * 100 * 1.5);
    }

    private void subirDeNivel(){
        this.nivel++;
        this.puntosAtributo += 3;

    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public int getFuerza() { return fuerza; }
    public int getDestreza() { return destreza; }
    public int getInteligencia() { return inteligencia; }
    public int getConstitucion() { return constitucion; }
    public int getSabiduria() { return sabiduria; }
    public int getSuerte() { return suerte; }

    public int getVidaActual() { return super.vidaActual; }
    public int getMana() { return mana; }
    public int getDañoFisico() { return dañoFisico; }
    public int getDañoMagico() { return dañoMagico; }
    public double getEvasion() { return evasion; }
    public int getDefensaTotal() { return defensaTotal; }

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public int getDañoDistancia() {
        return dañoDistancia;
    }

}
