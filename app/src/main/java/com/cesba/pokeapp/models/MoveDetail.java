package com.cesba.pokeapp.models;

public class MoveDetail {

    private int id;
    private String name;

    private Integer power;
    private Integer accuracy;

    private NamedResource type;
    private NamedResource damage_class;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPower() {
        return power;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public NamedResource getType() {
        return type;
    }

    public NamedResource getDamage_class() {
        return damage_class;
    }
}