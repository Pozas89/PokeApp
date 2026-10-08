package com.cesba.pokeapp;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.cesba.pokeapp.adapters.PokemonImageAdapter;
import com.cesba.pokeapp.api.ApiClient;
import com.cesba.pokeapp.api.PokeApiService;
import com.cesba.pokeapp.models.ChainLink;
import com.cesba.pokeapp.models.EvolutionChainResponse;
import com.cesba.pokeapp.models.Pokemon;
import com.cesba.pokeapp.models.PokemonAbility;
import com.cesba.pokeapp.models.PokemonSpecies;
import com.cesba.pokeapp.models.PokemonStat;
import com.cesba.pokeapp.models.PokemonType;
import com.cesba.pokeapp.models.PokemonVariety;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokedexActivity extends AppCompatActivity {

    // =========================================================
    // BUSCADOR
    // =========================================================

    private EditText etBuscarPokemon;
    private Button btnBuscar;

    // =========================================================
    // BOTONES
    // =========================================================

    private ImageButton btnRegresar;
    private ImageButton btnCry;
    private TextView btnFavorito;

    // =========================================================
    // INFORMACIÓN DEL POKÉMON
    // =========================================================

    private TextView tvNumeroPokemon;
    private TextView tvNombrePokemon;
    private TextView tvFormaActual;

    private TextView tvTipo1;
    private TextView tvTipo2;

    private TextView tvAltura;
    private TextView tvPeso;
    private TextView tvHabilidades;

    // =========================================================
    // IMAGEN NORMAL / SHINY
    // =========================================================

    private ViewPager2 viewPagerPokemon;
    private TextView tvModoImagen;

    // =========================================================
    // ESTADÍSTICAS
    // =========================================================

    private TextView tvHP;
    private TextView tvAtaque;
    private TextView tvDefensa;
    private TextView tvAtaqueEspecial;
    private TextView tvDefensaEspecial;
    private TextView tvVelocidad;
    private TextView tvTotalStats;

    private ProgressBar barHP;
    private ProgressBar barAtaque;
    private ProgressBar barDefensa;
    private ProgressBar barAtaqueEspecial;
    private ProgressBar barDefensaEspecial;
    private ProgressBar barVelocidad;

    // =========================================================
    // EVOLUCIONES
    // =========================================================

    private LinearLayout layoutEvoluciones;

    // =========================================================
    // FORMAS
    // =========================================================

    private LinearLayout seccionFormasEspeciales;
    private LinearLayout layoutFormasEspeciales;

    // =========================================================
    // API
    // =========================================================

    private PokeApiService pokeApiService;

    // =========================================================
    // AUDIO
    // =========================================================

    private MediaPlayer mediaPlayer;
    private String urlCry;

    // =========================================================
    // POKÉMON ACTUAL
    // =========================================================

    private String pokemonActual = "";


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pokedex);


        // =====================================================
        // BUSCADOR
        // =====================================================

        etBuscarPokemon =
                findViewById(R.id.etBuscarPokemon);

        btnBuscar =
                findViewById(R.id.btnBuscar);


        // =====================================================
        // BOTONES
        // =====================================================

        btnRegresar =
                findViewById(R.id.btnRegresar);

        btnCry =
                findViewById(R.id.btnCry);

        btnFavorito =
                findViewById(R.id.btnFavorito);


        // =====================================================
        // INFORMACIÓN
        // =====================================================

        tvNumeroPokemon =
                findViewById(R.id.tvNumeroPokemon);

        tvNombrePokemon =
                findViewById(R.id.tvNombrePokemon);

        tvFormaActual =
                findViewById(R.id.tvFormaActual);

        tvTipo1 =
                findViewById(R.id.tvTipo1);

        tvTipo2 =
                findViewById(R.id.tvTipo2);

        tvAltura =
                findViewById(R.id.tvAltura);

        tvPeso =
                findViewById(R.id.tvPeso);

        tvHabilidades =
                findViewById(R.id.tvHabilidades);


        // =====================================================
        // IMAGEN
        // =====================================================

        viewPagerPokemon =
                findViewById(R.id.viewPagerPokemon);

        tvModoImagen =
                findViewById(R.id.tvModoImagen);


        // =====================================================
        // ESTADÍSTICAS
        // =====================================================

        tvHP =
                findViewById(R.id.tvHP);

        tvAtaque =
                findViewById(R.id.tvAtaque);

        tvDefensa =
                findViewById(R.id.tvDefensa);

        tvAtaqueEspecial =
                findViewById(R.id.tvAtaqueEspecial);

        tvDefensaEspecial =
                findViewById(R.id.tvDefensaEspecial);

        tvVelocidad =
                findViewById(R.id.tvVelocidad);

        tvTotalStats =
                findViewById(R.id.tvTotalStats);


        barHP =
                findViewById(R.id.barHP);

        barAtaque =
                findViewById(R.id.barAtaque);

        barDefensa =
                findViewById(R.id.barDefensa);

        barAtaqueEspecial =
                findViewById(R.id.barAtaqueEspecial);

        barDefensaEspecial =
                findViewById(R.id.barDefensaEspecial);

        barVelocidad =
                findViewById(R.id.barVelocidad);


        // =====================================================
        // EVOLUCIONES
        // =====================================================

        layoutEvoluciones =
                findViewById(R.id.layoutEvoluciones);


        // =====================================================
        // FORMAS DISPONIBLES
        // =====================================================

        seccionFormasEspeciales =
                findViewById(R.id.seccionFormasEspeciales);

        layoutFormasEspeciales =
                findViewById(R.id.layoutFormasEspeciales);


        // =====================================================
        // API
        // =====================================================

        pokeApiService =
                ApiClient
                        .getClient()
                        .create(PokeApiService.class);


        // =====================================================
        // BOTÓN REGRESAR
        // =====================================================

        btnRegresar.setOnClickListener(
                v -> finish()
        );


        // =====================================================
        // BOTÓN BUSCAR
        // =====================================================

        btnBuscar.setOnClickListener(v -> {

            String nombre =
                    etBuscarPokemon
                            .getText()
                            .toString()
                            .trim()
                            .toLowerCase(Locale.ROOT);


            if (nombre.isEmpty()) {

                Toast.makeText(
                        PokedexActivity.this,
                        "Escribe el nombre o número de un Pokémon",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            buscarPokemon(nombre);
        });


        // =====================================================
        // BOTÓN CRY
        // =====================================================

        btnCry.setOnClickListener(
                v -> reproducirCry()
        );


        // =====================================================
        // CAMBIO NORMAL / SHINY
        // =====================================================

        viewPagerPokemon.registerOnPageChangeCallback(

                new ViewPager2.OnPageChangeCallback() {

                    @Override
                    public void onPageSelected(int position) {

                        super.onPageSelected(position);


                        if (position == 0) {

                            tvModoImagen.setText(
                                    "● ○   NORMAL"
                            );

                        } else {

                            tvModoImagen.setText(
                                    "○ ●   SHINY"
                            );
                        }
                    }
                }
        );


        // =====================================================
        // POKÉMON ALEATORIO AL ABRIR LA POKÉDEX
        // =====================================================

        cargarPokemonAleatorio();
    }


    // =========================================================
    // POKÉMON ALEATORIO
    // =========================================================

    private void cargarPokemonAleatorio() {

        Random random =
                new Random();


        // Pokémon #1 hasta #1025
        int idPokemon =
                random.nextInt(1025) + 1;


        buscarPokemon(
                String.valueOf(idPokemon)
        );
    }


    // =========================================================
    // BUSCAR POKÉMON
    // =========================================================

    private void buscarPokemon(String nombre) {

        detenerCry();


        pokeApiService
                .obtenerPokemon(nombre)
                .enqueue(
                        new Callback<Pokemon>() {

                            @Override
                            public void onResponse(
                                    Call<Pokemon> call,
                                    Response<Pokemon> response) {


                                if (response.isSuccessful()
                                        && response.body() != null) {


                                    Pokemon pokemon =
                                            response.body();


                                    // =================================
                                    // MOSTRAR DATOS
                                    // =================================

                                    mostrarPokemon(
                                            pokemon
                                    );


                                    // =================================
                                    // OBTENER ESPECIE BASE
                                    // =================================

                                    String especieBase;


                                    if (pokemon.getSpecies() != null
                                            && pokemon
                                            .getSpecies()
                                            .getName() != null
                                            && !pokemon
                                            .getSpecies()
                                            .getName()
                                            .isEmpty()) {


                                        especieBase =
                                                pokemon
                                                        .getSpecies()
                                                        .getName();


                                    } else {


                                        especieBase =
                                                pokemon.getName();

                                    }


                                    // =================================
                                    // EVOLUCIONES + FORMAS
                                    // =================================

                                    buscarEvoluciones(
                                            especieBase
                                    );


                                } else {


                                    Toast.makeText(
                                            PokedexActivity.this,
                                            "Pokémon no encontrado",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<Pokemon> call,
                                    Throwable t) {


                                Toast.makeText(
                                        PokedexActivity.this,
                                        "Error de conexión",
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
            Pokemon pokemon
    ) {

        // Guardar Pokémon actual
        pokemonActual =
                pokemon.getName();


        // =====================================================
        // MOSTRAR NOMBRE EN EL BUSCADOR
        // =====================================================

        etBuscarPokemon.setText(
                pokemon.getName()
        );


        // =====================================================
        // NÚMERO
        // =====================================================

        tvNumeroPokemon.setText(

                String.format(
                        Locale.ROOT,
                        "#%03d",
                        pokemon.getId()
                )

        );


        // =====================================================
        // NOMBRE
        // =====================================================

        tvNombrePokemon.setText(

                formatearNombreForma(
                        pokemon.getName()
                ).toUpperCase(
                        Locale.ROOT
                )

        );


        // =====================================================
        // FORMA ACTUAL
        // =====================================================

        mostrarFormaActual(
                pokemon
        );


        // =====================================================
        // ALTURA
        // =====================================================

        double altura =
                pokemon.getHeight()
                        / 10.0;


        tvAltura.setText(

                String.format(
                        Locale.ROOT,
                        "%.1f m",
                        altura
                )

        );


        // =====================================================
        // PESO
        // =====================================================

        double peso =
                pokemon.getWeight()
                        / 10.0;


        tvPeso.setText(

                String.format(
                        Locale.ROOT,
                        "%.1f kg",
                        peso
                )

        );


        // =====================================================
        // RESTO DE INFORMACIÓN
        // =====================================================

        mostrarImagenes(
                pokemon
        );


        mostrarTipos(
                pokemon
        );


        mostrarHabilidades(
                pokemon
        );


        mostrarEstadisticas(
                pokemon
        );


        obtenerCry(
                pokemon
        );
    }


    // =========================================================
    // IMÁGENES NORMAL / SHINY
    // =========================================================

    private void mostrarImagenes(
            Pokemon pokemon
    ) {

        List<String> imagenes =
                new ArrayList<>();


        if (pokemon.getSprites() != null) {


            // =============================================
            // NORMAL
            // =============================================

            if (pokemon
                    .getSprites()
                    .getFront_default() != null) {


                imagenes.add(

                        pokemon
                                .getSprites()
                                .getFront_default()

                );
            }


            // =============================================
            // SHINY
            // =============================================

            if (pokemon
                    .getSprites()
                    .getFront_shiny() != null) {


                imagenes.add(

                        pokemon
                                .getSprites()
                                .getFront_shiny()

                );
            }
        }


        PokemonImageAdapter adapter =
                new PokemonImageAdapter(
                        imagenes
                );


        viewPagerPokemon.setAdapter(
                adapter
        );


        viewPagerPokemon.setCurrentItem(
                0,
                false
        );


        if (imagenes.size() > 1) {


            tvModoImagen.setText(
                    "● ○   NORMAL   •   DESLIZA PARA SHINY"
            );


        } else {


            tvModoImagen.setText(
                    "●   NORMAL"
            );

        }
    }


    // =========================================================
    // TIPOS
    // =========================================================

    private void mostrarTipos(
            Pokemon pokemon
    ) {

        tvTipo1.setVisibility(
                View.GONE
        );


        tvTipo2.setVisibility(
                View.GONE
        );


        if (pokemon.getTypes() == null) {

            return;
        }


        List<PokemonType> tipos =
                pokemon.getTypes();


        // =====================================================
        // TIPO 1
        // =====================================================

        if (tipos.size() >= 1
                && tipos
                .get(0)
                .getType() != null) {


            tvTipo1.setText(

                    tipos
                            .get(0)
                            .getType()
                            .getName()
                            .toUpperCase(
                                    Locale.ROOT
                            )

            );


            tvTipo1.setVisibility(
                    View.VISIBLE
            );
        }


        // =====================================================
        // TIPO 2
        // =====================================================

        if (tipos.size() >= 2
                && tipos
                .get(1)
                .getType() != null) {


            tvTipo2.setText(

                    tipos
                            .get(1)
                            .getType()
                            .getName()
                            .toUpperCase(
                                    Locale.ROOT
                            )

            );


            tvTipo2.setVisibility(
                    View.VISIBLE
            );
        }
    }


    // =========================================================
    // HABILIDADES
    // =========================================================

    private void mostrarHabilidades(
            Pokemon pokemon
    ) {

        if (pokemon.getAbilities() == null
                || pokemon
                .getAbilities()
                .isEmpty()) {


            tvHabilidades.setText(
                    "Sin información"
            );


            return;
        }


        StringBuilder habilidades =
                new StringBuilder();


        for (PokemonAbility habilidad
                : pokemon.getAbilities()) {


            if (habilidad.getAbility() == null
                    || habilidad
                    .getAbility()
                    .getName() == null) {


                continue;
            }


            if (habilidades.length() > 0) {

                habilidades.append(
                        ", "
                );
            }


            habilidades.append(

                    formatearNombreForma(

                            habilidad
                                    .getAbility()
                                    .getName()

                    )

            );
        }


        tvHabilidades.setText(
                habilidades.toString()
        );
    }


    // =========================================================
    // ESTADÍSTICAS
    // =========================================================

    private void mostrarEstadisticas(
            Pokemon pokemon
    ) {

        if (pokemon.getStats() == null) {

            return;
        }


        int total = 0;


        for (PokemonStat stat
                : pokemon.getStats()) {


            if (stat.getStat() == null
                    || stat
                    .getStat()
                    .getName() == null) {


                continue;
            }


            String nombre =
                    stat
                            .getStat()
                            .getName();


            int valor =
                    stat.getBase_stat();


            total += valor;


            switch (nombre) {


                case "hp":

                    tvHP.setText(
                            String.valueOf(valor)
                    );


                    animarBarra(
                            barHP,
                            valor
                    );

                    break;


                case "attack":

                    tvAtaque.setText(
                            String.valueOf(valor)
                    );


                    animarBarra(
                            barAtaque,
                            valor
                    );

                    break;


                case "defense":

                    tvDefensa.setText(
                            String.valueOf(valor)
                    );


                    animarBarra(
                            barDefensa,
                            valor
                    );

                    break;


                case "special-attack":

                    tvAtaqueEspecial.setText(
                            String.valueOf(valor)
                    );


                    animarBarra(
                            barAtaqueEspecial,
                            valor
                    );

                    break;


                case "special-defense":

                    tvDefensaEspecial.setText(
                            String.valueOf(valor)
                    );


                    animarBarra(
                            barDefensaEspecial,
                            valor
                    );

                    break;


                case "speed":

                    tvVelocidad.setText(
                            String.valueOf(valor)
                    );


                    animarBarra(
                            barVelocidad,
                            valor
                    );

                    break;
            }
        }


        tvTotalStats.setText(
                String.valueOf(total)
        );
    }


    // =========================================================
    // ANIMACIÓN DE ESTADÍSTICAS
    // =========================================================

    private void animarBarra(
            ProgressBar barra,
            int valor
    ) {

        barra.setProgress(
                0
        );


        ObjectAnimator animacion =
                ObjectAnimator.ofInt(

                        barra,
                        "progress",
                        0,
                        valor

                );


        animacion.setDuration(
                700
        );


        animacion.start();
    }


    // =========================================================
    // OBTENER CRY
    // =========================================================

    private void obtenerCry(
            Pokemon pokemon
    ) {

        urlCry = null;


        if (pokemon.getCries() == null) {


            btnCry.setEnabled(
                    false
            );


            btnCry.setAlpha(
                    0.4f
            );


            return;
        }


        if (pokemon
                .getCries()
                .getLatest() != null) {


            urlCry =
                    pokemon
                            .getCries()
                            .getLatest();


        } else if (pokemon
                .getCries()
                .getLegacy() != null) {


            urlCry =
                    pokemon
                            .getCries()
                            .getLegacy();

        }


        boolean disponible =
                urlCry != null
                        && !urlCry.isEmpty();


        btnCry.setEnabled(
                disponible
        );


        btnCry.setAlpha(

                disponible
                        ? 1.0f
                        : 0.4f

        );
    }


    // =========================================================
    // REPRODUCIR CRY
    // =========================================================

    private void reproducirCry() {

        if (urlCry == null
                || urlCry.isEmpty()) {


            Toast.makeText(
                    this,
                    "Este Pokémon no tiene sonido disponible",
                    Toast.LENGTH_SHORT
            ).show();


            return;
        }


        detenerCry();


        mediaPlayer =
                new MediaPlayer();


        try {


            mediaPlayer.setDataSource(
                    urlCry
            );


            mediaPlayer.setOnPreparedListener(
                    MediaPlayer::start
            );


            mediaPlayer.setOnCompletionListener(
                    mp -> detenerCry()
            );


            mediaPlayer.prepareAsync();


        } catch (IOException e) {


            Toast.makeText(
                    this,
                    "No se pudo reproducir el sonido",
                    Toast.LENGTH_SHORT
            ).show();


            detenerCry();
        }
    }


    // =========================================================
    // DETENER CRY
    // =========================================================

    private void detenerCry() {

        if (mediaPlayer != null) {


            try {


                if (mediaPlayer.isPlaying()) {

                    mediaPlayer.stop();
                }


            } catch (IllegalStateException ignored) {

            }


            mediaPlayer.release();

            mediaPlayer = null;
        }
    }


    // =========================================================
    // BUSCAR EVOLUCIONES + FORMAS
    // =========================================================

    private void buscarEvoluciones(
            String especieBase
    ) {

        layoutEvoluciones.removeAllViews();

        layoutFormasEspeciales.removeAllViews();


        seccionFormasEspeciales.setVisibility(
                View.GONE
        );


        pokeApiService
                .obtenerEspecie(
                        especieBase
                )
                .enqueue(

                        new Callback<PokemonSpecies>() {

                            @Override
                            public void onResponse(
                                    Call<PokemonSpecies> call,
                                    Response<PokemonSpecies> response) {


                                if (!response.isSuccessful()
                                        || response.body() == null) {


                                    return;
                                }


                                PokemonSpecies especie =
                                        response.body();


                                // =============================
                                // EVOLUCIONES
                                // =============================

                                if (especie
                                        .getEvolution_chain() != null
                                        && especie
                                        .getEvolution_chain()
                                        .getUrl() != null) {


                                    obtenerCadena(

                                            especie
                                                    .getEvolution_chain()
                                                    .getUrl()

                                    );
                                }


                                // =============================
                                // FORMAS
                                // =============================

                                mostrarFormasEspeciales(
                                        especie
                                );
                            }


                            @Override
                            public void onFailure(
                                    Call<PokemonSpecies> call,
                                    Throwable t) {


                                seccionFormasEspeciales
                                        .setVisibility(
                                                View.GONE
                                        );
                            }
                        }
                );
    }


    // =========================================================
    // OBTENER CADENA EVOLUTIVA
    // =========================================================

    private void obtenerCadena(
            String url
    ) {

        pokeApiService
                .obtenerCadenaEvolutiva(
                        url
                )
                .enqueue(

                        new Callback<EvolutionChainResponse>() {

                            @Override
                            public void onResponse(
                                    Call<EvolutionChainResponse> call,
                                    Response<EvolutionChainResponse> response) {


                                if (!response.isSuccessful()
                                        || response.body() == null
                                        || response
                                        .body()
                                        .getChain() == null) {


                                    return;
                                }


                                List<String> nombres =
                                        new ArrayList<>();


                                obtenerNombresEvolucion(

                                        response
                                                .body()
                                                .getChain(),

                                        nombres

                                );


                                mostrarCadenaEvolutiva(
                                        nombres
                                );
                            }


                            @Override
                            public void onFailure(
                                    Call<EvolutionChainResponse> call,
                                    Throwable t) {

                            }
                        }
                );
    }


    // =========================================================
    // OBTENER NOMBRES DE EVOLUCIÓN
    // =========================================================

    private void obtenerNombresEvolucion(
            ChainLink chain,
            List<String> nombres
    ) {

        if (chain == null) {

            return;
        }


        if (chain.getSpecies() != null
                && chain
                .getSpecies()
                .getName() != null) {


            nombres.add(

                    chain
                            .getSpecies()
                            .getName()

            );
        }


        if (chain.getEvolves_to() == null) {

            return;
        }


        for (ChainLink siguiente
                : chain.getEvolves_to()) {


            obtenerNombresEvolucion(

                    siguiente,
                    nombres

            );
        }
    }


    // =========================================================
    // MOSTRAR CADENA EVOLUTIVA
    // =========================================================

    private void mostrarCadenaEvolutiva(
            List<String> nombres
    ) {

        layoutEvoluciones.removeAllViews();


        if (nombres == null
                || nombres.isEmpty()) {


            return;
        }


        for (int i = 0;
             i < nombres.size();
             i++) {


            agregarPokemonEvolucion(
                    nombres.get(i)
            );


            // =============================================
            // FLECHA
            // =============================================

            if (i < nombres.size() - 1) {


                TextView flecha =
                        new TextView(this);


                flecha.setText(
                        "  →  "
                );


                flecha.setTextSize(
                        22
                );


                flecha.setTextColor(
                        0xFF70E39A
                );


                flecha.setGravity(
                        Gravity.CENTER
                );


                layoutEvoluciones.addView(
                        flecha
                );
            }
        }
    }


    // =========================================================
    // AGREGAR POKÉMON DE EVOLUCIÓN
    // =========================================================

    private void agregarPokemonEvolucion(
            String nombre
    ) {

        LinearLayout contenedor =
                new LinearLayout(this);


        contenedor.setOrientation(
                LinearLayout.VERTICAL
        );


        contenedor.setGravity(
                Gravity.CENTER
        );


        contenedor.setClickable(
                true
        );


        contenedor.setFocusable(
                true
        );


        contenedor.setPadding(
                dp(6),
                dp(6),
                dp(6),
                dp(6)
        );


        // =====================================================
        // IMAGEN
        // =====================================================

        ImageView imagen =
                new ImageView(this);


        imagen.setLayoutParams(

                new LinearLayout.LayoutParams(
                        dp(75),
                        dp(75)
                )

        );


        imagen.setScaleType(
                ImageView.ScaleType.FIT_CENTER
        );


        // =====================================================
        // NOMBRE
        // =====================================================

        TextView texto =
                new TextView(this);


        texto.setText(

                formatearNombreForma(
                        nombre
                ).toUpperCase(
                        Locale.ROOT
                )

        );


        texto.setTextColor(
                0xFFFFFFFF
        );


        texto.setTextSize(
                11
        );


        texto.setGravity(
                Gravity.CENTER
        );


        // =====================================================
        // AGREGAR
        // =====================================================

        contenedor.addView(
                imagen
        );


        contenedor.addView(
                texto
        );


        layoutEvoluciones.addView(
                contenedor
        );


        // =====================================================
        // CLICK
        // =====================================================

        contenedor.setOnClickListener(v -> {


            buscarPokemon(
                    nombre.toLowerCase(
                            Locale.ROOT
                    )
            );


            Toast.makeText(
                    PokedexActivity.this,
                    "Cargando "
                            + formatearNombreForma(nombre)
                            + "...",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // IMAGEN
        // =====================================================

        cargarImagenPokemon(
                nombre,
                imagen
        );
    }


    // =========================================================
    // MOSTRAR FORMAS DISPONIBLES
    // =========================================================

    private void mostrarFormasEspeciales(
            PokemonSpecies especie
    ) {

        layoutFormasEspeciales.removeAllViews();


        if (especie.getVarieties() == null
                || especie
                .getVarieties()
                .isEmpty()) {


            seccionFormasEspeciales
                    .setVisibility(
                            View.GONE
                    );


            return;
        }


        // Si solo tiene la forma normal
        if (especie
                .getVarieties()
                .size() <= 1) {


            seccionFormasEspeciales
                    .setVisibility(
                            View.GONE
                    );


            return;
        }


        int formasEncontradas =
                0;


        for (PokemonVariety variedad
                : especie.getVarieties()) {


            if (variedad.getPokemon() == null
                    || variedad
                    .getPokemon()
                    .getName() == null) {


                continue;
            }


            String nombreForma =
                    variedad
                            .getPokemon()
                            .getName();


            String categoria;


            // =============================================
            // FORMA NORMAL
            // =============================================

            if (variedad.isDefault()) {


                categoria =
                        "NORMAL";


            } else {


                categoria =
                        obtenerCategoriaForma(
                                nombreForma
                        );

            }


            agregarFormaEspecial(
                    nombreForma,
                    categoria
            );


            formasEncontradas++;
        }


        if (formasEncontradas > 1) {


            seccionFormasEspeciales
                    .setVisibility(
                            View.VISIBLE
                    );


        } else {


            seccionFormasEspeciales
                    .setVisibility(
                            View.GONE
                    );

        }
    }


    // =========================================================
    // AGREGAR FORMA
    // =========================================================

    private void agregarFormaEspecial(
            String nombreForma,
            String categoria
    ) {

        // =====================================================
        // TARJETA
        // =====================================================

        LinearLayout tarjeta =
                new LinearLayout(this);


        tarjeta.setOrientation(
                LinearLayout.VERTICAL
        );


        tarjeta.setGravity(
                Gravity.CENTER
        );


        LinearLayout.LayoutParams parametros =
                new LinearLayout.LayoutParams(

                        dp(125),

                        LinearLayout.LayoutParams
                                .WRAP_CONTENT

                );


        parametros.setMargins(
                dp(6),
                dp(6),
                dp(6),
                dp(6)
        );


        tarjeta.setLayoutParams(
                parametros
        );


        tarjeta.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(8)
        );


        tarjeta.setBackgroundResource(
                R.drawable.fondo_forma_especial
        );


        tarjeta.setClickable(
                true
        );


        tarjeta.setFocusable(
                true
        );


        // =====================================================
        // IMAGEN
        // =====================================================

        ImageView imagen =
                new ImageView(this);


        imagen.setLayoutParams(

                new LinearLayout.LayoutParams(
                        dp(95),
                        dp(95)
                )

        );


        imagen.setScaleType(
                ImageView.ScaleType.FIT_CENTER
        );


        // =====================================================
        // NOMBRE
        // =====================================================

        TextView tvNombre =
                new TextView(this);


        tvNombre.setText(

                formatearNombreForma(
                        nombreForma
                )

        );


        tvNombre.setTextColor(
                0xFFFFFFFF
        );


        tvNombre.setTextSize(
                12
        );


        tvNombre.setGravity(
                Gravity.CENTER
        );


        tvNombre.setMaxLines(
                2
        );


        // =====================================================
        // CATEGORÍA
        // =====================================================

        TextView tvCategoria =
                new TextView(this);


        if (categoria.equals(
                "NORMAL"
        )) {


            tvCategoria.setText(
                    "FORMA NORMAL"
            );


        } else {


            tvCategoria.setText(
                    categoria
            );

        }


        tvCategoria.setTextColor(
                0xFFFFFFFF
        );


        tvCategoria.setTextSize(
                10
        );


        tvCategoria.setGravity(
                Gravity.CENTER
        );


        tvCategoria.setBackgroundResource(
                R.drawable.etiqueta_forma
        );


        LinearLayout.LayoutParams parametrosCategoria =
                new LinearLayout.LayoutParams(

                        LinearLayout.LayoutParams
                                .WRAP_CONTENT,

                        LinearLayout.LayoutParams
                                .WRAP_CONTENT

                );


        parametrosCategoria.topMargin =
                dp(5);


        tvCategoria.setLayoutParams(
                parametrosCategoria
        );


        // =====================================================
        // MARCAR FORMA ACTUAL
        // =====================================================

        if (nombreForma.equalsIgnoreCase(
                pokemonActual
        )) {


            tvCategoria.setText(

                    tvCategoria.getText()
                            + " • ACTUAL"

            );


            tarjeta.setAlpha(
                    1.0f
            );


        } else {


            tarjeta.setAlpha(
                    0.82f
            );

        }


        // =====================================================
        // AGREGAR VISTAS
        // =====================================================

        tarjeta.addView(
                imagen
        );


        tarjeta.addView(
                tvNombre
        );


        tarjeta.addView(
                tvCategoria
        );


        layoutFormasEspeciales.addView(
                tarjeta
        );


        // =====================================================
        // CLICK
        // =====================================================

        tarjeta.setOnClickListener(v -> {


            buscarPokemon(

                    nombreForma.toLowerCase(
                            Locale.ROOT
                    )

            );


            Toast.makeText(
                    PokedexActivity.this,
                    "Cargando "
                            + formatearNombreForma(
                            nombreForma
                    )
                            + "...",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // CARGAR IMAGEN
        // =====================================================

        cargarImagenPokemon(
                nombreForma,
                imagen
        );
    }


    // =========================================================
    // CARGAR IMAGEN
    // =========================================================

    private void cargarImagenPokemon(
            String nombre,
            ImageView imageView
    ) {

        pokeApiService
                .obtenerPokemon(
                        nombre
                )
                .enqueue(

                        new Callback<Pokemon>() {

                            @Override
                            public void onResponse(
                                    Call<Pokemon> call,
                                    Response<Pokemon> response) {


                                if (!response.isSuccessful()
                                        || response.body() == null
                                        || response
                                        .body()
                                        .getSprites() == null) {


                                    return;
                                }


                                String urlImagen =
                                        response
                                                .body()
                                                .getSprites()
                                                .getFront_default();


                                if (urlImagen != null
                                        && !urlImagen.isEmpty()) {


                                    Glide.with(
                                                    PokedexActivity.this
                                            )
                                            .load(
                                                    urlImagen
                                            )
                                            .into(
                                                    imageView
                                            );

                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<Pokemon> call,
                                    Throwable t) {

                            }
                        }
                );
    }


    // =========================================================
    // CATEGORÍA DE FORMA
    // =========================================================

    private String obtenerCategoriaForma(
            String nombre
    ) {

        String n =
                nombre.toLowerCase(
                        Locale.ROOT
                );


        // =====================================================
        // MEGA
        // =====================================================

        if (n.contains(
                "-mega-x"
        )) {

            return "MEGA X";
        }


        if (n.contains(
                "-mega-y"
        )) {

            return "MEGA Y";
        }


        if (n.contains(
                "-mega"
        )) {

            return "MEGA";
        }


        // =====================================================
        // REGIONALES
        // =====================================================

        if (n.contains(
                "-alola"
        )) {

            return "ALOLA";
        }


        if (n.contains(
                "-galar"
        )) {

            return "GALAR";
        }


        if (n.contains(
                "-hisui"
        )) {

            return "HISUI";
        }


        if (n.contains(
                "-paldea"
        )) {

            return "PALDEA";
        }


        // =====================================================
        // GIGANTAMAX
        // =====================================================

        if (n.contains("-gmax")
                || n.contains(
                "-gigantamax"
        )) {

            return "GIGAMAX";
        }


        // =====================================================
        // PRIMAL
        // =====================================================

        if (n.contains(
                "-primal"
        )) {

            return "PRIMAL";
        }


        // =====================================================
        // ORIGIN
        // =====================================================

        if (n.contains(
                "-origin"
        )) {

            return "ORIGIN";
        }


        // =====================================================
        // THERIAN
        // =====================================================

        if (n.contains(
                "-therian"
        )) {

            return "THERIAN";
        }


        // =====================================================
        // ALTERED
        // =====================================================

        if (n.contains(
                "-altered"
        )) {

            return "ALTERED";
        }


        // =====================================================
        // SKY
        // =====================================================

        if (n.contains(
                "-sky"
        )) {

            return "SKY";
        }


        // =====================================================
        // DEOXYS
        // =====================================================

        if (n.contains(
                "-attack"
        )) {

            return "ATTACK";
        }


        if (n.contains(
                "-defense"
        )) {

            return "DEFENSE";
        }


        if (n.contains(
                "-speed"
        )) {

            return "SPEED";
        }


        return "FORMA";
    }


    // =========================================================
    // MOSTRAR FORMA ACTUAL
    // =========================================================

    private void mostrarFormaActual(
            Pokemon pokemon
    ) {

        if (pokemon.getSpecies() == null
                || pokemon
                .getSpecies()
                .getName() == null) {


            tvFormaActual.setVisibility(
                    View.GONE
            );


            return;
        }


        String nombrePokemon =
                pokemon
                        .getName()
                        .toLowerCase(
                                Locale.ROOT
                        );


        String especie =
                pokemon
                        .getSpecies()
                        .getName()
                        .toLowerCase(
                                Locale.ROOT
                        );


        // =====================================================
        // FORMA NORMAL
        // =====================================================

        if (nombrePokemon.equals(
                especie
        )) {


            tvFormaActual.setVisibility(
                    View.GONE
            );


            return;
        }


        String categoria =
                obtenerCategoriaForma(
                        nombrePokemon
                );


        tvFormaActual.setText(
                "FORMA: " + categoria
        );


        tvFormaActual.setVisibility(
                View.VISIBLE
        );
    }


    // =========================================================
    // FORMATEAR NOMBRE
    // =========================================================

    private String formatearNombreForma(
            String nombre
    ) {

        if (nombre == null
                || nombre.isEmpty()) {


            return "";
        }


        String[] palabras =
                nombre.split("-");


        StringBuilder resultado =
                new StringBuilder();


        for (String palabra
                : palabras) {


            if (palabra.isEmpty()) {

                continue;
            }


            String palabraFormateada =
                    palabra
                            .substring(
                                    0,
                                    1
                            )
                            .toUpperCase(
                                    Locale.ROOT
                            )
                            + palabra.substring(
                            1
                    );


            if (resultado.length() > 0) {

                resultado.append(
                        " "
                );
            }


            resultado.append(
                    palabraFormateada
            );
        }


        return resultado.toString();
    }


    // =========================================================
    // CONVERTIR DP
    // =========================================================

    private int dp(
            int valor
    ) {

        return (int) (

                valor
                        * getResources()
                        .getDisplayMetrics()
                        .density

        );
    }


    // =========================================================
    // CERRAR ACTIVITY
    // =========================================================

    @Override
    protected void onDestroy() {

        detenerCry();

        super.onDestroy();
    }
}