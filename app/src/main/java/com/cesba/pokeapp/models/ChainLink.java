package com.cesba.pokeapp.models;

import java.util.List;

public class ChainLink {

    private NamedResource species;
    private List<ChainLink> evolves_to;

    public NamedResource getSpecies() {
        return species;
    }

    public List<ChainLink> getEvolves_to() {
        return evolves_to;
    }
}