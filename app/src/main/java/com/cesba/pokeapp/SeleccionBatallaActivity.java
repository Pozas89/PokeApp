package com.cesba.pokeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cesba.pokeapp.api.ApiClient;
import com.cesba.pokeapp.api.PokeApiService;
import com.cesba.pokeapp.models.Pokemon;
import com.cesba.pokeapp.models.PokemonStat;
import com.cesba.pokeapp.models.PokemonType;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeleccionBatallaActivity extends AppCompatActivity {

    private ImageButton btnRegresar;

    private EditText etPokemon1;
    private EditText etPokemon2;

    private Button btnBuscarPokemon1;
    private Button btnBuscarPokemon2;
    private Button btnIniciarBatalla;

    private ImageView imgPokemon1;
    private ImageView imgPokemon2;

    private TextView tvNombrePokemon1;
    private TextView tvNombrePokemon2;

    private TextView tvTipoPokemon1;
    private TextView tvTipoPokemon2;

    private TextView tvStatsPokemon1;
    private TextView tvStatsPokemon2;

    private ProgressBar progresoPokemon1;
    private ProgressBar progresoPokemon2;

    private PokeApiService pokeApiService;

    private Pokemon pokemon1;
    private Pokemon pokemon2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_seleccion_batalla
        );


        btnRegresar =
                findViewById(R.id.btnRegresarBatalla);

        etPokemon1 =
                findViewById(R.id.etPokemon1);

        etPokemon2 =
                findViewById(R.id.etPokemon2);

        btnBuscarPokemon1 =
                findViewById(R.id.btnBuscarPokemon1);

        btnBuscarPokemon2 =
                findViewById(R.id.btnBuscarPokemon2);

        btnIniciarBatalla =
                findViewById(R.id.btnIniciarBatalla);

        imgPokemon1 =
                findViewById(R.id.imgPokemon1);

        imgPokemon2 =
                findViewById(R.id.imgPokemon2);

        tvNombrePokemon1 =
                findViewById(R.id.tvNombrePokemon1);

        tvNombrePokemon2 =
                findViewById(R.id.tvNombrePokemon2);

        tvTipoPokemon1 =
                findViewById(R.id.tvTipoPokemon1);

        tvTipoPokemon2 =
                findViewById(R.id.tvTipoPokemon2);

        tvStatsPokemon1 =
                findViewById(R.id.tvStatsPokemon1);

        tvStatsPokemon2 =
                findViewById(R.id.tvStatsPokemon2);

        progresoPokemon1 =
                findViewById(R.id.progresoPokemon1);

        progresoPokemon2 =
                findViewById(R.id.progresoPokemon2);


        pokeApiService =
                ApiClient
                        .getClient()
                        .create(PokeApiService.class);


        btnRegresar.setOnClickListener(
                v -> finish()
        );


        btnBuscarPokemon1.setOnClickListener(
                v -> buscarPokemon(1)
        );


        btnBuscarPokemon2.setOnClickListener(
                v -> buscarPokemon(2)
        );


        btnIniciarBatalla.setOnClickListener(
                v -> iniciarBatalla()
        );
    }


    // =========================================================
    // BUSCAR POKÉMON
    // =========================================================

    private void buscarPokemon(int numero) {

        EditText campo;

        if (numero == 1) {
            campo = etPokemon1;
        } else {
            campo = etPokemon2;
        }


        String busqueda =
                campo
                        .getText()
                        .toString()
                        .trim()
                        .toLowerCase(Locale.ROOT);


        if (busqueda.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingresa el nombre o ID del Pokémon",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        mostrarCarga(
                numero,
                true
        );


        pokeApiService
                .obtenerPokemon(busqueda)
                .enqueue(
                        new Callback<Pokemon>() {

                            @Override
                            public void onResponse(
                                    Call<Pokemon> call,
                                    Response<Pokemon> response) {

                                mostrarCarga(
                                        numero,
                                        false
                                );


                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    Pokemon pokemon =
                                            response.body();


                                    if (numero == 1) {

                                        pokemon1 =
                                                pokemon;

                                    } else {

                                        pokemon2 =
                                                pokemon;
                                    }


                                    mostrarPokemon(
                                            numero,
                                            pokemon
                                    );


                                } else {

                                    Toast.makeText(
                                            SeleccionBatallaActivity.this,
                                            "Pokémon inexistente",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<Pokemon> call,
                                    Throwable t) {

                                mostrarCarga(
                                        numero,
                                        false
                                );


                                Toast.makeText(
                                        SeleccionBatallaActivity.this,
                                        "Error al consultar PokéAPI",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );
    }


    // =========================================================
    // MOSTRAR POKÉMON
    // =========================================================

    private void mostrarPokemon(
            int numero,
            Pokemon pokemon
    ) {

        ImageView imagen;

        TextView nombre;
        TextView tipo;
        TextView stats;


        if (numero == 1) {

            imagen =
                    imgPokemon1;

            nombre =
                    tvNombrePokemon1;

            tipo =
                    tvTipoPokemon1;

            stats =
                    tvStatsPokemon1;

        } else {

            imagen =
                    imgPokemon2;

            nombre =
                    tvNombrePokemon2;

            tipo =
                    tvTipoPokemon2;

            stats =
                    tvStatsPokemon2;
        }


        nombre.setText(
                pokemon
                        .getName()
                        .toUpperCase(Locale.ROOT)
        );


        tipo.setText(
                obtenerTipos(pokemon)
        );


        int hp =
                obtenerStat(
                        pokemon,
                        "hp"
                );

        int ataque =
                obtenerStat(
                        pokemon,
                        "attack"
                );

        int defensa =
                obtenerStat(
                        pokemon,
                        "defense"
                );

        int velocidad =
                obtenerStat(
                        pokemon,
                        "speed"
                );


        stats.setText(
                "HP " + hp
                        + "   •   ATK " + ataque
                        + "\nDEF " + defensa
                        + "   •   SPD " + velocidad
        );


        if (pokemon.getSprites() != null
                && pokemon
                .getSprites()
                .getFront_default() != null) {

            Glide.with(this)
                    .load(
                            pokemon
                                    .getSprites()
                                    .getFront_default()
                    )
                    .into(imagen);
        }
    }


    // =========================================================
    // OBTENER TIPOS
    // =========================================================

    private String obtenerTipos(
            Pokemon pokemon
    ) {

        if (pokemon.getTypes() == null
                || pokemon
                .getTypes()
                .isEmpty()) {

            return "SIN TIPO";
        }


        StringBuilder resultado =
                new StringBuilder();


        for (PokemonType tipo
                : pokemon.getTypes()) {

            if (tipo.getType() == null
                    || tipo
                    .getType()
                    .getName() == null) {

                continue;
            }


            if (resultado.length() > 0) {

                resultado.append(" / ");
            }


            resultado.append(
                    tipo
                            .getType()
                            .getName()
                            .toUpperCase(Locale.ROOT)
            );
        }


        return resultado.toString();
    }


    // =========================================================
    // OBTENER STAT
    // =========================================================

    private int obtenerStat(
            Pokemon pokemon,
            String nombreStat
    ) {

        if (pokemon.getStats() == null) {

            return 0;
        }


        for (PokemonStat stat
                : pokemon.getStats()) {


            if (stat.getStat() != null
                    && nombreStat.equals(
                    stat
                            .getStat()
                            .getName()
            )) {

                return stat.getBase_stat();
            }
        }


        return 0;
    }


    // =========================================================
    // CARGA
    // =========================================================

    private void mostrarCarga(
            int numero,
            boolean mostrar
    ) {

        if (numero == 1) {

            progresoPokemon1.setVisibility(
                    mostrar
                            ? View.VISIBLE
                            : View.GONE
            );

        } else {

            progresoPokemon2.setVisibility(
                    mostrar
                            ? View.VISIBLE
                            : View.GONE
            );
        }
    }


    // =========================================================
    // INICIAR BATALLA
    // =========================================================

    private void iniciarBatalla() {

        // Campos vacíos
        if (etPokemon1
                .getText()
                .toString()
                .trim()
                .isEmpty()
                || etPokemon2
                .getText()
                .toString()
                .trim()
                .isEmpty()) {

            Toast.makeText(
                    this,
                    "Selecciona ambos Pokémon",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // No fueron consultados correctamente
        if (pokemon1 == null
                || pokemon2 == null) {

            Toast.makeText(
                    this,
                    "Primero consulta ambos Pokémon",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Mismo Pokémon
        if (pokemon1.getId()
                == pokemon2.getId()) {

            Toast.makeText(
                    this,
                    "No puedes enfrentar al mismo Pokémon",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        Intent intent =
                new Intent(
                        SeleccionBatallaActivity.this,
                        BatallaActivity.class
                );


        intent.putExtra(
                "pokemon1",
                pokemon1.getName()
        );


        intent.putExtra(
                "pokemon2",
                pokemon2.getName()
        );


        startActivity(intent);
    }
}