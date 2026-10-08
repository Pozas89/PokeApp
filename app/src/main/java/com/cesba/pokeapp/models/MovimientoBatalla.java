package com.cesba.pokeapp.models;

public class MovimientoBatalla {

    private String nombre;
    private String tipo;
    private String clase;

    private int poder;
    private int precision;


    public MovimientoBatalla(
            String nombre,
            String tipo,
            String clase,
            int poder,
            int precision
    ) {

        this.nombre = nombre;
        this.tipo = tipo;
        this.clase = clase;
        this.poder = poder;
        this.precision = precision;
    }


    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getClase() {
        return clase;
    }

    public int getPoder() {
        return poder;
    }

    public int getPrecision() {
        return precision;
    }
}