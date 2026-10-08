package com.cesba.pokeapp;

import com.cesba.pokeapp.models.Cries;
import com.cesba.pokeapp.models.PokemonAbility;
import com.cesba.pokeapp.models.PokemonType;

import java.util.List;

public class Pokemon {

    private int id;
    private String name;
    private int height;
    private int weight;

    private Sprites sprites;
    private Cries cries;

    private List<PokemonType> types;
    private List<PokemonAbility> abilities;
    private List<PokemonStat> stats;

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

    public List<PokemonType> getTypes() {
        return types;
    }

    public List<PokemonAbility> getAbilities() {
        return abilities;
    }

    public List<PokemonStat> getStats() {
        return stats;
    }
}