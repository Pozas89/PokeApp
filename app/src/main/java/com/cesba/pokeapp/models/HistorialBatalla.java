package com.cesba.pokeapp.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

public class HistorialBatalla {

    private String id;
    private String pokemonJugador;
    private String pokemonCpu;
    private String ganador;

    private int hpRestante;
    private int hpMaxGanador;
    private int porcentajeHpRestante;
    private int turnos;

    private String registro;

    private String imagenJugador;
    private String imagenCpu;
    private String imagenGanador;

    private Timestamp fecha;

    public HistorialBatalla() {
        // Constructor vacío requerido por Firestore
    }

    public HistorialBatalla(
            String pokemonJugador,
            String pokemonCpu,
            String ganador,
            int hpRestante,
            int hpMaxGanador,
            int porcentajeHpRestante,
            int turnos,
            String registro,
            String imagenJugador,
            String imagenCpu,
            String imagenGanador,
            Timestamp fecha
    ) {
        this.pokemonJugador = pokemonJugador;
        this.pokemonCpu = pokemonCpu;
        this.ganador = ganador;
        this.hpRestante = hpRestante;
        this.hpMaxGanador = hpMaxGanador;
        this.porcentajeHpRestante = porcentajeHpRestante;
        this.turnos = turnos;
        this.registro = registro;
        this.imagenJugador = imagenJugador;
        this.imagenCpu = imagenCpu;
        this.imagenGanador = imagenGanador;
        this.fecha = fecha;
    }

    @Exclude
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPokemonJugador() {
        return pokemonJugador;
    }

    public void setPokemonJugador(String pokemonJugador) {
        this.pokemonJugador = pokemonJugador;
    }

    public String getPokemonCpu() {
        return pokemonCpu;
    }

    public void setPokemonCpu(String pokemonCpu) {
        this.pokemonCpu = pokemonCpu;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public int getHpRestante() {
        return hpRestante;
    }

    public void setHpRestante(int hpRestante) {
        this.hpRestante = hpRestante;
    }

    public int getHpMaxGanador() {
        return hpMaxGanador;
    }

    public void setHpMaxGanador(int hpMaxGanador) {
        this.hpMaxGanador = hpMaxGanador;
    }

    public int getPorcentajeHpRestante() {
        return porcentajeHpRestante;
    }

    public void setPorcentajeHpRestante(int porcentajeHpRestante) {
        this.porcentajeHpRestante = porcentajeHpRestante;
    }

    public int getTurnos() {
        return turnos;
    }

    public void setTurnos(int turnos) {
        this.turnos = turnos;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getImagenJugador() {
        return imagenJugador;
    }

    public void setImagenJugador(String imagenJugador) {
        this.imagenJugador = imagenJugador;
    }

    public String getImagenCpu() {
        return imagenCpu;
    }

    public void setImagenCpu(String imagenCpu) {
        this.imagenCpu = imagenCpu;
    }

    public String getImagenGanador() {
        return imagenGanador;
    }

    public void setImagenGanador(String imagenGanador) {
        this.imagenGanador = imagenGanador;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}