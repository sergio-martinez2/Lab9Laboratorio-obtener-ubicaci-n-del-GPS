package com.example.login_php.objetos;

public class Carga {
    // Atributos
    private int id;
    private String nombreDeCarga;
    private int pesoEnToneladas;
    private String ciudadDeOrigen;
    private String ciudadDeDestino;
    private String estadoDeCarga;
    private String creadorDeCarga; // Nuevo campo para el creador de la carga

    // Constructor
    public Carga(int id, String nombreDeCarga, int pesoEnToneladas, String ciudadDeOrigen, String ciudadDeDestino, String estadoDeCarga, String creadorDeCarga) {
        this.id = id;
        this.nombreDeCarga = nombreDeCarga;
        this.pesoEnToneladas = pesoEnToneladas;
        this.ciudadDeOrigen = ciudadDeOrigen;
        this.ciudadDeDestino = ciudadDeDestino;
        this.estadoDeCarga = estadoDeCarga;
        this.creadorDeCarga = creadorDeCarga; // Asignar el creador de la carga
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreDeCarga() {
        return nombreDeCarga;
    }

    public void setNombreDeCarga(String nombreDeCarga) {
        this.nombreDeCarga = nombreDeCarga;
    }

    public int getPesoEnToneladas() {
        return pesoEnToneladas;
    }

    public void setPesoEnToneladas(int pesoEnToneladas) {
        this.pesoEnToneladas = pesoEnToneladas;
    }

    public String getCiudadDeOrigen() {
        return ciudadDeOrigen;
    }

    public void setCiudadDeOrigen(String ciudadDeOrigen) {
        this.ciudadDeOrigen = ciudadDeOrigen;
    }

    public String getCiudadDeDestino() {
        return ciudadDeDestino;
    }

    public void setCiudadDeDestino(String ciudadDeDestino) {
        this.ciudadDeDestino = ciudadDeDestino;
    }

    public String getEstadoDeCarga() {
        return estadoDeCarga;
    }

    public void setEstadoDeCarga(String estadoDeCarga) {
        this.estadoDeCarga = estadoDeCarga;
    }

    // Agregar getters y setters para el campo "creador de la carga"
    public String getCreadorDeCarga() {
        return creadorDeCarga;
    }

    public void setCreadorDeCarga(String creadorDeCarga) {
        this.creadorDeCarga = creadorDeCarga;
    }

    // Método toString para representar la carga como una cadena de texto
    @Override
    public String toString() {
        return "ID: " + id +
                ", Nombre: " + nombreDeCarga + ", Peso: " + pesoEnToneladas +"(Ton)" + ", Origen: " + ciudadDeOrigen + ", Destino: " + ciudadDeDestino + ", Estado: " + estadoDeCarga + ", Creador: " + creadorDeCarga ;
    }
}

