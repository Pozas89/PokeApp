package com.cesba.pokeapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cesba.pokeapp.api.ApiClient;
import com.cesba.pokeapp.api.PokeApiService;
import com.cesba.pokeapp.models.HistorialBatalla;
import com.cesba.pokeapp.models.MoveDetail;
import com.cesba.pokeapp.models.MovimientoBatalla;
import com.cesba.pokeapp.models.Pokemon;
import com.cesba.pokeapp.models.PokemonMove;
import com.cesba.pokeapp.models.PokemonStat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatallaActivity extends AppCompatActivity {

    // =========================================================
    // INTERFAZ
    // =========================================================

    private ImageButton btnRegresarBatalla;

    private TextView tvTurno;
    private TextView tvNombreJugador;
    private TextView tvNombreCpu;
    private TextView tvHpJugador;
    private TextView tvHpCpu;
    private TextView tvMensajeBatalla;
    private TextView tvRegistroBatalla;
    private TextView tvGanador;
    private TextView tvResultadoDetalles;

    private ImageView imgPokemonJugador;
    private ImageView imgPokemonCpu;
    private ImageView imgGanador;

    private ProgressBar barHpJugador;
    private ProgressBar barHpCpu;

    private Button btnComenzarCombate;
    private Button btnRevancha;
    private Button btnElegirOtros;
    private Button btnVolverMenu;

    private LinearLayout panelAtaques;
    private LinearLayout panelResultado;

    private ScrollView scrollRegistro;


    // =========================================================
    // POKÉAPI
    // =========================================================

    private PokeApiService pokeApiService;


    // =========================================================
    // FIREBASE
    // =========================================================

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    // =========================================================
    // POKÉMON
    // =========================================================

    private Pokemon pokemonJugador;
    private Pokemon pokemonCpu;

    private String nombrePokemonJugador;
    private String nombrePokemonCpu;


    // =========================================================
    // VIDA
    // =========================================================

    private int hpMaxJugador;
    private int hpMaxCpu;

    private int hpActualJugador;
    private int hpActualCpu;


    // =========================================================
    // STATS JUGADOR
    // =========================================================

    private int ataqueJugador;
    private int defensaJugador;
    private int ataqueEspecialJugador;
    private int defensaEspecialJugador;
    private int velocidadJugador;


    // =========================================================
    // STATS CPU
    // =========================================================

    private int ataqueCpu;
    private int defensaCpu;
    private int ataqueEspecialCpu;
    private int defensaEspecialCpu;
    private int velocidadCpu;


    // =========================================================
    // MOVIMIENTOS
    // =========================================================

    private final List<MovimientoBatalla> movimientosJugador =
            new ArrayList<>();

    private final List<MovimientoBatalla> movimientosCpu =
            new ArrayList<>();

    private boolean movimientosJugadorListos = false;
    private boolean movimientosCpuListos = false;


    // =========================================================
    // CONTROL DE BATALLA
    // =========================================================

    private int pokemonCargados = 0;
    private int numeroTurno = 0;

    private boolean batallaIniciada = false;
    private boolean batallaTerminada = false;

    /*
     * Evita guardar dos veces la misma batalla
     * si por alguna razón finalizarBatalla() fuera llamado
     * más de una vez.
     */
    private boolean batallaGuardada = false;


    // =========================================================
    // UTILIDADES
    // =========================================================

    private final Handler handler =
            new Handler(Looper.getMainLooper());

    private final Random random =
            new Random();


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_batalla);


        // =====================================================
        // ENLAZAR XML
        // =====================================================

        btnRegresarBatalla =
                findViewById(R.id.btnRegresarBatalla);

        tvTurno =
                findViewById(R.id.tvTurno);

        tvNombreJugador =
                findViewById(R.id.tvNombreJugador);

        tvNombreCpu =
                findViewById(R.id.tvNombreCpu);

        tvHpJugador =
                findViewById(R.id.tvHpJugador);

        tvHpCpu =
                findViewById(R.id.tvHpCpu);

        tvMensajeBatalla =
                findViewById(R.id.tvMensajeBatalla);

        tvRegistroBatalla =
                findViewById(R.id.tvRegistroBatalla);

        imgPokemonJugador =
                findViewById(R.id.imgPokemonJugador);

        imgPokemonCpu =
                findViewById(R.id.imgPokemonCpu);

        barHpJugador =
                findViewById(R.id.barHpJugador);

        barHpCpu =
                findViewById(R.id.barHpCpu);

        btnComenzarCombate =
                findViewById(R.id.btnComenzarCombate);

        panelAtaques =
                findViewById(R.id.panelAtaques);

        panelResultado =
                findViewById(R.id.panelResultado);

        imgGanador =
                findViewById(R.id.imgGanador);

        tvGanador =
                findViewById(R.id.tvGanador);

        tvResultadoDetalles =
                findViewById(R.id.tvResultadoDetalles);

        btnRevancha =
                findViewById(R.id.btnRevancha);

        btnElegirOtros =
                findViewById(R.id.btnElegirOtros);

        btnVolverMenu =
                findViewById(R.id.btnVolverMenu);

        scrollRegistro =
                findViewById(R.id.scrollRegistro);


        // =====================================================
        // POKÉAPI
        // =====================================================

        pokeApiService =
                ApiClient
                        .getClient()
                        .create(PokeApiService.class);


        // =====================================================
        // FIREBASE
        // =====================================================

        mAuth =
                FirebaseAuth.getInstance();

        db =
                FirebaseFirestore.getInstance();


        // =====================================================
        // RECIBIR POKÉMON
        // =====================================================

        nombrePokemonJugador =
                getIntent().getStringExtra("pokemon1");

        nombrePokemonCpu =
                getIntent().getStringExtra("pokemon2");


        if (nombrePokemonJugador == null
                || nombrePokemonCpu == null) {

            Toast.makeText(
                    this,
                    "No se recibieron los Pokémon",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }


        // =====================================================
        // ESTADO INICIAL
        // =====================================================

        btnComenzarCombate.setEnabled(false);

        panelAtaques.setVisibility(View.GONE);

        panelResultado.setVisibility(View.GONE);

        tvMensajeBatalla.setText(
                "Cargando combatientes..."
        );


        // =====================================================
        // BOTONES
        // =====================================================

        btnRegresarBatalla.setOnClickListener(v ->
                finish()
        );


        btnComenzarCombate.setOnClickListener(v ->
                iniciarBatallaAutomatica()
        );


        btnRevancha.setOnClickListener(v ->
                prepararRevancha()
        );


        btnElegirOtros.setOnClickListener(v ->
                finish()
        );


        btnVolverMenu.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            BatallaActivity.this,
                            HomeActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);

            finish();
        });


        // =====================================================
        // CARGAR POKÉMON
        // =====================================================

        cargarPokemonJugador();

        cargarPokemonCpu();
    }


    // =========================================================
    // CARGAR POKÉMON JUGADOR
    // =========================================================

    private void cargarPokemonJugador() {

        pokeApiService
                .obtenerPokemon(nombrePokemonJugador)
                .enqueue(new Callback<Pokemon>() {

                    @Override
                    public void onResponse(
                            Call<Pokemon> call,
                            Response<Pokemon> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            pokemonJugador =
                                    response.body();

                            prepararPokemonJugador();

                            pokemonCargado();

                        } else {

                            errorCargaPokemon();
                        }
                    }


                    @Override
                    public void onFailure(
                            Call<Pokemon> call,
                            Throwable t
                    ) {

                        errorCargaPokemon();
                    }
                });
    }


    // =========================================================
    // CARGAR POKÉMON CPU
    // =========================================================

    private void cargarPokemonCpu() {

        pokeApiService
                .obtenerPokemon(nombrePokemonCpu)
                .enqueue(new Callback<Pokemon>() {

                    @Override
                    public void onResponse(
                            Call<Pokemon> call,
                            Response<Pokemon> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            pokemonCpu =
                                    response.body();

                            prepararPokemonCpu();

                            pokemonCargado();

                        } else {

                            errorCargaPokemon();
                        }
                    }


                    @Override
                    public void onFailure(
                            Call<Pokemon> call,
                            Throwable t
                    ) {

                        errorCargaPokemon();
                    }
                });
    }


    // =========================================================
    // PREPARAR JUGADOR
    // =========================================================

    private void prepararPokemonJugador() {

        tvNombreJugador.setText(
                pokemonJugador
                        .getName()
                        .toUpperCase(Locale.ROOT)
        );


        hpMaxJugador =
                obtenerStat(
                        pokemonJugador,
                        "hp"
                );

        hpActualJugador =
                hpMaxJugador;


        ataqueJugador =
                obtenerStat(
                        pokemonJugador,
                        "attack"
                );

        defensaJugador =
                obtenerStat(
                        pokemonJugador,
                        "defense"
                );

        ataqueEspecialJugador =
                obtenerStat(
                        pokemonJugador,
                        "special-attack"
                );

        defensaEspecialJugador =
                obtenerStat(
                        pokemonJugador,
                        "special-defense"
                );

        velocidadJugador =
                obtenerStat(
                        pokemonJugador,
                        "speed"
                );


        actualizarVidaJugador();


        // Sprite trasero del Pokémon 1
        if (pokemonJugador.getSprites() != null) {

            String spriteTrasero =
                    pokemonJugador
                            .getSprites()
                            .getBack_default();


            if (spriteTrasero != null
                    && !spriteTrasero.isEmpty()) {

                Glide.with(this)
                        .load(spriteTrasero)
                        .into(imgPokemonJugador);

            } else {

                String spriteFrontal =
                        pokemonJugador
                                .getSprites()
                                .getFront_default();


                if (spriteFrontal != null) {

                    Glide.with(this)
                            .load(spriteFrontal)
                            .into(imgPokemonJugador);
                }
            }
        }
    }


    // =========================================================
    // PREPARAR CPU
    // =========================================================

    private void prepararPokemonCpu() {

        tvNombreCpu.setText(
                pokemonCpu
                        .getName()
                        .toUpperCase(Locale.ROOT)
        );


        hpMaxCpu =
                obtenerStat(
                        pokemonCpu,
                        "hp"
                );

        hpActualCpu =
                hpMaxCpu;


        ataqueCpu =
                obtenerStat(
                        pokemonCpu,
                        "attack"
                );

        defensaCpu =
                obtenerStat(
                        pokemonCpu,
                        "defense"
                );

        ataqueEspecialCpu =
                obtenerStat(
                        pokemonCpu,
                        "special-attack"
                );

        defensaEspecialCpu =
                obtenerStat(
                        pokemonCpu,
                        "special-defense"
                );

        velocidadCpu =
                obtenerStat(
                        pokemonCpu,
                        "speed"
                );


        actualizarVidaCpu();


        if (pokemonCpu.getSprites() != null
                && pokemonCpu
                .getSprites()
                .getFront_default() != null) {

            Glide.with(this)
                    .load(
                            pokemonCpu
                                    .getSprites()
                                    .getFront_default()
                    )
                    .into(imgPokemonCpu);
        }
    }


    // =========================================================
    // AMBOS POKÉMON CARGADOS
    // =========================================================

    private void pokemonCargado() {

        pokemonCargados++;


        if (pokemonCargados == 2) {

            tvMensajeBatalla.setText(
                    "Cargando movimientos..."
            );


            tvRegistroBatalla.setText(

                    formatearNombre(
                            pokemonJugador.getName()
                    )

                            + " VS "

                            + formatearNombre(
                            pokemonCpu.getName()
                    )

                            + "\n\nPreparando combate automático..."
            );


            cargarMovimientosJugador();

            cargarMovimientosCpu();
        }
    }


    // =========================================================
    // ERROR DE CARGA
    // =========================================================

    private void errorCargaPokemon() {

        Toast.makeText(
                this,
                "No se pudo preparar la batalla",
                Toast.LENGTH_SHORT
        ).show();


        btnComenzarCombate.setEnabled(false);
    }


    // =========================================================
    // CARGAR MOVIMIENTOS JUGADOR
    // =========================================================

    private void cargarMovimientosJugador() {

        movimientosJugador.clear();

        movimientosJugadorListos = false;


        List<PokemonMove> disponibles =
                pokemonJugador.getMoves();


        if (disponibles == null
                || disponibles.isEmpty()) {

            return;
        }


        List<PokemonMove> copia =
                new ArrayList<>(disponibles);


        Collections.shuffle(copia);


        consultarSiguienteMovimientoJugador(
                copia,
                0
        );
    }


    private void consultarSiguienteMovimientoJugador(
            List<PokemonMove> lista,
            int indice
    ) {

        if (movimientosJugador.size() >= 4) {

            movimientosJugadorListos = true;

            comprobarPreparacionBatalla();

            return;
        }


        if (indice >= lista.size()) {

            movimientosJugadorListos =
                    !movimientosJugador.isEmpty();

            comprobarPreparacionBatalla();

            return;
        }


        PokemonMove pokemonMove =
                lista.get(indice);


        if (pokemonMove.getMove() == null
                || pokemonMove
                .getMove()
                .getName() == null) {

            consultarSiguienteMovimientoJugador(
                    lista,
                    indice + 1
            );

            return;
        }


        String nombreMovimiento =
                pokemonMove
                        .getMove()
                        .getName();


        pokeApiService
                .obtenerMovimiento(nombreMovimiento)
                .enqueue(new Callback<MoveDetail>() {

                    @Override
                    public void onResponse(
                            Call<MoveDetail> call,
                            Response<MoveDetail> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            MoveDetail detalle =
                                    response.body();


                            if (movimientoValido(detalle)) {

                                movimientosJugador.add(
                                        convertirMovimiento(detalle)
                                );
                            }
                        }


                        consultarSiguienteMovimientoJugador(
                                lista,
                                indice + 1
                        );
                    }


                    @Override
                    public void onFailure(
                            Call<MoveDetail> call,
                            Throwable t
                    ) {

                        consultarSiguienteMovimientoJugador(
                                lista,
                                indice + 1
                        );
                    }
                });
    }


    // =========================================================
    // CARGAR MOVIMIENTOS CPU
    // =========================================================

    private void cargarMovimientosCpu() {

        movimientosCpu.clear();

        movimientosCpuListos = false;


        List<PokemonMove> disponibles =
                pokemonCpu.getMoves();


        if (disponibles == null
                || disponibles.isEmpty()) {

            return;
        }


        List<PokemonMove> copia =
                new ArrayList<>(disponibles);


        Collections.shuffle(copia);


        consultarSiguienteMovimientoCpu(
                copia,
                0
        );
    }


    private void consultarSiguienteMovimientoCpu(
            List<PokemonMove> lista,
            int indice
    ) {

        if (movimientosCpu.size() >= 4) {

            movimientosCpuListos = true;

            comprobarPreparacionBatalla();

            return;
        }


        if (indice >= lista.size()) {

            movimientosCpuListos =
                    !movimientosCpu.isEmpty();

            comprobarPreparacionBatalla();

            return;
        }


        PokemonMove pokemonMove =
                lista.get(indice);


        if (pokemonMove.getMove() == null
                || pokemonMove
                .getMove()
                .getName() == null) {

            consultarSiguienteMovimientoCpu(
                    lista,
                    indice + 1
            );

            return;
        }


        String nombreMovimiento =
                pokemonMove
                        .getMove()
                        .getName();


        pokeApiService
                .obtenerMovimiento(nombreMovimiento)
                .enqueue(new Callback<MoveDetail>() {

                    @Override
                    public void onResponse(
                            Call<MoveDetail> call,
                            Response<MoveDetail> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            MoveDetail detalle =
                                    response.body();


                            if (movimientoValido(detalle)) {

                                movimientosCpu.add(
                                        convertirMovimiento(detalle)
                                );
                            }
                        }


                        consultarSiguienteMovimientoCpu(
                                lista,
                                indice + 1
                        );
                    }


                    @Override
                    public void onFailure(
                            Call<MoveDetail> call,
                            Throwable t
                    ) {

                        consultarSiguienteMovimientoCpu(
                                lista,
                                indice + 1
                        );
                    }
                });
    }


    // =========================================================
    // VALIDAR MOVIMIENTO
    // =========================================================

    private boolean movimientoValido(
            MoveDetail movimiento
    ) {

        return movimiento.getPower() != null
                && movimiento.getPower() > 0

                && movimiento.getType() != null
                && movimiento.getType().getName() != null

                && movimiento.getDamage_class() != null
                && movimiento.getDamage_class().getName() != null;
    }


    // =========================================================
    // CONVERTIR MOVIMIENTO
    // =========================================================

    private MovimientoBatalla convertirMovimiento(
            MoveDetail detalle
    ) {

        int precision =
                detalle.getAccuracy() != null
                        ? detalle.getAccuracy()
                        : 100;


        return new MovimientoBatalla(

                detalle.getName(),

                detalle
                        .getType()
                        .getName(),

                detalle
                        .getDamage_class()
                        .getName(),

                detalle.getPower(),

                precision
        );
    }


    // =========================================================
    // COMPROBAR PREPARACIÓN
    // =========================================================

    private void comprobarPreparacionBatalla() {

        if (!movimientosJugadorListos
                || !movimientosCpuListos) {

            return;
        }


        btnComenzarCombate.setEnabled(true);


        tvMensajeBatalla.setText(
                "¡Combatientes listos!"
        );


        tvRegistroBatalla.setText(

                formatearNombre(
                        pokemonJugador.getName()
                )

                        + " VS "

                        + formatearNombre(
                        pokemonCpu.getName()
                )

                        + "\n\nBatalla automática preparada."

                        + "\nPulsa INICIAR BATALLA."
        );
    }


    // =========================================================
    // INICIAR BATALLA AUTOMÁTICA
    // =========================================================

    private void iniciarBatallaAutomatica() {

        if (batallaIniciada) {

            return;
        }


        if (!movimientosJugadorListos
                || !movimientosCpuListos) {

            Toast.makeText(
                    this,
                    "La batalla todavía no está preparada",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        batallaIniciada = true;

        batallaTerminada = false;

        /*
         * Esta es una nueva batalla, por lo tanto
         * todavía no ha sido guardada.
         */
        batallaGuardada = false;

        numeroTurno = 0;


        hpActualJugador =
                hpMaxJugador;

        hpActualCpu =
                hpMaxCpu;


        actualizarVidaJugador();

        actualizarVidaCpu();


        panelResultado.setVisibility(
                View.GONE
        );


        panelAtaques.setVisibility(
                View.GONE
        );


        btnComenzarCombate.setVisibility(
                View.GONE
        );


        tvTurno.setText(
                "TURNO 0"
        );


        tvRegistroBatalla.setText(
                "¡COMIENZA LA BATALLA!\n"
        );


        tvMensajeBatalla.setText(
                "¡La batalla ha comenzado!"
        );


        if (velocidadJugador > velocidadCpu) {

            agregarRegistro(

                    formatearNombre(
                            pokemonJugador.getName()
                    )

                            + " es más rápido y atacará primero."
            );


            handler.postDelayed(
                    this::turnoAutomaticoJugador,
                    1000
            );

        } else if (velocidadCpu > velocidadJugador) {

            agregarRegistro(

                    formatearNombre(
                            pokemonCpu.getName()
                    )

                            + " es más rápido y atacará primero."
            );


            handler.postDelayed(
                    this::turnoAutomaticoCpu,
                    1000
            );

        } else {

            /*
             * Si tienen la misma velocidad,
             * usamos Attack para desempatar.
             */

            if (ataqueJugador >= ataqueCpu) {

                agregarRegistro(

                        "Ambos tienen la misma velocidad. "

                                + formatearNombre(
                                pokemonJugador.getName()
                        )

                                + " atacará primero."
                );


                handler.postDelayed(
                        this::turnoAutomaticoJugador,
                        1000
                );

            } else {

                agregarRegistro(

                        "Ambos tienen la misma velocidad. "

                                + formatearNombre(
                                pokemonCpu.getName()
                        )

                                + " atacará primero."
                );


                handler.postDelayed(
                        this::turnoAutomaticoCpu,
                        1000
                );
            }
        }
    }


    // =========================================================
    // TURNO AUTOMÁTICO JUGADOR
    // =========================================================

    private void turnoAutomaticoJugador() {

        if (batallaTerminada
                || movimientosJugador.isEmpty()) {

            return;
        }


        MovimientoBatalla movimiento =
                movimientosJugador.get(

                        random.nextInt(
                                movimientosJugador.size()
                        )
                );


        numeroTurno++;


        tvTurno.setText(
                "TURNO " + numeroTurno
        );


        String atacante =
                formatearNombre(
                        pokemonJugador.getName()
                );


        String defensor =
                formatearNombre(
                        pokemonCpu.getName()
                );


        tvMensajeBatalla.setText(

                atacante

                        + " usó "

                        + formatearMovimiento(
                        movimiento.getNombre()
                )

                        + "!"
        );


        animarAtaque(
                imgPokemonJugador,
                true
        );


        handler.postDelayed(() -> {

            if (batallaTerminada) {

                return;
            }


            int dañoCalculado =
                    calcularDanioJugador(
                            movimiento
                    );


            int dañoReal =
                    Math.min(
                            dañoCalculado,
                            hpActualCpu
                    );


            int hpAnterior =
                    hpActualCpu;


            hpActualCpu =
                    Math.max(
                            0,
                            hpActualCpu - dañoReal
                    );


            int porcentajeQuitado =
                    calcularPorcentajeDanio(
                            dañoReal,
                            hpMaxCpu
                    );


            animarGolpe(
                    imgPokemonCpu
            );


            animarBarraVida(
                    barHpCpu,
                    hpAnterior,
                    hpActualCpu,
                    hpMaxCpu
            );


            actualizarVidaCpu();


            agregarRegistro(

                    "\nTurno "

                            + numeroTurno

                            + "\n"

                            + atacante

                            + " usó "

                            + formatearMovimiento(
                            movimiento.getNombre()
                    )

                            + "\nCausa "

                            + dañoReal

                            + " puntos de daño."

                            + "\n"

                            + defensor

                            + " perdió "

                            + porcentajeQuitado

                            + "% de su vida."

                            + "\n"

                            + defensor

                            + " queda con "

                            + hpActualCpu

                            + " HP ("

                            + calcularPorcentaje(
                            hpActualCpu,
                            hpMaxCpu
                    )

                            + "%).\n"
            );


            if (hpActualCpu <= 0) {

                finalizarBatalla(
                        pokemonJugador,
                        hpActualJugador,
                        true
                );

                return;
            }


            handler.postDelayed(
                    this::turnoAutomaticoCpu,
                    1100
            );


        }, 400);
    }


    // =========================================================
    // TURNO AUTOMÁTICO CPU
    // =========================================================

    private void turnoAutomaticoCpu() {

        if (batallaTerminada
                || movimientosCpu.isEmpty()) {

            return;
        }


        MovimientoBatalla movimiento =
                movimientosCpu.get(

                        random.nextInt(
                                movimientosCpu.size()
                        )
                );


        numeroTurno++;


        tvTurno.setText(
                "TURNO " + numeroTurno
        );


        String atacante =
                formatearNombre(
                        pokemonCpu.getName()
                );


        String defensor =
                formatearNombre(
                        pokemonJugador.getName()
                );


        tvMensajeBatalla.setText(

                atacante

                        + " usó "

                        + formatearMovimiento(
                        movimiento.getNombre()
                )

                        + "!"
        );


        animarAtaque(
                imgPokemonCpu,
                false
        );


        handler.postDelayed(() -> {

            if (batallaTerminada) {

                return;
            }


            int dañoCalculado =
                    calcularDanioCpu(
                            movimiento
                    );


            int dañoReal =
                    Math.min(
                            dañoCalculado,
                            hpActualJugador
                    );


            int hpAnterior =
                    hpActualJugador;


            hpActualJugador =
                    Math.max(
                            0,
                            hpActualJugador - dañoReal
                    );


            int porcentajeQuitado =
                    calcularPorcentajeDanio(
                            dañoReal,
                            hpMaxJugador
                    );


            animarGolpe(
                    imgPokemonJugador
            );


            animarBarraVida(
                    barHpJugador,
                    hpAnterior,
                    hpActualJugador,
                    hpMaxJugador
            );


            actualizarVidaJugador();


            agregarRegistro(

                    "\nTurno "

                            + numeroTurno

                            + "\n"

                            + atacante

                            + " usó "

                            + formatearMovimiento(
                            movimiento.getNombre()
                    )

                            + "\nCausa "

                            + dañoReal

                            + " puntos de daño."

                            + "\n"

                            + defensor

                            + " perdió "

                            + porcentajeQuitado

                            + "% de su vida."

                            + "\n"

                            + defensor

                            + " queda con "

                            + hpActualJugador

                            + " HP ("

                            + calcularPorcentaje(
                            hpActualJugador,
                            hpMaxJugador
                    )

                            + "%).\n"
            );


            if (hpActualJugador <= 0) {

                finalizarBatalla(
                        pokemonCpu,
                        hpActualCpu,
                        false
                );

                return;
            }


            handler.postDelayed(
                    this::turnoAutomaticoJugador,
                    1100
            );


        }, 400);
    }


    // =========================================================
    // CALCULAR % DE DAÑO
    // =========================================================

    private int calcularPorcentajeDanio(
            int daño,
            int hpMaximo
    ) {

        if (hpMaximo <= 0) {

            return 0;
        }


        return Math.max(
                0,
                Math.min(
                        100,
                        Math.round(
                                (daño * 100f)
                                        / hpMaximo
                        )
                )
        );
    }


    // =========================================================
    // CALCULAR DAÑO JUGADOR
    // =========================================================

    private int calcularDanioJugador(
            MovimientoBatalla movimiento
    ) {

        int ataque;
        int defensa;


        if ("special".equals(
                movimiento.getClase()
        )) {

            ataque =
                    ataqueEspecialJugador;

            defensa =
                    defensaEspecialCpu;

        } else {

            ataque =
                    ataqueJugador;

            defensa =
                    defensaCpu;
        }


        return calcularDanio(
                ataque,
                defensa,
                movimiento.getPoder()
        );
    }


    // =========================================================
    // CALCULAR DAÑO CPU
    // =========================================================

    private int calcularDanioCpu(
            MovimientoBatalla movimiento
    ) {

        int ataque;
        int defensa;


        if ("special".equals(
                movimiento.getClase()
        )) {

            ataque =
                    ataqueEspecialCpu;

            defensa =
                    defensaEspecialJugador;

        } else {

            ataque =
                    ataqueCpu;

            defensa =
                    defensaJugador;
        }


        return calcularDanio(
                ataque,
                defensa,
                movimiento.getPoder()
        );
    }


    // =========================================================
    // FÓRMULA DE DAÑO
    // =========================================================

    private int calcularDanio(
            int ataque,
            int defensa,
            int poder
    ) {

        defensa =
                Math.max(
                        1,
                        defensa
                );


        int daño =
                ((ataque * poder)
                        / (defensa * 5))
                        + 2;


        return Math.max(
                1,
                daño
        );
    }


    // =========================================================
    // ANIMACIÓN ATAQUE
    // =========================================================

    private void animarAtaque(
            ImageView imagen,
            boolean haciaDerecha
    ) {

        float desplazamiento =
                haciaDerecha
                        ? 65f
                        : -65f;


        imagen.animate()
                .translationX(desplazamiento)
                .setDuration(180)
                .withEndAction(() ->

                        imagen.animate()
                                .translationX(0f)
                                .setDuration(180)
                                .start()
                )
                .start();
    }


    // =========================================================
    // ANIMACIÓN GOLPE
    // =========================================================

    private void animarGolpe(
            ImageView imagen
    ) {

        imagen.animate()
                .alpha(0.20f)
                .setDuration(100)
                .withEndAction(() ->

                        imagen.animate()
                                .alpha(1f)
                                .setDuration(120)
                                .start()
                )
                .start();
    }


    // =========================================================
    // ANIMAR HP
    // =========================================================

    private void animarBarraVida(
            ProgressBar barra,
            int hpAnterior,
            int hpNuevo,
            int hpMax
    ) {

        if (hpMax <= 0) {

            return;
        }


        int porcentajeAnterior =
                calcularPorcentaje(
                        hpAnterior,
                        hpMax
                );


        int porcentajeNuevo =
                calcularPorcentaje(
                        hpNuevo,
                        hpMax
                );


        ObjectAnimator animacion =
                ObjectAnimator.ofInt(
                        barra,
                        "progress",
                        porcentajeAnterior,
                        porcentajeNuevo
                );


        animacion.setDuration(600);

        animacion.start();


        actualizarColorBarra(
                barra,
                porcentajeNuevo
        );
    }


    // =========================================================
    // COLOR HP
    // =========================================================

    private void actualizarColorBarra(
            ProgressBar barra,
            int porcentaje
    ) {

        int color;


        if (porcentaje > 50) {

            color =
                    Color.parseColor(
                            "#2ECC71"
                    );

        } else if (porcentaje > 20) {

            color =
                    Color.parseColor(
                            "#FBC02D"
                    );

        } else {

            color =
                    Color.parseColor(
                            "#E53935"
                    );
        }


        barra.setProgressTintList(
                ColorStateList.valueOf(
                        color
                )
        );
    }


    // =========================================================
    // ACTUALIZAR VIDA JUGADOR
    // =========================================================

    private void actualizarVidaJugador() {

        hpActualJugador =
                Math.max(
                        0,
                        hpActualJugador
                );


        int porcentaje =
                calcularPorcentaje(
                        hpActualJugador,
                        hpMaxJugador
                );


        barHpJugador.setProgress(
                porcentaje
        );


        actualizarColorBarra(
                barHpJugador,
                porcentaje
        );


        tvHpJugador.setText(

                hpActualJugador

                        + " / "

                        + hpMaxJugador

                        + " HP   "

                        + porcentaje

                        + "%"
        );
    }


    // =========================================================
    // ACTUALIZAR VIDA CPU
    // =========================================================

    private void actualizarVidaCpu() {

        hpActualCpu =
                Math.max(
                        0,
                        hpActualCpu
                );


        int porcentaje =
                calcularPorcentaje(
                        hpActualCpu,
                        hpMaxCpu
                );


        barHpCpu.setProgress(
                porcentaje
        );


        actualizarColorBarra(
                barHpCpu,
                porcentaje
        );


        tvHpCpu.setText(

                hpActualCpu

                        + " / "

                        + hpMaxCpu

                        + " HP   "

                        + porcentaje

                        + "%"
        );
    }


    // =========================================================
    // CALCULAR PORCENTAJE
    // =========================================================

    private int calcularPorcentaje(
            int actual,
            int maximo
    ) {

        if (maximo <= 0) {

            return 0;
        }


        return Math.max(
                0,
                Math.min(
                        100,
                        Math.round(
                                (actual * 100f)
                                        / maximo
                        )
                )
        );
    }


    // =========================================================
    // REGISTRO DE BATALLA
    // =========================================================

    private void agregarRegistro(
            String mensaje
    ) {

        String actual =
                tvRegistroBatalla
                        .getText()
                        .toString();


        tvRegistroBatalla.setText(

                actual
                        + "\n"
                        + mensaje
        );


        scrollRegistro.post(() ->

                scrollRegistro.fullScroll(
                        View.FOCUS_DOWN
                )
        );
    }


    // =========================================================
    // FINALIZAR BATALLA
    // =========================================================

    private void finalizarBatalla(
            Pokemon ganador,
            int hpRestante,
            boolean ganoJugador
    ) {

        /*
         * Evita que un Handler pendiente pueda intentar
         * continuar atacando.
         */
        if (batallaTerminada) {

            return;
        }


        batallaTerminada = true;

        batallaIniciada = false;


        String nombreGanador =
                formatearNombre(
                        ganador.getName()
                );


        tvMensajeBatalla.setText(

                "¡"
                        + nombreGanador
                        + " gana la batalla!"
        );


        panelAtaques.setVisibility(
                View.GONE
        );


        panelResultado.setVisibility(
                View.VISIBLE
        );


        if (ganoJugador) {

            tvGanador.setText(

                    "🏆 "

                            + nombreGanador
                            .toUpperCase(Locale.ROOT)

                            + " 🏆"
            );

        } else {

            tvGanador.setText(

                    nombreGanador
                            .toUpperCase(Locale.ROOT)

                            + " GANA"
            );
        }


        int hpMaxGanador =
                ganoJugador
                        ? hpMaxJugador
                        : hpMaxCpu;


        int porcentajeRestante =
                calcularPorcentaje(
                        hpRestante,
                        hpMaxGanador
                );


        tvResultadoDetalles.setText(

                "HP restante: "

                        + hpRestante

                        + " / "

                        + hpMaxGanador

                        + " ("

                        + porcentajeRestante

                        + "%)"

                        + "\nTurnos: "

                        + numeroTurno
        );


        if (ganador.getSprites() != null
                && ganador
                .getSprites()
                .getFront_default() != null) {

            Glide.with(this)
                    .load(
                            ganador
                                    .getSprites()
                                    .getFront_default()
                    )
                    .into(imgGanador);
        }


        /*
         * Primero agregamos el resultado final al registro.
         * Así también se guarda en Firestore.
         */
        agregarRegistro(

                "\n===================="

                        + "\nFIN DE LA BATALLA"

                        + "\nGanador: "

                        + nombreGanador

                        + "\nHP restante: "

                        + hpRestante

                        + " ("

                        + porcentajeRestante

                        + "%)"

                        + "\nTurnos totales: "

                        + numeroTurno

                        + "\n===================="
        );


        // =====================================================
        // GUARDAR EN FIRESTORE
        // =====================================================

        guardarBatallaEnHistorial(
                ganador,
                hpRestante,
                ganoJugador
        );
    }


    // =========================================================
    // GUARDAR BATALLA EN FIRESTORE
    // =========================================================

    private void guardarBatallaEnHistorial(
            Pokemon ganador,
            int hpRestante,
            boolean ganoJugador
    ) {

        /*
         * Evita duplicados.
         */
        if (batallaGuardada) {

            return;
        }


        batallaGuardada = true;


        FirebaseUser usuario =
                mAuth.getCurrentUser();


        if (usuario == null) {

            batallaGuardada = false;

            Toast.makeText(
                    this,
                    "No hay un usuario autenticado",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        int hpMaxGanador =
                ganoJugador
                        ? hpMaxJugador
                        : hpMaxCpu;


        int porcentajeRestante =
                calcularPorcentaje(
                        hpRestante,
                        hpMaxGanador
                );


        // =====================================================
        // IMÁGENES
        // =====================================================

        String imagenJugador = "";

        String imagenCpu = "";

        String imagenGanador = "";


        if (pokemonJugador != null
                && pokemonJugador.getSprites() != null
                && pokemonJugador
                .getSprites()
                .getFront_default() != null) {

            imagenJugador =
                    pokemonJugador
                            .getSprites()
                            .getFront_default();
        }


        if (pokemonCpu != null
                && pokemonCpu.getSprites() != null
                && pokemonCpu
                .getSprites()
                .getFront_default() != null) {

            imagenCpu =
                    pokemonCpu
                            .getSprites()
                            .getFront_default();
        }


        if (ganador != null
                && ganador.getSprites() != null
                && ganador
                .getSprites()
                .getFront_default() != null) {

            imagenGanador =
                    ganador
                            .getSprites()
                            .getFront_default();
        }


        // =====================================================
        // REGISTRO COMPLETO
        // =====================================================

        String registroCompleto =
                tvRegistroBatalla
                        .getText()
                        .toString();


        // =====================================================
        // CREAR OBJETO
        // =====================================================

        HistorialBatalla batalla =
                new HistorialBatalla(

                        formatearNombre(
                                pokemonJugador.getName()
                        ),

                        formatearNombre(
                                pokemonCpu.getName()
                        ),

                        formatearNombre(
                                ganador.getName()
                        ),

                        hpRestante,

                        hpMaxGanador,

                        porcentajeRestante,

                        numeroTurno,

                        registroCompleto,

                        imagenJugador,

                        imagenCpu,

                        imagenGanador,

                        Timestamp.now()
                );


        // =====================================================
        // GUARDAR
        // =====================================================

        db.collection("usuarios")

                .document(
                        usuario.getUid()
                )

                .collection(
                        "historial_batallas"
                )

                .add(batalla)

                .addOnSuccessListener(
                        documentReference -> {

                            batalla.setId(
                                    documentReference.getId()
                            );


                            Toast.makeText(
                                    BatallaActivity.this,
                                    "Batalla guardada en el historial",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )

                .addOnFailureListener(
                        e -> {

                            /*
                             * Permitimos reintentar si hubo un error.
                             */
                            batallaGuardada = false;


                            Toast.makeText(
                                    BatallaActivity.this,
                                    "No se pudo guardar la batalla",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );
    }


    // =========================================================
    // PREPARAR REVANCHA
    // =========================================================

    private void prepararRevancha() {

        handler.removeCallbacksAndMessages(
                null
        );


        batallaIniciada = false;

        batallaTerminada = false;

        /*
         * La revancha será otra batalla,
         * así que podrá guardarse nuevamente.
         */
        batallaGuardada = false;

        numeroTurno = 0;


        hpActualJugador =
                hpMaxJugador;

        hpActualCpu =
                hpMaxCpu;


        actualizarVidaJugador();

        actualizarVidaCpu();


        imgPokemonJugador.setAlpha(
                1f
        );

        imgPokemonCpu.setAlpha(
                1f
        );


        imgPokemonJugador.setTranslationX(
                0f
        );

        imgPokemonCpu.setTranslationX(
                0f
        );


        tvTurno.setText(
                "TURNO 0"
        );


        tvMensajeBatalla.setText(
                "¡Listos para la revancha!"
        );


        tvRegistroBatalla.setText(

                "Revancha preparada.\n\n"

                        + formatearNombre(
                        pokemonJugador.getName()
                )

                        + " VS "

                        + formatearNombre(
                        pokemonCpu.getName()
                )
        );


        panelAtaques.setVisibility(
                View.GONE
        );


        panelResultado.setVisibility(
                View.GONE
        );


        btnComenzarCombate.setVisibility(
                View.VISIBLE
        );


        btnComenzarCombate.setEnabled(
                true
        );
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
    // FORMATEAR NOMBRE
    // =========================================================

    private String formatearNombre(
            String nombre
    ) {

        if (nombre == null
                || nombre.isEmpty()) {

            return "";
        }


        return nombre
                .substring(0, 1)
                .toUpperCase(Locale.ROOT)

                + nombre.substring(1);
    }


    // =========================================================
    // FORMATEAR MOVIMIENTO
    // =========================================================

    private String formatearMovimiento(
            String nombre
    ) {

        if (nombre == null) {

            return "";
        }


        return nombre
                .replace("-", " ")
                .toUpperCase(Locale.ROOT);
    }


    // =========================================================
    // ON DESTROY
    // =========================================================

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(
                null
        );

        super.onDestroy();
    }
}