package com.cesba.pokeapp.models;

import java.util.List;

public class PokemonSpecies {

    private String name;

    private NamedResource evolution_chain;

    private List<PokemonVariety> varieties;

    public String getName() {
        return name;
    }

    public NamedResource getEvolution_chain() {
        return evolution_chain;
    }

    public List<PokemonVariety> getVarieties() {
        return varieties;
    }
}