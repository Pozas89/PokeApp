package com.cesba.pokeapp.models;

import java.util.List;

public class Pokemon {

    private int id;
    private String name;
    private int height;
    private int weight;

    private Sprites sprites;
    private Cries cries;
    private NamedResource species;

    private List<PokemonType> types;
    private List<PokemonAbility> abilities;
    private List<PokemonStat> stats;

    // NUEVO: movimientos disponibles
    private List<PokemonMove> moves;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public Cries getCries() {
        return cries;
    }

    public NamedResource getSpecies() {
        return species;
    }

    public List<PokemonType> getTypes() {
        return types;
    }

    public List<PokemonAbility> getAbilities() {
        return abilities;
    }

    public List<PokemonStat> getStats() {
        return stats;
    }

    public List<PokemonMove> getMoves() {
        return moves;
    }
}