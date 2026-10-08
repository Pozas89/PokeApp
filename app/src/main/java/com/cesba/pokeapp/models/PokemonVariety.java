package com.cesba.pokeapp.models;

public class PokemonVariety {

    private boolean is_default;
    private NamedResource pokemon;

    public boolean isDefault() {
        return is_default;
    }

    public NamedResource getPokemon() {
        return pokemon;
    }
}