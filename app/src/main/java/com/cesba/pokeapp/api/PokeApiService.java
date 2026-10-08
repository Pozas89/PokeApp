package com.cesba.pokeapp.api;

import com.cesba.pokeapp.models.EvolutionChainResponse;
import com.cesba.pokeapp.models.MoveDetail;
import com.cesba.pokeapp.models.Pokemon;
import com.cesba.pokeapp.models.PokemonSpecies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface PokeApiService {

    // =========================================================
    // OBTENER POKÉMON
    // =========================================================

    @GET("pokemon/{pokemon}")
    Call<Pokemon> obtenerPokemon(
            @Path("pokemon") String pokemon
    );


    // =========================================================
    // OBTENER ESPECIE
    // =========================================================

    @GET("pokemon-species/{pokemon}")
    Call<PokemonSpecies> obtenerEspecie(
            @Path("pokemon") String pokemon
    );


    // =========================================================
    // OBTENER CADENA EVOLUTIVA
    // =========================================================

    @GET
    Call<EvolutionChainResponse> obtenerCadenaEvolutiva(
            @Url String url
    );


    // =========================================================
    // OBTENER INFORMACIÓN DE UN MOVIMIENTO
    // =========================================================

    @GET("move/{move}")
    Call<MoveDetail> obtenerMovimiento(
            @Path("move") String move
    );
}